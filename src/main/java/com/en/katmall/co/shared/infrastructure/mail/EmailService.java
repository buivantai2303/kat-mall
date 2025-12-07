/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.mail;

import com.en.katmall.co.shared.infrastructure.config.properties.AppProperties;
import com.en.katmall.co.shared.infrastructure.config.properties.MailProperties;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Email service for sending emails.
 * Supports both plain text and HTML emails with templates.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    /**
     * Sends a simple text email
     * 
     * @param to      Recipient email
     * @param subject Email subject
     * @param text    Email body text
     */
    @Async
    public void sendSimpleEmail(String to, String subject, String text) {
        if (!mailProperties.isEnabled()) {
            log.info("Email disabled. Would send to: {} with subject: {}", to, subject);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailProperties.getFrom(), mailProperties.getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false);

            mailSender.send(message);
            log.info("Email sent to: {} with subject: {}", to, subject);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    /**
     * Sends an HTML email using a template
     * 
     * @param to           Recipient email
     * @param subject      Email subject
     * @param templateName Thymeleaf template name (without .html)
     * @param variables    Template variables
     */
    @Async
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        if (!mailProperties.isEnabled()) {
            log.info("Email disabled. Would send HTML to: {} with template: {}", to, templateName);
            return;
        }

        try {
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailProperties.getFrom(), mailProperties.getFromName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("HTML email sent to: {} with template: {}", to, templateName);
        } catch (Exception e) {
            log.error("Failed to send HTML email to {}: {}", to, e.getMessage());
        }
    }

    /**
     * Sends a verification email for registration
     * 
     * @param to              Recipient email
     * @param verificationUrl Full verification URL
     * @param expirationHours Hours until expiration
     */
    @Async
    public void sendVerificationEmail(String to, String verificationUrl, int expirationHours) {
        String subject = "Verify your KatMall account";

        Map<String, Object> variables = Map.of(
                "verificationUrl", verificationUrl,
                "expirationHours", expirationHours,
                "appName", appProperties.getName());

        // Try HTML template first, fallback to plain text
        try {
            sendHtmlEmail(to, subject, "email/verification", variables);
        } catch (Exception e) {
            log.warn("HTML email failed, sending plain text: {}", e.getMessage());
            sendVerificationEmailPlainText(to, verificationUrl, expirationHours);
        }
    }

    /**
     * Sends a plain text verification email
     */
    private void sendVerificationEmailPlainText(String to, String verificationUrl, int expirationHours) {
        String subject = "Verify your KatMall account";
        String text = String.format("""
                Hello,

                Thank you for registering at %s.

                Please click the link below to verify your account:
                %s

                This link will expire in %d hours.

                If you did not register, please ignore this email.

                Best regards,
                %s Team
                """, appProperties.getName(), verificationUrl, expirationHours, appProperties.getName());

        sendSimpleEmail(to, subject, text);
    }

    /**
     * Sends a welcome email after successful registration
     * 
     * @param to   Recipient email
     * @param name User's name (optional)
     */
    @Async
    public void sendWelcomeEmail(String to, String name) {
        String subject = "Welcome to KatMall!";
        String displayName = (name != null && !name.isBlank()) ? name : "there";

        String text = String.format("""
                Hello %s,

                Welcome to %s!

                Your account has been activated successfully.
                You can now log in and start shopping.

                Best regards,
                %s Team
                """, displayName, appProperties.getName(), appProperties.getName());

        sendSimpleEmail(to, subject, text);
    }

    /**
     * Sends a password reset email
     * 
     * @param to       Recipient email
     * @param resetUrl Password reset URL
     */
    @Async
    public void sendPasswordResetEmail(String to, String resetUrl) {
        String subject = "Reset your KatMall password";

        String text = String.format("""
                Hello,

                You have requested to reset your password for your %s account.

                Please click the link below to reset your password:
                %s

                This link will expire in 1 hour.

                If you did not request a password reset, please ignore this email.

                Best regards,
                %s Team
                """, appProperties.getName(), resetUrl, appProperties.getName());

        sendSimpleEmail(to, subject, text);
    }
}

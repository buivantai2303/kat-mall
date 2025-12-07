# Java Code Commenting Standards Guide

## Overview

This guide defines the commenting standards for all Java code in the KatMall project. **ALL methods MUST have Javadoc comments - no exceptions.**

---

## 1. File Header (Before package declaration)

Every Java file MUST start with the following header:

```java
/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.module.layer;
```

---

## 2. Class/Interface Header (Before class definition)

All classes and interfaces MUST have Javadoc comments:

```java
/**
 * Brief description of the class purpose.
 * Additional details if needed.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class ExampleClass {
    // Class implementation
}
```

### Examples by Type:

**Use Case:**
```java
/**
 * Use Case: Register New User
 * Creates a pending registration and sends verification email/SMS.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Service
public class RegisterUseCase { }
```

**Domain Model:**
```java
/**
 * User aggregate root.
 * Represents a registered user in the system.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public class UserModel extends BaseEntity<String> { }
```

**Repository:**
```java
/**
 * Repository interface for UserModel entity.
 * Defines the contract for user persistence operations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public interface UserRepository { }
```

---

## 3. Method Javadoc (REQUIRED FOR ALL METHODS)

⚠️ **IMPORTANT: EVERY method MUST have Javadoc comments. No method should be left without documentation.**

### Public Methods

```java
/**
 * Brief description of what the method does.
 * 
 * @param param1 Description of first parameter
 * @param param2 Description of second parameter
 * @return Description of return value
 * @throws ExceptionType Description of when this exception is thrown
 */
public ReturnType methodName(Type param1, Type param2) {
    // Implementation
}
```

### Private Methods

Even private methods MUST have Javadoc:

```java
/**
 * Validates that the identifier is not already registered.
 * 
 * @param identifier User identifier (email or phone)
 * @param type       Type of identifier
 * @throws DomainException if identifier already exists
 */
private void checkNotAlreadyRegistered(String identifier, KTypeIdentifier type) {
    // Implementation
}
```

### Methods Without Parameters

```java
/**
 * Gets the currently authenticated user.
 * 
 * @return Authenticated user principal
 * @throws DomainException if user is not authenticated
 */
private AuthenticatedUser getAuthenticatedUser() {
    // Implementation
}
```

### Void Methods

```java
/**
 * Sends verification email/SMS to the user.
 * 
 * @param registration Pending registration model
 */
private void sendVerification(MemberRegistrationModel registration) {
    // Implementation
}
```

---

## 4. Field Comments

All class fields SHOULD have comments, especially constants:

```java
/** Maximum number of resend attempts */
private static final int MAX_RESEND_ATTEMPTS = 3;

/** Error code for account locked */
private static final String ERROR_ACCOUNT_LOCKED = "ACCOUNT_LOCKED";

/** User repository for persistence operations */
private final UserRepository userRepository;
```

---

## 5. Inline Comments

Use single-line comments to explain complex logic:

```java
// Check if account is locked before proceeding
if (userModel.getStatus() == KTypeUserStatus.LOCKED) {
    throw new DomainException(ERROR_ACCOUNT_LOCKED, "Account is locked");
}

// Normalize phone number to standard format (remove +84, spaces)
String normalizedPhone = KTypeIdentifier.normalizePhone(phone);
```

---

## 6. TODO Comments

Use TODO comments for future improvements:

```java
// TODO: Implement SMS verification service
// TODO: Add rate limiting for login attempts
```

---

## 7. Section Comments (Optional)

For long classes, use section comments to organize code:

```java
// ==================== PUBLIC METHODS ====================

// ==================== PRIVATE HELPERS ====================

// ==================== MAPPERS ====================
```

---

## 8. Checklist Before Commit

Before committing code, verify:

- [ ] File header is present
- [ ] Class/Interface has Javadoc with @author and @version
- [ ] **ALL public methods have Javadoc**
- [ ] **ALL private methods have Javadoc**
- [ ] All parameters are documented with @param
- [ ] Return values are documented with @return
- [ ] Exceptions are documented with @throws
- [ ] Complex logic has inline comments
- [ ] Constants have comments explaining their purpose

---

## 9. Anti-patterns (DO NOT DO)

❌ **Empty comments:**
```java
/**
 */
public void doSomething() { }
```

❌ **Obvious comments:**
```java
/**
 * Gets the name
 * @return the name
 */
public String getName() { return name; }
```

✅ **Better:**
```java
/**
 * Gets the user's display name for UI purposes.
 * Returns the full name if available, otherwise the email prefix.
 * 
 * @return Display name for the user
 */
public String getDisplayName() { }
```

❌ **Missing method comments (UNACCEPTABLE):**
```java
private void validatePassword(String password) {
    // NO JAVADOC - THIS IS NOT ALLOWED
}
```
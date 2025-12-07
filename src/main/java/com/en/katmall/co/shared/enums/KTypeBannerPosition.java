/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.enums;

import lombok.Getter;

/**
 * Enumeration of banner display positions on the website.
 * Defines where banners can be placed for promotional content.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Getter
public enum KTypeBannerPosition {

    /**
     * Main slider on home page
     */
    HOME_SLIDER("HOME_SLIDER", "Home Page Slider", "Slider chính trang chủ"),

    /**
     * Side banner on home page
     */
    HOME_SIDE("HOME_SIDE", "Home Page Side Banner", "Banner bên trang chủ"),

    /**
     * Banner in category listing pages
     */
    CATEGORY_TOP("CATEGORY_TOP", "Category Page Top", "Đầu trang danh mục"),

    /**
     * Banner on product detail pages
     */
    PRODUCT_DETAIL("PRODUCT_DETAIL", "Product Detail Banner", "Banner trang chi tiết sản phẩm"),

    /**
     * Banner in checkout flow
     */
    CHECKOUT("CHECKOUT", "Checkout Banner", "Banner trang thanh toán"),

    /**
     * Pop-up banner
     */
    POPUP("POPUP", "Popup Banner", "Banner popup"),

    /**
     * Footer banner
     */
    FOOTER("FOOTER", "Footer Banner", "Banner chân trang");

    private final String code;
    private final String nameEn;
    private final String nameVi;

    KTypeBannerPosition(String code, String nameEn, String nameVi) {
        this.code = code;
        this.nameEn = nameEn;
        this.nameVi = nameVi;
    }

    /**
     * Gets display name based on language
     * 
     * @param languageCode The language code ('vi' or 'en')
     * @return Localized display name
     */
    public String getDisplayName(String languageCode) {
        return "vi".equalsIgnoreCase(languageCode) ? nameVi : nameEn;
    }

    /**
     * Finds enum by code value
     * 
     * @param code The code to search for
     * @return The matching enum value
     * @throws IllegalArgumentException if code not found
     */
    public static KTypeBannerPosition fromCode(String code) {
        for (KTypeBannerPosition position : values()) {
            if (position.code.equalsIgnoreCase(code)) {
                return position;
            }
        }
        throw new IllegalArgumentException("Unknown banner position code: " + code);
    }
}

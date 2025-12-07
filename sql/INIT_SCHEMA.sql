-- ============================================================================
-- KATMALL DATABASE SCHEMA - COMPLETE INITIALIZATION SCRIPT
-- Author: tai.buivan@outlook.com
-- Version: 1.0
-- Description: Complete database schema for KatMall e-commerce platform
-- ============================================================================

-- ============================================================================
-- SECTION 1: SCHEMA INITIALIZATION
-- Reset public schema and create admin role for database access
-- ============================================================================

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

-- UUID extension removed as we use VARCHAR for IDs

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'admin') THEN
        CREATE ROLE admin WITH LOGIN PASSWORD 'admin';
    END IF;
END $$;

GRANT ALL ON SCHEMA public TO admin;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO admin;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO admin;


-- ============================================================================
-- SECTION 2: IDENTITY MODULE TABLES
-- Core user authentication, authorization, and member registration
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 2.1 Users Table
-- Main user account storage for customers (both local and OAuth)
-- ----------------------------------------------------------------------------
CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,                    -- Unique user identifier (ULID format)
    username VARCHAR(50) UNIQUE,                    -- Optional username for display
    email VARCHAR(255) NOT NULL UNIQUE,             -- Email address (required, unique)
    userpassword VARCHAR(255),                      -- BCrypt hashed password (nullable for OAuth users)
    first_name VARCHAR(100),                        -- User's first name
    last_name VARCHAR(100),                         -- User's last name
    phone_number VARCHAR(20),                       -- Contact phone number
    avatar_url VARCHAR(512),                        -- Profile picture URL
    auth_provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL',  -- Authentication provider
    provider_id VARCHAR(255),                       -- External ID from OAuth provider
    is_active BOOLEAN NOT NULL DEFAULT TRUE,        -- Account active status
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,     -- Email/phone verification status
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,       -- Account lock status
    locked_at TIMESTAMP,                            -- Timestamp when account was locked
    lock_reason VARCHAR(255),                       -- Reason for account lock
    created_at TIMESTAMP,                           -- Account creation timestamp
    updated_at TIMESTAMP,                           -- Last update timestamp
    CONSTRAINT chk_users_auth CHECK (
        (auth_provider = 'LOCAL' AND userpassword IS NOT NULL) OR
        (auth_provider != 'LOCAL' AND provider_id IS NOT NULL)
    )
);

COMMENT ON TABLE users IS 'Main user account table for customer authentication';
COMMENT ON COLUMN users.userpassword IS 'Nullable for OAuth users';
COMMENT ON COLUMN users.auth_provider IS 'LOCAL, GOOGLE, FACEBOOK, APPLE';
COMMENT ON COLUMN users.provider_id IS 'External ID from OAuth provider';
COMMENT ON COLUMN users.is_locked IS 'Account lock status';
COMMENT ON COLUMN users.locked_at IS 'Timestamp when account was locked';
COMMENT ON COLUMN users.lock_reason IS 'Reason for account lock';

-- ----------------------------------------------------------------------------
-- 2.2 Member Registrations Table
-- Temporary storage during registration before email/phone verification
-- Records are moved to users table after successful verification
-- ----------------------------------------------------------------------------
CREATE TABLE member_registrations (
    id VARCHAR(255) PRIMARY KEY,                    -- Unique registration identifier
    identifier VARCHAR(255) NOT NULL UNIQUE,        -- Email or phone number
    identifier_type VARCHAR(20) NOT NULL CHECK (identifier_type IN ('EMAIL', 'PHONE')),
    password_hash VARCHAR(255) NOT NULL,            -- BCrypt hashed password
    verification_token VARCHAR(255) NOT NULL UNIQUE, -- Unique token for verification
    verification_attempts INT NOT NULL DEFAULT 1,   -- Number of verification emails/SMS sent
    expires_at TIMESTAMP NOT NULL,                  -- Token expiration timestamp
    created_at TIMESTAMP,                           -- Registration creation timestamp
    last_sent_at TIMESTAMP,                         -- Last verification email/SMS sent
    is_verified BOOLEAN NOT NULL DEFAULT FALSE      -- Verification status
);

COMMENT ON TABLE member_registrations IS 'Temporary storage for registration pending verification';
COMMENT ON COLUMN member_registrations.identifier IS 'Email or phone number';
COMMENT ON COLUMN member_registrations.identifier_type IS 'EMAIL or PHONE';
COMMENT ON COLUMN member_registrations.password_hash IS 'BCrypt hashed password';
COMMENT ON COLUMN member_registrations.verification_token IS 'Unique token for email/SMS verification';
COMMENT ON COLUMN member_registrations.verification_attempts IS 'Number of verification emails/SMS sent';
COMMENT ON COLUMN member_registrations.expires_at IS 'Token expiration timestamp';
COMMENT ON COLUMN member_registrations.last_sent_at IS 'Last verification email/SMS sent timestamp';

-- ----------------------------------------------------------------------------
-- 2.3 Administrators Table
-- Admin account storage for back-office operations
-- ----------------------------------------------------------------------------
CREATE TABLE administrators (
    id VARCHAR(255) PRIMARY KEY,                    -- Unique admin identifier
    username VARCHAR(50) NOT NULL UNIQUE,           -- Admin username
    email VARCHAR(255) NOT NULL UNIQUE,             -- Admin email address
    adminpassword VARCHAR(255) NOT NULL,            -- BCrypt hashed password
    full_name VARCHAR(100),                         -- Admin's full name
    employee_code VARCHAR(50),                      -- Internal employee code
    is_active BOOLEAN NOT NULL DEFAULT TRUE,        -- Account active status
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,       -- Account lock status
    locked_at TIMESTAMP,                            -- Timestamp when locked
    lock_reason VARCHAR(255),                       -- Reason for lock
    created_at TIMESTAMP,                           -- Creation timestamp
    updated_at TIMESTAMP                            -- Last update timestamp
);

COMMENT ON TABLE administrators IS 'Administrator accounts for back-office access';
COMMENT ON COLUMN administrators.adminpassword IS 'BCrypt hashed password';
COMMENT ON COLUMN administrators.employee_code IS 'Internal employee code';

-- ----------------------------------------------------------------------------
-- 2.4 Roles and Permissions Tables
-- Role-based access control (RBAC) system
-- ----------------------------------------------------------------------------
CREATE TABLE roles (
    id VARCHAR(255) PRIMARY KEY,                    -- Unique role identifier
    code VARCHAR(50) NOT NULL UNIQUE,               -- Role code (SUPER_ADMIN, ADMIN, etc.)
    name VARCHAR(100) NOT NULL,                     -- Display name
    description VARCHAR(255)                        -- Role description
);

COMMENT ON TABLE roles IS 'System roles for RBAC';
COMMENT ON COLUMN roles.code IS 'Unique role identifier code';

CREATE TABLE permissions (
    id VARCHAR(255) PRIMARY KEY,                    -- Unique permission identifier
    code VARCHAR(100) NOT NULL UNIQUE,              -- Permission code
    name VARCHAR(100) NOT NULL,                     -- Display name
    description VARCHAR(255)                        -- Permission description
);

COMMENT ON TABLE permissions IS 'System permissions for fine-grained access control';
COMMENT ON COLUMN permissions.code IS 'Unique permission identifier code';

-- Admin-Role many-to-many relationship
CREATE TABLE admin_roles (
    admin_id VARCHAR(255) NOT NULL REFERENCES administrators(id) ON DELETE CASCADE,
    role_id VARCHAR(255) NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (admin_id, role_id)
);

COMMENT ON TABLE admin_roles IS 'Many-to-many relationship between administrators and roles';

-- Role-Permission many-to-many relationship
CREATE TABLE role_permissions (
    role_id VARCHAR(255) NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id VARCHAR(255) NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

COMMENT ON TABLE role_permissions IS 'Many-to-many relationship between roles and permissions';

-- ----------------------------------------------------------------------------
-- 2.5 Token Blacklist Table
-- Stores revoked JWT tokens for logout/security purposes
-- ----------------------------------------------------------------------------
CREATE TABLE token_blacklist (
    id VARCHAR(255) PRIMARY KEY,                    -- Unique identifier
    token_jti VARCHAR(255) NOT NULL UNIQUE,         -- JWT ID for token identification
    user_id VARCHAR(255),                           -- User who owned the token
    user_type VARCHAR(20),                          -- USER or ADMIN
    expires_at TIMESTAMP NOT NULL,                  -- Original token expiration time
    revoked_at TIMESTAMP NOT NULL                   -- Time when token was revoked
);

COMMENT ON TABLE token_blacklist IS 'Revoked JWT tokens for security';
COMMENT ON COLUMN token_blacklist.token_jti IS 'JWT ID for token identification';
COMMENT ON COLUMN token_blacklist.user_type IS 'USER or ADMIN';
COMMENT ON COLUMN token_blacklist.expires_at IS 'Original token expiration time';
COMMENT ON COLUMN token_blacklist.revoked_at IS 'Time when token was revoked';

-- ----------------------------------------------------------------------------
-- 2.6 User Addresses Table
-- Customer shipping and billing addresses
-- ----------------------------------------------------------------------------
CREATE TABLE user_addresses (
    id VARCHAR(255) PRIMARY KEY,                    -- Unique address identifier
    user_id VARCHAR(255) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    alias VARCHAR(50),                              -- Friendly name (Home, Office)
    full_name VARCHAR(100),                         -- Recipient name
    phone_number VARCHAR(20),                       -- Contact phone
    address_line1 VARCHAR(255) NOT NULL,            -- Street address
    address_line2 VARCHAR(255),                     -- Additional address info
    city VARCHAR(100) NOT NULL,                     -- City/Province
    district VARCHAR(100),                          -- District (Quận/Huyện)
    ward VARCHAR(100),                              -- Ward (Phường/Xã)
    country VARCHAR(100) NOT NULL DEFAULT 'Vietnam',
    postal_code VARCHAR(20),                        -- Postal/ZIP code
    is_default_billing BOOLEAN DEFAULT FALSE,       -- Default billing address
    is_default_shipping BOOLEAN DEFAULT FALSE,      -- Default shipping address
    created_at TIMESTAMP
);

COMMENT ON TABLE user_addresses IS 'Customer shipping and billing addresses';
COMMENT ON COLUMN user_addresses.alias IS 'Friendly name for address (Home, Office)';
COMMENT ON COLUMN user_addresses.district IS 'Quận/Huyện';
COMMENT ON COLUMN user_addresses.ward IS 'Phường/Xã';

-- ----------------------------------------------------------------------------
-- 2.7 Postal Codes Reference Table
-- Vietnamese postal code reference data
-- ----------------------------------------------------------------------------
CREATE TABLE postal_codes (
    code VARCHAR(10) PRIMARY KEY,                   -- Postal code
    city VARCHAR(100) NOT NULL,                     -- City/Province
    district VARCHAR(100),                          -- District
    ward VARCHAR(100),                              -- Ward
    full_address VARCHAR(255)                       -- Formatted full address
);

COMMENT ON TABLE postal_codes IS 'Vietnamese postal code reference data';
COMMENT ON COLUMN postal_codes.city IS 'Tỉnh/Thành phố';
COMMENT ON COLUMN postal_codes.district IS 'Quận/Huyện';
COMMENT ON COLUMN postal_codes.ward IS 'Phường/Xã';
COMMENT ON COLUMN postal_codes.full_address IS 'Formatted full address string';

-- Identity Module Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_phone ON users(phone_number);
CREATE INDEX idx_users_provider ON users(auth_provider, provider_id);
CREATE INDEX idx_user_addresses_user_id ON user_addresses(user_id);
CREATE INDEX idx_token_blacklist_jti ON token_blacklist(token_jti);
CREATE INDEX idx_token_blacklist_expires ON token_blacklist(expires_at);
CREATE INDEX idx_member_reg_identifier ON member_registrations(identifier);
CREATE INDEX idx_member_reg_token ON member_registrations(verification_token);
CREATE INDEX idx_member_reg_expires ON member_registrations(expires_at);


-- ============================================================================
-- SECTION 3: CMS MODULE TABLES
-- Content Management System for news, banners, and marketing
-- ============================================================================

-- News articles with translations
CREATE TABLE cms_news (
    id VARCHAR(255) PRIMARY KEY,
    slug VARCHAR(255) NOT NULL UNIQUE,              -- URL-friendly identifier
    thumbnail_url VARCHAR(512),                     -- Thumbnail image
    author_id VARCHAR(255) REFERENCES administrators(id),
    published_at TIMESTAMP,
    is_published BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP
);

COMMENT ON TABLE cms_news IS 'News articles for the platform';
COMMENT ON COLUMN cms_news.slug IS 'URL-friendly identifier';

CREATE TABLE cms_news_translations (
    id VARCHAR(255) PRIMARY KEY,
    news_id VARCHAR(255) NOT NULL REFERENCES cms_news(id) ON DELETE CASCADE,
    language_code VARCHAR(5) NOT NULL,              -- ISO 639-1 code (vi, en)
    title VARCHAR(255) NOT NULL,
    content TEXT,
    UNIQUE (news_id, language_code)
);

COMMENT ON TABLE cms_news_translations IS 'Multilingual news content';
COMMENT ON COLUMN cms_news_translations.language_code IS 'ISO 639-1 code (vi, en)';

-- Banner management
CREATE TABLE cms_banners (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    image_url VARCHAR(512) NOT NULL,
    target_url VARCHAR(512),
    display_position VARCHAR(50),                   -- Banner placement location
    priority INT DEFAULT 0,                         -- Display order priority
    is_active BOOLEAN DEFAULT TRUE
);

COMMENT ON TABLE cms_banners IS 'Marketing banners for homepage and promotions';
COMMENT ON COLUMN cms_banners.display_position IS 'Banner placement location';
COMMENT ON COLUMN cms_banners.priority IS 'Display order priority';

-- Coupon/Voucher system
CREATE TABLE coupons (
    code VARCHAR(50) PRIMARY KEY,                   -- Coupon code
    description VARCHAR(255),
    discount_type VARCHAR(20) NOT NULL CHECK (discount_type IN ('PERCENTAGE', 'FIXED_AMOUNT')),
    discount_value DECIMAL(15, 2) NOT NULL,
    max_discount_amount DECIMAL(15, 2),             -- Cap for percentage discounts
    min_order_value DECIMAL(15, 2) DEFAULT 0,       -- Minimum order to apply
    max_usage_limit INT,                            -- Maximum uses allowed
    usage_count INT DEFAULT 0,                      -- Current usage count
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

COMMENT ON TABLE coupons IS 'Discount coupons and vouchers';
COMMENT ON COLUMN coupons.discount_type IS 'PERCENTAGE or FIXED_AMOUNT';
COMMENT ON COLUMN coupons.max_discount_amount IS 'Maximum discount cap for PERCENTAGE type';
COMMENT ON COLUMN coupons.min_order_value IS 'Minimum order value to apply coupon';
COMMENT ON COLUMN coupons.max_usage_limit IS 'Maximum number of times coupon can be used';

-- Tags for products
CREATE TABLE tags (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    slug VARCHAR(50) NOT NULL UNIQUE                -- URL-friendly identifier
);

COMMENT ON TABLE tags IS 'Product tags for categorization';
COMMENT ON COLUMN tags.slug IS 'URL-friendly identifier';

-- User notifications
CREATE TABLE notifications (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    type VARCHAR(50) DEFAULT 'SYSTEM' CHECK (type IN ('SYSTEM', 'ORDER', 'PROMO', 'SECURITY')),
    reference_id VARCHAR(255),                      -- Reference to related entity
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP
);

COMMENT ON TABLE notifications IS 'User notifications';
COMMENT ON COLUMN notifications.type IS 'SYSTEM, ORDER, PROMO, SECURITY';
COMMENT ON COLUMN notifications.reference_id IS 'Optional reference to related entity (order_id, etc)';

-- CMS Indexes
CREATE INDEX idx_cms_news_slug ON cms_news(slug);
CREATE INDEX idx_cms_news_author ON cms_news(author_id);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_user_unread ON notifications(user_id, is_read) WHERE is_read = FALSE;


-- ============================================================================
-- SECTION 4: CATALOG MODULE TABLES
-- Product catalog, categories, brands, and attributes
-- ============================================================================

-- Categories with hierarchical structure
CREATE TABLE categories (
    id VARCHAR(255) PRIMARY KEY,
    parent_id VARCHAR(255) REFERENCES categories(id) ON DELETE SET NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    tree_path VARCHAR(255),                         -- Materialized path (ROOT_ID/CHILD_ID)
    is_active BOOLEAN DEFAULT TRUE
);

COMMENT ON TABLE categories IS 'Product categories with hierarchy';
COMMENT ON COLUMN categories.tree_path IS 'Materialized path for hierarchy optimization';

CREATE TABLE category_translations (
    id VARCHAR(255) PRIMARY KEY,
    category_id VARCHAR(255) NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    language_code VARCHAR(5) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    UNIQUE (category_id, language_code)
);

COMMENT ON TABLE category_translations IS 'Multilingual category content';

-- Brands
CREATE TABLE brands (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    logo_url VARCHAR(512)
);

COMMENT ON TABLE brands IS 'Product brands';

-- Products (Aggregate Root)
CREATE TABLE products (
    id VARCHAR(255) PRIMARY KEY,
    category_id VARCHAR(255) REFERENCES categories(id),
    brand_id VARCHAR(255) REFERENCES brands(id),
    slug VARCHAR(255) NOT NULL UNIQUE,
    base_price DECIMAL(15, 2),                      -- Default price before variant
    weight DECIMAL(10, 2),                          -- Weight in kg for shipping
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    deleted_at TIMESTAMP                            -- Soft delete timestamp
);

COMMENT ON TABLE products IS 'Product aggregate root';
COMMENT ON COLUMN products.base_price IS 'Default price before variant pricing';
COMMENT ON COLUMN products.weight IS 'Weight in kilograms for shipping calculation';
COMMENT ON COLUMN products.deleted_at IS 'Soft delete timestamp';

CREATE TABLE product_translations (
    id VARCHAR(255) PRIMARY KEY,
    product_id VARCHAR(255) NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    language_code VARCHAR(5) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    meta_title VARCHAR(255),                        -- SEO title tag
    meta_description VARCHAR(500),                  -- SEO meta description
    UNIQUE (product_id, language_code)
);

COMMENT ON TABLE product_translations IS 'Multilingual product content';
COMMENT ON COLUMN product_translations.meta_title IS 'SEO title tag';
COMMENT ON COLUMN product_translations.meta_description IS 'SEO meta description';

-- Product Variants (within Product Aggregate)
CREATE TABLE product_variants (
    id VARCHAR(255) PRIMARY KEY,
    product_id VARCHAR(255) NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    sku VARCHAR(100) NOT NULL UNIQUE,               -- Stock Keeping Unit
    price DECIMAL(15, 2) NOT NULL,
    compare_at_price DECIMAL(15, 2),                -- Original/strikethrough price
    image_url VARCHAR(512),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP
);

COMMENT ON TABLE product_variants IS 'Product variants (size, color combinations)';
COMMENT ON COLUMN product_variants.sku IS 'Stock Keeping Unit';
COMMENT ON COLUMN product_variants.compare_at_price IS 'Original/strikethrough price for display';

-- Product Tags relationship
CREATE TABLE product_tags (
    product_id VARCHAR(255) NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    tag_id VARCHAR(255) NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, tag_id)
);

COMMENT ON TABLE product_tags IS 'Many-to-many relationship between products and tags';

-- Product Reviews
CREATE TABLE product_reviews (
    id VARCHAR(255) PRIMARY KEY,
    product_id VARCHAR(255) NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    user_id VARCHAR(255) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    is_verified_purchase BOOLEAN DEFAULT FALSE,     -- True if user purchased
    created_at TIMESTAMP
);

COMMENT ON TABLE product_reviews IS 'Customer product reviews';
COMMENT ON COLUMN product_reviews.rating IS 'Rating from 1 to 5 stars';
COMMENT ON COLUMN product_reviews.is_verified_purchase IS 'True if user purchased this product';

CREATE TABLE review_images (
    id VARCHAR(255) PRIMARY KEY,
    review_id VARCHAR(255) NOT NULL REFERENCES product_reviews(id) ON DELETE CASCADE,
    image_url VARCHAR(512) NOT NULL
);

COMMENT ON TABLE review_images IS 'Images attached to product reviews';

-- Product Attributes (for variants)
CREATE TABLE attributes (
    id VARCHAR(255) PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE                -- color, size, material
);

COMMENT ON TABLE attributes IS 'Product attribute definitions';
COMMENT ON COLUMN attributes.code IS 'Unique attribute identifier (color, size)';

CREATE TABLE attribute_translations (
    id VARCHAR(255) PRIMARY KEY,
    attribute_id VARCHAR(255) NOT NULL REFERENCES attributes(id) ON DELETE CASCADE,
    language_code VARCHAR(5) NOT NULL,
    name VARCHAR(100) NOT NULL,
    UNIQUE (attribute_id, language_code)
);

CREATE TABLE attribute_values (
    id VARCHAR(255) PRIMARY KEY,
    attribute_id VARCHAR(255) NOT NULL REFERENCES attributes(id),
    code VARCHAR(50)
);

COMMENT ON TABLE attribute_values IS 'Possible values for attributes';

CREATE TABLE attribute_value_translations (
    id VARCHAR(255) PRIMARY KEY,
    attribute_value_id VARCHAR(255) NOT NULL REFERENCES attribute_values(id) ON DELETE CASCADE,
    language_code VARCHAR(5) NOT NULL,
    value VARCHAR(100) NOT NULL,
    UNIQUE (attribute_value_id, language_code)
);

CREATE TABLE variant_attribute_values (
    variant_id VARCHAR(255) NOT NULL REFERENCES product_variants(id) ON DELETE CASCADE,
    attribute_value_id VARCHAR(255) NOT NULL REFERENCES attribute_values(id),
    PRIMARY KEY (variant_id, attribute_value_id)
);

COMMENT ON TABLE variant_attribute_values IS 'Links variants to their attribute values';

-- Catalog Indexes
CREATE INDEX idx_categories_parent ON categories(parent_id);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_brand ON products(brand_id);
CREATE INDEX idx_products_slug ON products(slug);
CREATE INDEX idx_products_active ON products(is_active) WHERE deleted_at IS NULL;
CREATE INDEX idx_product_variants_product_id ON product_variants(product_id);
CREATE INDEX idx_product_variants_sku ON product_variants(sku);
CREATE INDEX idx_product_reviews_product ON product_reviews(product_id);
CREATE INDEX idx_product_tags_tag ON product_tags(tag_id);


-- ============================================================================
-- SECTION 5: INVENTORY MODULE TABLES
-- Stock management, locations, and transactions
-- ============================================================================

CREATE TABLE inventory_locations (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),                               -- WAREHOUSE, STORE, 3PL
    address VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE
);

COMMENT ON TABLE inventory_locations IS 'Warehouse and store locations';
COMMENT ON COLUMN inventory_locations.type IS 'WAREHOUSE, STORE, 3PL';

CREATE TABLE inventory_stocks (
    id VARCHAR(255) PRIMARY KEY,
    location_id VARCHAR(255) NOT NULL REFERENCES inventory_locations(id),
    variant_id VARCHAR(255) NOT NULL REFERENCES product_variants(id),
    quantity_on_hand INT NOT NULL DEFAULT 0,        -- Physical stock count
    quantity_reserved INT NOT NULL DEFAULT 0,       -- Reserved for pending orders
    low_stock_threshold INT DEFAULT 10,             -- Alert threshold
    version INT NOT NULL DEFAULT 0,                 -- Optimistic locking
    updated_at TIMESTAMP,
    UNIQUE (location_id, variant_id)
);

COMMENT ON TABLE inventory_stocks IS 'Stock levels per location per variant';
COMMENT ON COLUMN inventory_stocks.quantity_on_hand IS 'Physical stock count';
COMMENT ON COLUMN inventory_stocks.quantity_reserved IS 'Reserved for pending orders';
COMMENT ON COLUMN inventory_stocks.low_stock_threshold IS 'Alert threshold';
COMMENT ON COLUMN inventory_stocks.version IS 'Optimistic locking version';

CREATE TABLE inventory_transactions (
    id VARCHAR(255) PRIMARY KEY,
    location_id VARCHAR(255) NOT NULL REFERENCES inventory_locations(id),
    variant_id VARCHAR(255) NOT NULL REFERENCES product_variants(id),
    quantity_change INT NOT NULL,                   -- Positive=in, Negative=out
    transaction_type VARCHAR(50) NOT NULL CHECK (transaction_type IN ('IMPORT', 'SALE', 'RETURN', 'ADJUSTMENT', 'RESERVE', 'RELEASE')),
    reference_id VARCHAR(255),                      -- Reference to order, shipment
    reason VARCHAR(255),
    created_by VARCHAR(255),
    created_at TIMESTAMP
);

COMMENT ON TABLE inventory_transactions IS 'Stock movement audit trail';
COMMENT ON COLUMN inventory_transactions.quantity_change IS 'Positive for inbound, negative for outbound';
COMMENT ON COLUMN inventory_transactions.transaction_type IS 'IMPORT, SALE, RETURN, ADJUSTMENT, RESERVE, RELEASE';
COMMENT ON COLUMN inventory_transactions.reference_id IS 'Reference to order_id, shipment_id, etc';

-- Inventory Indexes
CREATE INDEX idx_inv_stocks_location_variant ON inventory_stocks(location_id, variant_id);
CREATE INDEX idx_inv_transactions_variant ON inventory_transactions(variant_id);
CREATE INDEX idx_inv_transactions_created_at ON inventory_transactions(created_at);


-- ============================================================================
-- SECTION 6: WISHLIST MODULE TABLES
-- Customer wishlists
-- ============================================================================

CREATE TABLE wishlists (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP,
    UNIQUE (user_id)
);

COMMENT ON TABLE wishlists IS 'Customer wishlists (one per user)';

CREATE TABLE wishlist_items (
    id VARCHAR(255) PRIMARY KEY,
    wishlist_id VARCHAR(255) NOT NULL REFERENCES wishlists(id) ON DELETE CASCADE,
    product_id VARCHAR(255) REFERENCES products(id),
    variant_id VARCHAR(255) REFERENCES product_variants(id),
    created_at TIMESTAMP,
    UNIQUE (wishlist_id, product_id, variant_id)
);

COMMENT ON TABLE wishlist_items IS 'Items in customer wishlists';

CREATE INDEX idx_wishlist_items_product ON wishlist_items(product_id);


-- ============================================================================
-- SECTION 7: ORDERING MODULE TABLES
-- Snapshots, carts, checkout, orders, shipments
-- ============================================================================

-- Immutable snapshots for order history
CREATE TABLE address_snapshots (
    id VARCHAR(255) PRIMARY KEY,
    full_name VARCHAR(100),
    phone_number VARCHAR(20),
    email VARCHAR(255),
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    city VARCHAR(100),
    district VARCHAR(100),
    ward VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    created_at TIMESTAMP
);

COMMENT ON TABLE address_snapshots IS 'Immutable address snapshot for order history';

CREATE TABLE purchaser_snapshots (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255),                           -- Reference only
    full_name VARCHAR(100),
    email VARCHAR(255),
    phone_number VARCHAR(20),
    created_at TIMESTAMP
);

COMMENT ON TABLE purchaser_snapshots IS 'Immutable purchaser info snapshot';
COMMENT ON COLUMN purchaser_snapshots.user_id IS 'Reference to original user (informational only)';

CREATE TABLE item_snapshots (
    id VARCHAR(255) PRIMARY KEY,
    variant_id VARCHAR(255),
    product_id VARCHAR(255),
    sku VARCHAR(100) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    variant_name VARCHAR(255),
    product_image_url VARCHAR(512),
    attributes_text TEXT,                           -- Serialized attributes
    weight DECIMAL(10, 2),
    quantity INT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    total_price DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP
);

COMMENT ON TABLE item_snapshots IS 'Immutable product snapshot at time of purchase';
COMMENT ON COLUMN item_snapshots.attributes_text IS 'Serialized attributes (Color: Red; Size: M)';

-- Shopping carts
CREATE TABLE carts (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255),                           -- Nullable for guest carts
    expires_at TIMESTAMP,                           -- Cart expiration
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

COMMENT ON TABLE carts IS 'Shopping carts for customers and guests';
COMMENT ON COLUMN carts.user_id IS 'Nullable for guest carts, set on login merge';
COMMENT ON COLUMN carts.expires_at IS 'Cart expiration for cleanup';

CREATE TABLE cart_items (
    id VARCHAR(255) PRIMARY KEY,
    cart_id VARCHAR(255) NOT NULL REFERENCES carts(id) ON DELETE CASCADE,
    variant_id VARCHAR(255) NOT NULL REFERENCES product_variants(id),
    quantity INT NOT NULL DEFAULT 1
);

COMMENT ON TABLE cart_items IS 'Items in shopping carts';

-- Checkout sessions
CREATE TABLE checkout_sessions (
    id VARCHAR(255) PRIMARY KEY,
    session_token VARCHAR(255) UNIQUE,
    cart_id VARCHAR(255) REFERENCES carts(id) ON DELETE SET NULL,
    user_id VARCHAR(255),
    purchaser_snapshot_id VARCHAR(255) REFERENCES purchaser_snapshots(id),
    shipping_snapshot_id VARCHAR(255) REFERENCES address_snapshots(id),
    billing_snapshot_id VARCHAR(255) REFERENCES address_snapshots(id),
    status VARCHAR(50) DEFAULT 'INITIATED' CHECK (status IN ('INITIATED', 'PENDING_PAYMENT', 'COMPLETED', 'EXPIRED', 'ABANDONED')),
    expires_at TIMESTAMP,
    created_at TIMESTAMP
);

COMMENT ON TABLE checkout_sessions IS 'Active checkout sessions';
COMMENT ON COLUMN checkout_sessions.status IS 'INITIATED, PENDING_PAYMENT, COMPLETED, EXPIRED, ABANDONED';

-- Orders (Aggregate Root)
CREATE TABLE orders (
    id VARCHAR(255) PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,       -- Human-readable order ID
    user_id VARCHAR(255) REFERENCES users(id),
    purchaser_snapshot_id VARCHAR(255) REFERENCES purchaser_snapshots(id),
    shipping_snapshot_id VARCHAR(255) REFERENCES address_snapshots(id),
    billing_snapshot_id VARCHAR(255) REFERENCES address_snapshots(id),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED')),
    subtotal DECIMAL(15, 2) NOT NULL,
    shipping_total DECIMAL(15, 2) NOT NULL DEFAULT 0,
    tax_total DECIMAL(15, 2) NOT NULL DEFAULT 0,
    discount_total DECIMAL(15, 2) NOT NULL DEFAULT 0,
    grand_total DECIMAL(15, 2) NOT NULL,
    note TEXT,
    created_at TIMESTAMP
);

COMMENT ON TABLE orders IS 'Order aggregate root';
COMMENT ON COLUMN orders.order_number IS 'Human-readable order ID';
COMMENT ON COLUMN orders.status IS 'PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED';

CREATE TABLE order_items (
    order_id VARCHAR(255) NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    item_snapshot_id VARCHAR(255) NOT NULL REFERENCES item_snapshots(id),
    PRIMARY KEY (order_id, item_snapshot_id)
);

COMMENT ON TABLE order_items IS 'Items in orders';

CREATE TABLE order_adjustments (
    id VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    promotion_id VARCHAR(255),
    promotion_code VARCHAR(50),
    amount DECIMAL(15, 2) NOT NULL,                 -- Discount amount
    type VARCHAR(50) CHECK (type IN ('SHIPPING_DISCOUNT', 'ITEM_DISCOUNT', 'ORDER_DISCOUNT')),
    description VARCHAR(255)
);

COMMENT ON TABLE order_adjustments IS 'Discounts and adjustments applied to orders';
COMMENT ON COLUMN order_adjustments.type IS 'SHIPPING_DISCOUNT, ITEM_DISCOUNT, ORDER_DISCOUNT';
COMMENT ON COLUMN order_adjustments.amount IS 'Discount amount applied';

-- Shipments
CREATE TABLE shipments (
    id VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    tracking_number VARCHAR(100),
    carrier_name VARCHAR(100),                      -- GHTK, GHN, VNPost
    status VARCHAR(50) CHECK (status IN ('PENDING', 'SHIPPED', 'IN_TRANSIT', 'DELIVERED', 'FAILED', 'RETURNED')),
    shipped_at TIMESTAMP,
    delivered_at TIMESTAMP
);

COMMENT ON TABLE shipments IS 'Order shipment tracking';
COMMENT ON COLUMN shipments.status IS 'PENDING, SHIPPED, IN_TRANSIT, DELIVERED, FAILED, RETURNED';
COMMENT ON COLUMN shipments.carrier_name IS 'Shipping carrier (GHTK, GHN, VNPost)';

CREATE TABLE shipment_items (
    id VARCHAR(255) PRIMARY KEY,
    shipment_id VARCHAR(255) NOT NULL REFERENCES shipments(id) ON DELETE CASCADE,
    item_snapshot_id VARCHAR(255) NOT NULL REFERENCES item_snapshots(id),
    quantity INT NOT NULL
);

COMMENT ON TABLE shipment_items IS 'Items included in shipments';

-- Ordering Indexes
CREATE INDEX idx_carts_user_id ON carts(user_id);
CREATE INDEX idx_carts_expires ON carts(expires_at);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_user_status ON orders(user_id, status);
CREATE INDEX idx_shipments_order_id ON shipments(order_id);
CREATE INDEX idx_shipments_tracking ON shipments(tracking_number);


-- ============================================================================
-- SECTION 8: RETURNS MODULE TABLES
-- Order returns and refunds
-- ============================================================================

CREATE TABLE returns (
    id VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    user_id VARCHAR(255) NOT NULL REFERENCES users(id),
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'RECEIVED', 'REFUNDED')),
    reason TEXT,
    refund_amount DECIMAL(15, 2),
    admin_note TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

COMMENT ON TABLE returns IS 'Order return requests';
COMMENT ON COLUMN returns.status IS 'PENDING, APPROVED, REJECTED, RECEIVED, REFUNDED';

CREATE TABLE return_items (
    id VARCHAR(255) PRIMARY KEY,
    return_id VARCHAR(255) NOT NULL REFERENCES returns(id) ON DELETE CASCADE,
    item_snapshot_id VARCHAR(255) NOT NULL REFERENCES item_snapshots(id),
    quantity INT NOT NULL,
    reason VARCHAR(255)
);

COMMENT ON TABLE return_items IS 'Items in return requests';

CREATE INDEX idx_returns_order_id ON returns(order_id);
CREATE INDEX idx_returns_status ON returns(status);


-- ============================================================================
-- SECTION 9: PAYMENT MODULE TABLES
-- Payment processing and transactions
-- ============================================================================

CREATE TABLE payments (
    id VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL REFERENCES orders(id),
    amount DECIMAL(15, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,            -- COD, MOMO, VNPAY, BANK_TRANSFER
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'REFUNDED')),
    created_at TIMESTAMP
);

COMMENT ON TABLE payments IS 'Payment records for orders';
COMMENT ON COLUMN payments.payment_method IS 'COD, MOMO, VNPAY, BANK_TRANSFER';
COMMENT ON COLUMN payments.status IS 'PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED';

CREATE TABLE payment_transactions (
    id VARCHAR(255) PRIMARY KEY,
    payment_id VARCHAR(255) NOT NULL REFERENCES payments(id) ON DELETE CASCADE,
    gateway_transaction_id VARCHAR(255),            -- ID from payment gateway
    gateway_response_code VARCHAR(50),
    raw_response_text TEXT,                         -- Raw response for debugging
    status VARCHAR(50),
    performed_at TIMESTAMP
);

COMMENT ON TABLE payment_transactions IS 'Payment gateway transaction log';
COMMENT ON COLUMN payment_transactions.gateway_transaction_id IS 'Transaction ID from payment gateway';
COMMENT ON COLUMN payment_transactions.raw_response_text IS 'Raw response from payment gateway for debugging';

CREATE TABLE refund_transactions (
    id VARCHAR(255) PRIMARY KEY,
    payment_transaction_id VARCHAR(255) NOT NULL REFERENCES payment_transactions(id),
    refund_amount DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED')),
    gateway_refund_id VARCHAR(255),
    created_at TIMESTAMP
);

COMMENT ON TABLE refund_transactions IS 'Refund transaction records';
COMMENT ON COLUMN refund_transactions.status IS 'PENDING, PROCESSING, COMPLETED, FAILED';

CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payment_trans_gateway_id ON payment_transactions(gateway_transaction_id);


-- ============================================================================
-- SECTION 10: AUDIT LOG TABLE
-- System-wide audit trail
-- ============================================================================

CREATE TABLE audit_logs (
    id VARCHAR(255) PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    record_id VARCHAR(255) NOT NULL,
    action VARCHAR(20) NOT NULL CHECK (action IN ('CREATE', 'UPDATE', 'DELETE')),
    actor_id VARCHAR(255),
    actor_type VARCHAR(20),                         -- USER, ADMIN, SYSTEM
    old_data_text TEXT,                             -- Serialized old data
    new_data_text TEXT,                             -- Serialized new data
    ip_address VARCHAR(45),
    created_at TIMESTAMP
);

COMMENT ON TABLE audit_logs IS 'System-wide audit trail for all changes';
COMMENT ON COLUMN audit_logs.action IS 'CREATE, UPDATE, DELETE';
COMMENT ON COLUMN audit_logs.actor_type IS 'USER, ADMIN, SYSTEM';
COMMENT ON COLUMN audit_logs.old_data_text IS 'Serialized old record data';
COMMENT ON COLUMN audit_logs.new_data_text IS 'Serialized new record data';

CREATE INDEX idx_audit_logs_table_record ON audit_logs(table_name, record_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
CREATE INDEX idx_audit_logs_actor ON audit_logs(actor_id, actor_type);


-- ============================================================================
-- SECTION 11: SEED DATA
-- Initial data for roles, permissions, admin, and reference data
-- ============================================================================

-- Roles
INSERT INTO roles (id, code, name, description) VALUES
    ('a0000000-0000-0000-0000-000000000001', 'SUPER_ADMIN', 'Super Administrator', 'Full system access'),
    ('a0000000-0000-0000-0000-000000000002', 'ADMIN', 'Administrator', 'Administrative access'),
    ('a0000000-0000-0000-0000-000000000003', 'SUPPORT', 'Customer Support', 'Customer support access');

-- Permissions
INSERT INTO permissions (id, code, name, description) VALUES
    ('b0000000-0000-0000-0000-000000000001', 'USER_READ', 'View Users', 'Can view user information'),
    ('b0000000-0000-0000-0000-000000000002', 'USER_WRITE', 'Manage Users', 'Can create/update users'),
    ('b0000000-0000-0000-0000-000000000003', 'PRODUCT_READ', 'View Products', 'Can view products'),
    ('b0000000-0000-0000-0000-000000000004', 'PRODUCT_WRITE', 'Manage Products', 'Can manage products'),
    ('b0000000-0000-0000-0000-000000000005', 'ORDER_READ', 'View Orders', 'Can view orders'),
    ('b0000000-0000-0000-0000-000000000006', 'ORDER_WRITE', 'Manage Orders', 'Can manage orders'),
    ('b0000000-0000-0000-0000-000000000007', 'SYSTEM_ADMIN', 'System Administration', 'Full system access');

-- Role-Permission mappings (Super Admin gets all)
INSERT INTO role_permissions (role_id, permission_id) VALUES
    ('a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001'),
    ('a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000002'),
    ('a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000003'),
    ('a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000004'),
    ('a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000005'),
    ('a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000006'),
    ('a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000007');

-- Default Admin (password: 'password')
INSERT INTO administrators (id, username, email, adminpassword, full_name, employee_code, is_active) VALUES
    ('c0000000-0000-0000-0000-000000000001', 'admin', 'admin@microbook.vn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/n3.rkNJE.bvcCzxh.cQVy', 'System Administrator', 'EMP001', TRUE);

INSERT INTO admin_roles (admin_id, role_id) VALUES
    ('c0000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000001');

-- Vietnamese Postal Codes
INSERT INTO postal_codes (code, city, district, ward, full_address) VALUES
    ('70000', 'Hồ Chí Minh', 'Quận 1', NULL, 'Quận 1, TP. Hồ Chí Minh'),
    ('70100', 'Hồ Chí Minh', 'Quận 3', NULL, 'Quận 3, TP. Hồ Chí Minh'),
    ('70200', 'Hồ Chí Minh', 'Quận 5', NULL, 'Quận 5, TP. Hồ Chí Minh'),
    ('70300', 'Hồ Chí Minh', 'Quận 7', NULL, 'Quận 7, TP. Hồ Chí Minh'),
    ('70400', 'Hồ Chí Minh', 'Quận 10', NULL, 'Quận 10, TP. Hồ Chí Minh'),
    ('70500', 'Hồ Chí Minh', 'Quận Bình Thạnh', NULL, 'Quận Bình Thạnh, TP. Hồ Chí Minh'),
    ('70600', 'Hồ Chí Minh', 'Quận Gò Vấp', NULL, 'Quận Gò Vấp, TP. Hồ Chí Minh'),
    ('70700', 'Hồ Chí Minh', 'Quận Tân Bình', NULL, 'Quận Tân Bình, TP. Hồ Chí Minh'),
    ('70800', 'Hồ Chí Minh', 'Quận Phú Nhuận', NULL, 'Quận Phú Nhuận, TP. Hồ Chí Minh'),
    ('71000', 'Hồ Chí Minh', 'TP. Thủ Đức', NULL, 'TP. Thủ Đức, TP. Hồ Chí Minh'),
    ('10000', 'Hà Nội', 'Quận Hoàn Kiếm', NULL, 'Quận Hoàn Kiếm, Hà Nội'),
    ('10100', 'Hà Nội', 'Quận Ba Đình', NULL, 'Quận Ba Đình, Hà Nội'),
    ('10200', 'Hà Nội', 'Quận Đống Đa', NULL, 'Quận Đống Đa, Hà Nội'),
    ('10300', 'Hà Nội', 'Quận Hai Bà Trưng', NULL, 'Quận Hai Bà Trưng, Hà Nội'),
    ('10400', 'Hà Nội', 'Quận Cầu Giấy', NULL, 'Quận Cầu Giấy, Hà Nội'),
    ('10500', 'Hà Nội', 'Quận Thanh Xuân', NULL, 'Quận Thanh Xuân, Hà Nội'),
    ('10600', 'Hà Nội', 'Quận Hoàng Mai', NULL, 'Quận Hoàng Mai, Hà Nội'),
    ('10700', 'Hà Nội', 'Quận Long Biên', NULL, 'Quận Long Biên, Hà Nội'),
    ('50000', 'Đà Nẵng', 'Quận Hải Châu', NULL, 'Quận Hải Châu, Đà Nẵng'),
    ('50100', 'Đà Nẵng', 'Quận Thanh Khê', NULL, 'Quận Thanh Khê, Đà Nẵng'),
    ('50200', 'Đà Nẵng', 'Quận Sơn Trà', NULL, 'Quận Sơn Trà, Đà Nẵng'),
    ('50300', 'Đà Nẵng', 'Quận Ngũ Hành Sơn', NULL, 'Quận Ngũ Hành Sơn, Đà Nẵng'),
    ('65000', 'Cần Thơ', 'Quận Ninh Kiều', NULL, 'Quận Ninh Kiều, Cần Thơ'),
    ('60000', 'Bình Dương', 'TP. Thủ Dầu Một', NULL, 'TP. Thủ Dầu Một, Bình Dương'),
    ('61000', 'Đồng Nai', 'TP. Biên Hòa', NULL, 'TP. Biên Hòa, Đồng Nai'),
    ('77000', 'Bà Rịa - Vũng Tàu', 'TP. Vũng Tàu', NULL, 'TP. Vũng Tàu, Bà Rịa - Vũng Tàu'),
    ('31000', 'Hải Phòng', 'Quận Hồng Bàng', NULL, 'Quận Hồng Bàng, Hải Phòng'),
    ('48000', 'Khánh Hòa', 'TP. Nha Trang', NULL, 'TP. Nha Trang, Khánh Hòa'),
    ('66000', 'Đồng Tháp', 'TP. Cao Lãnh', NULL, 'TP. Cao Lãnh, Đồng Tháp'),
    ('67000', 'An Giang', 'TP. Long Xuyên', NULL, 'TP. Long Xuyên, An Giang');

-- Default Warehouse
INSERT INTO inventory_locations (id, name, type, address, is_active) VALUES
    ('d0000000-0000-0000-0000-000000000001', 'Kho Chính HCM', 'WAREHOUSE', 'Quận 7, TP. Hồ Chí Minh', TRUE);

-- ============================================================================
-- END OF SCHEMA
-- ============================================================================

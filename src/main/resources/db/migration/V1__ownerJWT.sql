CREATE TABLE owners (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(20) NOT NULL UNIQUE,
    business_name VARCHAR(100) NOT NULL,
    business_type ENUM('SINGLE', 'CHAIN', 'FRANCHISE'),
    address TEXT,
    timezone VARCHAR(100) DEFAULT 'Asia/Kolkata',
    profile_picture_url VARCHAR(255),
    selected_plan ENUM('BASIC', 'PREMIUM', 'PRO') NOT NULL,
    payment_method ENUM('CREDIT_CARD', 'BANK_TRANSFER', 'UPI') DEFAULT NULL,
    account_status ENUM('TRIAL', 'ACTIVE', 'EXPIRED', 'SUSPENDED') DEFAULT 'TRIAL',
    trial_end_date DATE DEFAULT NULL,
    membership_end_date DATE DEFAULT NULL,
    last_payment_date DATE DEFAULT NULL,
    next_billing_date DATE DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES owners(id)
);

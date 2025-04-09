CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    joining_date DATE,
    membership_end_date DATE,
    package_name VARCHAR(255),
    payment_status ENUM('PARTIAL', 'COMPLETED'),
    amount_paid DECIMAL(10,2),
    mobile_number VARCHAR(15),
    email VARCHAR(255),
    membership_status ENUM('ACTIVE', 'SUSPENDED'),
    profile_photo_url TEXT,
    payment_method ENUM('CASH', 'UPI'),
    membership_photo_url TEXT,
    gym_owner_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE enquiries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    mobile_number VARCHAR(15),
    email VARCHAR(255),
    interest_level ENUM('HIGH', 'MODERATE', 'LOW'),
    enquiry_date DATE,
    gym_owner_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE staff (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    mobile_number VARCHAR(15),
    email VARCHAR(255),
    joining_date DATE,
    gym_owner_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    profile_photo_url TEXT
);

CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    gym_owner_id BIGINT NOT NULL,
    member_id BIGINT,
    notification_type ENUM('SMS', 'WHATSAPP'),
    message TEXT,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE staff_attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    staff_id BIGINT NOT NULL,
    gym_owner_id BIGINT NOT NULL,
    date DATE NOT NULL,
    status ENUM('PRESENT', 'ABSENT', 'LEAVE') DEFAULT 'PRESENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add gender and dob to members table
ALTER TABLE members
ADD COLUMN gender ENUM('MALE', 'FEMALE', 'OTHER') DEFAULT 'MALE',
ADD COLUMN date_of_birth DATE;

-- Add gender and dob to owners table
ALTER TABLE owners
ADD COLUMN gender ENUM('MALE', 'FEMALE', 'OTHER') DEFAULT 'MALE',
ADD COLUMN date_of_birth DATE;

-- Add gender and dob to staff table
ALTER TABLE staff
ADD COLUMN gender ENUM('MALE', 'FEMALE', 'OTHER') DEFAULT 'MALE',
ADD COLUMN date_of_birth DATE;

ALTER TABLE owners
MODIFY payment_method
ENUM('CREDIT_CARD', 'BANK_TRANSFER', 'UPI', 'CARD', 'CASH');
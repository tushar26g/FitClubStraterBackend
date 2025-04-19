ALTER TABLE members
MODIFY payment_method
ENUM('CASH', 'UPI', 'CARD');

ALTER TABLE owners
MODIFY payment_method
ENUM('CREDIT_CARD', 'BANK_TRANSFER', 'UPI', 'CARD');
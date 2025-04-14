-- Add profile photo, height, and weight to members table
ALTER TABLE members
ADD COLUMN profile_photo LONGBLOB,
ADD COLUMN height_cm DOUBLE,
ADD COLUMN weight_kg DOUBLE;

-- Add profile photo to gym_owners table
ALTER TABLE owners
ADD COLUMN profile_photo LONGBLOB;

-- Add profile photo to staff table
ALTER TABLE staff
ADD COLUMN profile_photo LONGBLOB;

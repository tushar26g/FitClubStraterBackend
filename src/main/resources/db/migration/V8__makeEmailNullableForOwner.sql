-- Step 1: Drop the UNIQUE constraint if it exists
ALTER TABLE owners DROP INDEX email;

-- Step 2: Modify the email column to allow NULLs
ALTER TABLE owners MODIFY COLUMN email VARCHAR(100) NULL;

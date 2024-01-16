-- In this SQL file, write (and comment!) the typical SQL queries users will run on your database

-- Find your subscriptions
SELECT * FROM "subscriptions"
JOIN "users" ON "subscriptions"."user_id" = "users"."id"
WHERE "username" = 'Jonny123';

-- Look at at all the current subscriptions
SELECT * FROM "current_subscriptions";

-- Add new users to the database
INSERT INTO "users" ("full_name", "username", "password")
VALUES ('Enzo Carvalho', 'EnzoC2024', 'd33d57fd498d5807a93f2c7d72df4ed7');

-- Add new subscriptions into the database
INSERT INTO "subscriptions" ("user_id", "type", "payment_type")
VALUES
(5, 'Free', NULL),
(6, 'Host', 'PayPal'),
(7, 'Host+', 'MasterCard');

-- Bill new transactions for current subscricptions (this would probably run automatically every month)
INSERT INTO "transactions" ("subscription_id", "amount")
SELECT "sub_id", "amount" FROM "current_subscriptions";

-- Find all transactions that occurred in the current month
SELECT * FROM "transactions"
WHERE strftime('%m', date)
LIKE strftime('%m', CURRENT_DATE);

-- Find the total revenue of the current month
SELECT SUM("amount") AS "Monthly revenue" FROM "transactions"
WHERE strftime('%m', date)
LIKE strftime('%m', CURRENT_DATE);

-- Find all the people who ended their subscription this current month (churn)
SELECT * FROM "subscriptions"
WHERE "end" != ''
AND strftime('%m', end)
LIKE strftime('%m', CURRENT_DATE);

-- Find all reviews of 4 or more stars (out of 5)
SELECT * FROM "reviews"
WHERE "rating" >= 4;

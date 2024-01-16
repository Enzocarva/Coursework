-- In this SQL file, write (and comment!) the schema of your database, including the CREATE TABLE, CREATE INDEX, CREATE VIEW, etc. statements that compose it
-- This database aims to be a very simple version of the database of a website that sells services through subcriptions (e.g. Netflix, Wix, etc)

-- Represents the users of the website
CREATE TABLE "users" (
    "id" INTEGER,
    "full_name" TEXT NOT NULL,
    "username" TEXT NOT NULL UNIQUE,
    "password" TEXT NOT NULL,
    PRIMARY KEY("id")
);

-- The types, payment mehtod, start and end dates of the website's subcriptions
CREATE TABLE "subscriptions" (
    "id" INTEGER,
    "user_id" INTEGER,
    "type" TEXT NOT NULL DEFAULT 'Free' CHECK ("type" IN ('Host', 'Host+', 'Free')),
    "start" NUMERIC NOT NULL DEFAULT CURRENT_DATE,
    "end" NUMERIC NOT NULL DEFAULT '',
    "payment_type" TEXT DEFAULT NULL CHECK ("payment_type" IN ('Visa', 'MasterCard', 'Amex', 'PayPal')),
    PRIMARY KEY("id"),
    FOREIGN KEY("user_id") REFERENCES "users"("id")
);

-- The transactions that happen in the website in the form of its users paying for their recurring subscriptions
CREATE TABLE "transactions" (
    "id" INTEGER,
    "subscription_id" INTEGER,
    "amount" REAL NOT NULL,
    "date" NUMERIC NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY("id"),
    FOREIGN KEY("subscription_id") REFERENCES "subscriptions"("id")
);

-- The users' reviews of the website and its services as a whole
CREATE TABLE "reviews" (
    "id" INTEGER,
    "user_id" INTEGER,
    "rating" INTEGER CHECK("rating" IN(1, 2, 3, 4, 5)),
    "contents" TEXT NOT NULL,
    PRIMARY KEY("id"),
    FOREIGN KEY("user_id") REFERENCES "users"("id")
);

-- A view that gathers the most important and essential data to track, an easy way to look at all the current subscriptions tha are generating income
CREATE VIEW "current_subscriptions" AS
SELECT "subscriptions"."id" AS "sub_id", "username", "type", "start", "payment_type", "amount"
FROM "subscriptions"
JOIN "users" ON "subscriptions"."user_id" = "users"."id"
JOIN "transactions" ON "subscriptions"."id" = "transactions"."subscription_id"
WHERE "end" = '' AND "type" != 'Free'
GROUP BY "subscription_id"
ORDER BY "start" DESC;

-- Automatically make a transaction charging the user their new subscription amount
CREATE TRIGGER "create_transaction"
AFTER INSERT ON "subscriptions"
FOR EACH ROW
WHEN NEW."type" != 'Free'
BEGIN
    INSERT INTO "transactions" ("subscription_id", "amount")
    VALUES (NEW."id",
        CASE NEW."type"
            WHEN 'Host' THEN 97.99
            ELSE 148.99
        END
    );
END;

-- Every time a uesr switches from one subscription to another this trigger automatically sets an end date to their current subscription
CREATE TRIGGER "end_old_subscription"
BEFORE INSERT ON "subscriptions"
FOR EACH ROW
WHEN NEW."user_id" IN (
    SELECT "user_id" FROM "subscriptions"
)
BEGIN
    UPDATE "subscriptions"
    SET "end" = CURRENT_DATE
    WHERE "id" = (
        SELECT "id" FROM "subscriptions"
        WHERE "user_id" = NEW."user_id"
        ORDER BY "id" DESC
        LIMIT 1
    );
END;

-- Speed up looking up reviews based on their rating
CREATE INDEX "reviews_rating_index" ON "reviews" ("rating");

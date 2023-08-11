-- Keep a log of any SQL queries you execute as you solve the mystery.

-- Find description from crime_scene_reports table:
SELECT description FROM crime_scene_reports
 WHERE year = 2021
 AND month = 07
 AND day = 28
 AND street = 'Humphrey Street';


    --Theft of the CS50 duck took place at 10:15am at the Humphrey Street bakery.
    --Interviews were conducted today with three witnesses who were present at the time – each of
    --their interview transcripts mentions the bakery. Littering took place at 16:36. No known witnesses.


-- Find the people who had interviews on the same date as the crime (July 28th, 2021)
SELECT name FROM interviews
 WHERE year = 2021
 AND month = 07
 AND day = 28;
    --Jose, Eugene, Barbara, Ruth, Raymond, Lily

-- Find interviews of the three witnesses (Eugene, Ruth and Raymond)
SELECT transcript FROM interviews
 WHERE name = 'Eugene';

    --“I suppose,” said Holmes, “that when Mr. Windibank came back from France he was very annoyed at
    --your having gone to the ball.” I don't know the thief's name, but it was someone I recognized.
    --Earlier this morning, before I arrived at Emma's bakery, I was walking by the ATM on
    --Leggett Street and saw the thief there withdrawing some money.

SELECT transcript FROM interviews
 WHERE name = 'Ruth';

    --“I will get her to show me.” Sometime within ten minutes of the theft, I saw the thief get into a car in the
    --bakery parking lot and drive away. If you have security footage from the bakery parking lot,
    --you might want to look for cars that left the parking lot in that time frame

SELECT transcript FROM interviews
 WHERE name = 'Raymond';

    --“Good-night, Mister Sherlock Holmes.” As the thief was leaving the bakery, they called someone who talked
    --to them for less than a minute. In the call, I heard the thief say that they were planning to take the
    --earliest flight out of Fiftyville tomorrow. The thief then asked the person on the other end of
    --the phone to purchase the flight ticket.


-- Find ATM transcations from where and when the theif made his/hers
SELECT id, transaction_type, amount, account_number FROM atm_transactions
 WHERE atm_location = 'Leggett Street'
 AND year = 2021
 AND month = 07
 AND day = 28;

    -- --+-----+------------------+--------+----------------+
    -- | id  | transaction_type | amount | account_number |
    -- +-----+------------------+--------+----------------+
    -- | 246 | withdraw         | 48     | 28500762       |
    -- | 264 | withdraw         | 20     | 28296815       |
    -- | 266 | withdraw         | 60     | 76054385       |
    -- | 267 | withdraw         | 50     | 49610011       |
    -- | 269 | withdraw         | 80     | 16153065       |
    -- | 275 | deposit          | 10     | 86363979       |
    -- | 288 | withdraw         | 20     | 25506511       |
    -- | 313 | withdraw         | 30     | 81061156       |
    -- | 336 | withdraw         | 35     | 26013199       |
    -- +-----+------------------+--------+----------------+


-- Find license plate from security footage from bakery
SELECT id, activity, license_plate FROM bakery_security_logs
 WHERE year = 2021
 AND month = 07
 AND day = 28
 AND hour = 10
 AND minute > 14
 AND minute < 26;

    -- +-----+----------+---------------+
    -- | id  | activity | license_plate |
    -- +-----+----------+---------------+
    -- | 260 | exit     | 5P2BI95       |
    -- | 261 | exit     | 94KL13X       |
    -- | 262 | exit     | 6P58WS2       |
    -- | 263 | exit     | 4328GD8       |
    -- | 264 | exit     | G412CB7       |
    -- | 265 | exit     | L93JTIZ       |
    -- | 266 | exit     | 322W7JE       |
    -- | 267 | exit     | 0NTHK55       |
    -- +-----+----------+---------------+

-- Find flights from Fiftyville on the day the thief left (July 29th, 2021)
SELECT * FROM flights
 WHERE year = 2021
 AND month = 07
 AND day = 29
 AND origin_airport_id = (SELECT id FROM airports WHERE city = 'Fiftyville');

    -- +----+-------------------+------------------------+------+-------+-----+------+--------+
    -- | id | origin_airport_id | destination_airport_id | year | month | day | hour | minute |
    -- +----+-------------------+------------------------+------+-------+-----+------+--------+
    -- | 18 | 8                 | 6                      | 2021 | 7     | 29  | 16   | 0      |
    -- | 23 | 8                 | 11                     | 2021 | 7     | 29  | 12   | 15     |
    -- | 36 | 8                 | 4                      | 2021 | 7     | 29  | 8    | 20     |
    -- | 43 | 8                 | 1                      | 2021 | 7     | 29  | 9    | 30     |
    -- | 53 | 8                 | 9                      | 2021 | 7     | 29  | 15   | 20     |
    -- +----+-------------------+------------------------+------+-------+-----+------+--------+


-- Find the passport number of everyone in the earliest flight out of Fiftyville on the 29th (flight_id = 36)
SELECT passport_number FROM passengers
 WHERE flight_id = 36;

    -- +-----------------+
    -- | passport_number |
    -- +-----------------+
    -- | 7214083635      |
    -- | 1695452385      |
    -- | 5773159633      |
    -- | 1540955065      |
    -- | 8294398571      |
    -- | 1988161715      |
    -- | 9878712108      |
    -- | 8496433585      |
    -- +-----------------+


-- Find person_id's from the account numbers obtained from the atm_transactions table based on what witness saw
SELECT bank_accounts.person_id FROM bank_accounts
 WHERE account_number IN
    (SELECT account_number FROM atm_transactions
      WHERE atm_location = 'Leggett Street'
      AND year = 2021
      AND month = 07
      AND day = 28);

    -- +-----------+
    -- | person_id |
    -- +-----------+
    -- | 686048    |
    -- | 948985    |
    -- | 514354    |
    -- | 458378    |
    -- | 395717    |
    -- | 396669    |
    -- | 467400    |
    -- | 449774    |
    -- | 438727    |
    -- +-----------+


-- Find caller and receiver of phone call the thief made to his accomplice to buy his flight
SELECT id, caller, receiver, duration FROM phone_calls
 WHERE year = 2021
 AND month = 07
 AND day = 28
 AND duration < 60; --duration is in seconds

    -- +-----+----------------+----------------+----------+
    -- | id  |     caller     |    receiver    | duration |
    -- +-----+----------------+----------------+----------+
    -- | 221 | (130) 555-0289 | (996) 555-8899 | 51       |
    -- | 224 | (499) 555-9472 | (892) 555-8872 | 36       |
    -- | 233 | (367) 555-5533 | (375) 555-8161 | 45       |
    -- | 251 | (499) 555-9472 | (717) 555-1342 | 50       |
    -- | 254 | (286) 555-6063 | (676) 555-6554 | 43       |
    -- | 255 | (770) 555-1861 | (725) 555-3243 | 49       |
    -- | 261 | (031) 555-6622 | (910) 555-3251 | 38       |
    -- | 279 | (826) 555-1652 | (066) 555-9701 | 55       |
    -- | 281 | (338) 555-6650 | (704) 555-2131 | 54       |
    -- +-----+----------------+----------------+----------+


    -- +--------+---------+----------------+-----------------+---------------+
    -- |   id   |  name   |  phone_number  | passport_number | license_plate |
    -- +--------+---------+----------------+-----------------+---------------+
    -- | 395717 | Kenny   | (826) 555-1652 | 9878712108      | 30G67EN       |
    -- | 398010 | Sofia   | (130) 555-0289 | 1695452385      | G412CB7       |
    -- | 438727 | Benista | (338) 555-6650 | 9586786673      | 8X428L0       |
    -- | 449774 | Taylor  | (286) 555-6063 | 1988161715      | 1106N58       |
    -- | 514354 | Diana   | (770) 555-1861 | 3592750733      | 322W7JE       |
    -- | 560886 | Kelsey  | (499) 555-9472 | 8294398571      | 0NTHK55       |
    -- | 686048 | Bruce   | (367) 555-5533 | 5773159633      | 94KL13X       |
    -- | 907148 | Carina  | (031) 555-6622 | 9628244268      | Q12B3Z3       |
    -- +--------+---------+----------------+-----------------+---------------


-- Find thief's name
SELECT * FROM people
 WHERE phone_number IN
    (SELECT caller FROM phone_calls
     WHERE year = 2021
     AND month = 07
     AND day = 28
     AND duration < 60)
        AND passport_number IN
         (SELECT passport_number FROM passengers
          WHERE flight_id = 36)
            AND license_plate IN
             (SELECT license_plate FROM bakery_security_logs
              WHERE year = 2021
              AND month = 07
              AND day = 28
              AND hour = 10
              AND minute > 14
              AND minute < 26)
                AND id IN
                (SELECT bank_accounts.person_id FROM bank_accounts
                 WHERE account_number IN
                    (SELECT account_number FROM atm_transactions
                     WHERE atm_location = 'Leggett Street'
                     AND year = 2021
                     AND month = 07
                     AND day = 28));

    -- +--------+-------+----------------+-----------------+---------------+
    -- |   id   | name  |  phone_number  | passport_number | license_plate |
    -- +--------+-------+----------------+-----------------+---------------+
    -- | 686048 | Bruce | (367) 555-5533 | 5773159633      | 94KL13X       |
    -- +--------+-------+----------------+-----------------+---------------+

-- City the thief escaped to (id 4 was found based on the flight logs from flight 36,
-- which was the earliest flight out of Fiftyville)
SELECT city FROM airports WHERE id = 4;

    -- +---------------+
    -- |     city      |
    -- +---------------+
    -- | New York City |
    -- +---------------+

-- Find accomplice
SELECT name FROM people
 WHERE phone_number = '(375) 555-8161';

    -- +-------+
    -- | name  |
    -- +-------+
    -- | Robin |
    -- +-------+

-- BATMAN AND ROBIN AND BATMAN LEFT TO NYC!

-- *** The Lost Letter ***

-- Get all the packages that came to and from Varsha's place (returns nothing)
SELECT * FROM scans
JOIN addresses ON addresses.id = scans.address_id
WHERE addresses.address = '2 Finnegan Street';

-- Get all the packages that came to and from Anneke's place
SELECT * FROM scans
JOIN addresses ON addresses.id = scans.address_id
WHERE addresses.address = '900 Somerville Avenue';

-- Output:
-- +-------+-----------+------------+------------+--------+----------------------------+-----+-----------------------+-------------+
-- |  id   | driver_id | package_id | address_id | action |         timestamp          | id  |        address        |    type     |
-- +-------+-----------+------------+------------+--------+----------------------------+-----+-----------------------+-------------+
-- | 54    | 1         | 384        | 432        | Pick   | 2023-07-11 19:33:55.241794 | 432 | 900 Somerville Avenue | Residential |
-- | 1323  | 9         | 5436       | 432        | Pick   | 2023-07-16 04:44:53.137720 | 432 | 900 Somerville Avenue | Residential |
-- | 11912 | 20        | 2437       | 432        | Pick   | 2023-08-22 00:39:15.313132 | 432 | 900 Somerville Avenue | Residential |
-- | 17581 | 13        | 3529       | 432        | Pick   | 2023-09-10 20:01:41.833214 | 432 | 900 Somerville Avenue | Residential |
-- +-------+-----------+------------+------------+--------+----------------------------+-----+-----------------------+-------------+

-- Get information on every package Anneke sent
SELECT * FROM packages WHERE id IN (
    SELECT package_id FROM scans
    JOIN addresses ON addresses.id = scans.address_id WHERE addresses.address = '900 Somerville Avenue'
);

-- Output:
-- +------+-----------------------+-----------------+---------------+
-- |  id  |       contents        | from_address_id | to_address_id |
-- +------+-----------------------+-----------------+---------------+
-- | 384  | Congratulatory letter | 432             | 854           |
-- | 2437 | String                | 432             | 484           |
-- | 3529 | Letter opener         | 432             | 585           |
-- | 5436 | Whiteboard            | 432             | 4984          |
-- +------+-----------------------+-----------------+---------------+

-- Get all scan information on "Congratulatory letter" package
SELECT * FROM scans WHERE package_id = 384;

-- Output:
-- +----+-----------+------------+------------+--------+----------------------------+
-- | id | driver_id | package_id | address_id | action |         timestamp          |
-- +----+-----------+------------+------------+--------+----------------------------+
-- | 54 | 1         | 384        | 432        | Pick   | 2023-07-11 19:33:55.241794 |
-- | 94 | 1         | 384        | 854        | Drop   | 2023-07-11 23:07:04.432178 |
-- +----+-----------+------------+------------+--------+----------------------------+

-- Get all adress information on address id '854'
SELECT * FROM addresses WHERE id = 854;

-- Output:
-- +-----+-------------------+-------------+
-- | id  |      address      |    type     |
-- +-----+-------------------+-------------+
-- | 854 | 2 Finnigan Street | Residential |
-- +-----+-------------------+-------------+


-- *** The Devious Delivery ***

-- Get all packages with no from address
SELECT * FROM packages WHERE from_address_id IS NULL;

-- Output:
-- +------+---------------+-----------------+---------------+
-- |  id  |   contents    | from_address_id | to_address_id |
-- +------+---------------+-----------------+---------------+
-- | 5098 | Duck debugger | NULL            | 50            |
-- +------+---------------+-----------------+---------------+

-- Get the address where the package was dropped off
SELECT * FROM addresses WHERE id IN (
    SELECT address_id FROM scans WHERE package_id = 5098 AND action = 'Drop'
);

-- Output:
-- +-----+------------------+----------------+
-- | id  |     address      |      type      |
-- +-----+------------------+----------------+
-- | 348 | 7 Humboldt Place | Police Station |
-- +-----+------------------+----------------+


-- *** The Forgotten Gift ***

-- Find the address ids of both the to and from addresses
SELECT * FROM addresses WHERE address = '728 Maple Place' OR address = '109 Tileston Street';

-- Output:
+------+---------------------+-------------+
|  id  |       address       |    type     |
+------+---------------------+-------------+
| 4983 | 728 Maple Place     | Residential |
| 9873 | 109 Tileston Street | Residential |
+------+---------------------+-------------+

-- Get the package that has the to and from address ids retrieved above
SELECT * FROM packages WHERE from_address_id = 9873 AND to_address_id = 4983;

-- Output:
+------+----------+-----------------+---------------+
|  id  | contents | from_address_id | to_address_id |
+------+----------+-----------------+---------------+
| 9523 | Flowers  | 9873            | 4983          |
+------+----------+-----------------+---------------+

-- Get all the scan information based on the retrieved package id above
SELECT * FROM scans WHERE package_id = 9523;

-- Output:
-- +-------+-----------+------------+------------+--------+----------------------------+
-- |  id   | driver_id | package_id | address_id | action |         timestamp          |
-- +-------+-----------+------------+------------+--------+----------------------------+
-- | 10432 | 11        | 9523       | 9873       | Pick   | 2023-08-16 21:41:43.219831 |
-- | 10500 | 11        | 9523       | 7432       | Drop   | 2023-08-17 03:31:36.856889 |
-- | 12432 | 17        | 9523       | 7432       | Pick   | 2023-08-23 19:41:47.913410 |
-- +-------+-----------+------------+------------+--------+----------------------------+

-- Get the driver's name who last picked up the package
SELECT name FROM drivers WHERE id = 17;

-- Output
-- +-------+
-- | name  |
-- +-------+
-- | Mikel |
-- +-------+

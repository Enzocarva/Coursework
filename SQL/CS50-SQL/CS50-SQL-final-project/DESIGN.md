# Design Document

By Enzo Carvalho

Video overview: <https://youtu.be/xxBj9c4GqNE>

## Scope

In this section you should answer the following questions:

* What is the purpose of your database?

The purpose of my database is to simulate what could be the very basic, yet fully functional database of a website that sells some kind of service by charging monthly subscriptions (Netflix, Wix, etc.). The main goal is just to store the subscriptions' information and how well the website is performing from a monetary/business standpoint.

* Which people, places, things, etc. are you including in the scope of your database?

The scope of my database is quite limited, it entails the **users** of the website, **reviews** of the website/business as a whole, and most importantly the **subscriptions** and their relative **transactions**. Specifically, the database will at most allow the website/business owner and administrators to inspect how well (or poorly) the business is doing based on its number of active subscriptions and how much revenue they are bringing in.

* Which people, places, things, etc. are *outside* the scope of your database?

Considering this is `supposed` to be a legitimate website's database, there are quite a few key things outside its scope. There is nothing tracking the users at all except for if they sign up for a subscription or not. Another key feature would be getting more information from the users like email, phone number and address. In other words, everything outside of the actual subscriptions and reviews are outside of the scope of this database.

## Functional Requirements

In this section you should answer the following questions:

* What should a user be able to do with your database?

The administrators can perform CRUD operations on the users and their relative subscriptions, reviews and transactions. The users will have limited access to the database and operations they can perform. Some sample operations: Add new users to the database, look at at all the current subscriptions, add new subscriptions into the database, find all transactions that occurred in the current month.

* What's beyond the scope of what a user should be able to do with your database?

The only people with full access are the administrators of the webiste, the usesrs will only be able to manipulate their own data. Anything beyond the following is outside of the scope of the database: Full CRUD on their own users table value, read and update only their own subscriptions, reviews, and only read their transactions.

## Representation

The entities are represented in SQLite tables with the schema described below.

### Entities

In this section you should answer the following questions:

* Which entities will you choose to represent in your database?
* What attributes will those entities have?
* Why did you choose the types you did?
* Why did you choose the constraints you did?

The database has the following entities:

#### Users
* `id`, represents a unique ID for each user with the `INTEGER` type affinity. This column has the constraint `PRIMARY KEY` on it.
* `full_name`, reporesents the full name of the user with the `TEXT` type affinity since it is the best choice for storing name fields.
* `username`, reporesents the username of the user with the `TEXT` type affinity for the same reason as `full_name`. The column constraint `UNIQUE` is applied to ensure that all usernames are distinct.
* `password`, represents the hashed (I used MD5 here for demonstration only, but in reality I would use a more secure hashing function) password of the user with the `TEXT` type affinity for the same reason as `full_name`.

#### Subscriptions
* `id`, represents a unique ID for each subscription with the `INTEGER` type affinity. This column has the constraint `PRIMARY KEY` on it.
* `user_id`, represents the ID of the user that owns this subscription with the `INTEGER` type affinity. The column constraint `FOREIGN KEY` is applied referencing the `id` column from the `users` table to maintain data security.
* `type`, represents the type of subscription out of 3 different options, 'Free', 'Host' and 'Host+' using the `TEXT` type affinity since, unsurprisingly, it is the best choice to text fields. The column constraints `DEFAULT` and `CHECK` are applied to set `type` to 'Free' if nothing is specified, and to ensure the 3 options are the only ones that can be stored, respectively.
* `start`, represents when the subscription started using the `NUMERIC` type affinity since it is the designated choice to store dates and timestamps per SQLite's documentation at <https://www.sqlite.org/datatype3.html>. The column constraint `DEFAULT` is applied to set `start` to the current date using the SQLite keyword `CURRENT_DATE`.
* `end`, represents the date in which the subscription ended using the `NUMERIC` type affinity for the same reason as `start`. The column constraint `DEFAULT` is used to set `end` to a blank space (not ended) unless otherwise specified.
* `payment_type`, represents the method of payment the user chose using the `TEXT` type affinity for the same reason as in `type`. The column constraints `DEFAULT` and `CHECK` are applied for the same reasons as in `type`, but with different options: 'NULL' as the default, 'Visa', 'Amex', 'MasterCard', and 'PayPal'.

#### Transactions
* `id`, represents a unique ID for each transaction with the `INTEGER` type affinity. This column has the constraint `PRIMARY KEY` on it.
* `subscription_id`,represents the ID of the subscription to which the transaction belongs to, with the `INTEGER` type affinity. The column constraint `FOREIGN KEY` is applied referencing the `id` column from the `subscriptions` table to maintain data security.
* `amount`, represents the amount of money being transferred in the transaction. It uses the `REAL` type affinity to represent money as a floating point number (e.g. 97.99 represents $97 and 97 cents).
* `date`, represents the date and time the transaction took place using the `NUMERIC` type affinity for the same reason as `start` in the **Subscriptions** table and entitiy section. The column constraint `DEFAULT` is applied to set `date` to the current date and time using the SQLite keyword `CURRENT_TIMESTAMP`.

#### Reviews
* `id`, represents a unique ID for each review with the `INTEGER` type affinity. This column has the constraint `PRIMARY KEY` on it.
* `user_id`, represents the ID of the user that wrote this review with the `INTEGER` type affinity. The column constraint `FOREIGN KEY` is applied referencing the `id` column from the `users` table to maintain data security.
* `rating`, represents the rating out of 5 that the user chose on their review with the `INTEGER` type affinity, the ratings will be diplayed as integers and not floating point values. The column constraint `CHECK` is uesd to ensure the rating can only be the integers 1 through 5.
* `contents`, represents the written message of the review with the `TEXT` type affinity since it is ideal for storing text fields.

All columns are required to have data, therefore they all have the `NOT NULL` column constraint on, given they are not a `PRIMARY KEY` or `FOREIGN KEY`.

### Relationships

In this section you should include your entity relationship diagram and describe the relationships between the entities in your database.

![ER Diagram](CS50_database_final_ER_diagram.png)

* A user can write 0 or many reviews. 0 if they never choose to leave a review and many if they choose to write multiple. A review can have one and only one user (author). It is assumed that no two users will collaborate to write a single review.
* A user can have 0 or many subscriptions. 0 if they just signed up with to the website and haven't yet signed up for a subscription, and many if they have had more than one subscription (including a free one). A subscription has one and only one user, users cannot share subscriptions.
* A subscription has 0 or many transactions. 0 if it's a free subscription, and many if it's a paid subscription that has been active for more than one month.

## Optimizations

In this section you should answer the following questions:

* Which optimizations (e.g., indexes, views) did you create? Why?

There is one view called `current_subscriptions` that shows all of the most important data, which are the current active subscriptions. It shows the subscription id, usernam, type, start date and payment type of every active subscription to fascilitate using this data.

There are two triggers: `create_transaction` automatically creates a transaction with the appropriate amount for a new subscription. `end_old_subscription` sets the end date (terminate) of a current subscription when a new one is added by a user that already had an existing subscription. Both of these were made to automate and speed up some of the more common tasks the database has to do.

The only index created is the `reviews_rating_index`, because as per the `queries.sql`, it is common for the administrators and users to look up reviews of the website based on their rating number. Therefore, indexing the `rating` column in the `reviews` table is worthwhile.

Almost all of the other queries used the `subscription`'s `date` column, but unfortunately, due to all of those queries using the `strftime()` function, indexes don't speed up those queries. Therefore no other indexes were made, but a valid alternative would be to make these queries less automated (don't use `strftime`), so that more indexes can be implemented.

## Limitations

In this section you should answer the following questions:

* What are the limitations of your design?
* What might your database not be able to represent very well?

This database assumes a subscription is individual and can't be shared. In order to support group subscriptions, there would need to be a shift to a many to many relationship between users and subscriptions.

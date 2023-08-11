--In 13.sql, write a SQL query to list the names of all people who starred
--in a movie in which Kevin Bacon also starred.

SELECT DISTINCT name FROM people
JOIN stars ON people.id = stars.person_id
JOIN movies ON movies.id = stars.movie_id
WHERE title IN
(SELECT title FROM movies
JOIN stars ON stars.movie_id = movies.id
JOIN people ON people.id = stars.person_id
WHERE name = 'Kevin Bacon' AND birth = 1958)
AND name != 'Kevin Bacon';
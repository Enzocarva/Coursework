SELECT first_name, last_name, (salary / H) AS 'dollars per hit' FROM players
JOIN salaries ON players.id = salaries.player_id
JOIN performances ON players.id = performances.player_id
WHERE salaries.year = 2001 AND performances.year = 2001
AND "dollars per hit" IS NOT NULL
ORDER BY "dollars per hit" ASC, first_name, last_name
LIMIT 10;

SELECT first_name, last_name, salary, salaries.year AS year, HR FROM players
JOIN salaries ON players.id = salaries.player_id
JOIN performances ON players.id = performances.player_id
WHERE salaries.year = performances.year
ORDER BY players.id, salaries.year DESC, HR DESC, salary DESC;

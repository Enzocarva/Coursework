SELECT salary FROM salaries
JOIN performances ON salaries.player_id = performances.player_id
WHERE performances.year = 2001 AND salaries.year = 2001
ORDER BY "HR" DESC
LIMIT 1;

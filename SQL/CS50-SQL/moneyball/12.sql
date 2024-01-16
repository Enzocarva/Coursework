SELECT first_name, last_name FROM players
WHERE players.id IN (
    SELECT players.id FROM players
    JOIN salaries ON players.id = salaries.player_id
    JOIN performances ON players.id = performances.player_id
    WHERE salaries.year = 2001 AND performances.year = 2001
    AND (salary / H)
    AND (salary / H) IS NOT NULL
    ORDER BY (salary / H) ASC, first_name, last_name
    LIMIT 10
) AND players.id IN (
    SELECT players.id FROM players
    JOIN salaries ON players.id = salaries.player_id
    JOIN performances ON players.id = performances.player_id
    WHERE salaries.year = 2001 AND performances.year = 2001
    AND (salary / RBI)
    AND (salary / RBI) IS NOT NULL
    ORDER BY (salary / RBI) ASC, first_name, last_name
    LIMIT 10
) ORDER BY players.id;

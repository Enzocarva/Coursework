SELECT username FROM users
JOIN messages ON users.id = to_user_id
GROUP BY to_user_id
ORDER BY COUNT(to_user_id) DESC, username
LIMIT 1;

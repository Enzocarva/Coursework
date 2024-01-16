SELECT friend_id FROM friends
JOIN users ON users.id = friends.user_id
WHERE username = 'lovelytrust487'
INTERSECT
SELECT friend_id FROM friends
JOIN users ON users.id = friends.user_id
WHERE username = 'exceptionalinspiration482';

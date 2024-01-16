SELECT city, COUNT(city) AS 'Num of Schools' FROM schools
WHERE type = 'Public School' GROUP BY city
ORDER BY "Num of Schools" DESC, city LIMIT 10;

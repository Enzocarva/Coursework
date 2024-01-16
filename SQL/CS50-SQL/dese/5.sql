SELECT city, COUNT(city) AS 'Num of Schools' FROM schools
WHERE type = 'Public School' GROUP BY city
HAVING "Num of Schools" <= 3
ORDER BY "Num of Schools" DESC, city;

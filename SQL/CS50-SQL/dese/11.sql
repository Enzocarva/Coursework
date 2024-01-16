SELECT name, per_pupil_expenditure, graduated
FROM schools JOIN expenditures ON schools.district_id = expenditures.district_id
JOIN graduation_rates ON graduation_rates.school_id = schools.id
ORDER BY per_pupil_expenditure DESC, name;

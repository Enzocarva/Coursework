SELECT type, graduated FROM schools JOIN graduation_rates ON schools.id = graduation_rates.school_id GROUP BY type;

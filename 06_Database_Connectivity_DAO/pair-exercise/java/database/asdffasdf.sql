SELECT * FROM employee e
WHERE e.employee_id NOT IN (SELECT pe.employee_id FROM project_employee pe);


SELECT * FROM project 
WHERE ((to_date > CURRENT_DATE) 
OR (to_date IS NULL))
AND (from_date IS NOT NULL);

SELECT * FROM project p
WHERE (to_date > CURRENT_DATE);

SELECT * FROM project p
WHERE (to_date IS NULL)
AND (from_date IS NOT NULL);

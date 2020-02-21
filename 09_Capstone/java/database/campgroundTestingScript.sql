SELECT * FROM campground JOIN park ON park.park_id = campground.park_id
WHERE park.name = 'Acadia';

SELECT * FROM reservation WHERE site_id = 1 AND from_date = '2020-03-01' AND to_date = '2020-03-02';

SELECT * FROM reservation WHERE site_id = 1 AND from_date = '2020-02-14' AND to_date = '2020-02-17';

SELECT * FROM reservation WHERE site_id = 1 AND from_date BETWEEN '2020-02-14' AND '2020-03-02';


SELECT * FROM site s JOIN reservation r ON r.site_id = s.site_id
 WHERE((to_date > CURRENT_DATE) OR (to_date IS NULL)) AND (from_date IS NOT NULL); 
 
 
SELECT site_number, max_occupancy, accessible, max_rv_length, utilities, r.name, SUM(cg.daily_fee * (to_date - from_date)) AS total_fee
 FROM site s JOIN reservation r ON r.site_id = s.site_id
 JOIN campground cg ON cg.campground_id = s.campground_id
 WHERE --NOT (SELECT ((to_date > CURRENT_DATE) OR (to_date IS NULL)) AND (from_date IS NOT NULL)) 
   (cg.campground_id = 1)
 GROUP BY s.site_id, s.site_number, s.max_occupancy, s.accessible, s.max_rv_length, s.utilities, r.name
 ORDER BY s.site_id ASC LIMIT 5;
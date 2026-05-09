INSERT INTO availability_slots (provider_id, start_time, end_time, status)
SELECT 1, '2026-04-23 10:00', '2026-04-23 10:30', 'OPEN'
WHERE NOT EXISTS (SELECT 1 FROM availability_slots WHERE id = 1);

INSERT INTO availability_slots (provider_id, start_time, end_time, status)
SELECT 2, '2026-04-24 13:00', '2026-04-24 13:30', 'OPEN'
WHERE NOT EXISTS (SELECT 1 FROM availability_slots WHERE id = 2);

INSERT INTO availability_slots (provider_id, start_time, end_time, status)
SELECT 3, '2026-04-25 15:30', '2026-04-25 16:00', 'OPEN'
WHERE NOT EXISTS (SELECT 1 FROM availability_slots WHERE id = 3);

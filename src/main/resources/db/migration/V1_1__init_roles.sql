INSERT INTO roles
(id, role)
SELECT '1', 'ADMIN'
WHERE
NOT EXISTS (
SELECT id FROM roles WHERE id = '1'
);

INSERT INTO roles
(id, role)
SELECT '2', 'USER'
WHERE
NOT EXISTS (
SELECT id FROM roles WHERE id = '2'
);
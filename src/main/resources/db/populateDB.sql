DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2022-06-15 10:00', 'Завтрак юзера', 600, 100000),
       ('2022-06-15 15:00', 'Обед юзера', 1000, 100000),
       ('2022-06-16 00:00', 'Ночные перекусы юзера', 333, 100000),
       ('2022-06-15 10:00', 'Завтрак админа', 400, 100001);
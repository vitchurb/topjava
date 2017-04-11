DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (dateTime, description, calories, user_id) VALUES
  ('20170110 13:42', 'Завтрак', 515, 100000),
  ('20170111 17:02', 'Обед', 1054, 100000),
  ('20170111 21:58', 'Ужин', 101, 100000),
  ('20170110 10:03', 'Завтрак', 500, 100001),
  ('20170111 11:00', 'Завтрак', 700, 100001)


INSERT INTO user (username, email, password) VALUES ('user', 'user@user', '$2a$08$abOsyGdgBFbuM4Z83IwzIuX0PwNpm07Sapqy/JZqXlP9FNDR98eSu')
INSERT INTO user (username, email, password) VALUES ('admin', 'admin@admin', '$2a$08$zAg6oF4ZjT3yuU5bFxr/qu0o6bIdYiH5BGnW/tKoUYXKxsUMe4YRy')
INSERT INTO role (name) VALUES ('ROLE_USER')
INSERT INTO role (name) VALUES ('ROLE_ADMIN')
INSERT INTO user_role (user_id, roles_id) VALUES (1,1)
INSERT INTO user_role (user_id, roles_id) VALUES (2,1)
INSERT INTO user_role (user_id, roles_id) VALUES (2,2)
INSERT INTO role(name) values("ROLE_USER");
INSERT INTO role(name) values("ROLE_WATCHMAN");
INSERT INTO role(name) values("ROLE_ADMIN");

INSERT INTO users (email, name, password, phone_number, photo_url) VALUES ('diomedesdiaz@unicesar.edu.co', 'Diomedes Díaz', 'asd1234', '3012345678', 'https://i.pravatar.com/150?img=1');

INSERT INTO users (email, name, password, phone_number, photo_url) VALUES ('arianayrom@unicesar.edu.co', 'Ariana Romero', '4321dsa', '3019876543', 'https://i.pravatar.com/150?img=2');

INSERT INTO users (email, name, password, phone_number, photo_url) VALUES ('juanalbertomunozdavid@unicesar.edo.co', 'Juan Alberto Munoz', 'asd1234', '3011234567', 'https://i.pravatar.com/150?img=3');

INSERT INTO users (email, name, password, phone_number, photo_url) VALUES ('manuelamartinezgarcia@unicesar.edu.co', 'Manuela Martinez', '4321dsa', '3019876543', 'https://i.pravatar.com/150?img=2');

INSERT INTO users (email, name, password, phone_number, photo_url) VALUES ('andresriveraperez@unicesar.edu.co', 'Andres Rivera', 'asd1234', '3017654321', 'https://i.pravatar.com/150?img=1');

INSERT INTO users (email, name, password, phone_number, photo_url) VALUES ('luisafernandalopezramirez@unicesar.edu.co', 'Luisa Fernanda Lopez', '4321dsa', '3015432167', 'https://i.pravatar.com/150?img=4');

INSERT INTO users (email, name, password, phone_number, photo_url) VALUES ('carolinamartinezrodriguez@unicesar.edu.co', 'Carolina Martinez', 'asd1234', '3016543217', 'https://i.pravatar.com/3');

INSERT INTO users (email, name, password, phone_number, photo_url) VALUES ('juancamilomendezsanchez@unicesar.edu.co', 'Juan Camilo Mendez', '4321dsa', '3015432168', 'https://i.pravatar.com/4');

INSERT INTO users (email, name, password, phone_number, photo_url) VALUES ('julianaramirezgomez@unicesar.edu.co', 'Juliana Ramirez', 'asd1234', '3014321679', 'https://i.pravatar.com/5');

INSERT INTO user_role (id, role_id, user_id) VALUES (1, 1, 1);
INSERT INTO user_role (id, role_id, user_id) VALUES (2, 1, 2);
INSERT INTO user_role (id, role_id, user_id) VALUES (3, 1, 3);
INSERT INTO user_role (id, role_id, user_id) VALUES (4, 1, 4);
INSERT INTO user_role (id, role_id, user_id) VALUES (5, 1, 5);
INSERT INTO user_role (id, role_id, user_id) VALUES (6, 1, 6);
INSERT INTO user_role (id, role_id, user_id) VALUES (7, 1, 7);
INSERT INTO user_role (id, role_id, user_id) VALUES (8, 2, 8);
INSERT INTO user_role (id, role_id, user_id) VALUES (9, 2, 9);

INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2015, 1, 'YAMAHA', 'MX 125', 'DSA123', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2018, 1, 'BAJAJ', 'BOXER', 'ROB121', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2011, 2, 'KAWAZAKI', 'KS 125', 'ASD123', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2021, 3, 'KTM', 'BOLD', 'ZCZ314', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2018, 4, 'SUZUKI', 'RACER', 'XAX12E', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2012, 4, 'HONDA', 'REBEL 250', 'XAM21Q', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2013, 4, 'HONDA', 'APE 100', 'QRA421', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2022, 5, 'HONDA', 'XR', 'AKC12A', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2011, 5, 'SUZUKI', 'DALOT GT', 'ICH415', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (1840, 5, 'KAWAZAKI', 'PARKER', 'KER101', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2001, 6, 'BAJAJ', 'CT 100', 'LAM999', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2015, 6, 'KAWAZAKI', 'NINJA', 'MAN81K', true);
INSERT INTO vehicle (model, owner_id, brand, line, plate,is_parked) VALUES (2022, 6, 'SUZUKI', 'C90', 'KIR141', true);

INSERT INTO confidence_circle (user_id, confidence_circle_id) VALUES (1, 2);
INSERT INTO confidence_circle (user_id, confidence_circle_id) VALUES (1, 3);
INSERT INTO confidence_circle (user_id, confidence_circle_id) VALUES (2, 4);
INSERT INTO confidence_circle (user_id, confidence_circle_id) VALUES (3, 1);
INSERT INTO confidence_circle (user_id, confidence_circle_id) VALUES (4, 5);
INSERT INTO confidence_circle (user_id, confidence_circle_id) VALUES (4, 6);
INSERT INTO confidence_circle (user_id, confidence_circle_id) VALUES (4, 7);
INSERT INTO confidence_circle (user_id, confidence_circle_id) VALUES (5, 6);



drop table if exists users, commandes, compizzas, ingpizzas, pizzas, ingredients cascade;

create table ingredients (ino int primary key, name text, prix float);
insert into ingredients values (1,'pomme de terre',0.3),
(2,'poivrons',0.25),
(3,'chorizo',0.45),
(4,'lardons',0.4),
(5,'aubergines',0.2),
(6,'champignons',0.2),
(7,'fromage',0.5),
(8,'ananas',0.15),
(9,'tomates',0.35),
(10,'olives',0.1),
(11,'oeufs',0.2),
(12,'jambon',0.4);

create table pizzas (pno int primary key, name text, type text, prixBase float);
insert into pizzas values (1,'margherita','tomate',8),
(2,'savoyarde','creme',9),
(3,'reine','tomate',7);

create table ingpizzas (pno int, ino int, primary key (pno, ino));
insert into ingpizzas values (1,7),(1,6),(1,9),
(2,1),(2,4),(2,7),
(3,6),(3,7),(3,12);

create table commandes (cno int primary key, uno int, datecomm date);
insert into commandes values (1,1,'2022-10-12'),
(2,1,'2022-10-19'),
(3,2,'2022-11-03');

create table compizzas (cno int, pno int);
insert into compizzas values (1,1),(1,2),
(2,2),
(3,1),(3,3),(3,3);

create table users (uno int primary key, login text, pwd text);
insert into users values (1,'jean','jean'),(2,'paul','paul');

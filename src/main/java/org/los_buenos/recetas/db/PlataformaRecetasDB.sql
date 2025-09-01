-- drop database if exists PlataformaRecetasDB;
create database PlataformaRecetasDB;
use PlataformaRecetasDB;

create table Usuarios(
	idUsuario integer auto_increment,
    nombreUsuario varchar(64),
    apellidoUsuario varchar(64),
    contraseña varchar(64),
    rol varchar(64),
    constraint pk_usuario primary key (idUsuario)
);

create table Recetas(
	idReceta integer auto_increment,
    tituloReceta varchar(64),
    ingredientes varchar(64),
    instrucciones varchar(128),
    tiempoPreparacion time,
    idUsuario integer,
    constraint pk_receta primary key (idReceta),
    constraint fk_receta_usuario foreign key (idUsuario)
		references Usuarios(idUsuario)
);

create table Calificaciones(
	idCalificacion integer auto_increment,
    puntuacion enum('1','2','3','4','5'),
    idUsuario integer,
    idReceta integer,
    constraint pk_calificacion primary key (idCalificacion),
    constraint fk_calificacion_usuario foreign key (idUsuario)
		references Usuarios(idUsuario),
	constraint fk_calificacion_receta foreign key (idReceta)
		references Recetas(idReceta)
);
DROP DATABASE IF EXISTS BolsaEmpleo;
CREATE DATABASE BolsaEmpleo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE BolsaEmpleo;

/* =========================
   TABLA USUARIO
   - username:
       EMPRESA/OFERENTE -> correo
       ADMIN            -> identificación
   - rol: ADMIN | EMPRESA | OFERENTE
   ========================= */
create table Usuario (
  id bigint auto_increment,
  username varchar(120) unique not null,
  clave varchar(100) not null,        /* BCrypt */
  rol varchar(10) not null,
  habilitado tinyint(1) not null default 1,
  fecha_creacion datetime not null default current_timestamp,
  primary key(id)
);

/* =========================
   TABLA ADMIN (perfil)
   ========================= */
create table Admin (
  id bigint auto_increment,
  usuario_id bigint unique not null,
  identificacion varchar(30) unique not null,
  nombre varchar(80),
  primary key(id),
  foreign key (usuario_id) references Usuario(id)
);

/* =========================
   TABLA EMPRESA (perfil)
   - NO tiene correo (está en Usuario.username)
   - requiere aprobación
   ========================= */
create table Empresa (
  id bigint auto_increment,
  usuario_id bigint unique not null,

  nombre varchar(120) not null,
  localizacion varchar(160) not null,
  telefono varchar(30) not null,
  descripcion text,

  estado_aprobacion varchar(12) not null default 'PENDIENTE', /* PENDIENTE/APROBADO/RECHAZADO */
  fecha_registro datetime not null default current_timestamp,

  /* auditoría (recomendado) */
  fecha_aprobacion datetime null,
  aprobado_por_admin_id bigint null,

  primary key(id),
  foreign key (usuario_id) references Usuario(id),
  foreign key (aprobado_por_admin_id) references Admin(id)
);

/* =========================
   TABLA OFERENTE (perfil)
   - NO tiene correo (está en Usuario.username)
   - requiere aprobación
   - CV: ruta
   ========================= */
create table Oferente (
  id bigint auto_increment,
  usuario_id bigint unique not null,

  identificacion varchar(30) unique not null,
  nombre varchar(80) not null,
  primer_apellido varchar(80) not null,
  nacionalidad varchar(60) not null,
  telefono varchar(30) not null,
  lugar_residencia varchar(160) not null,

  estado_aprobacion varchar(12) not null default 'PENDIENTE', /* PENDIENTE/APROBADO/RECHAZADO */
  fecha_registro datetime not null default current_timestamp,

  cv_path varchar(255),
  cv_nombre_original varchar(255),
  cv_mime varchar(80),
  cv_fecha_subida datetime,

  /* auditoría (recomendado) */
  fecha_aprobacion datetime null,
  aprobado_por_admin_id bigint null,

  primary key(id),
  foreign key (usuario_id) references Usuario(id),
  foreign key (aprobado_por_admin_id) references Admin(id)
);

/* =========================
   TABLA CARACTERISTICA (jerárquica)
   ========================= */
create table Caracteristica (
  id bigint auto_increment,
  nombre varchar(120) not null,
  id_padre bigint null,
  primary key(id),
  foreign key (id_padre) references Caracteristica(id)
);

/* =========================
   TABLA PUESTO
   - PUBLICO: visible para cualquiera
   - PRIVADO: solo para oferentes registrados (en tu lógica)
   - activo: permite desactivar
   ========================= */
create table Puesto (
  id bigint auto_increment,
  empresa_id bigint not null,

  descripcion_general text not null,
  salario_ofrecido decimal(12,2),

  tipo_publicacion varchar(10) not null default 'PUBLICO', /* PUBLICO/PRIVADO */
  activo tinyint(1) not null default 1,

  fecha_registro datetime not null default current_timestamp,
  fecha_desactivacion datetime null,

  primary key(id),
  foreign key (empresa_id) references Empresa(id)
);

/* =========================
   TABLA PUESTO_CARACTERISTICA (requisitos)
   - nivel_deseado: 1..5
   ========================= */
create table PuestoCaracteristica (
  puesto_id bigint not null,
  caracteristica_id bigint not null,
  nivel_deseado int not null,
  primary key(puesto_id, caracteristica_id),
  foreign key (puesto_id) references Puesto(id),
  foreign key (caracteristica_id) references Caracteristica(id)
);

/* =========================
   TABLA OFERENTE_HABILIDAD
   - nivel: 1..5
   ========================= */
create table OferenteHabilidad (
  oferente_id bigint not null,
  caracteristica_id bigint not null,
  nivel int not null,
  primary key(oferente_id, caracteristica_id),
  foreign key (oferente_id) references Oferente(id),
  foreign key (caracteristica_id) references Caracteristica(id)
);

/* =========================================================
   INSERTS DEMO
   Reemplazá los hashes por los tuyos (HashTest.java).
   ========================================================= */

/* ADMIN demo */
insert into Usuario (username, clave, rol)
values ('administrador@correo.com', '$2a$12$esH9VOmi2lwFmh60ZH.Zo.kn3QTJ9DlbCfo7SuTABopgSai.EiO8O', 'ADMIN');
/* clave 111 */

insert into Admin (usuario_id, identificacion, nombre)
values (last_insert_id(), 'ADMIN01', 'Administrador');

/* Características jerárquicas demo */
insert into Caracteristica (nombre, id_padre) values ('Lenguajes de programación', null);
set @leng := last_insert_id();

insert into Caracteristica (nombre, id_padre) values ('Tecnologías Web', null);
set @web := last_insert_id();

insert into Caracteristica (nombre, id_padre) values ('Java', @leng);
insert into Caracteristica (nombre, id_padre) values ('C#', @leng);
insert into Caracteristica (nombre, id_padre) values ('HTML', @web);
insert into Caracteristica (nombre, id_padre) values ('CSS', @web);
insert into Caracteristica (nombre, id_padre) values ('JavaScript', @web);

/* EMPRESA demo */
insert into Usuario (username, clave, rol)
values ('empresa@correo.com', '$2a$12$SGGHeK8BBUt5HAp3J0ZqZuQUfyh92QGyY.TwZWLp98zP1ra8o2JS2', 'EMPRESA');
/* clave 222 */

set @uemp := last_insert_id();

insert into Empresa (usuario_id, nombre, localizacion, telefono, descripcion, estado_aprobacion)
values (@uemp, 'Empresa Demo', 'San José', '2222-2222', 'Empresa de ejemplo', 'PENDIENTE');

/* OFERENTE demo */
insert into Usuario (username, clave, rol)
values ('oferente@correo.com', '$2a$12$iUlQtYXbuBbienG0qMiUsOKbSAl64/zah1YAktj.fo4FRBinqT8j6', 'OFERENTE');
/* clave 333 */

set @uof := last_insert_id();

insert into Oferente (usuario_id, identificacion, nombre, primer_apellido, nacionalidad, telefono, lugar_residencia, estado_aprobacion)
values (@uof, '1-1111-1111', 'Ana', 'Pérez', 'CR', '8888-8888', 'Heredia', 'PENDIENTE');

/* =========================================================
   EJEMPLO: aprobar empresa y crear un puesto público (opcional)
   (Descomentá si querés datos de prueba completos)
   =========================================================
-- update Empresa set estado_aprobacion='APROBADO', fecha_aprobacion=now(), aprobado_por_admin_id=1 where id=1;

-- insert into Puesto (empresa_id, descripcion_general, salario_ofrecido, tipo_publicacion, activo)
-- values (1, 'Desarrollador Java Jr', 1200.00, 'PUBLICO', 1);

-- set @puesto := last_insert_id();
-- -- Requisito: Java nivel 3 (asumiendo que Java quedó con id=3)
-- insert into PuestoCaracteristica (puesto_id, caracteristica_id, nivel_deseado) values (@puesto, 3, 3);

-- -- Habilidad oferente: Java nivel 4 (oferente id=1, Java id=3)
-- insert into OferenteHabilidad (oferente_id, caracteristica_id, nivel) values (1, 3, 4);
   ========================================================= */
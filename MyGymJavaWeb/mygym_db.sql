CREATE DATABASE IF NOT EXISTS mygym_db;

USE mygym_db;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fechaNacimiento DATE,
    genero VARCHAR(50),
    altura DOUBLE,
    peso DOUBLE,
    experiencia VARCHAR(100),
    diasDisponibles INT,
    objetivo VARCHAR(100),
    prioridadMuscular VARCHAR(100)
);

CREATE TABLE ejercicios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    grupoMuscular VARCHAR(100),
    dificultad VARCHAR(100),
    imagenUrl VARCHAR(255)
);

CREATE TABLE rutinas_guardadas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    rutina_json TEXT,
    fecha_guardada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completada BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

/* Creamos algunos usuarios con sus roles */
INSERT INTO `com_usuario` (usuario, contrasena, estado, nombre_completo, nombre_corto) VALUES ('76328021','$2a$10$WkFZKzb4/zuqYF15QGTBiOM6LE31/7f1mWBzTQjY49g/jIaHyQr1q',1, 'RODRIGO ALBERTO VALVERDE RODRIGUEZ', 'RODRIGO');

INSERT INTO `com_role` (nombre, usuarios_id) VALUES ('ROLE_ADMIN', 1);


/* Creamos parametro fecha por defecto para procedimiento patologia*/
#INSERT INTO `com_parametro_patologia` (fecha_solicitud_externo, fecha_solicitud_interno) VALUES ('2020-02-15', '2020-02-15');

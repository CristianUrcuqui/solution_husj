/* Creamos algunos usuarios con sus roles */
INSERT INTO `com_usuario` (usuario, contrasena, estado, nombre_completo, nombre_corto) VALUES ('76328021','$2a$10$WkFZKzb4/zuqYF15QGTBiOM6LE31/7f1mWBzTQjY49g/jIaHyQr1q',1, 'RODRIGO ALBERTO VALVERDE RODRIGUEZ', 'RODRIGO');

INSERT INTO `com_role` (`nombre`) VALUES ('ROLE_ADMIN');
INSERT INTO `com_role` (`nombre`) VALUES ('ROLE_PATOLOGIA');
INSERT INTO `com_role` (`nombre`) VALUES ('ROLE_TMP');

INSERT INTO `usuarios_roles` (`usuarios_id`, `roles_id`) VALUES ('1', '1');


/* Creamos parametro fecha por defecto para procedimiento patologia*/
#INSERT INTO `com_parametro_patologia` (fecha_solicitud_externo, fecha_solicitud_interno, nombre) VALUES ('2020-02-15', '2020-02-15', 'patologia');


/* Creamos empleado para cumpleaños*/
INSERT INTO `cal_calendario` (nombre_completo, numero_identificacion, fecha_nacimiento, correo, enviado) VALUES ('PEPITO', '123456789', '2020-02-25', 'chapumix@hotmail.com', false);

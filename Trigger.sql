
use bidaion_tablas;

 update Usuario
 SET DNI = '13456718z', Clave='562'
 WHERE DNI='12345678z';

-- ALTER TABLE `Usuario`
-- ADD COLUMN `fecha` DATE;

DROP TRIGGER IF EXISTS fecha_modificacion;
DELIMITER $$
CREATE TRIGGER fecha_modificacion
before UPDATE ON `Usuario`
FOR EACH ROW BEGIN
set  NEW.fecha=NOW();
END $$
-- MySQL dump 10.13  Distrib 5.1.49, for Win32 (ia32)
--
-- Host: localhost    Database: academia
-- ------------------------------------------------------
-- Server version	5.1.49-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alumno`
--

DROP TABLE IF EXISTS `alumno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alumno` (
  `idalumno` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(25) NOT NULL,
  `nombres` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `fechanacimiento` date DEFAULT NULL,
  `sexo` char(1) NOT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `titularnombres` varchar(45) DEFAULT NULL,
  `titularapellidos` varchar(45) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL,
  `titulardpi` varchar(15) DEFAULT NULL,
  `establecimiento` varchar(100) DEFAULT NULL,
  `direccionestablecimiento` varchar(125) DEFAULT NULL,
  `gradoestablecimiento` varchar(45) DEFAULT NULL,
  `codigomineduc` varchar(50) DEFAULT NULL,
  `fechabaja` date DEFAULT NULL,
  `observacion` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`idalumno`),
  UNIQUE KEY `codigo_UNIQUE` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumno`
--

LOCK TABLES `alumno` WRITE;
/*!40000 ALTER TABLE `alumno` DISABLE KEYS */;
/*!40000 ALTER TABLE `alumno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alumnosengrupo`
--

DROP TABLE IF EXISTS `alumnosengrupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alumnosengrupo` (
  `iddetallegrupo` int(11) NOT NULL AUTO_INCREMENT,
  `alumno_idalumno` int(11) NOT NULL,
  `grupo_idgrupo` int(11) NOT NULL,
  `fechainicio` date NOT NULL,
  `beca` float DEFAULT '0',
  PRIMARY KEY (`iddetallegrupo`),
  KEY `fk_asignaciones_alumno1_idx` (`alumno_idalumno`),
  KEY `fk_alumnosencarrera_grupo1_idx` (`grupo_idgrupo`),
  CONSTRAINT `fk_alumnosencarrera_grupo1` FOREIGN KEY (`grupo_idgrupo`) REFERENCES `grupo` (`idgrupo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_asignaciones_alumno1` FOREIGN KEY (`alumno_idalumno`) REFERENCES `alumno` (`idalumno`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumnosengrupo`
--

LOCK TABLES `alumnosengrupo` WRITE;
/*!40000 ALTER TABLE `alumnosengrupo` DISABLE KEYS */;
/*!40000 ALTER TABLE `alumnosengrupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carrera`
--

DROP TABLE IF EXISTS `carrera`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carrera` (
  `idcarrera` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) NOT NULL,
  `observacion` varchar(150) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL,
  PRIMARY KEY (`idcarrera`),
  UNIQUE KEY `descripcion_UNIQUE` (`descripcion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrera`
--

LOCK TABLES `carrera` WRITE;
/*!40000 ALTER TABLE `carrera` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrera` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cicloescolar`
--

DROP TABLE IF EXISTS `cicloescolar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cicloescolar` (
  `idañoescolar` int(11) NOT NULL AUTO_INCREMENT,
  `Descripcion` varchar(45) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL,
  `fechainicio` date DEFAULT NULL,
  `fechacierre` date DEFAULT NULL,
  PRIMARY KEY (`idañoescolar`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cicloescolar`
--

LOCK TABLES `cicloescolar` WRITE;
/*!40000 ALTER TABLE `cicloescolar` DISABLE KEYS */;
/*!40000 ALTER TABLE `cicloescolar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curso`
--

DROP TABLE IF EXISTS `curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `curso` (
  `idcurso` int(11) NOT NULL AUTO_INCREMENT,
  `nombrecurso` varchar(100) NOT NULL,
  `fecharegistro` date NOT NULL,
  `estado` tinyint(1) NOT NULL,
  PRIMARY KEY (`idcurso`),
  UNIQUE KEY `nombrecurso_UNIQUE` (`nombrecurso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curso`
--

LOCK TABLES `curso` WRITE;
/*!40000 ALTER TABLE `curso` DISABLE KEYS */;
/*!40000 ALTER TABLE `curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `descripcionrecibo`
--

DROP TABLE IF EXISTS `descripcionrecibo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `descripcionrecibo` (
  `iddescripcionrecibo` int(11) NOT NULL AUTO_INCREMENT,
  `cantidad` float DEFAULT NULL,
  `precio` float NOT NULL,
  `recibo_idrecibo` int(11) NOT NULL,
  `pago_idpago` int(11) NOT NULL,
  PRIMARY KEY (`iddescripcionrecibo`),
  KEY `fk_descripcionrecibo_recibo1` (`recibo_idrecibo`),
  KEY `fk_descripcionrecibo_pago1` (`pago_idpago`),
  CONSTRAINT `fk_descripcionrecibo_pago1` FOREIGN KEY (`pago_idpago`) REFERENCES `otrospagos` (`idpago`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_descripcionrecibo_recibo1` FOREIGN KEY (`recibo_idrecibo`) REFERENCES `recibodepago` (`idrecibo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `descripcionrecibo`
--

LOCK TABLES `descripcionrecibo` WRITE;
/*!40000 ALTER TABLE `descripcionrecibo` DISABLE KEYS */;
/*!40000 ALTER TABLE `descripcionrecibo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detallepensun`
--

DROP TABLE IF EXISTS `detallepensun`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detallepensun` (
  `iddetallepensun` int(11) NOT NULL AUTO_INCREMENT,
  `pensun_idpensun` int(11) NOT NULL,
  `curso_idcurso` int(11) NOT NULL,
  PRIMARY KEY (`iddetallepensun`),
  KEY `fk_pensuncarrera_pensun1_idx` (`pensun_idpensun`),
  KEY `fk_detallepensun_curso1_idx` (`curso_idcurso`),
  CONSTRAINT `fk_detallepensun_curso1` FOREIGN KEY (`curso_idcurso`) REFERENCES `curso` (`idcurso`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pensuncarrera_pensun1` FOREIGN KEY (`pensun_idpensun`) REFERENCES `pensum` (`idpensum`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detallepensun`
--

LOCK TABLES `detallepensun` WRITE;
/*!40000 ALTER TABLE `detallepensun` DISABLE KEYS */;
/*!40000 ALTER TABLE `detallepensun` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detrecibo`
--

DROP TABLE IF EXISTS `detrecibo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detrecibo` (
  `iddetrecibo` int(11) NOT NULL AUTO_INCREMENT,
  `recibodepago_idrecibo` int(11) NOT NULL,
  `proyeccionpagos_idproyeccionpagos` int(11) NOT NULL,
  PRIMARY KEY (`iddetrecibo`),
  KEY `fk_detrecibo_recibodepago1_idx` (`recibodepago_idrecibo`),
  KEY `fk_detrecibo_proyeccionpagos1_idx` (`proyeccionpagos_idproyeccionpagos`),
  CONSTRAINT `fk_detrecibo_proyeccionpagos1` FOREIGN KEY (`proyeccionpagos_idproyeccionpagos`) REFERENCES `proyeccionpagos` (`idproyeccionpagos`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_detrecibo_recibodepago1` FOREIGN KEY (`recibodepago_idrecibo`) REFERENCES `recibodepago` (`idrecibo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detrecibo`
--

LOCK TABLES `detrecibo` WRITE;
/*!40000 ALTER TABLE `detrecibo` DISABLE KEYS */;
/*!40000 ALTER TABLE `detrecibo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faltaalumno`
--

DROP TABLE IF EXISTS `faltaalumno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `faltaalumno` (
  `idfaltaalumno` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) DEFAULT NULL,
  `alumno_idalumno` int(11) NOT NULL,
  `usuario_idusuario` int(11) NOT NULL,
  PRIMARY KEY (`idfaltaalumno`),
  KEY `fk_tipofalta_alumno1_idx` (`alumno_idalumno`),
  KEY `fk_tipofalta_usuario1_idx` (`usuario_idusuario`),
  CONSTRAINT `fk_tipofalta_alumno1` FOREIGN KEY (`alumno_idalumno`) REFERENCES `alumno` (`idalumno`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tipofalta_usuario1` FOREIGN KEY (`usuario_idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faltaalumno`
--

LOCK TABLES `faltaalumno` WRITE;
/*!40000 ALTER TABLE `faltaalumno` DISABLE KEYS */;
/*!40000 ALTER TABLE `faltaalumno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grupo`
--

DROP TABLE IF EXISTS `grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupo` (
  `idgrupo` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(45) NOT NULL,
  `descripcion` varchar(60) NOT NULL,
  `fechainicio` date NOT NULL,
  `fechafin` date NOT NULL,
  `cantalumnos` int(11) NOT NULL,
  `dia` varchar(20) NOT NULL,
  `horariode` time NOT NULL,
  `horarioa` time NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `carrera_idcarrera` int(11) NOT NULL,
  `profesor_idcatedratico` int(11) NOT NULL,
  `inscripcion` float NOT NULL,
  `colegiatura` float NOT NULL,
  PRIMARY KEY (`idgrupo`),
  KEY `fk_grupo_carrera1_idx` (`carrera_idcarrera`),
  KEY `fk_grupo_profesor1_idx` (`profesor_idcatedratico`),
  CONSTRAINT `fk_grupo_carrera1` FOREIGN KEY (`carrera_idcarrera`) REFERENCES `carrera` (`idcarrera`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_grupo_profesor1` FOREIGN KEY (`profesor_idcatedratico`) REFERENCES `profesor` (`idcatedratico`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grupo`
--

LOCK TABLES `grupo` WRITE;
/*!40000 ALTER TABLE `grupo` DISABLE KEYS */;
/*!40000 ALTER TABLE `grupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `idmenu` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(60) NOT NULL,
  `principal` varchar(60) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idmenu`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mes`
--

DROP TABLE IF EXISTS `mes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mes` (
  `idmes` int(11) NOT NULL AUTO_INCREMENT,
  `mes` varchar(15) NOT NULL,
  PRIMARY KEY (`idmes`),
  UNIQUE KEY `mes_UNIQUE` (`mes`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mes`
--

LOCK TABLES `mes` WRITE;
/*!40000 ALTER TABLE `mes` DISABLE KEYS */;
INSERT INTO `mes` VALUES (4,'Abril'),(8,'Agosto'),(12,'Diciembre'),(1,'Enero'),(2,'Febrero'),(13,'Inscripción'),(7,'Julio'),(6,'Junio'),(3,'Marzo'),(5,'Mayo'),(11,'Noviembre'),(10,'Octubre'),(9,'Septiembre');
/*!40000 ALTER TABLE `mes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mora`
--

DROP TABLE IF EXISTS `mora`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mora` (
  `idmora` int(11) NOT NULL AUTO_INCREMENT,
  `mora` float DEFAULT NULL,
  `exoneracion` float DEFAULT '0',
  `estado` tinyint(1) DEFAULT '0',
  `proyeccionpagos_idproyeccionpagos` int(11) NOT NULL,
  PRIMARY KEY (`idmora`),
  KEY `fk_mora_proyeccionpagos1_idx` (`proyeccionpagos_idproyeccionpagos`),
  CONSTRAINT `fk_mora_proyeccionpagos1` FOREIGN KEY (`proyeccionpagos_idproyeccionpagos`) REFERENCES `proyeccionpagos` (`idproyeccionpagos`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mora`
--

LOCK TABLES `mora` WRITE;
/*!40000 ALTER TABLE `mora` DISABLE KEYS */;
/*!40000 ALTER TABLE `mora` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas`
--

DROP TABLE IF EXISTS `notas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notas` (
  `idnotas` int(11) NOT NULL AUTO_INCREMENT,
  `notafinal` float DEFAULT NULL,
  `curso_idcurso` int(11) NOT NULL,
  `alumno_idalumno` int(11) NOT NULL,
  PRIMARY KEY (`idnotas`),
  KEY `fk_notas_curso1_idx` (`curso_idcurso`),
  KEY `fk_notas_alumno1_idx` (`alumno_idalumno`),
  CONSTRAINT `fk_notas_alumno1` FOREIGN KEY (`alumno_idalumno`) REFERENCES `alumno` (`idalumno`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_notas_curso1` FOREIGN KEY (`curso_idcurso`) REFERENCES `curso` (`idcurso`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas`
--

LOCK TABLES `notas` WRITE;
/*!40000 ALTER TABLE `notas` DISABLE KEYS */;
/*!40000 ALTER TABLE `notas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `otrospagos`
--

DROP TABLE IF EXISTS `otrospagos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `otrospagos` (
  `idpago` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) NOT NULL,
  `costo` float NOT NULL,
  `estado` tinyint(1) NOT NULL,
  PRIMARY KEY (`idpago`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `otrospagos`
--

LOCK TABLES `otrospagos` WRITE;
/*!40000 ALTER TABLE `otrospagos` DISABLE KEYS */;
/*!40000 ALTER TABLE `otrospagos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagos`
--

DROP TABLE IF EXISTS `pagos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagos` (
  `idpagos` int(11) NOT NULL AUTO_INCREMENT,
  `mes_idmes` int(11) NOT NULL,
  `año` int(11) DEFAULT NULL,
  `monto` float DEFAULT NULL,
  `fechavencimiento` date DEFAULT NULL,
  `grupo_idgrupo` int(11) NOT NULL,
  PRIMARY KEY (`idpagos`),
  KEY `fk_pagos_mes1_idx` (`mes_idmes`),
  KEY `fk_pagos_grupo1_idx` (`grupo_idgrupo`),
  CONSTRAINT `fk_pagos_grupo1` FOREIGN KEY (`grupo_idgrupo`) REFERENCES `grupo` (`idgrupo`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_pagos_mes1` FOREIGN KEY (`mes_idmes`) REFERENCES `mes` (`idmes`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagos`
--

LOCK TABLES `pagos` WRITE;
/*!40000 ALTER TABLE `pagos` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pensum`
--

DROP TABLE IF EXISTS `pensum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pensum` (
  `idpensum` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) NOT NULL,
  `carrera_idcarrera` int(11) NOT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idpensum`),
  UNIQUE KEY `descripcion_UNIQUE` (`descripcion`),
  KEY `fk_pensun_carrera1_idx` (`carrera_idcarrera`),
  CONSTRAINT `fk_pensun_carrera1` FOREIGN KEY (`carrera_idcarrera`) REFERENCES `carrera` (`idcarrera`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pensum`
--

LOCK TABLES `pensum` WRITE;
/*!40000 ALTER TABLE `pensum` DISABLE KEYS */;
/*!40000 ALTER TABLE `pensum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfilusuario`
--

DROP TABLE IF EXISTS `perfilusuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perfilusuario` (
  `idperfilusuario` int(11) NOT NULL AUTO_INCREMENT,
  `menu_idmenu` int(11) NOT NULL,
  `usuario_idusuario` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idperfilusuario`),
  KEY `fk_perfilusuario_menu1_idx` (`menu_idmenu`),
  KEY `fk_perfilusuario_usuario1_idx` (`usuario_idusuario`),
  CONSTRAINT `fk_perfilusuario_menu1` FOREIGN KEY (`menu_idmenu`) REFERENCES `menu` (`idmenu`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_perfilusuario_usuario1` FOREIGN KEY (`usuario_idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfilusuario`
--

LOCK TABLES `perfilusuario` WRITE;
/*!40000 ALTER TABLE `perfilusuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `perfilusuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profesor`
--

DROP TABLE IF EXISTS `profesor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profesor` (
  `idcatedratico` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(45) NOT NULL,
  `identificacion` varchar(45) DEFAULT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `telefono` varchar(16) DEFAULT NULL,
  `fechainicio` date NOT NULL,
  PRIMARY KEY (`idcatedratico`),
  UNIQUE KEY `codigo_UNIQUE` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profesor`
--

LOCK TABLES `profesor` WRITE;
/*!40000 ALTER TABLE `profesor` DISABLE KEYS */;
/*!40000 ALTER TABLE `profesor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyeccionpagos`
--

DROP TABLE IF EXISTS `proyeccionpagos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyeccionpagos` (
  `idproyeccionpagos` int(11) NOT NULL AUTO_INCREMENT,
  `mes_idmes` int(11) NOT NULL,
  `año` int(11) DEFAULT NULL,
  `monto` float NOT NULL,
  `fechavencimiento` date NOT NULL,
  `estado` tinyint(1) DEFAULT '0',
  `asignado` tinyint(1) DEFAULT '1',
  `alumnosengrupo_iddetallegrupo` int(11) NOT NULL,
  PRIMARY KEY (`idproyeccionpagos`),
  KEY `fk_proyeccionpagos_mes1_idx` (`mes_idmes`),
  KEY `fk_proyeccionpagos_alumnosengrupo1_idx` (`alumnosengrupo_iddetallegrupo`),
  CONSTRAINT `fk_proyeccionpagos_alumnosengrupo1` FOREIGN KEY (`alumnosengrupo_iddetallegrupo`) REFERENCES `alumnosengrupo` (`iddetallegrupo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_proyeccionpagos_mes1` FOREIGN KEY (`mes_idmes`) REFERENCES `mes` (`idmes`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyeccionpagos`
--

LOCK TABLES `proyeccionpagos` WRITE;
/*!40000 ALTER TABLE `proyeccionpagos` DISABLE KEYS */;
/*!40000 ALTER TABLE `proyeccionpagos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recibodepago`
--

DROP TABLE IF EXISTS `recibodepago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recibodepago` (
  `idrecibo` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` date DEFAULT NULL,
  `alumno_idalumno` int(11) NOT NULL,
  `tipopago_idtipopago` int(11) NOT NULL,
  `total` float NOT NULL,
  `usuario_idusuario` int(11) NOT NULL,
  PRIMARY KEY (`idrecibo`),
  KEY `fk_recibo_alumno1` (`alumno_idalumno`),
  KEY `fk_recibo_tipopago1` (`tipopago_idtipopago`),
  KEY `fk_recibo_usuario1_idx` (`usuario_idusuario`),
  CONSTRAINT `fk_recibo_alumno1` FOREIGN KEY (`alumno_idalumno`) REFERENCES `alumno` (`idalumno`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_recibo_tipopago1` FOREIGN KEY (`tipopago_idtipopago`) REFERENCES `tipopago` (`idtipopago`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_recibo_usuario1` FOREIGN KEY (`usuario_idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recibodepago`
--

LOCK TABLES `recibodepago` WRITE;
/*!40000 ALTER TABLE `recibodepago` DISABLE KEYS */;
/*!40000 ALTER TABLE `recibodepago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipopago`
--

DROP TABLE IF EXISTS `tipopago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipopago` (
  `idtipopago` int(11) NOT NULL AUTO_INCREMENT,
  `tipopago` varchar(20) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `fecharegistro` date NOT NULL,
  PRIMARY KEY (`idtipopago`),
  UNIQUE KEY `tipopago_UNIQUE` (`tipopago`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipopago`
--

LOCK TABLES `tipopago` WRITE;
/*!40000 ALTER TABLE `tipopago` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipopago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `idusuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(60) NOT NULL,
  `usuario` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `fechacreacion` date DEFAULT NULL,
  `fechabaja` date DEFAULT NULL,
  PRIMARY KEY (`idusuario`),
  UNIQUE KEY `usuario_UNIQUE` (`usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Administrador','admin','admin1',1,'2015-02-23',NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-02-23 11:52:42

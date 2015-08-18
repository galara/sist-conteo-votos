-- MySQL dump 10.13  Distrib 5.6.10, for Win32 (x86)
--
-- Host: localhost    Database: votos
-- ------------------------------------------------------
-- Server version	5.6.10

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
-- Table structure for table `candidato`
--

DROP TABLE IF EXISTS `candidato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `candidato` (
  `idcandidato` int(11) NOT NULL AUTO_INCREMENT,
  `nomnbres` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `partido_idpartido` int(11) NOT NULL,
  `puesto_idpuesto` int(11) NOT NULL,
  PRIMARY KEY (`idcandidato`,`partido_idpartido`,`puesto_idpuesto`),
  KEY `fk_candidato_partido1_idx` (`partido_idpartido`),
  KEY `fk_candidato_puesto1_idx` (`puesto_idpuesto`),
  CONSTRAINT `fk_candidato_partido1` FOREIGN KEY (`partido_idpartido`) REFERENCES `partido_politico` (`idpartido`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_candidato_puesto1` FOREIGN KEY (`puesto_idpuesto`) REFERENCES `puesto` (`idpuesto`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidato`
--

LOCK TABLES `candidato` WRITE;
/*!40000 ALTER TABLE `candidato` DISABLE KEYS */;
/*!40000 ALTER TABLE `candidato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `centro`
--

DROP TABLE IF EXISTS `centro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `centro` (
  `idcentro` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(70) NOT NULL,
  `municipio_idmunicipio` int(11) NOT NULL,
  PRIMARY KEY (`idcentro`,`municipio_idmunicipio`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  KEY `fk_centro_municipio1_idx` (`municipio_idmunicipio`),
  CONSTRAINT `fk_centro_municipio1` FOREIGN KEY (`municipio_idmunicipio`) REFERENCES `municipio` (`idmunicipio`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `centro`
--

LOCK TABLES `centro` WRITE;
/*!40000 ALTER TABLE `centro` DISABLE KEYS */;
/*!40000 ALTER TABLE `centro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departamento`
--

DROP TABLE IF EXISTS `departamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `departamento` (
  `iddepartamento` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`iddepartamento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departamento`
--

LOCK TABLES `departamento` WRITE;
/*!40000 ALTER TABLE `departamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `departamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_votos`
--

DROP TABLE IF EXISTS `detalle_votos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detalle_votos` (
  `iddetalle_votos` int(11) NOT NULL AUTO_INCREMENT,
  `cant_votos` int(11) DEFAULT NULL,
  `candidato_idcandidato` int(11) NOT NULL,
  `mesa_idmesa` int(11) NOT NULL,
  PRIMARY KEY (`iddetalle_votos`,`candidato_idcandidato`,`mesa_idmesa`),
  KEY `fk_detalle_votos_candidato1_idx` (`candidato_idcandidato`),
  KEY `fk_detalle_votos_mesa1_idx` (`mesa_idmesa`),
  CONSTRAINT `fk_detalle_votos_candidato1` FOREIGN KEY (`candidato_idcandidato`) REFERENCES `candidato` (`idcandidato`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_detalle_votos_mesa1` FOREIGN KEY (`mesa_idmesa`) REFERENCES `mesa` (`idmesa`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_votos`
--

LOCK TABLES `detalle_votos` WRITE;
/*!40000 ALTER TABLE `detalle_votos` DISABLE KEYS */;
/*!40000 ALTER TABLE `detalle_votos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `idmenu` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `principal` varchar(45) DEFAULT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idmenu`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'puesto','Principal',1);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mesa`
--

DROP TABLE IF EXISTS `mesa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mesa` (
  `idmesa` int(11) NOT NULL AUTO_INCREMENT,
  `num_mesa` varchar(45) NOT NULL,
  `centro_idcentro` int(11) NOT NULL,
  PRIMARY KEY (`idmesa`,`centro_idcentro`),
  UNIQUE KEY `num_mesa_UNIQUE` (`num_mesa`),
  KEY `fk_mesa_centro1_idx` (`centro_idcentro`),
  CONSTRAINT `fk_mesa_centro1` FOREIGN KEY (`centro_idcentro`) REFERENCES `centro` (`idcentro`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mesa`
--

LOCK TABLES `mesa` WRITE;
/*!40000 ALTER TABLE `mesa` DISABLE KEYS */;
/*!40000 ALTER TABLE `mesa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `municipio`
--

DROP TABLE IF EXISTS `municipio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `municipio` (
  `idmunicipio` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(70) NOT NULL,
  `cant_centros` int(11) DEFAULT NULL,
  `mesas_del` int(11) DEFAULT NULL,
  `mesas_al` int(11) DEFAULT NULL,
  `departamento_iddepartamento` int(11) NOT NULL,
  PRIMARY KEY (`idmunicipio`,`departamento_iddepartamento`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  KEY `fk_municipio_departamento1_idx` (`departamento_iddepartamento`),
  CONSTRAINT `fk_municipio_departamento1` FOREIGN KEY (`departamento_iddepartamento`) REFERENCES `departamento` (`iddepartamento`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `municipio`
--

LOCK TABLES `municipio` WRITE;
/*!40000 ALTER TABLE `municipio` DISABLE KEYS */;
/*!40000 ALTER TABLE `municipio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partido_politico`
--

DROP TABLE IF EXISTS `partido_politico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partido_politico` (
  `idpartido` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `estado` tinyint(1) DEFAULT '0',
  `fecharegistro` date DEFAULT NULL,
  PRIMARY KEY (`idpartido`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partido_politico`
--

LOCK TABLES `partido_politico` WRITE;
/*!40000 ALTER TABLE `partido_politico` DISABLE KEYS */;
/*!40000 ALTER TABLE `partido_politico` ENABLE KEYS */;
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
  `estado` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`idperfilusuario`,`menu_idmenu`,`usuario_idusuario`),
  KEY `fk_perfilusuario_menu1_idx` (`menu_idmenu`),
  KEY `fk_perfilusuario_usuario1_idx` (`usuario_idusuario`),
  CONSTRAINT `fk_perfilusuario_menu1` FOREIGN KEY (`menu_idmenu`) REFERENCES `menu` (`idmenu`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_perfilusuario_usuario1` FOREIGN KEY (`usuario_idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfilusuario`
--

LOCK TABLES `perfilusuario` WRITE;
/*!40000 ALTER TABLE `perfilusuario` DISABLE KEYS */;
INSERT INTO `perfilusuario` VALUES (3,1,2,1);
/*!40000 ALTER TABLE `perfilusuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `puesto`
--

DROP TABLE IF EXISTS `puesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `puesto` (
  `idpuesto` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `estado` tinyint(1) DEFAULT NULL,
  `fecharegistro` date DEFAULT NULL,
  PRIMARY KEY (`idpuesto`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puesto`
--

LOCK TABLES `puesto` WRITE;
/*!40000 ALTER TABLE `puesto` DISABLE KEYS */;
INSERT INTO `puesto` VALUES (1,'Usuario Digitador',1,'2015-08-18');
/*!40000 ALTER TABLE `puesto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `idusuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `telefono` varchar(45) DEFAULT NULL,
  `usuario` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `puesto_idpuesto` int(11) NOT NULL,
  `estado` tinyint(1) DEFAULT '0',
  `fechacreacion` date NOT NULL,
  PRIMARY KEY (`idusuario`,`puesto_idpuesto`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  UNIQUE KEY `usuario_UNIQUE` (`usuario`),
  KEY `fk_usuario_puesto_idx` (`puesto_idpuesto`),
  CONSTRAINT `fk_usuario_puesto` FOREIGN KEY (`puesto_idpuesto`) REFERENCES `puesto` (`idpuesto`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (2,'Gustavo Adolfo Lara','55555555','glara','glara',1,1,'2015-08-19');
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

-- Dump completed on 2015-08-18 11:38:07

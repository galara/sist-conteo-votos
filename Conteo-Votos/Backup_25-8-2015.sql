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
  `codigo` varchar(45) NOT NULL,
  `partido_idpartido` int(11) NOT NULL,
  `puesto_idpuesto` int(11) NOT NULL,
  `estado` tinyint(1) DEFAULT '0',
  `municipio_idmunicipio` int(11) NOT NULL,
  PRIMARY KEY (`idcandidato`,`partido_idpartido`,`puesto_idpuesto`,`municipio_idmunicipio`),
  UNIQUE KEY `codigo_UNIQUE` (`codigo`),
  KEY `fk_candidato_partido1_idx` (`partido_idpartido`),
  KEY `fk_candidato_puesto1_idx` (`puesto_idpuesto`),
  KEY `fk_candidato_municipio1_idx` (`municipio_idmunicipio`),
  CONSTRAINT `fk_candidato_municipio1` FOREIGN KEY (`municipio_idmunicipio`) REFERENCES `municipio` (`idmunicipio`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_candidato_partido1` FOREIGN KEY (`partido_idpartido`) REFERENCES `partido_politico` (`idpartido`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_candidato_puesto1` FOREIGN KEY (`puesto_idpuesto`) REFERENCES `puesto` (`idpuesto`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidato`
--

LOCK TABLES `candidato` WRITE;
/*!40000 ALTER TABLE `candidato` DISABLE KEYS */;
INSERT INTO `candidato` VALUES (7,'C-7',2,5,1,1);
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
  `estado` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idcentro`,`municipio_idmunicipio`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  KEY `fk_centro_municipio1_idx` (`municipio_idmunicipio`),
  CONSTRAINT `fk_centro_municipio1` FOREIGN KEY (`municipio_idmunicipio`) REFERENCES `municipio` (`idmunicipio`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `centro`
--

LOCK TABLES `centro` WRITE;
/*!40000 ALTER TABLE `centro` DISABLE KEYS */;
INSERT INTO `centro` VALUES (1,'E.O.U.M JORNADA MATUTINA BARRIO MONTERREY',1,1),(2,'E.O.U. DE PARVULOS FEDERICO MORA',1,1),(3,'E.O.U.M. SOLEDAD AYAU',1,1),(4,'E.O.U.M. TIPO FED. RUBEN VILLAGRAN PAUL',1,1),(5,'E.O.U.M. JORNADA MATUTINA VILLAS DEL PEDREGAL II',1,1),(6,'E.O.U.M. DINAMARCA',1,1),(7,'INST. ED. BASICA CARLOS MANUEL ARANA OSORIO',1,1),(8,'INST. BASICO COOP PARCELAMIENTO CABALLO BLANCO',1,1),(9,'E.O.R.M. JORNDA MATUTINA PARCELAMIENTO CABALLO BLANCO',1,1),(10,'E.O.R.M. JORNADA MATUTINA ALDEA LA GUITARRA',1,1),(11,'E.O.R.M. JORNADA MATUTINA ALDEA ALDEA LAS PILAS',1,1),(12,'E.O.R.M. JORNADA MATUTINA CASERIO NUEVA CANDELARIA',1,1),(13,'E.O.U.M. JORNADA MATUTINA VILLAS DEL PEDREGAL I',1,1),(14,'E.O.U.M NUMERO 1 SAN SEBASTIAN',2,1),(15,'E.O.U.M NUMERO 2 SAN SEBASTIAN',2,1),(16,'E.O.R.M. ALDEA SAN LUIS',2,1),(17,'E.O.U.M SANTA CRUZ MULUA',3,1),(18,'INST. NAC. DE EDUC. BASICA INEB SANTA CRUZ MULUA',3,1),(19,'E.O.U.M. SAN MARTIN ZAPOTITLAN',4,1),(20,'INSTITUTO NAC DE EDUCACION BASICA POR COOPERATIVA ITEBA',4,1),(21,'SALON MUNICIPAL SAN MARTIN ZAPOTITLAN',4,1),(22,'INSTITUTO NACIONAL DE EDUCACION BASICA INEBOC SAN FELIPE',5,1),(23,'SALON DE USOS MULTIPLES SAN FELIPE',5,1),(24,'SALON DE USOS MULTIPLES PLANTA ALTA SAN FELIPE',5,1),(25,'SALON MUNICIPAL SAN FELIPE',5,1),(26,'E.O.U.M. 20 DE OCTUBRE SAN ANDRES VILLA SECA',6,1),(27,'ANEXO E.O.U.M. 20 DE OCTUBRE SAN ANDRES VILLA SECA',6,1),(28,'SALON PARROQUIAL SAN ANDRES VILLA SECA',6,1),(29,'EDIFICIO DE CORREOS Y TELEGRAFOS SAN ANDRES VILLA SECA',6,1),(30,'INSTITUTO NAC DE EDUC BASICA POR COOPERATIVA SAN ANDRES VILLA SECA ',6,1),(31,'E.O.R.M. ALDEA TULATE',6,1),(32,'E.O.R.M. PARCELAMIENTO LA MAQUINA LINEA C-4 EJE SECTOR SIS',6,1),(33,'E.O.R.M. ALDEA PAJALES CENTRAL',6,1),(34,'E.O.R.M. PARCELAMIENTO CENTRO URBANO DOS LA MAQUINA',6,1),(35,'SALON MUNICIPAL SAN ANDRES VILLA SECA',6,1),(36,'COLEGIO CENTRO EXPERIMENTAL DEL PACIFICO',7,1),(37,'SALON MUNICIPAL DE USOS MULTIPLES CHAMPERICO',7,1),(38,'E.O.R.M. ALDEA EL ROSARIO ',7,1),(39,'SALON COMUNAL CASERIO NUEVO CAJOLA',7,1),(40,'INSTITUTO MIXTO DE ED. BASICO POR COOPERATIVA PARC EL ROSARIO',7,1),(41,'SALON EMPRESA PORTUARIA NACIONAL CHAMPERICO',7,1),(42,'ESCUELA DE EDUCACION PARA EL HOGAR INTERCULTURAL',8,1),(43,'INSTITUTO DE EDUCACION DIVERSIFICADA COM AGRARIA CANDELARIA XOLHUITZ',8,1),(44,'E.O.R.M. ALDEA O CANTON MORAZAN',8,1),(45,'E.O.R.M. ALDEA O CANTON MONTUFAR SECTOR UNO Y DOS',8,1),(46,'E.O.R.M. ALDEA VERSALLES',8,1),(47,'E.O.R.M. ALDEA GRANADOS SECTOR I',8,1),(48,'ESCUELA OFICIAL DE PARVULOS LEONES 70',8,1),(49,'INSTITUTO TECNICO INDUSTRIAL EL ASINTAL',9,1),(50,'E.O.U.M. EL ASINTAL',9,1),(51,'E.O.R.M. ALDEA SIBANA SECCION II O SIBANA SUR',9,1),(52,'E.O.R.M. PROF JUAN ALBERTO RANGEL GALINDO ALDEA XAB CENTRO',9,1),(53,'E.O.R.M. BARRIO XAB SECCION NUMERO 2 O BARRIO SAN JOSE XAB',9,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departamento`
--

LOCK TABLES `departamento` WRITE;
/*!40000 ALTER TABLE `departamento` DISABLE KEYS */;
INSERT INTO `departamento` VALUES (1,'Retalhuleu');
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
  `usuario_idusuario` int(11) NOT NULL,
  PRIMARY KEY (`iddetalle_votos`,`candidato_idcandidato`,`mesa_idmesa`,`usuario_idusuario`),
  KEY `fk_detalle_votos_candidato1_idx` (`candidato_idcandidato`),
  KEY `fk_detalle_votos_mesa1_idx` (`mesa_idmesa`),
  KEY `fk_detalle_votos_usuario1_idx` (`usuario_idusuario`),
  CONSTRAINT `fk_detalle_votos_candidato1` FOREIGN KEY (`candidato_idcandidato`) REFERENCES `candidato` (`idcandidato`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_detalle_votos_mesa1` FOREIGN KEY (`mesa_idmesa`) REFERENCES `mesa` (`idmesa`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_detalle_votos_usuario1` FOREIGN KEY (`usuario_idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
-- Table structure for table `mesa`
--

DROP TABLE IF EXISTS `mesa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mesa` (
  `idmesa` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `centro_idcentro` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idmesa`,`centro_idcentro`),
  UNIQUE KEY `num_mesa_UNIQUE` (`nombre`),
  KEY `fk_mesa_centro1_idx` (`centro_idcentro`),
  CONSTRAINT `fk_mesa_centro1` FOREIGN KEY (`centro_idcentro`) REFERENCES `centro` (`idcentro`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=437 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mesa`
--

LOCK TABLES `mesa` WRITE;
/*!40000 ALTER TABLE `mesa` DISABLE KEYS */;
INSERT INTO `mesa` VALUES (1,'10236',1,1),(2,'10237',1,1),(3,'10238',1,1),(4,'10239',1,1),(5,'10240',1,1),(6,'10241',1,1),(7,'10242',1,1),(8,'10243',1,1),(9,'10244',1,1),(10,'10245',1,1),(11,'10246',1,1),(12,'10247',1,1),(13,'10248',1,1),(14,'10249',1,1),(15,'10250',2,1),(16,'10251',2,1),(17,'10252',2,1),(18,'10253',2,1),(19,'10254',2,1),(20,'10255',2,1),(21,'10256',3,1),(22,'10257',3,1),(23,'10258',3,1),(24,'10259',3,1),(25,'10260',3,1),(26,'10261',3,1),(27,'10262',4,1),(28,'10263',4,1),(29,'10264',4,1),(30,'10265',4,1),(31,'10266',4,1),(32,'10267',4,1),(33,'10268',4,1),(34,'10269',4,1),(35,'10270',4,1),(36,'10271',4,1),(37,'10272',4,1),(38,'10273',4,1),(39,'10274',4,1),(40,'10275',4,1),(41,'10276',4,1),(42,'10277',5,1),(43,'10278',5,1),(44,'10279',5,1),(45,'10280',5,1),(46,'10281',5,1),(47,'10282',5,1),(48,'10283',6,1),(49,'10284',6,1),(50,'10285',6,1),(51,'10286',6,1),(52,'10287',6,1),(53,'10288',6,1),(54,'10289',6,1),(55,'10290',7,1),(56,'10291',7,1),(57,'10292',7,1),(58,'10293',7,1),(59,'10294',7,1),(60,'10295',7,1),(61,'10296',7,1),(62,'10297',7,1),(63,'10298',7,1),(64,'10299',7,1),(65,'10300',7,1),(66,'10301',7,1),(67,'10302',7,1),(68,'10303',7,1),(69,'10304',7,1),(70,'10305',7,1),(71,'10306',7,1),(72,'10307',8,1),(73,'10308',8,1),(74,'10309',8,1),(75,'10310',8,1),(76,'10311',8,1),(77,'10312',8,1),(78,'10313',8,1),(79,'10314',8,1),(80,'10315',8,1),(81,'10316',8,1),(82,'10317',9,1),(83,'10318',9,1),(84,'10319',9,1),(85,'10320',9,1),(86,'10321',9,1),(87,'10322',9,1),(88,'10323',9,1),(89,'10324',9,1),(90,'10325',9,1),(91,'10326',10,1),(92,'10327',10,1),(93,'10328',10,1),(94,'10329',10,1),(95,'10330',10,1),(96,'10331',10,1),(97,'10332',10,1),(98,'10333',11,1),(99,'10334',11,1),(100,'10335',11,1),(101,'10336',12,1),(102,'10337',12,1),(103,'10338',12,1),(104,'10339',12,1),(105,'10340',12,1),(106,'10341',13,1),(107,'10342',13,1),(108,'10343',13,1),(109,'10344',13,1),(110,'10345',13,1),(111,'10346',13,1),(112,'10347',13,1),(113,'10348',13,1),(114,'10349',13,1),(115,'10350',14,1),(116,'10351',14,1),(117,'10352',14,1),(118,'10353',14,1),(119,'10354',14,1),(120,'10355',14,1),(121,'10356',14,1),(122,'10357',14,1),(123,'10358',14,1),(124,'10359',14,1),(125,'10360',14,1),(126,'10361',14,1),(127,'10362',14,1),(128,'10363',14,1),(129,'10364',14,1),(130,'10365',14,1),(131,'10366',14,1),(132,'10367',14,1),(133,'10368',14,1),(134,'10369',14,1),(135,'10370',15,1),(136,'10371',15,1),(137,'10372',15,1),(138,'10373',15,1),(139,'10374',15,1),(140,'10375',16,1),(141,'10376',16,1),(142,'10377',16,1),(143,'10378',16,1),(144,'10379',16,1),(145,'10380',16,1),(146,'10381',16,1),(147,'10382',16,1),(148,'10383',16,1),(149,'10384',16,1),(150,'10385',15,1),(151,'10386',15,1),(152,'10387',15,1),(153,'10388',17,1),(154,'10389',17,1),(155,'10390',17,1),(156,'10391',17,1),(157,'10392',17,1),(158,'10393',17,1),(159,'10394',17,1),(160,'10395',17,1),(161,'10396',17,1),(162,'10397',17,1),(163,'10398',17,1),(164,'10399',17,1),(165,'10400',17,1),(166,'10401',17,1),(167,'10402',17,1),(168,'10403',18,1),(169,'10404',18,1),(170,'10405',18,1),(171,'10406',18,1),(172,'10407',18,1),(173,'10408',18,1),(174,'10409',19,1),(175,'10410',19,1),(176,'10411',19,1),(177,'10412',19,1),(178,'10413',19,1),(179,'10414',19,1),(180,'10415',19,1),(181,'10416',20,1),(182,'10417',20,1),(183,'10418',20,1),(184,'10419',20,1),(185,'10420',20,1),(186,'10421',20,1),(187,'10422',20,1),(188,'10423',20,1),(189,'10424',21,1),(190,'10425',21,1),(191,'10426',21,1),(192,'10427',21,1),(193,'10428',21,1),(194,'10429',22,1),(195,'10430',22,1),(196,'10431',22,1),(197,'10432',22,1),(198,'10433',22,1),(199,'10434',22,1),(200,'10435',22,1),(201,'10436',22,1),(202,'10437',22,1),(203,'10438',22,1),(204,'10439',22,1),(205,'10440',22,1),(206,'10441',22,1),(207,'10442',22,1),(208,'10443',22,1),(209,'10444',22,1),(210,'10445',23,1),(211,'10446',23,1),(212,'10447',23,1),(213,'10448',23,1),(214,'10449',23,1),(215,'10450',24,1),(216,'10451',24,1),(217,'10452',25,1),(218,'10453',25,1),(219,'10454',25,1),(220,'10455',25,1),(221,'10456',25,1),(222,'10457',25,1),(223,'10458',22,1),(224,'10459',22,1),(225,'10460',22,1),(226,'10461',26,1),(227,'10462',26,1),(228,'10463',26,1),(229,'10464',26,1),(230,'10465',27,1),(231,'10466',27,1),(232,'10467',27,1),(233,'10468',27,1),(234,'10469',27,1),(235,'10470',28,1),(236,'10471',29,1),(237,'10472',30,1),(238,'10473',30,1),(239,'10474',30,1),(240,'10475',30,1),(241,'10476',30,1),(242,'10477',30,1),(243,'10478',30,1),(244,'10479',30,1),(245,'10480',31,1),(246,'10481',31,1),(247,'10482',31,1),(248,'10483',31,1),(249,'10484',32,1),(250,'10485',32,1),(251,'10486',32,1),(252,'10487',32,1),(253,'10488',32,1),(254,'10489',32,1),(255,'10490',32,1),(256,'10491',32,1),(257,'10492',33,1),(258,'10493',33,1),(259,'10494',33,1),(260,'10495',33,1),(261,'10496',33,1),(262,'10497',33,1),(263,'10498',33,1),(264,'10499',34,1),(265,'10500',34,1),(266,'10501',34,1),(267,'10502',34,1),(268,'10503',34,1),(269,'10504',34,1),(270,'10505',34,1),(271,'10506',34,1),(272,'10507',34,1),(273,'10508',34,1),(274,'10509',34,1),(275,'10510',34,1),(276,'10511',34,1),(277,'10512',34,1),(278,'10513',34,1),(279,'10514',35,1),(280,'10515',35,1),(281,'10516',35,1),(282,'10517',35,1),(283,'10518',36,1),(284,'10519',36,1),(285,'10520',36,1),(286,'10521',36,1),(287,'10522',36,1),(288,'10523',36,1),(289,'10524',36,1),(290,'10525',36,1),(291,'10526',37,1),(292,'10527',37,1),(293,'10528',37,1),(294,'10529',37,1),(295,'10530',37,1),(296,'10531',37,1),(297,'10532',37,1),(298,'10533',37,1),(299,'10534',38,1),(300,'10535',38,1),(301,'10536',38,1),(302,'10537',38,1),(303,'10538',38,1),(304,'10539',38,1),(305,'10540',38,1),(306,'10541',38,1),(307,'10542',38,1),(308,'10543',38,1),(309,'10544',38,1),(310,'10545',38,1),(311,'10546',38,1),(312,'10547',39,1),(313,'10548',39,1),(314,'10549',39,1),(315,'10550',40,1),(316,'10551',40,1),(317,'10552',40,1),(318,'10553',40,1),(319,'10554',40,1),(320,'10555',40,1),(321,'10556',40,1),(322,'10557',40,1),(323,'10558',40,1),(324,'10559',40,1),(325,'10560',40,1),(326,'10561',40,1),(327,'10562',40,1),(328,'10563',41,1),(329,'10564',41,1),(330,'10565',41,1),(331,'10566',42,1),(332,'10567',42,1),(333,'10568',42,1),(334,'10569',42,1),(335,'10570',42,1),(336,'10571',42,1),(337,'10572',42,1),(338,'10573',42,1),(339,'10574',42,1),(340,'10575',42,1),(341,'10576',43,1),(342,'10577',43,1),(343,'10578',43,1),(344,'10579',43,1),(345,'10580',43,1),(346,'10581',43,1),(347,'10582',43,1),(348,'10583',43,1),(349,'10584',43,1),(350,'10585',43,1),(351,'10586',43,1),(352,'10587',43,1),(353,'10588',44,1),(354,'10589',44,1),(355,'10590',44,1),(356,'10591',44,1),(357,'10592',44,1),(358,'10593',44,1),(359,'10594',44,1),(360,'10595',44,1),(361,'10596',44,1),(362,'10597',44,1),(363,'10598',44,1),(364,'10599',44,1),(365,'10600',45,1),(366,'10601',45,1),(367,'10602',45,1),(368,'10603',45,1),(369,'10604',45,1),(370,'10605',45,1),(371,'10606',45,1),(372,'10607',46,1),(373,'10608',46,1),(374,'10609',46,1),(375,'10610',46,1),(376,'10611',46,1),(377,'10612',46,1),(378,'10613',46,1),(379,'10614',46,1),(380,'10615',47,1),(381,'10616',47,1),(382,'10617',47,1),(383,'10618',47,1),(384,'10619',48,1),(385,'10620',48,1),(386,'10621',48,1),(387,'10622',49,1),(388,'10623',49,1),(389,'10624',49,1),(390,'10625',49,1),(391,'10626',49,1),(392,'10627',49,1),(393,'10628',49,1),(394,'10629',49,1),(395,'10630',49,1),(396,'10631',49,1),(397,'10632',49,1),(398,'10633',49,1),(399,'10634',49,1),(400,'10635',49,1),(401,'10636',49,1),(402,'10637',50,1),(403,'10638',50,1),(404,'10639',50,1),(405,'10640',50,1),(406,'10641',50,1),(407,'10642',50,1),(408,'10643',51,1),(409,'10644',51,1),(410,'10645',51,1),(411,'10646',51,1),(412,'10647',51,1),(413,'10648',51,1),(414,'10649',51,1),(415,'10650',51,1),(416,'10651',51,1),(417,'10652',51,1),(418,'10653',51,1),(419,'10654',52,1),(420,'10655',52,1),(421,'10656',52,1),(422,'10657',52,1),(423,'10658',52,1),(424,'10659',52,1),(425,'10660',52,1),(426,'10661',52,1),(427,'10662',52,1),(428,'10663',53,1),(429,'10664',53,1),(430,'10665',53,1),(431,'10666',53,1),(432,'10667',53,1),(433,'10668',53,1),(434,'10669',53,1),(435,'10670',50,1),(436,'10671',50,1);
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
  `estado` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idmunicipio`,`departamento_iddepartamento`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  KEY `fk_municipio_departamento1_idx` (`departamento_iddepartamento`),
  CONSTRAINT `fk_municipio_departamento1` FOREIGN KEY (`departamento_iddepartamento`) REFERENCES `departamento` (`iddepartamento`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `municipio`
--

LOCK TABLES `municipio` WRITE;
/*!40000 ALTER TABLE `municipio` DISABLE KEYS */;
INSERT INTO `municipio` VALUES (1,'RETALHULEU',14,10236,10349,1,1),(2,'SAN SEBASTIAN',4,10350,10387,1,1),(3,'SANTA CRUZ MULUA',3,10388,10408,1,1),(4,'SAN MARTIN ZAPOTITLAN',4,10409,10428,1,1),(5,'SAN FELIPE',5,10445,10460,1,1),(6,'SAN ANDRES VILLA SECA',10,10461,10517,1,1),(7,'CHAMPERICO',6,10518,10565,1,1),(8,'NUEVO SAN CARLOS',7,10566,10621,1,1),(9,'EL ASINTAL',6,10637,10671,1,1),(10,'N/A',0,0,0,1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partido_politico`
--

LOCK TABLES `partido_politico` WRITE;
/*!40000 ALTER TABLE `partido_politico` DISABLE KEYS */;
INSERT INTO `partido_politico` VALUES (1,'COMITÉ CIVICO EL OJO',1,'2015-08-24'),(2,'CONVERGENCIA',1,'2015-08-24'),(3,'CREO',1,'2015-08-24'),(4,'ENCUNETRO POR GUATEMALA',1,'2015-08-24'),(5,'FCN',1,'2015-08-24'),(6,'FUERZA',1,'2015-08-24'),(7,'LIDER',1,'2015-08-24'),(8,'MILPA',1,'2015-08-24'),(9,'MNR',1,'2015-08-24'),(10,'MOVIMIENTO REFORMADOR',1,'2015-08-24'),(11,'NUEVA NACION',1,'2015-08-24'),(12,'PAN',1,'2015-08-24'),(13,'PARTICIPATIVO CIUDADANO',1,'2015-08-24'),(14,'PATRIOTA',1,'2015-08-24'),(15,'PRI',1,'2015-08-24'),(16,'PROYECTO PROGRESISTA',1,'2015-08-24'),(17,'TODOS',1,'2015-08-24'),(18,'UCN',1,'2015-08-24'),(19,'UNE',1,'2015-08-24'),(20,'VIVA',1,'2015-08-24'),(21,'WINAQ URNG',1,'2015-08-24');
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puesto`
--

LOCK TABLES `puesto` WRITE;
/*!40000 ALTER TABLE `puesto` DISABLE KEYS */;
INSERT INTO `puesto` VALUES (1,'Presidente',1,'2015-08-21'),(2,'Diputado Listado Nacianal',1,'2015-08-21'),(3,'Diputado Parlacen',1,'2015-08-21'),(4,'Diputado Distrital',1,'2015-08-21'),(5,'Alcalde',1,'2015-08-21'),(7,'Voto Nulo',1,'2015-08-21'),(8,'Voto Blanco',1,'2015-08-21');
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
  `estado` tinyint(1) DEFAULT '0',
  `fechacreacion` date NOT NULL,
  PRIMARY KEY (`idusuario`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  UNIQUE KEY `usuario_UNIQUE` (`usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (4,'Administrador','12345678','Admin','admin2015',1,'2015-08-24');
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

-- Dump completed on 2015-08-25  8:00:49

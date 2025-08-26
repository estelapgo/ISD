-- ----------------------------------------------------------------------------
-- Model
-- -----------------------------------------------------------------------------

DROP TABLE purchase;
DROP TABLE `match`;

-- -----------match----------------
CREATE TABLE `match` (
    idmatch BIGINT NOT NULL AUTO_INCREMENT,
    matchdate DATETIME NOT NULL,
    ticketsprice FLOAT NOT NULL,
    capacity INT NOT NULL,
    soldunits INT NOT NULL,
    registerdate DATETIME NOT NULL,
    visitingteam VARCHAR(255) COLLATE latin1_bin NOT NULL,

    CONSTRAINT MatchPK PRIMARY KEY (idmatch),
    CONSTRAINT VALID_PRICE CHECK (ticketsprice > 0), -- ESTO ES UNA RESTRICCION QUE YA ES VALIDADA POR EL INPUTVALIDATIONEXCEPTION
    CONSTRAINT VALID_CAPACITY CHECK (capacity > 0),
    CONSTRAINT VALID_SOLD_UNITS CHECK (soldunits <= `match`.capacity) ) ENGINE = InnoDB;

-- -----------purchase----------------
CREATE TABLE purchase (
    idpurchase BIGINT NOT NULL AUTO_INCREMENT,
    useremail VARCHAR(255) COLLATE latin1_bin NOT NULL,
    bankcard VARCHAR (16) COLLATE latin1_bin NOT NULL, -- NO DEBERÍA VALER UN INT??
    purchasedate DATETIME NOT NULL,
    matchid BIGINT NOT NULL,
    units INT NOT NULL,
    collected BOOLEAN NOT NULL,

    CONSTRAINT PurchasePK PRIMARY KEY (idpurchase),
    CONSTRAINT MatchIdFK FOREIGN KEY (matchid)
        REFERENCES `match`(idmatch) ON DELETE CASCADE ) ENGINE = InnoDB;
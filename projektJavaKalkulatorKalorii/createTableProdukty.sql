CREATE TABLE IF NOT EXISTS Produkty(
    idProduktu INT NOT NULL AUTO_INCREMENT,
    nazwaProduktu VARCHAR(255) NOT NULL,
    wartoscEnergetyczna FLOAT,
    bialka FLOAT,
    tluszcze FLOAT,
    weglowodany FLOAT,
    ilosc FLOAT,
    jednostkaMiary VARCHAR(8),
    PRIMARY KEY(idProduktu)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS Zjedzone(
    id INT NOT NULL AUTO_INCREMENT,
    idPosilku INT NOT NULL,
    nazwaProduktu VARCHAR(255) NOT NULL,
    wartoscEnergetyczna FLOAT,
    bialka FLOAT,
    tluszcze FLOAT,
    weglowodany FLOAT,
    ilosc FLOAT,
    jednostkaMiary VARCHAR(8),
    PRIMARY KEY(id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8; 

CREATE TABLE IF NOT EXISTS Posilki(
    idPosilku INT NOT NULL AUTO_INCREMENT,
    idUzytkownika INT,
    godzinaPosilku TIMESTAMP,
    nazwaPosilku VARCHAR(255) NOT NULL,
    PRIMARY KEY(idPosilku)
) ENGINE = InnoDB DEFAULT CHARSET = utf8; 


CREATE TABLE IF NOT EXISTS Uzytkownicy(
    idUzytkownika INT NOT NULL AUTO_INCREMENT,
    nazwaUzytkownika VARCHAR(255),
	plec VARCHAR(32),
	wiek INT NOT NULL,
	waga FLOAT NOT NULL,
	wzrost FLOAT NOT NULL,
	poziomRuchu INT,
	cel INT,
	wartoscEnergetyczna FLOAT,
	bialka FLOAT,
	tluszcze FLOAT,
	weglowodany FLOAT,
    PRIMARY KEY(idUzytkownika)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;


--CREATE TABLE IF NOT EXISTS Uzytkownicy(
--    idUzytkownika INT NOT NULL AUTO_INCREMENT,
--    nazwaUzytkownika VARCHAR(255),
--    PRIMARY KEY(idUzytkownika)
--) ENGINE = InnoDB DEFAULT CHARSET = utf8;





-- select user,host from mysql.user;
-- show grants for 'user'@'host';
-- revoke all privileges on *.* from 'user'@'host';

grant ALL ON `licznikkalorii`.* TO 'karolina'@'localhost';
flush privileges;

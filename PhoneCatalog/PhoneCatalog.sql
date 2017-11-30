CREATE DATABASE PhoneCatalog;

CREATE TABLE PhoneCatalog.members(
    ID int(10) NOT NULL AUTO_INCREMENT,
    Fname varchar(15) NOT NULL,
	Lname varchar(15) NOT NULL,
	phone int(15), 
    PRIMARY KEY (ID)
);

INSERT INTO PhoneCatalog.members (Fname, Lname, phone) VALUES 
('chris','dalamitras', '6909884866'),
('agishlaos','georgoylias', '6909884855'),
('angel','agiorgiths', '6909884833'),
('swkraths','saxarakis', '6909884811'),
('chris','dimitrioy', '6909884822'),
('nikos','kolitsas', '6909884833'),
('labis','anastopoylos', '6909884444');
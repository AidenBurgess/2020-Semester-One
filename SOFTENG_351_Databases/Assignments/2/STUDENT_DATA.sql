CREATE TABLE students(
sid int(4) NOT NULL ,
PASSWORD int(5) ,
fname varchar(20) ,
lname varchar(20) ,
sType varchar(5) CHECK (sType IN ('GRAD','UGRAD')),
major char(4) CHECK (major IN ('CSC','MATH','POLS','HIST')),
gradAssistant char(1) CHECK (gradAssistant IN ('Y','N')),
inState char(1) CHECK (inState IN ('Y','N')),
PRIMARY KEY (sid)
);

insert into students values
(1111,1111,'John','Davison','UGRAD','CSC','N','Y');
insert into students values
(2222,2222,'Jacob','Oram','UGRAD','CSC','N','N');
insert into students values
(3333,3333,'Ashish','Bagai','GRAD','CSC','Y','N');
insert into students values
(4444,4444,'Joe','Harris','GRAD','CSC','N','Y');
insert into students values
(5555,5555,'Andy','Blignaut','GRAD','CSC','N','Y');
insert into students values
(6666,6666,'Pommie','Mbangwa','GRAD','CSC','N','Y');
insert into students values
(7777,7777,'Ian','Healy','GRAD','CSC','N','Y');
insert into students values
(8888,8888,'Dougie','Marillier','GRAD','CSC','N','Y');

CREATE TABLE staff(
tid int(4) PRIMARY KEY,
PASSWORD int(5),
fname varchar(20),
lname varchar(20),
staffType varchar(10) CHECK (staffType IN ('REGISTRAR','DEPARTMENT'))
);

insert into staff values
(1000,1000,'Venette','Rice','DEPARTMENT');
insert into staff values
(2000,2000,'Alison','Payne','REGISTRAR');

create view lunarUsers as
(select sid uid, password, 'STUDENT' uType
from students) union
(select tid uid, password, staffType uType
from staff);

CREATE TABLE courses(
cprefix char(4) ,
cno int(4) ,
ctitle varchar(50) ,
chours int(2) ,
PRIMARY KEY (cprefix,cno)
);

insert into courses values ('CSC',1010,'Computers and Applications',3);
insert into courses values ('CSC',2010,'Introduction to Computer Science',3);
insert into courses values ('CSC',2310,'Intro to Programming in Java',3);
insert into courses values ('CSC',2311,'Introduction to Programming in C++',3);
insert into courses values ('CSC',3410,'Data Structures',3);
insert into courses values ('CSC',3210,'Computer Organization',3);
insert into courses values ('CSC',3320,'Systems Programming in Unix and C',3);
insert into courses values ('MATH',2211,'Calculus I',5);
insert into courses values ('MATH',2212,'Calculus II',5);
insert into courses values ('MATH',2420,'Discrete Mathematics',3);
insert into courses values ('CSC',6220,'Networks',4);
insert into courses values ('CSC',8220,'Advanced Networks',4);
insert into courses values ('CSC',6710,'Database',4);
insert into courses values ('CSC',8710,'Advanced Database',4);
insert into courses values ('CSC',6820,'Graphics',4);
insert into courses values ('CSC',8820,'Advanced Graphics',4);
insert into courses values ('POLS',1200,'Intro Political Sci',3);

CREATE TABLE sections(
term char(2) CHECK (term IN ('FA','SP','SU')),
year int(4),
crn int(5),
cprefix char(4),
cno int(4),
section int(2),
days char(6),
startTime char(5),
endTime char(5),
room varchar(10),
cap int(3),
instructor varchar(30),
auth char(1) CHECK (auth IN ('Y','N')),
PRIMARY KEY (term, year, crn),
FOREIGN KEY (cprefix, cno) REFERENCES courses(cprefix, cno)
);

insert into sections values
('SU',2002,10101,'CSC',1010,1,'MWF','09.00','09.50','105G',35,'Bhola','N');
insert into sections values
('SU',2002,10701,'POLS',1200,1,'TR','09.00','09.50','205Sp',25,'Jones','N');
insert into sections values
('FA',2002,10101,'CSC',2010,1,'MWF','09.00','09.50','105G',35,'Bhola','N');
insert into sections values
('FA',2002,10102,'CSC',2010,2,'MWF','10.00','10.50','105CS',40,'Henry','N');
insert into sections values
('FA',2002,10103,'CSC',2310,1,'MWF','12.00','12.50','106G',30,'Henry','N');
insert into sections values
('FA',2002,10104,'CSC',2311,1,'MWF','15.00','15.50','205G',35,'Liu','N');
insert into sections values
('FA',2002,10201,'CSC',6220,1,'TR','19.00','20.40','405G',25,'Hundewale','N');
insert into sections values
('FA',2002,10202,'CSC',6710,1,'TR','16.00','17.15','115CS',25,'Madiraju','N');
insert into sections values
('FA',2002,10203,'CSC',8820,1,'MWF','09.00','09.50','605G',25,'Owen','N');
insert into sections values
('FA',2002,10301,'MATH',2211,1,'TR','11.00','12.50','305G',35,'Li','N');
insert into sections values
('FA',2002,10302,'MATH',2211,2,'MWF','09.00','10.50','106GB',35,'Davis','N');
insert into sections values
('SP',2003,10101,'CSC',2010,1,'MWF','09.00','09.50','105G',35,'Bhola','N');
insert into sections values
('SP',2003,10102,'CSC',2010,2,'MWF','10.00','10.50','105CS',40,'Henry','N');
insert into sections values
('SP',2003,10103,'CSC',2310,1,'MWF','12.00','12.50','106G',30,'Henry','N');
insert into sections values
('SP',2003,10104,'CSC',2311,1,'MWF','15.00','15.50','205G',35,'Liu','N');
insert into sections values
('SP',2003,10201,'CSC',6220,1,'TR','19.00','20.40','405G',25,'Hundewale','N');
insert into sections values
('SP',2003,10202,'CSC',6710,1,'TR','16.00','17.15','115CS',25,'Madiraju','N');
insert into sections values
('SP',2003,10203,'CSC',8220,1,'MWF','09.00','09.50','605G',25,'Bourgeois','Y');
insert into sections values
('SP',2003,10301,'MATH',2211,1,'TR','11.00','12.50','305G',35,'Li','N');
insert into sections values
('SP',2003,10302,'MATH',2211,2,'MWF','09.00','10.50','606GB',35,'Miller','N');
insert into sections values
('SP',2003,10303,'MATH',2212,1,'MWF','09.00','10.50','706GB',35,'Davis','N');
insert into sections values
('SP',2003,10304,'MATH',2420,1,'TR','14.00','14.50','106GB',35,'Domke','N');
insert into sections values
('SP',2003,10405,'CSC',8710,1,'MW','17.30','18.45','206GB',35,'Dogdu','N');
insert into sections values
('SP',2003,10406,'CSC',8820,1,'TR','19.15','20.55','306GB',3,'Owen','N');

CREATE TABLE enrolls (
sid int(4),
term char(2) CHECK (term IN ('FA','SP','SU')),
year int(4),
crn int(5),
grade char(2) CHECK (grade IN ('A','B','C','D','F','I','IP','S','U')),
PRIMARY KEY ( sid, term, year, crn ) ,
FOREIGN KEY ( sid ) REFERENCES students(sid),
FOREIGN KEY ( term, year, crn ) REFERENCES sections(term, year, crn)
);

insert into enrolls values (1111,'SU',2002,10101,'A');
insert into enrolls values (1111,'SU',2002,10701,'C');
insert into enrolls values (1111,'FA',2002,10101,null);
insert into enrolls values (1111,'FA',2002,10103,null);
insert into enrolls values (1111,'FA',2002,10301,null);
insert into enrolls values (2222,'FA',2002,10101,null);
insert into enrolls values (2222,'FA',2002,10201,null);
insert into enrolls values (3333,'FA',2002,10201,null);
insert into enrolls values (3333,'FA',2002,10202,null);
insert into enrolls values (3333,'FA',2002,10203,null);

CREATE TABLE authorizations (
term char(2) CHECK (term IN ('FA','SP','SU')),
year int(4),
crn int(5),
sid int(4),
authType char(4) CHECK (authType IN ('OVFL','AUTH')),
PRIMARY KEY (term, year, crn, sid, authType),
FOREIGN KEY (sid) REFERENCES students(sid),
FOREIGN KEY (term, year, crn) REFERENCES sections(term, year, crn)
);

CREATE TABLE fixedFee(
feeName varchar(30) PRIMARY KEY ,
fee numeric(5, 2)
);

insert into fixedFee values ('Technology Fee',75.00);
insert into fixedFee values ('Health Fee',30.00);
insert into fixedFee values ('Activity Fee',65.00);
insert into fixedFee values ('Transportation Fee',25.00);

CREATE TABLE variableFeeRate(
sType varchar(6) CHECK (sType IN ('GRAD','UGRAD')),
inOrOutOfState varchar( 10 ) CHECK (inOrOutOfState IN ('INSTATE','OUTOFSTATE')),
fee numeric(6, 2),
PRIMARY KEY (sType, inOrOutOfState)
);

insert into variableFeeRate values ('GRAD','INSTATE',125.00);
insert into variableFeeRate values ('GRAD','OUTOFSTATE',500.00);
insert into variableFeeRate values ('UGRAD','INSTATE',100.00);
insert into variableFeeRate values ('UGRAD','OUTOFSTATE',400.00);
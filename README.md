 ************************************************************
 ____   _____   ____   _____  _______                 ________
|    | |     | |    | |     |    |               /|   |___ \
|____| |     | |____| |     |    |              / |     __) |
|\     |     | |    | |     |    |     \    /     |    |__ <
| \    |     | |    | |     |    |      \  /      |    ___) |
|  \   |_____| |____| |_____|    |       \/  .    | . |____/

version 1.3, last release 14.04.2016

*************************************************************

AUTHORS	Credits
Marcin Maczka & Bartosz Arciemowicz

*************************************************************

MODIFIED by:
 ________________ _____ _________________
|                |     |                 |
| Adrian Drabik  |  &  |  Bartosz Klys   |
|________________|_____|_________________|

*************************************************************
READ THIS!

Some dependencies are not up to date because then, they did not
work properly:
mySQL Connector Driver - recommended version 5.1.38, not newer !

For more information about dependencies take a look on About Page.

Every library has unique ID in database. If any book has no tag
there is a NULL in field.

*************************************************************

THANKS	Acknowledgments
google, stackoverflow etc..

*************************************************************

INSTALL	Installation instructions

How to set-up MySQL database (in terminal):
1. sudo apt-get install mysql-server
2. mysql -u root -p
3. create database name_of_database;
4. show name_of_database;
5. use name_of_database;
6. show tables;
You can also download MySQL Workbench from Software Center.


To install this software just copy it into some catalog.
You also need libraries.xml present in this catalog.

In persistence.xml file you need to type the name of your database and password (example below):
<property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/name_of_database"/>
<property name="javax.persistence.jdbc.password" value="password_to_database"/>

*************************************************************

RUNNING

Robot has two modes. Background and User Interface.

To start our application in GUI mode you may run java file.
In this mode you can check your results.

If you want to run in background you should add argument "-b".
In this mode actual searching will happen. Results will be save
to the database and you can check the results in graphical user
interface mode.

*************************************************************

TESTS
Some tests are designed as integration tests. Please mind that
those are excluded from Maven test phase. In order to run them
you must delete "<groups>" element in maven-surefire-plugin.

*************************************************************

COPYING / LICENSE	Copyright and licensing information
2016 All Rights Reserved

*************************************************************

BUGS	Known bugs and instructions on reporting new ones

In case of any bugs please do not hesitate and feel free to contact us:
Adrian_Drabik@epam.com
Bartosz_Klys@epam.com

*************************************************************
Enjoy!

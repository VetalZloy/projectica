# [Projectica]

Projectica is a social network, which helps young developers to find like-minded ones. Also Projectica gives you abilities to create projects(startups) by your wish and hand-pick team for such a project. 

## Important
Projectica uses [messaging] to provide communication to users.
   
### Deploying
Projectica requires Java 8, Maven, Redis and some RDBMS. Here is shown deploying process for Windows, MySQL and Gmail.

1. Clone or download Projectica
2. Open terminal, change directory to project folder and type:
```bat
mysql -uroot -p
create database projectica_db;
exit
mysql -uroot -p projectica_db < projectica_db_dump.sql
```
2. Change some properties in src/main/resources folder:
2.1 Edit dao.properties (username and password for database)
2.2 Edit util.properties. Example for Gmail (gmail may block you - [solution]):
```properties
#Email properties
email.username=--your gmail address--
email.password=--your password--
email.host=smtp.gmail.com
email.port=587
```
3. Open terminal, change directory to project folder and type:
```bat
mvn package
java -jar target/Projectica.jar
```
4. Projectica works!!!



   [Projectica]: <https://projectica.me>
   [About]: <https://projectica.me/about>
   [solution]: <https://support.google.com/accounts/answer/6010255>
   [messaging]: https://github.com/VetalZloy/messaging
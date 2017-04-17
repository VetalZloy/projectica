# [Projectica]

Projectica is a social network, which helps young developers to find like-minded ones. Also Projectica gives you abilities to create projects(startups) by your wish and hand-pick team for such project. If you need more details, visit [About] page.
   
### Deploying
Projectica requires Java 8. Here is shown deploying process for Windows, MySQL and Gmail.

1. Clone or download Projectica
2. Open terminal, change directory to project folder and type:
```bat
mysql -uroot -p
create database projectica_db;
exit
mysql -uroot -p projectica_db < projectica_db_dump.sql
```
3. Close terminal
4. Edit src/main/resources/dao.properties (username and password for database)
5. Edit src/main/resources/util.properties. Example for Gmail (gmail may block you - [solution]) :
```properties
#Email properties
email.username=--your gmail address--
email.password=--your password--
email.host=smtp.gmail.com
email.port=587
```

6. Run package.bat or package_without_test.bat in project directory.
7. Projectica.war in target deirectory ready for deploy!



   [Projectica]: <https://projectica.me>
   [About]: <https://projectica.me/about>
   [solution]: <https://support.google.com/accounts/answer/6010255>
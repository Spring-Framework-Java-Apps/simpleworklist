simpleworklist
==============

Your Todo List for Getting Things Done

Software-Stack: Java EE, JPA, Spring Frameworks, Spring MVC, Twitter Bootstrap, jQuery, HTML5

Setup local Test-System
-----------------------

1. Setup a MySQL-Database 
2. Setup Wildfly10 as described in http://thomas-woehlke.blogspot.de/2016/02/mysql-datasource-for-wildfly10-like.html
3. copy src/main/resources/worklist_sample.properties to _src/main/resources/worklist.properties
4. build project with: mvn clean install 
5. Deploy by cp deployment/ROOT.war ~/srv/wildfly-10.0.0.Final/standalone/deployments/
6. start wildfly server: cd ~/srv/wildfly-10.0.0.Final/bin ; ./standalone.sh

Getting Things Done
-------------------
GTD&reg; and Getting Things Done&reg; are registered trademarks of the David Allen Company. 
SimpleWorklist is not affiliated with or endorsed by the David Allen Company.
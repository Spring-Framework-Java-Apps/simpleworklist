simpleworklist
==============

Your Todo List for Getting Things Done

Software-Stack: Spring-Boot, JPA, Spring MVC, Thymeleaf, Twitter Bootstrap, jQuery, HTML5

Setup local Test-System
-----------------------

1. Setup a PostgreSQL 10-Database
3. copy src/main/resources/worklist_sample.properties to _src/main/resources/worklist.properties
4. start project with: mvn clean spring-boot:run

Getting Things Done
-------------------
GTD&reg; and Getting Things Done&reg; are registered trademarks of the David Allen Company. 
SimpleWorklist is not affiliated with or endorsed by the David Allen Company.

Setup a PostgreSQL 10-Database
------------------------------
* https://www.postgresql.org/download/
* Windows and macOS - Graphical installer by *BigSQL*: https://www.openscg.com/bigsql/postgresql/installers.jsp/
* Windows: 
    1. start cmd as admin
    2. cd \PostgreSQL
    3. pgc start
    4. pgc pgadmin3

Developer Reminder
------------------
* Create serialVersionUID for Classes with "implements Serializable": use the JDK tool “serialver“


Drag and Drop
-------------
* https://jqueryhouse.com/jquery-drag-and-drop-plugins/
* https://mdbootstrap.com/plugins/jquery/draggable/#introduction
* https://github.com/Shopify/draggable/tree/master/examples/src/content/Droppable/UniqueDropzone
* https://github.com/Shopify/draggable#documentation
* https://jqueryhouse.com/jquery-drag-and-drop-plugins/
* https://shopify.github.io/draggable/examples/simple-list.html
* https://www.elated.com/drag-and-drop-with-jquery-your-essential-guide/
* https://medium.com/@okandavut/using-jquery-ui-drag-drop-64a24e75e805
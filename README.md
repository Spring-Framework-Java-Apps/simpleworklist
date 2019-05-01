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

Shortcuts
---------
* https://fontawesome.com/icons?d=gallery&m=free
* https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
* https://ckeditor.com/docs/ckeditor4/latest/guide/dev_installation.html
* http://impossibl.github.io/pgjdbc-ng/docs/current/user-guide/#drivers
* https://devcenter.heroku.com/articles/getting-started-with-java
* https://devcenter.heroku.com/articles/java-support#specifying-a-java-version

# java-getting-started

A barebones Java app, which can easily be deployed to Heroku.

This application supports the [Getting Started with Java on Heroku](https://devcenter.heroku.com/articles/getting-started-with-java) article - check it out.

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

## Running Locally

Make sure you have Java and Maven installed.  Also, install the [Heroku CLI](https://cli.heroku.com/).

```sh
$ git clone https://github.com/heroku/java-getting-started.git
$ cd java-getting-started
$ mvn install
$ heroku local:start
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

If you're going to use a database, ensure you have a local `.env` file that reads something like this:

```
JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/java_database_name
```

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

## Documentation

For more information about using Java on Heroku, see these Dev Center articles:

- [Java on Heroku](https://devcenter.heroku.com/categories/java)

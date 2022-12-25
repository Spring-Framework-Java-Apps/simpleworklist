# HOWTO

### Howto
* [Spring dependency-management-plugin](https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/)
* [Example on github: heroku and Gradle](https://github.com/heroku/gradle-getting-started/blob/master/build.gradle)
* [Howto: Using lombok](https://projectlombok.org/setup/overview)
* [Howto: Gradle Docker Compose](https://bmuschko.com/blog/gradle-docker-compose/)

### Documentation
* [spring-boot](https://spring.io/projects/spring-boot)
* [spring-data-jpa](https://docs.spring.io/spring-data/jpa/docs/2.7.6/reference/html/)
* [spring-framework](https://docs.spring.io/spring-framework/docs/5.3.23/reference/html/)
* [spring-framework Data Access](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/data-access.html)
* [spring-framework WebMVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
* [spring-boot reference](https://docs.spring.io/spring-boot/docs/2.7.5/reference/html/)
* [spring-boot appendix-application-properties](https://docs.spring.io/spring-boot/docs/2.7.5/reference/html/application-properties.html#appendix.application-properties)
* [spring-boot api](https://docs.spring.io/spring-boot/docs/2.7.5/api/)
* [spring-data-jpa](https://docs.spring.io/spring-data/jpa/docs/2.7.5/reference/html/)

### Github Repos
* [spring-cloud-contract](https://github.com/spring-cloud/spring-cloud-contract)
* [dependency-management-plugin](https://github.com/spring-gradle-plugins/dependency-management-plugin)
* [asciidoctor-gradle-plugin](https://github.com/asciidoctor/asciidoctor-gradle-plugin)
* [gradle-docker-compose-plugin](https://github.com/avast/gradle-docker-compose-plugin)
* [liquibase-gradle-plugin](https://github.com/liquibase/liquibase-gradle-plugin)

## Database and JPA

### DB Datatypes
* [H2 Datatypes](http://www.h2database.com/html/datatypes.html)
* [PostgreSQL Datatypes](https://www.postgresql.org/docs/11/datatype.html)
* LocalDateTime and TimeZone: TODO

### UUID and Optimistic Locking
* UUID: TODO

### Database Schema Evolution with Spring Boot JPA - liquibase
* TODO
* org.flywaydb:flyway
* [org.liquibase:liquibase](https://www.liquibase.org/documentation/maven/index.html)
 

## Frontend with webjars
### updating webjar Versions:
* change Version in pom.xml section properties (Line 82)
* change Version in src/main/resources/templates/layout/page.html Section head.tw-head and div.tw-footer (Lines 16, 98) 

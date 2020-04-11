# TODO 

* https://www.baeldung.com/spring-session
* EmbeddedSolrServer:
* https://docs.spring.io/spring-data/solr/docs/4.1.6.RELEASE/reference/html/#solr.annotation
* https://www.youtube.com/watch?v=hieLEsp5cTk


## 2.3.7
Fixed #32, Fixed #36,  Fixed #89, Fixed #83, Fixed #84, Fixed #85, Fixed #88, Fixed #90, Fixed #91, Fixed #99


## 2.3.8
Fixed #79, Fixed #80, Fixed #81, Fixed #100, Fixed #101


### BUGS:
*  #101 ERROR: relation "spring_session" does not exist

````
 Unexpected error occurred in scheduled task

org.springframework.jdbc.BadSqlGrammarException: PreparedStatementCallback; bad SQL grammar [DELETE FROM SPRING_SESSION WHERE EXPIRY_TIME < ?]; nested exception is org.postgresql.util.PSQLException: ERROR: relation "spring_session" does not exist
  Position: 13

Caused by: org.postgresql.util.PSQLException: ERROR: relation "spring_session" does not exist
  Position: 13
````

### Read about Spring-Session:
* [Spring Session with JDBC](https://www.baeldung.com/spring-session-jdbc)
* [Spring Session with JDBC on Github](https://github.com/eugenp/tutorials/tree/master/spring-session/spring-session-jdbc)
* [Spring Session with JDBC](https://www.javadevjournal.com/spring/spring-session-with-jdbc/)

## 2.3.9 Fixed #79, Fixed #80, Fixed #81, Fixed #100, Fixed #101

## 2.3.10 - Bugfixing 

* F007 Add first new Task: broken

An error happened during template parsing (template: "class path resource [templates/task/add.html]"


## F008 Add first new Project: broken
-> Form OK
-> Save Data: Error

http://localhost:8080/project/root
-> Button "add new project"
http://localhost:8080/project/add/new/project
-> Save -> ERROR
Errorpage - Refer to logfile:
````
2020-04-03 14:30:37.148 DEBUG 2864 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:30:58.514 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : POST "/project/addchild/", parameters={masked}
2020-04-03 14:30:58.586  WARN 2864 --- [nio-8080-exec-8] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'POST' not supported]
2020-04-03 14:30:58.586 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : Completed 405 METHOD_NOT_ALLOWED
2020-04-03 14:30:58.590 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : "ERROR" dispatch for POST "/fehler", parameters={masked}
2020-04-03 14:30:58.590 DEBUG 2864 --- [nio-8080-exec-8] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped to org.woehlke.simpleworklist.application.error.MyErrorController#handleError(HttpServletRequest, Model)
2020-04-03 14:30:58.591  WARN 2864 --- [nio-8080-exec-8] o.w.s.error.MyErrorController            : errorMessage :Request method 'POST' not supported
2020-04-03 14:30:58.591  WARN 2864 --- [nio-8080-exec-8] o.w.s.error.MyErrorController            : 405Method Not Allowed
2020-04-03 14:30:58.593 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : Exiting from "ERROR" dispatch, status 405
````

Fixed #103, Fixed #102

## 2.3.11 
Fixed #107, Fixed #108

## 2.3.12
Fixed #106, Fixed #109, Fixed #110

Deployment to heroku
 
# Maven Config:
## maven-changes-plugin
https://maven.apache.org/plugins/maven-changes-plugin/examples/configuring-github-report.html

## maven-enforcer-plugin
http://maven.apache.org/enforcer/maven-enforcer-plugin/plugin-info.html
http://maven.apache.org/enforcer/enforcer-rules/
https://maven.apache.org/enforcer/enforcer-rules/requireFilesExist.html
https://maven.apache.org/enforcer/enforcer-rules/requireEnvironmentVariable.html
https://maven.apache.org/enforcer/enforcer-rules/requireActiveProfile.html
https://maven.apache.org/enforcer/enforcer-rules/dependencyConvergence.html
https://www.baeldung.com/maven-enforcer-plugin

# 2.3.14
Fixed #122, Fixed #123, Fixed #127, Fixed #130, Fixed #152, Fixed #153, Fixed #154, Fixed #155, Fixed #156, Fixed #157, Fixed #158, Fixed #159, Fixed #160, Fixed #161, Fixed #162, Fixed #163, Fixed #164, Fixed #165, Fixed #166, Fixed #167

 

https://developer.okta.com/blog/2019/03/28/test-java-spring-boot-junit5

# 2.3.15
Fixed #129, Fixed #126, Fixed #168 
https://github.com/thymeleaf/thymeleaf-testing

# 2.3.16 
Fixed #169, Fixed #170, Fixed #171, Fixed #172, Fixed #183 


# 2.3.17
#226 
#227
#228
#229
#230
#231
#232
#233 

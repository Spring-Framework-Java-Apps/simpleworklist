# Changelog

## 2.3.7
* Fixed #32, Fixed #36,  Fixed #89, Fixed #83, Fixed #84, Fixed #85, Fixed #88, Fixed #90, Fixed #91, Fixed #99

## 2.3.8
* Fixed #79, Fixed #80, Fixed #81, Fixed #100, Fixed #101

## BUGS:
*  Issue #101 ERROR: relation "spring_session" does not exist
````
 Unexpected error occurred in scheduled task

org.springframework.jdbc.BadSqlGrammarException: PreparedStatementCallback; bad SQL grammar [DELETE FROM SPRING_SESSION WHERE EXPIRY_TIME < ?]; nested exception is org.postgresql.util.PSQLException: ERROR: relation "spring_session" does not exist
  Position: 13

Caused by: org.postgresql.util.PSQLException: ERROR: relation "spring_session" does not exist
  Position: 13
````

## Read about Spring-Session:
* [Spring Session with JDBC](https://www.baeldung.com/spring-session-jdbc)
* [Spring Session with JDBC on Github](https://github.com/eugenp/tutorials/tree/master/spring-session/spring-session-jdbc)
* [Spring Session with JDBC](https://www.javadevjournal.com/spring/spring-session-with-jdbc/)

## 2.3.9 
* Fixed #79, Fixed #80, Fixed #81, Fixed #100, Fixed #101

## 2.3.10 
* Bugfixing
* F007 Add first new Task: broken
* An error happened during template parsing (template: "class path resource [templates/task/add.html]")

## F008 Add first new Project: broken
* -> Form OK
* -> Save Data: Error
* http://localhost:8080/project/root
* -> Button "add new project"
* http://localhost:8080/project/add/new/project
* -> Save -> ERROR
* Errorpage - Refer to logfile:
````
2020-04-03 14:30:37.148 DEBUG 2864 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:30:58.514 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : POST "/project/addchild/", parameters={masked}
2020-04-03 14:30:58.586  WARN 2864 --- [nio-8080-exec-8] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'POST' not supported]
2020-04-03 14:30:58.586 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : Completed 405 METHOD_NOT_ALLOWED
2020-04-03 14:30:58.590 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : "ERROR" dispatch for POST "/fehler", parameters={masked}
2020-04-03 14:30:58.590 DEBUG 2864 --- [nio-8080-exec-8] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped to org.woehlke.simpleworklist.common.error.MyErrorController#handleError(HttpServletRequest, Model)
2020-04-03 14:30:58.591  WARN 2864 --- [nio-8080-exec-8] o.w.s.error.MyErrorController            : errorMessage :Request method 'POST' not supported
2020-04-03 14:30:58.591  WARN 2864 --- [nio-8080-exec-8] o.w.s.error.MyErrorController            : 405Method Not Allowed
2020-04-03 14:30:58.593 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : Exiting from "ERROR" dispatch, status 405
````
* Fixed #103, Fixed #102

## 2.3.11 
* Fixed #107, Fixed #108

## 2.3.12
* Fixed #106, Fixed #109, Fixed #110
* Deployment to heroku
 
# Maven Config:
## maven-changes-plugin
* https://maven.apache.org/plugins/maven-changes-plugin/examples/configuring-github-report.html

## maven-enforcer-plugin
* http://maven.apache.org/enforcer/maven-enforcer-plugin/plugin-info.html
* http://maven.apache.org/enforcer/enforcer-rules/
* https://maven.apache.org/enforcer/enforcer-rules/requireFilesExist.html
* https://maven.apache.org/enforcer/enforcer-rules/requireEnvironmentVariable.html
* https://maven.apache.org/enforcer/enforcer-rules/requireActiveProfile.html
* https://maven.apache.org/enforcer/enforcer-rules/dependencyConvergence.html
* shttps://www.baeldung.com/maven-enforcer-plugin

## 2.3.14
* Fixed #122, Fixed #123, Fixed #127, Fixed #130, Fixed #152, Fixed #153, Fixed #154, Fixed #155, Fixed #156, Fixed #157, Fixed #158, Fixed #159, Fixed #160, Fixed #161, Fixed #162, Fixed #163, Fixed #164, Fixed #165, Fixed #166, Fixed #167
* https://developer.okta.com/blog/2019/03/28/test-java-spring-boot-junit5

## 2.3.15
* Fixed #129, Fixed #126, Fixed #168 
* https://github.com/thymeleaf/thymeleaf-testing

## 2.3.16 
* Fixed #169, Fixed #170, Fixed #171, Fixed #172, Fixed #183 

## 2.3.17
* Fixed #223, Fixed #224, Fixed #225, Fixed #226, Fixed #227, Fixed #228, Fixed #229, Fixed #230, Fixed #231, Fixed #232, Fixed #233 
* Fixed #201, Fixed #202, Fixed #203, Fixed #204, Fixed #205, Fixed #206, Fixed #207, Fixed #208, Fixed #209, Fixed #210, Fixed #211, Fixed #212, Fixed #213, Fixed #214, Fixed #215, Fixed #216, Fixed #217, Fixed #218, Fixed #219 
* Fixed #220, Fixed #221, Fixed #222 

## 2.3.18
* Fixed #128, Fixed #192, Fixed #193, Fixed #194, Fixed #195, Fixed #196, Fixed #197, Fixed #198, Fixed #199, Fixed #200, Fixed #239, Fixed #240

## 2.3.19
* Fixed #241, Fixed #242, Fixed #243, Fixed #252

## 2.3.20
* fixed #255 Update Spring-Boot to 2.3.0

## 2.3.23
* fixed #274 add Annotations to suppress Deprecation Warnings
* fixed #273 resort Issues, Milestones and Releases
* fixed #271 Milestone Burndown: Update Spring-Boot to current Version 2.3.3
* fixed #270 Update to Spring Framework for Spring-Boot 2.3.3
* fixed #269 mvnw docker-compose:up
* fixed #268 update webjars
* fixed #267 Update to Spring Security xyz for Spring-Boot 2.3.3
* fixed #266 Update to Spring Data Bom Neumann-SR3 for Spring-Boot 2.3.3
* fixed #264 setup travis-ci
* fixed #263 setup github-ci
* fixed #262 setup on my current Linux Machines
* fixed #260 Compare pom.xml and others to bloodmoney spring-webapp
* fixed #259 Update Spring-Boot to current Version 2.3.3 
* fixed #275 setup dev Databases enhancement
* fixed #272 prepare Release 2.3.23
* fixed #265 make release with maven plugin

## 2.3.24
* fixed #277 heroku: Compiled slug size: 617.9M is too large (max is 500M)
* fixed #279 Formular Layout Errors
* fixed #283 prepare Release 2.3.24 
* fixed #284 make release with maven plugin

## 2.3.25
* fixed #285 Update spring-boot 2.3.3 to 2.3.4
* fixed #286 Update spring-data-releasetrain to Neumann-SR4
* fixed #287 Update spring-session-bom to Dragonfruit-SR1

## 2.3.26
* fixed #249 TaskRepository: move the JQL Query-String to Entity as Prepared Statement

## 2.3.27
* fixed  #298 try out java15

## 2.3.28
* fixed #302 update Spring-Boot to Version to 2.3.7
* Issue #304 update webjars for bootstrap et al.
* fixed #305 Update spring-data-releasetrain to latest GA
* fixed #306 Update spring-session-bom to latest GA
* fixed #308 Password Recovery is broken

## 2.3.29
* fixed #303 update Spring-Boot to Version to 2.4.1

## 2.3.30
* fixed #310 change devops actions

## 2.3.31
* fixed #311 User Selfservice is broken
* fixed #312 User2UserMessage Chat is broken
* fixed #313 Change Password - update HTML Layout to current Bootstrap Version
* fixed #314 Set Default Language - update HTML Layout to current Bootstrap Version
* fixed #315 Add Context - update HTML Layout to current Bootstrap Version
* fixed #316 Add Context - add Back Button

## 2.3.32
* fixed #318 Broken: http://localhost:8080/taskstate/task/1153/changeorderto/1051
* fixed #319 groovy-all Warning during startup
* fixed #320 warning: SLF4J: Class path contains multiple SLF4J bindings.
* fixed #321 jar is too fat for deployment on heroku

# 2.3.33
* Issue #322 small refactoring of packages

## 2.3.34
* Issue #317 switch Context is broken

## 2.3.35
* Issue #234 Taskstate: Task Edit Form -> change Project via DropDown
* Issue #235 Project/Root: Task Edit Form -> change Project via DropDown
* Issue #236 Project/id: Task Edit Form -> change Project via DropDown

# TODO 

## doc
* https://www.baeldung.com/spring-session
* EmbeddedSolrServer:
* https://docs.spring.io/spring-data/solr/docs/4.1.6.RELEASE/reference/html/#solr.annotation
* https://www.youtube.com/watch?v=hieLEsp5cTk

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

## Joinfaces
* https://github.com/joinfaces/joinfaces
* http://joinfaces.org/
* https://docs.joinfaces.org/4.3.4/reference/
* https://docs.joinfaces.org/current/reference/
* https://developer.okta.com/blog/2019/03/28/test-java-spring-boot-junit5
* https://github.com/thymeleaf/thymeleaf-testing

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
* fixed #322 small refactoring of packages
* fixed #323 check version for optimistic locking to org.woehlke.simpleworklist.domain.project.Project
* fixed #324 removed version for optimistic locking to org.woehlke.simpleworklist.domain.context.Context
* fixed #325 add version for optimistic locking to org.woehlke.simpleworklist.domain.task.Task

## 2.3.34
* fixed #317 switch Context is broken
* fixed #326 add Context to Breadcrumb
* fixed #327 AbstractController: rename rootCategories to rootProjects (line 77)
* fixed #328 AbstractController: rename allCategories to allProjects (line 66)
* fixed #329 AbstractController: remove side effects from getContext (line 145, 159) 
* fixed #330 Add Project to Root Project: preset correct Context 
* fixed #331 Edit Project: update HTML Layout to current Bootstrap Version

## 2.3.35  
* Issue #234 Taskstate: Task Edit Form -> change Project via DropDown
* Issue #235 Project/Root: Task Edit Form -> change Project via DropDown
* Issue #236 Project/id: Task Edit Form -> change Project via DropDown
* wontfix #251, wontfix #224, wontfix #245, wontfix #246


## 2.4.7
* fixed #381 Transient_Class_Modell - Update UML Documentation after Refactorings due to Bugfixes
* fixed #382 Controller_Classes - Update UML Documentation after Refactorings due to Bugfixes
* fixed #383 F006 Page after first successful Login: change from inbox to today

## 2.4.8
* fixed #375 reenable integration tests

## 2.4.9
* fixed #390 refactor integration tests: One Test class for each Controller Class
* fixed #391 refactor integration tests: One Test class for each Service Class in package meso

## 2.4.10-SNAPSHOT
* minor changes

## 2.4.11-SNAPSHOT
* fixed #380 add User Use Cases to UML Documentation
* fixed #393 UML Documentation: Service Classes DB
* fixed #394 UML Documentation: Service Classes Meso

## 2.4.12-SNAPSHOT
* fixed #388 make task.taskstate, task.duedate and taskstate workflow independent to each other


## 3.0.0.rc.1
* fixed #401 Migrate spring-boot from 2 to 3

## 3.0.0.rc.2
* fixed #404 Task List View: LocalDate and LocalDateTime HTML rendering for Locale and in desired format
* fixed #405 User List View: LocalDate and LocalDateTime HTML rendering for Locale and in desired format

## 3.0.0.rc.3
* fixed #403 Task pagination is broken due to dependencies
* #406 Liquibase Database Schema Evolution of changes by migrating spring-boot from 2 to 3 and Jakarta EE 10 JPA
* fixed #407 update Last Login Date is broken

## 3.0.x
* #377 add List of URL (Patterns) to Documentation
* #378 Add Search Index and search functionality
* #386 make Project Navigation collapsible
* #389 apply Database Schema Evolution for: 386 make Project Navigation collapsible
* #392 refactor integration tests: add Unit Tests for Beans and Entities

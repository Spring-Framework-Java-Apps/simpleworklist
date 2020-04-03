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
	at org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator.doTranslate(SQLErrorCodeSQLExceptionTranslator.java:235)
	at org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator.translate(AbstractFallbackSQLExceptionTranslator.java:72)
	at org.springframework.jdbc.core.JdbcTemplate.translateException(JdbcTemplate.java:1443)
	at org.springframework.jdbc.core.JdbcTemplate.execute(JdbcTemplate.java:633)
	at org.springframework.jdbc.core.JdbcTemplate.update(JdbcTemplate.java:862)
	at org.springframework.jdbc.core.JdbcTemplate.update(JdbcTemplate.java:917)
	at org.springframework.jdbc.core.JdbcTemplate.update(JdbcTemplate.java:927)
	at org.springframework.session.jdbc.JdbcOperationsSessionRepository.lambda$cleanUpExpiredSessions$7(JdbcOperationsSessionRepository.java:616)
	at org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:140)
	at org.springframework.session.jdbc.JdbcOperationsSessionRepository.cleanUpExpiredSessions(JdbcOperationsSessionRepository.java:615)
	at org.springframework.session.jdbc.config.annotation.web.http.JdbcHttpSessionConfiguration.lambda$configureTasks$0(JdbcHttpSessionConfiguration.java:213)
	at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54)
	at org.springframework.scheduling.concurrent.ReschedulingRunnable.run(ReschedulingRunnable.java:93)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
	at java.base/java.lang.Thread.run(Thread.java:830)
Caused by: org.postgresql.util.PSQLException: ERROR: relation "spring_session" does not exist
  Position: 13
	at org.postgresql.core.v3.QueryExecutorImpl.receiveErrorResponse(QueryExecutorImpl.java:2578)
	at org.postgresql.core.v3.QueryExecutorImpl.processResults(QueryExecutorImpl.java:2313)
	at org.postgresql.core.v3.QueryExecutorImpl.execute(QueryExecutorImpl.java:331)
	at org.postgresql.jdbc.PgStatement.executeInternal(PgStatement.java:448)
	at org.postgresql.jdbc.PgStatement.execute(PgStatement.java:369)
	at org.postgresql.jdbc.PgPreparedStatement.executeWithFlags(PgPreparedStatement.java:159)
	at org.postgresql.jdbc.PgPreparedStatement.executeUpdate(PgPreparedStatement.java:125)
	at com.zaxxer.hikari.pool.ProxyPreparedStatement.executeUpdate(ProxyPreparedStatement.java:61)
	at com.zaxxer.hikari.pool.HikariProxyPreparedStatement.executeUpdate(HikariProxyPreparedStatement.java)
	at org.springframework.jdbc.core.JdbcTemplate.lambda$update$0(JdbcTemplate.java:867)
	at org.springframework.jdbc.core.JdbcTemplate.execute(JdbcTemplate.java:617)
	... 15 common frames omitted

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

````
2020-04-03 14:29:28.044 DEBUG 2864 --- [nio-8080-exec-9] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.115 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/ckeditor.js", parameters={}
2020-04-03 14:29:28.116 DEBUG 2864 --- [nio-8080-exec-8] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.238 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.387 DEBUG 2864 --- [nio-8080-exec-6] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/config.js?t=J1QB", parameters={masked}
2020-04-03 14:29:28.388 DEBUG 2864 --- [nio-8080-exec-6] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.422 DEBUG 2864 --- [nio-8080-exec-6] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.438 DEBUG 2864 --- [io-8080-exec-10] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/skins/moono-lisa/editor_gecko.css?t=J1QB", parameters={masked}
2020-04-03 14:29:28.438 DEBUG 2864 --- [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/lang/de.js?t=J1QB", parameters={masked}
2020-04-03 14:29:28.439 DEBUG 2864 --- [nio-8080-exec-2] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.439 DEBUG 2864 --- [io-8080-exec-10] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.502 DEBUG 2864 --- [io-8080-exec-10] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.550 DEBUG 2864 --- [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.563 DEBUG 2864 --- [nio-8080-exec-3] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/styles.js?t=J1QB", parameters={masked}
2020-04-03 14:29:28.563 DEBUG 2864 --- [nio-8080-exec-3] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.618 DEBUG 2864 --- [nio-8080-exec-3] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.642 DEBUG 2864 --- [nio-8080-exec-7] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/plugins/scayt/skins/moono-lisa/scayt.css", parameters={}
2020-04-03 14:29:28.642 DEBUG 2864 --- [nio-8080-exec-4] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/plugins/tableselection/styles/tableselection.css", parameters={}
2020-04-03 14:29:28.642 DEBUG 2864 --- [nio-8080-exec-5] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/plugins/wsc/skins/moono-lisa/wsc.css", parameters={}
2020-04-03 14:29:28.642 DEBUG 2864 --- [nio-8080-exec-7] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.642 DEBUG 2864 --- [nio-8080-exec-4] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.643 DEBUG 2864 --- [nio-8080-exec-5] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.646 DEBUG 2864 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/plugins/scayt/dialogs/dialog.css", parameters={}
2020-04-03 14:29:28.647 DEBUG 2864 --- [nio-8080-exec-1] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.711 DEBUG 2864 --- [nio-8080-exec-4] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.732 DEBUG 2864 --- [nio-8080-exec-9] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/plugins/copyformatting/styles/copyformatting.css", parameters={}
2020-04-03 14:29:28.733 DEBUG 2864 --- [nio-8080-exec-9] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.766 DEBUG 2864 --- [nio-8080-exec-5] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.835 DEBUG 2864 --- [nio-8080-exec-7] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.880 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/plugins/tableselection/styles/tableselection.css", parameters={}
2020-04-03 14:29:28.881 DEBUG 2864 --- [nio-8080-exec-8] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.881 DEBUG 2864 --- [nio-8080-exec-6] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/contents.css?t=J1QB", parameters={masked}
2020-04-03 14:29:28.882 DEBUG 2864 --- [io-8080-exec-10] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/plugins/copyformatting/styles/copyformatting.css", parameters={}
2020-04-03 14:29:28.882 DEBUG 2864 --- [io-8080-exec-10] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.882 DEBUG 2864 --- [nio-8080-exec-6] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.895 DEBUG 2864 --- [nio-8080-exec-9] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.951 DEBUG 2864 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:28.954 DEBUG 2864 --- [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/skins/moono-lisa/icons.png?t=8b53603e8", parameters={masked}
2020-04-03 14:29:28.955 DEBUG 2864 --- [nio-8080-exec-2] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:29:28.999 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:29.024 DEBUG 2864 --- [io-8080-exec-10] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:29.048 DEBUG 2864 --- [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:29:29.072 DEBUG 2864 --- [nio-8080-exec-6] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:30:37.047 DEBUG 2864 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : GET "/webjars/ckeditor/4.11.3/full/plugins/magicline/images/icon.png?t=J1QB", parameters={masked}
2020-04-03 14:30:37.048 DEBUG 2864 --- [nio-8080-exec-1] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler ["/webjars/"]
2020-04-03 14:30:37.148 DEBUG 2864 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
2020-04-03 14:30:58.514 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : POST "/project/addchild/", parameters={masked}
2020-04-03 14:30:58.586  WARN 2864 --- [nio-8080-exec-8] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'POST' not supported]
2020-04-03 14:30:58.586 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : Completed 405 METHOD_NOT_ALLOWED
2020-04-03 14:30:58.590 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : "ERROR" dispatch for POST "/fehler", parameters={masked}
2020-04-03 14:30:58.590 DEBUG 2864 --- [nio-8080-exec-8] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped to org.woehlke.simpleworklist.error.MyErrorController#handleError(HttpServletRequest, Model)
2020-04-03 14:30:58.591  WARN 2864 --- [nio-8080-exec-8] o.w.s.error.MyErrorController            : errorMessage :Request method 'POST' not supported
2020-04-03 14:30:58.591  WARN 2864 --- [nio-8080-exec-8] o.w.s.error.MyErrorController            : 405Method Not Allowed
2020-04-03 14:30:58.593 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : Exiting from "ERROR" dispatch, status 405
````

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
2020-04-03 14:30:58.590 DEBUG 2864 --- [nio-8080-exec-8] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped to org.woehlke.simpleworklist.error.MyErrorController#handleError(HttpServletRequest, Model)
2020-04-03 14:30:58.591  WARN 2864 --- [nio-8080-exec-8] o.w.s.error.MyErrorController            : errorMessage :Request method 'POST' not supported
2020-04-03 14:30:58.591  WARN 2864 --- [nio-8080-exec-8] o.w.s.error.MyErrorController            : 405Method Not Allowed
2020-04-03 14:30:58.593 DEBUG 2864 --- [nio-8080-exec-8] o.s.web.servlet.DispatcherServlet        : Exiting from "ERROR" dispatch, status 405
```


# TODO 

* https://www.baeldung.com/spring-session
* EmbeddedSolrServer:
* https://docs.spring.io/spring-data/solr/docs/4.1.6.RELEASE/reference/html/#solr.annotation
* https://www.youtube.com/watch?v=hieLEsp5cTk


# 2.3.7
Fixed #32, Fixed #36,  Fixed #89, Fixed #83, Fixed #84, Fixed #85, Fixed #88, Fixed #90, Fixed #91, Fixed #99


# 2.3.8
Fixed #79, Fixed #80, Fixed #81, Fixed #100, Fixed #101


## BUGS:
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

# 2.3.9 Fixed #79, Fixed #80, Fixed #81, Fixed #100, Fixed #101

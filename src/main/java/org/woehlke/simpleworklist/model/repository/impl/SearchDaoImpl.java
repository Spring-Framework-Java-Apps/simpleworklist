package org.woehlke.simpleworklist.model.repository.impl;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.model.entities.Project;
import org.woehlke.simpleworklist.model.entities.Task;
import org.woehlke.simpleworklist.model.entities.UserAccount;
import org.woehlke.simpleworklist.model.SearchResult;
import org.woehlke.simpleworklist.model.repository.SearchDao;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tw on 14.02.16.
 */
@Repository
public class SearchDaoImpl implements SearchDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public SearchResult search(String searchterm, UserAccount userAccount) {
        try {
            SearchResult searchResult = new SearchResult();
            searchResult.setSearchterm(searchterm);
            List<Task> taskList = new ArrayList<>();
            List<Project> projectList = new ArrayList<>();
            FullTextEntityManager fullTextEntityManager =
                    org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
            // create native Lucene query unsing the query DSL
            // alternatively you can write the Lucene query using the Lucene query parser
            // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
            QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                    .buildQueryBuilder().forEntity(Task.class).get();
            org.apache.lucene.search.Query luceneQuery1 = qb
                    .keyword()
                    .onFields("title", "text")
                    .matching(searchterm)
                    .createQuery();
            org.apache.lucene.search.Query luceneQuery2 = qb
                    .keyword()
                    .onFields("userAccount.id")
                    .matching(userAccount.getId().toString())
                    .createQuery();
            org.apache.lucene.search.Query luceneQuery = qb
                    .bool()
                    .must(luceneQuery1)
                    .must(luceneQuery2)
                    .createQuery();
            LOGGER.info(luceneQuery.toString());
            // wrap Lucene query in a javax.persistence.Query
            javax.persistence.Query jpaQuery =
                    fullTextEntityManager.createFullTextQuery(luceneQuery, Task.class);
            // execute search
            List result = jpaQuery.getResultList();
            for (Object foundItem : result) {
                if (foundItem instanceof Task) {
                    Task item = (Task) foundItem;
                    LOGGER.info("found: " + item.toString());
                    taskList.add(item);
                } else {
                    LOGGER.info("found: " + foundItem.toString());
                }
            }
            searchResult.setTaskList(taskList);
            qb = fullTextEntityManager.getSearchFactory()
                    .buildQueryBuilder().forEntity(Project.class).get();
            luceneQuery1 = qb
                    .keyword()
                    .onFields("name", "description")
                    .matching(searchterm)
                    .createQuery();
            luceneQuery2 = qb
                    .keyword()
                    .onFields("userAccount.id")
                    .matching(userAccount.getId().toString())
                    .createQuery();
            luceneQuery = qb
                    .bool()
                    .must(luceneQuery1)
                    .must(luceneQuery2)
                    .createQuery();
            LOGGER.info(luceneQuery.toString());
            // wrap Lucene query in a javax.persistence.Query
            jpaQuery =
                    fullTextEntityManager.createFullTextQuery(luceneQuery, Project.class);
            // execute search
            result = jpaQuery.getResultList();
            for (Object foundItem : result) {
                if (foundItem instanceof Project) {
                    Project item = (Project) foundItem;
                    LOGGER.info("found: " + item.toString());
                    projectList.add(item);
                } else {
                    LOGGER.info("found: " + foundItem.toString());
                }
            }
            searchResult.setProjectList(projectList);
            return searchResult;
        } catch (RuntimeException e){
            LOGGER.info(e.getMessage());
            Throwable t = e.getCause();
            while(t != null){
                LOGGER.info(t.getMessage());
                t = t.getCause();
            }
            return null;
        }
    }

    @Override
    public void resetSearchIndex() {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
        TypedQuery<Task> findAllActionItems = fullTextEntityManager.createQuery ("select a from Task a",Task.class);
        TypedQuery<Project> findAllCategories = fullTextEntityManager.createQuery("select c from Project c",Project.class);
        for(Project project :findAllCategories.getResultList()){
            fullTextEntityManager.index(project);
        }
        for(Task task :findAllActionItems.getResultList()){
            fullTextEntityManager.index(task);
        }
        //fullTextEntityManager.flushToIndexes();
        //fullTextEntityManager.clear();
    }
}

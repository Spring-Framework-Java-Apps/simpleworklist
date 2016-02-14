package org.woehlke.simpleworklist.repository;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.entities.ActionItem;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.model.SearchResult;

import javax.inject.Inject;
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
    public SearchResult search(String searchterm) {
        SearchResult searchResult = new SearchResult();
        searchResult.setSearchterm(searchterm);
        List<ActionItem> actionItemList = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
        // create native Lucene query unsing the query DSL
        // alternatively you can write the Lucene query using the Lucene query parser
        // or the Lucene programmatic API. The Hibernate Search DSL is recommended though
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(ActionItem.class).get();
        org.apache.lucene.search.Query luceneQuery = qb
                .keyword()
                .onFields("title", "text")
                .matching(searchterm)
                .createQuery();
        // wrap Lucene query in a javax.persistence.Query
        javax.persistence.Query jpaQuery =
                fullTextEntityManager.createFullTextQuery(luceneQuery, ActionItem.class);
        // execute search
        List result = jpaQuery.getResultList();
        for (Object foundItem:result){
            if(foundItem instanceof ActionItem){
                ActionItem item = (ActionItem) foundItem;
                LOGGER.info("found: "+item.toString());
                actionItemList.add(item);
            } else {
                LOGGER.info("found: "+foundItem.toString());
            }
        }
        searchResult.setActionItemList(actionItemList);
        qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Category.class).get();
        luceneQuery = qb
                .keyword()
                .onFields("name", "description")
                .matching(searchterm)
                .createQuery();
        // wrap Lucene query in a javax.persistence.Query
        jpaQuery =
                fullTextEntityManager.createFullTextQuery(luceneQuery, Category.class);
        // execute search
        result = jpaQuery.getResultList();
        for (Object foundItem:result){
            if(foundItem instanceof Category){
                Category item = (Category) foundItem;
                LOGGER.info("found: "+item.toString());
                categoryList.add(item);
            } else {
                LOGGER.info("found: "+foundItem.toString());
            }
        }
        searchResult.setCategoryList(categoryList);
        return searchResult;
    }

    @Override
    public void resetSearchIndex() {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
        TypedQuery<ActionItem> findAllActionItems = fullTextEntityManager.createQuery ("select a from ActionItem a",ActionItem.class);
        TypedQuery<Category> findAllCategories = fullTextEntityManager.createQuery("select c from Category c",Category.class);
        for(Category category:findAllCategories.getResultList()){
            fullTextEntityManager.index( category );
        }
        for(ActionItem actionItem:findAllActionItems.getResultList()){
            fullTextEntityManager.index( actionItem );
        }
        fullTextEntityManager.flushToIndexes();
        fullTextEntityManager.clear();
    }
}

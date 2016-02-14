package org.woehlke.simpleworklist.model;

import org.woehlke.simpleworklist.entities.ActionItem;
import org.woehlke.simpleworklist.entities.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tw on 14.02.16.
 */
public class SearchResult {

    private String searchterm = "";
    private List<ActionItem> actionItemList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();

    public String getSearchterm() {
        return searchterm;
    }

    public void setSearchterm(String searchterm) {
        this.searchterm = searchterm;
    }

    public List<ActionItem> getActionItemList() {
        return actionItemList;
    }

    public void setActionItemList(List<ActionItem> actionItemList) {
        this.actionItemList = actionItemList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;

        if (searchterm != null ? !searchterm.equals(that.searchterm) : that.searchterm != null) return false;
        if (actionItemList != null ? !actionItemList.equals(that.actionItemList) : that.actionItemList != null)
            return false;
        return categoryList != null ? categoryList.equals(that.categoryList) : that.categoryList == null;

    }

    @Override
    public int hashCode() {
        int result = searchterm != null ? searchterm.hashCode() : 0;
        result = 31 * result + (actionItemList != null ? actionItemList.hashCode() : 0);
        result = 31 * result + (categoryList != null ? categoryList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "searchterm='" + searchterm + '\'' +
                ", actionItemList=" + actionItemList +
                ", categoryList=" + categoryList +
                '}';
    }
}

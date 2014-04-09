package org.woehlke.beachbox.web;

import org.hibernate.validator.constraints.SafeHtml;

import java.io.Serializable;

/**
 * Created by Fert on 07.04.2014.
 */
public class SessionBean implements Serializable {

    @SafeHtml
    private String searchString;

    private int beginIndex;
    private int endIndex;
    private int currentIndex;
    private String sort;
    private String sortDir;
    private int pageSize;
    private boolean bearbeiten = false;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isBearbeiten() {
        return bearbeiten;
    }

    public void setBearbeiten(boolean bearbeiten) {
        this.bearbeiten = bearbeiten;
    }

    public boolean isEmpty() {
        boolean retVal = false;
        if(searchString == null || searchString.isEmpty()){
            retVal = true;
        }
        return retVal;
    }
}

package org.woehlke.simpleworklist.search;

import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.project.Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tw on 14.02.16.
 */
public class SearchResult implements Serializable {

    private static final long serialVersionUID = 1682809351146047764L;

    private String searchterm = "";
    private List<Task> taskList = new ArrayList<>();
    private List<Project> projectList = new ArrayList<>();

    public String getSearchterm() {
        return searchterm;
    }

    public void setSearchterm(String searchterm) {
        this.searchterm = searchterm;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;

        if (searchterm != null ? !searchterm.equals(that.searchterm) : that.searchterm != null) return false;
        if (taskList != null ? !taskList.equals(that.taskList) : that.taskList != null)
            return false;
        return projectList != null ? projectList.equals(that.projectList) : that.projectList == null;

    }

    @Override
    public int hashCode() {
        int result = searchterm != null ? searchterm.hashCode() : 0;
        result = 31 * result + (taskList != null ? taskList.hashCode() : 0);
        result = 31 * result + (projectList != null ? projectList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "searchterm='" + searchterm + '\'' +
                ", taskList=" + taskList +
                ", projectList=" + projectList +
                '}';
    }
}

package org.woehlke.simpleworklist.model;

import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Breadcrumb implements Serializable {

    private List<BreadcrumbItem> breadcrumb;

    public Breadcrumb() {
        List<BreadcrumbItem> breadcrumb = new ArrayList<>();
        BreadcrumbItem home = new BreadcrumbItem("Home","/");
        breadcrumb.add(home);
        this.breadcrumb = breadcrumb;
    }

    public List<BreadcrumbItem> getBreadcrumb() {
        return breadcrumb;
    }

    public int size(){
        return breadcrumb.size();
    }

    public void addProjectRoot(){
        String urlProject = "/project/0";
        String name = "Projects";
        BreadcrumbItem breadcrumbItemProject = new BreadcrumbItem(name,urlProject);
        breadcrumb.add(breadcrumbItemProject);
    }

    public void addProject(Project thisProject){
        String urlProject = "/project/" + thisProject.getId();
        BreadcrumbItem breadcrumbItemProject = new BreadcrumbItem(thisProject.getName(),urlProject);
        breadcrumb.add(breadcrumbItemProject);
    }

    public void addTask(Task task){
        String urlTask = "/task/" + task.getId();
        BreadcrumbItem breadcrumbItemTask = new BreadcrumbItem(task.getTitle(),urlTask);
        breadcrumb.add(breadcrumbItemTask);
    }

    public void addTaskstate(String taskStateView){
        String urlTaskstate = "/taskstate/" + taskStateView;
        BreadcrumbItem breadcrumbItemTaskstate = new BreadcrumbItem(taskStateView,urlTaskstate);
        breadcrumb.add(breadcrumbItemTaskstate);
    }
}

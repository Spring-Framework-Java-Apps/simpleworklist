package org.woehlke.simpleworklist.breadcrumb;

import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.task.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Breadcrumb implements Serializable {

    private static final long serialVersionUID = 7932703111140692689L;

    private List<BreadcrumbItem> breadcrumb;

    private final Locale locale;

    public Breadcrumb(Locale locale) {
        this.locale = locale;
        List<BreadcrumbItem> breadcrumb = new ArrayList<>();
        String homeString;
        if(locale == Locale.GERMAN){
            homeString =  "Start";
        } else {
            homeString =  "Home";
        }
        BreadcrumbItem home = new BreadcrumbItem(homeString,"/");
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
        String name;
        if(this.locale == Locale.GERMAN){
            name = "Projekte";
        } else {
            name = "Projects";
        }
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

    public void addTaskstate(String taskStateView, String urlTaskstate){
        BreadcrumbItem breadcrumbItemTaskstate = new BreadcrumbItem(taskStateView,urlTaskstate);
        breadcrumb.add(breadcrumbItemTaskstate);
    }

    public void addPage(String name, String url) {
        BreadcrumbItem breadcrumbItemTaskstate = new BreadcrumbItem(name,url);
        breadcrumb.add(breadcrumbItemTaskstate);
    }
}

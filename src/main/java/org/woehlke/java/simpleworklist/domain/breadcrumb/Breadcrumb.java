package org.woehlke.java.simpleworklist.domain.breadcrumb;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.woehlke.java.simpleworklist.domain.context.Context;
import org.woehlke.java.simpleworklist.domain.project.Project;
import org.woehlke.java.simpleworklist.domain.task.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@EqualsAndHashCode
@ToString
public class Breadcrumb implements Serializable {

    private static final long serialVersionUID = 7932703111140692689L;

    private List<BreadcrumbItem> breadcrumb;
    private final Locale locale;

    public Breadcrumb(Locale locale, Context context) {
        this.locale = locale;
        List<BreadcrumbItem> breadcrumb = new ArrayList<>();
        String homeString,contextItemString;
        if(locale == Locale.GERMAN){
            homeString =  "Start";
            contextItemString = context.getNameDe();
        } else {
            homeString =  "Home";
            contextItemString =context.getNameEn();
        }
        contextItemString =  " [ " + contextItemString + " ] ";
        BreadcrumbItem home = new BreadcrumbItem(homeString,"/");
        breadcrumb.add(home);
        BreadcrumbItem contextItem = new BreadcrumbItem(contextItemString,"/");
        breadcrumb.add(contextItem);
        this.breadcrumb = breadcrumb;
    }

    public int size(){
        return breadcrumb.size();
    }

    public void addProjectRoot(){
        String urlProject = "/project/root";
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

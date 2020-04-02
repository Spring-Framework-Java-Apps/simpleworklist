package org.woehlke.simpleworklist.oodm;

//import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.task.Task;

public class Matchers {

    //@Factory
    public static <T> Matcher<Project> categoryNotNullObject() {
        return new IsNotNullObjectOfCategory();
    }

    //@Factory
    public static <T> Matcher<Project> categoryNullObject() {
        return new IsNullObjectOfCategory();
    }

    //@Factory
    public static <T> Matcher<Project> categorySameIdObject(long id) {
        return new IsSameObjectOfCategory(id);
    }

    //@Factory
    public static <T> Matcher<Task> actionItemNullObject() {
        return new IsNullObjectOfActionItem();
    }

    //@Factory
    public static <T> Matcher<Task> actionItemNotNullObject() {
        return new IsNotNullObjectOfActionItem();
    }

    //@Factory
    public static <T> Matcher<Task> actionItemSameIdObject(long id) {
        return new IsSameObjectOfActionItem(id);
    }
}

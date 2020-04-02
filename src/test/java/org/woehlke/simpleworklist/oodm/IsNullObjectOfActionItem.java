package org.woehlke.simpleworklist.oodm;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.woehlke.simpleworklist.task.Task;

public class IsNullObjectOfActionItem extends TypeSafeMatcher<Task> {

    @Override
    public void describeTo(Description description) {
        description.appendText("Null Task Object");
    }

    @Override
    protected boolean matchesSafely(Task item) {
        return item.getId().longValue() == 0;
    }

}

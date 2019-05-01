package org.woehlke.simpleworklist.oodm;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.woehlke.simpleworklist.oodm.entities.Task;

public class IsNotNullObjectOfActionItem extends TypeSafeMatcher<Task> {

    @Override
    public void describeTo(Description description) {
        description.appendText("Not Null Task Object");
    }

    @Override
    protected boolean matchesSafely(Task item) {
        return item.getId().longValue() > 0;
    }

}

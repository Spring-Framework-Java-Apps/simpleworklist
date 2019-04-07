package org.woehlke.simpleworklist.entities;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

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

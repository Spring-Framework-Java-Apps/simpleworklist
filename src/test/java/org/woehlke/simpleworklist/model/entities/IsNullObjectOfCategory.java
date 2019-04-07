package org.woehlke.simpleworklist.model.entities;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsNullObjectOfCategory extends TypeSafeMatcher<Project> {

    @Override
    public void describeTo(Description description) {
        description.appendText("Null Project Object");
    }

    @Override
    protected boolean matchesSafely(Project item) {
        return item.getId().longValue() == 0;
    }

}

package org.woehlke.simpleworklist.entities;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsNotNullObjectOfCategory extends TypeSafeMatcher<Project> {

    @Override
    public void describeTo(Description description) {
        description.appendText("Not Null Project Object");
    }

    @Override
    protected boolean matchesSafely(Project item) {
        return item.getId().longValue() > 0;
    }

}

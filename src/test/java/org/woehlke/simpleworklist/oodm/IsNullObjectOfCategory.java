package org.woehlke.simpleworklist.oodm;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.woehlke.simpleworklist.oodm.entities.Project;

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

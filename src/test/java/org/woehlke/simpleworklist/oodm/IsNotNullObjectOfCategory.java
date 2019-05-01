package org.woehlke.simpleworklist.oodm;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.woehlke.simpleworklist.oodm.entities.Project;

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

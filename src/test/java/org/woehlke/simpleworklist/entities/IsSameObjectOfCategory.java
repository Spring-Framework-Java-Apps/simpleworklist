package org.woehlke.simpleworklist.entities;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsSameObjectOfCategory extends TypeSafeMatcher<Project> {

    private long id;

    public IsSameObjectOfCategory(long id) {
        super();
        this.id = id;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is Project Object with demanded id");
    }

    @Override
    protected boolean matchesSafely(Project item) {
        return item.getId().longValue() == this.id;
    }

}
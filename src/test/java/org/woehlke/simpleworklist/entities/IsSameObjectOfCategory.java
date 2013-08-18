package org.woehlke.simpleworklist.entities;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.woehlke.simpleworklist.entities.Category;

public class IsSameObjectOfCategory extends TypeSafeMatcher<Category> {

    private long id;

    public IsSameObjectOfCategory(long id) {
        super();
        this.id = id;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is Category Object with demanded id");
    }

    @Override
    protected boolean matchesSafely(Category item) {
        return item.getId().longValue() == this.id;
    }

}
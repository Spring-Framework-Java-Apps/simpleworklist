package org.woehlke.simpleworklist.model.entities;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsSameObjectOfActionItem extends TypeSafeMatcher<Task> {

    private long id;

    public IsSameObjectOfActionItem(long id) {
        super();
        this.id = id;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is Task Object with demanded id");
    }

    @Override
    protected boolean matchesSafely(Task item) {
        return this.id == item.getId().longValue();
    }

}

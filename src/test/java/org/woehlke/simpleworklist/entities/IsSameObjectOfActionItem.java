package org.woehlke.simpleworklist.entities;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsSameObjectOfActionItem extends TypeSafeMatcher<ActionItem> {

    private long id;

    public IsSameObjectOfActionItem(long id) {
        super();
        this.id = id;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is ActionItem Object with demanded id");
    }

    @Override
    protected boolean matchesSafely(ActionItem item) {
        return this.id == item.getId().longValue();
    }

}

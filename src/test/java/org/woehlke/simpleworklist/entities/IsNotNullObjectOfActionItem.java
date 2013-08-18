package org.woehlke.simpleworklist.entities;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsNotNullObjectOfActionItem extends TypeSafeMatcher<ActionItem> {

    @Override
    public void describeTo(Description description) {
        description.appendText("Not Null ActionItem Object");
    }

    @Override
    protected boolean matchesSafely(ActionItem item) {
        return item.getId().longValue() > 0;
    }

}

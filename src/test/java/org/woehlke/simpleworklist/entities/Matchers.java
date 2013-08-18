package org.woehlke.simpleworklist.entities;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class Matchers {

    @Factory
    public static <T> Matcher<Category> categoryNotNullObject() {
        return new IsNotNullObjectOfCategory();
    }

    @Factory
    public static <T> Matcher<Category> categoryNullObject() {
        return new IsNullObjectOfCategory();
    }

    @Factory
    public static <T> Matcher<Category> categorySameIdObject(long id) {
        return new IsSameObjectOfCategory(id);
    }

    @Factory
    public static <T> Matcher<ActionItem> actionItemNullObject() {
        return new IsNullObjectOfActionItem();
    }

    @Factory
    public static <T> Matcher<ActionItem> actionItemNotNullObject() {
        return new IsNotNullObjectOfActionItem();
    }

    @Factory
    public static <T> Matcher<ActionItem> actionItemSameIdObject(long id) {
        return new IsSameObjectOfActionItem(id);
    }
}

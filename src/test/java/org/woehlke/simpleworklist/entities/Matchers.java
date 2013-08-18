package org.woehlke.simpleworklist.entities;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.Data;

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
    public static <T> Matcher<Data> dataNullObject() {
        return new IsNullObjectOfData();
    }

    @Factory
    public static <T> Matcher<Data> dataNotNullObject() {
        return new IsNotNullObjectOfData();
    }


}

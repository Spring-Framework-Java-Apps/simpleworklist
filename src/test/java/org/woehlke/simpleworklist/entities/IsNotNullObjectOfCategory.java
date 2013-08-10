package org.woehlke.simpleworklist.entities;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.woehlke.simpleworklist.entities.Category;

public class IsNotNullObjectOfCategory extends TypeSafeMatcher<Category> {

	@Override
	public void describeTo(Description description) {
		description.appendText("Not Null Category Object");	
	}

	@Override
	protected boolean matchesSafely(Category item) {
		return item.getId().longValue()>0;
	}

}

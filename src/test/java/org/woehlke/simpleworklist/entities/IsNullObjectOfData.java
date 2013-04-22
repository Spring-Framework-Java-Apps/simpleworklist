package org.woehlke.simpleworklist.entities;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.woehlke.simpleworklist.entities.Data;

public class IsNullObjectOfData extends TypeSafeMatcher<Data> {

	@Override
	public void describeTo(Description description) {
		description.appendText("Null Data Object");	
	}

	@Override
	protected boolean matchesSafely(Data item) {
		return item.getId()==0;
	}

}

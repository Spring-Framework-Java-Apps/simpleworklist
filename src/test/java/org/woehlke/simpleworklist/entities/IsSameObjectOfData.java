package org.woehlke.simpleworklist.entities;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.woehlke.simpleworklist.entities.Data;

public class IsSameObjectOfData extends TypeSafeMatcher<Data>{

	private long id;
	
	public IsSameObjectOfData(long id){
		super();
		this.id=id;
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendText("is Data Object with demanded id");		
	}

	@Override
	protected boolean matchesSafely(Data item) {
		return this.id==item.getId().longValue();
	}

}

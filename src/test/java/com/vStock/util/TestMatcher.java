package com.vStock.util;

import org.hamcrest.Description;
import org.hamcrest.Matchers;

public class TestMatcher<T> extends org.hamcrest.BaseMatcher<T>{
	private String message;

	public TestMatcher(String message) {
		this.message = message;
	}
	@Override
	public boolean matches(Object actual) {
		// TODO Auto-generated method stub
		return Matchers.startsWith(this.message).matches(actual);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("a string starting with ").appendValue(this.message);
	}

}

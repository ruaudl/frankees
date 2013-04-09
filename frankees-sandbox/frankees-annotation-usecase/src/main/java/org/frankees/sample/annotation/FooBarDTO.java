package org.frankees.sample.annotation;

import org.frankees.annotation.Buildable;

@Buildable
public class FooBarDTO {

	private String foo;

	private Integer bar;

	public String getFoo() {
		return foo;
	}

	public void setFoo(String foo) {
		this.foo = foo;
	}

	public Integer getBar() {
		return bar;
	}

	public void setBar(Integer bar) {
		this.bar = bar;
	}

}

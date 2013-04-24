package org.frankees.annotation.reflection;

import javax.lang.model.util.SimpleAnnotationValueVisitor6;

public class StringAnnotationValueVisitor extends
		SimpleAnnotationValueVisitor6<String, Void> {

	@Override
	public String visitString(String s, Void p) {
		return s;
	}
}
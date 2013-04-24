package org.frankees.annotation.reflection;

import java.util.List;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

public class ArrayAnnotationValueVisitor extends
		SimpleAnnotationValueVisitor6<List<? extends AnnotationValue>, Void> {

	@Override
	public List<? extends AnnotationValue> visitArray(
			List<? extends AnnotationValue> vals, Void p) {
		return vals;
	}
}
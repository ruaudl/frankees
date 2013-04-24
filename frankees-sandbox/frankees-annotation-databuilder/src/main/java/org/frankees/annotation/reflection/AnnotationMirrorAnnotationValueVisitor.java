package org.frankees.annotation.reflection;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

public class AnnotationMirrorAnnotationValueVisitor extends
		SimpleAnnotationValueVisitor6<AnnotationMirror, Void> {

	@Override
	public AnnotationMirror visitAnnotation(AnnotationMirror a, Void p) {
		return a;
	}
}
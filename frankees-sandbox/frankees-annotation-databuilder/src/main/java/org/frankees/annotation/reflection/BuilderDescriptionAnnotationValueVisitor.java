package org.frankees.annotation.reflection;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

import org.frankees.builder.BuilderDescription;

public class BuilderDescriptionAnnotationValueVisitor extends
		SimpleAnnotationValueVisitor6<BuilderDescription, Void> {

	public BuilderDescription visitType(TypeMirror t, Void p) {
		return new BuilderDescriptionTypeVisitor().visit(t);
	}
}
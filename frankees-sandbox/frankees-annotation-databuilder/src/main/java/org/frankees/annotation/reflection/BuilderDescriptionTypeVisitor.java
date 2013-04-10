package org.frankees.annotation.reflection;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.SimpleTypeVisitor6;

import org.frankees.builder.BuilderDescription;

public class BuilderDescriptionTypeVisitor extends
		SimpleTypeVisitor6<BuilderDescription, Void> {

	@Override
	public BuilderDescription visitDeclared(DeclaredType t, Void p) {
		return new BuilderDescriptionElementVisitor().visit(t.asElement());
	}

}

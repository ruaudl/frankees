package org.frankees.annotation.reflection;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.SimpleTypeVisitor6;

import org.frankees.builder.TypeDescription;

public class TypeDescriptionTypeVisitor extends
		SimpleTypeVisitor6<TypeDescription, Void> {

	@Override
	public TypeDescription visitDeclared(DeclaredType t, Void p) {
		return new TypeDescriptionElementVisitor().visit(t.asElement());
	}

}

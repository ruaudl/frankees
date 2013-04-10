package org.frankees.annotation.reflection;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor6;

import org.frankees.builder.TypeDescription;

public class TypeDescriptionElementVisitor extends
		SimpleElementVisitor6<TypeDescription, Void> {

	@Override
	public TypeDescription visitType(TypeElement e, Void p) {
		TypeDescription typeDescription = visit(e.getEnclosingElement());
		typeDescription.setClassName(e.getSimpleName().toString());
		return typeDescription;
	}

	@Override
	public TypeDescription visitPackage(PackageElement e, Void p) {
		return new TypeDescription(e.getQualifiedName().toString(), null);
	}

	@Override
	protected TypeDescription defaultAction(Element e, Void p) {
		if (e.getEnclosingElement() != null) {
			return visit(e.getEnclosingElement(), p);
		}
		return super.defaultAction(e, p);
	}
}

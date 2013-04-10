package org.frankees.annotation.reflection;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.SimpleElementVisitor6;

import org.frankees.builder.PropertyDescription;

public class PropertyDescriptionElementVisitor extends
		SimpleElementVisitor6<PropertyDescription, Void> {

	@Override
	public PropertyDescription visitExecutable(ExecutableElement e, Void p) {
		if (e.getSimpleName().toString().startsWith("get")
				&& e.getParameters().isEmpty()) {
			String propertyName = e.getSimpleName().toString();
			propertyName = propertyName.substring(3, 4).toLowerCase()
					+ propertyName.substring(4);
			return new PropertyDescription(propertyName,
					new TypeDescriptionTypeVisitor().visit(e.getReturnType()));
		}
		return super.defaultAction(e, p);
	}
}

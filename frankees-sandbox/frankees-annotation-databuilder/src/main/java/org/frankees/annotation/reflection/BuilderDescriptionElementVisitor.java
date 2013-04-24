package org.frankees.annotation.reflection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor6;

import org.frankees.builder.BuilderDescription;
import org.frankees.builder.PropertyDescription;
import org.frankees.builder.TypeDescription;

public class BuilderDescriptionElementVisitor extends
		SimpleElementVisitor6<BuilderDescription, Void> {

	@Override
	public BuilderDescription visitType(TypeElement e, Void p) {
		TypeDescription objectTypeDescription = new TypeDescriptionElementVisitor()
				.visit(e);

		List<? extends Element> elements = e.getEnclosedElements();
		Set<PropertyDescription> properties = new HashSet<PropertyDescription>();
		for (Element element : elements) {
			PropertyDescription property = new PropertyDescriptionElementVisitor()
					.visit(element);
			if (property != null) {
				properties.add(property);
			}
		}

		return new BuilderDescription(null, objectTypeDescription, properties);
	}
}
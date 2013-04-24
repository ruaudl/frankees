package org.frankees.annotation.reflection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.SimpleElementVisitor6;

import org.frankees.builder.BuilderDescription;

public class BuilderDescriptionsElementVisitor extends
		SimpleElementVisitor6<Set<BuilderDescription>, String> {

	@Override
	public Set<BuilderDescription> visitPackage(PackageElement e, String n) {
		List<? extends Element> classes = e.getEnclosedElements();
		Set<BuilderDescription> descriptions = new HashSet<BuilderDescription>(
				classes.size());
		for (Element element : classes) {
			if (element.getSimpleName().toString().matches(n)) {
				BuilderDescription description = new BuilderDescriptionElementVisitor()
						.visit(element);
				if (description != null) {
					descriptions.add(description);
				}
			}
		}
		return descriptions;
	}
}
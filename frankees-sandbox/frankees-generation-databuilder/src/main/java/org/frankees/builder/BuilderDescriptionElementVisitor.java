package org.frankees.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor6;

public class BuilderDescriptionElementVisitor extends
		SimpleElementVisitor6<BuilderDescription, Void> {
	@Override
	public BuilderDescription visitPackage(PackageElement e, Void p) {
		BuilderDescription description = new BuilderDescription();
		description.setObjectPackageName(e.getQualifiedName().toString());
		return description;
	}

	@Override
	public BuilderDescription visitExecutable(ExecutableElement e, Void p) {
		BuilderDescription description = new BuilderDescription();
		if (e.getSimpleName().toString().startsWith("get")) {
			String propertyName = e.getSimpleName().toString().substring(3, 4)
					.toLowerCase()
					+ e.getSimpleName().toString().substring(4);
			description.setProperties(Collections.singletonMap(propertyName, e
					.getReturnType().toString()));
		} else {
			description.setProperties(Collections.<String, String> emptyMap());
		}
		return description;
	}

	@Override
	public BuilderDescription visitType(TypeElement e, Void p) {
		BuilderDescription description = new BuilderDescription();
		description.setObjectClassName(e.getSimpleName().toString());

		BuilderDescription packageDescription = visit(e.getEnclosingElement());
		if (packageDescription != null) {
			description.setObjectPackageName(packageDescription
					.getObjectPackageName());
		}

		List<? extends Element> elements = e.getEnclosedElements();
		Map<String, String> properties = new HashMap<String, String>();
		for (Element element : elements) {
			BuilderDescription setterDescription = visit(element);
			if (setterDescription != null) {
				properties.putAll(setterDescription.getProperties());
			}
		}
		description.setProperties(properties);

		return description;
	}
}
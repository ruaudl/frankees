package org.frankees.annotation;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.frankees.annotation.reflection.AnnotationMirrorAnnotationValueVisitor;
import org.frankees.annotation.reflection.ArrayAnnotationValueVisitor;
import org.frankees.annotation.reflection.BuilderDescriptionAnnotationValueVisitor;
import org.frankees.annotation.reflection.BuilderDescriptionElementVisitor;
import org.frankees.annotation.reflection.BuilderDescriptionsElementVisitor;
import org.frankees.annotation.reflection.StringAnnotationValueVisitor;
import org.frankees.builder.BuilderBuilder;
import org.frankees.builder.BuilderDescription;
import org.frankees.builder.TypeDescription;

public abstract class AbstractBuilderAnnotationProcessor extends
		AbstractProcessor {
	
	protected void printError(String message, Element element) {
		processingEnv.getMessager().printMessage(Kind.ERROR, message, element);
	}

	protected void printWarning(String message, Element element) {
		processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING,
				message, element);
	}

	protected void buildBuilder(BuilderDescription description, Element element) {
		try {
			String classContent = BuilderBuilder.build(description);

			JavaFileObject file = processingEnv.getFiler()
					.createSourceFile(
							description.getBuilderTypeDescription().toString(),
							element);
			file.openWriter().append(classContent).close();
		} catch (IOException e) {
			printError("Unable to create builder source class: "
					+ description.getBuilderTypeDescription().toString()
					+ ". Cause: " + e.getMessage(), element);
		}
	}

	protected void customizeBuilder(BuilderDescription builderDescription,
			AnnotationMirror annotationMirror) {

		if (builderDescription.getBuilderTypeDescription() == null) {
			builderDescription.setBuilderTypeDescription(new TypeDescription());
		}

		String builderPackageName = extractStringValue("builderPackageName",
				annotationMirror);
		if (builderPackageName != null
				&& !builderPackageName.isEmpty()
				&& builderDescription.getBuilderTypeDescription()
						.getPackageName() == null) {
			builderDescription.getBuilderTypeDescription().setPackageName(
					builderPackageName);
		}

		String builderClassSuffix = extractStringValue("builderClassSuffix",
				annotationMirror);
		if (builderClassSuffix != null
				&& !builderClassSuffix.isEmpty()
				&& builderDescription.getBuilderTypeDescription()
						.getClassName() == null) {
			builderDescription.getBuilderTypeDescription().setClassName(
					builderDescription.getObjectTypeDescription()
							.getClassName() + builderClassSuffix);
		}
	}

	protected BuilderDescription extractBuilder(
			AnnotationMirror annotationMirror, Element element) {

		BuilderDescription builderDescription = null;

		AnnotationValue value = extractValue("value", annotationMirror);
		if (value == null) {
			printWarning("Bean class missing to create builder", element);
		} else {
			builderDescription = new BuilderDescriptionAnnotationValueVisitor()
					.visit(value);
		}
		customizeBuilder(builderDescription, annotationMirror);

		return builderDescription;
	}

	protected BuilderDescription extractBuildable(TypeElement annotation,
			Element element) {

		BuilderDescription builderDescription = new BuilderDescriptionElementVisitor()
				.visit(element);

		AnnotationMirror mirror = extractMirror(annotation, element);
		customizeBuilder(builderDescription, mirror);

		return builderDescription;
	}

	protected Set<BuilderDescription> extractBuilders(AnnotationMirror mirror,
			Element element) {

		Set<BuilderDescription> descriptions = Collections.emptySet();

		List<? extends AnnotationValue> innerValues = extractArrayValue(
				"value", mirror);
		if (innerValues != null && !innerValues.isEmpty()) {
			descriptions = new HashSet<BuilderDescription>();
			for (AnnotationValue innerValue : innerValues) {
				AnnotationMirror innerMirror = new AnnotationMirrorAnnotationValueVisitor()
						.visit(innerValue);
				BuilderDescription description = extractBuilder(innerMirror,
						element);

				if (description != null) {
					descriptions.add(description);
				}
			}
		}

		return descriptions;
	}

	protected Set<BuilderDescription> extractBuilders(String beanPackageName,
			String beanClassName, Element element) {

		Set<BuilderDescription> descriptions = Collections.emptySet();

		if (beanPackageName == null || beanPackageName.isEmpty()
				|| beanClassName == null) {
			printWarning(
					"Bean package or class names missing to create builders",
					element);
		} else {
			PackageElement beanPackageElement = processingEnv.getElementUtils()
					.getPackageElement(beanPackageName);

			descriptions = new BuilderDescriptionsElementVisitor().visit(
					beanPackageElement, beanClassName);

			printWarning(String.format("Package element class: %s [%s]",
					beanPackageElement, beanPackageElement.getClass()), element);
			printWarning(String.format("Package enclosed elements: %s",
					beanPackageElement.getEnclosedElements()), element);

			if (descriptions == null || descriptions.isEmpty()) {
				printWarning(
						"Bean classes missing to create builders for package "
								+ beanPackageName, element);
			}
		}

		return descriptions;
	}

	protected String extractStringValue(String valueName,
			AnnotationMirror mirror) {

		String stringValue = null;

		AnnotationValue value = extractValue(valueName, mirror);
		if (value != null) {
			stringValue = new StringAnnotationValueVisitor().visit(value);
		}

		return stringValue;
	}

	protected List<? extends AnnotationValue> extractArrayValue(
			String valueName, AnnotationMirror mirror) {

		List<? extends AnnotationValue> arrayValue = null;

		AnnotationValue value = extractValue(valueName, mirror);
		if (value != null) {
			arrayValue = new ArrayAnnotationValueVisitor().visit(value);
		}

		return arrayValue;
	}

	protected AnnotationValue extractValue(String valueName,
			AnnotationMirror annotationMirror) {

		AnnotationValue value = null;

		Map<? extends ExecutableElement, ? extends AnnotationValue> values = processingEnv
				.getElementUtils().getElementValuesWithDefaults(
						annotationMirror);
		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values
				.entrySet()) {
			if (entry.getKey().getSimpleName().toString().equals(valueName)) {
				value = entry.getValue();
				break;
			}
		}
		return value;
	}

	protected AnnotationMirror extractMirror(TypeElement annotation,
			Element element) {

		for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
			if (processingEnv.getTypeUtils().isSameType(annotation.asType(),
					annotationMirror.getAnnotationType())) {
				return annotationMirror;
			}
		}

		return null;
	}

}
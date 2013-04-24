package org.frankees.annotation;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.frankees.annotation.reflection.ArrayAnnotationValueVisitor;
import org.frankees.annotation.reflection.StringAnnotationValueVisitor;
import org.frankees.builder.BuilderBuilder;
import org.frankees.builder.BuilderDescription;
import org.frankees.builder.TypeDescription;

public abstract class AbstractBuilderAnnotationProcessor extends
		AbstractProcessor {

	protected void buildBuilder(BuilderDescription builderDescription,
			Element element) {
		try {
			String classContent = BuilderBuilder.build(builderDescription);

			JavaFileObject file = processingEnv.getFiler().createSourceFile(
					builderDescription.getBuilderTypeDescription().toString(),
					element);
			file.openWriter().append(classContent).close();
		} catch (IOException e) {
			processingEnv.getMessager().printMessage(
					Kind.ERROR,
					"Unable to create builder source class: "
							+ builderDescription.getBuilderTypeDescription()
									.toString() + ". Cause: " + e.getMessage(),
					element);
		}
	}

	protected void customizeBuilder(BuilderDescription builderDescription,
			TypeElement annotation, Element element) {

		if (builderDescription.getBuilderTypeDescription() == null) {
			builderDescription.setBuilderTypeDescription(new TypeDescription());
		}

		String builderPackageName = extractStringValue("builderPackageName",
				annotation, element);
		if (builderPackageName == null || builderPackageName.isEmpty()) {
			builderPackageName = builderDescription.getObjectTypeDescription()
					.getPackageName();
		}
		builderDescription.getBuilderTypeDescription().setPackageName(
				builderPackageName);

		String builderClassSuffix = extractStringValue("builderClassSuffix",
				annotation, element);
		builderDescription.getBuilderTypeDescription().setClassName(
				builderDescription.getObjectTypeDescription().getClassName()
						+ builderClassSuffix);
	}

	protected Map<? extends ExecutableElement, ? extends AnnotationValue> extractValues(
			TypeElement annotation, Element element) {

		Map<? extends ExecutableElement, ? extends AnnotationValue> values = Collections
				.emptyMap();

		for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
			if (processingEnv.getTypeUtils().isSameType(annotation.asType(),
					annotationMirror.getAnnotationType())) {
				values = processingEnv.getElementUtils()
						.getElementValuesWithDefaults(annotationMirror);
				break;
			}
		}

		return values;
	}

	protected AnnotationValue extractValue(String valueName,
			Map<? extends ExecutableElement, ? extends AnnotationValue> values) {
		AnnotationValue value = null;
		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values
				.entrySet()) {
			if (entry.getKey().getSimpleName().toString().equals(valueName)) {
				value = entry.getValue();
				break;
			}
		}
		return value;
	}

	protected AnnotationValue extractValue(String valueName,
			AnnotationMirror annotationMirror) {
		return extractValue(valueName, processingEnv.getElementUtils()
				.getElementValuesWithDefaults(annotationMirror));
	}

	protected AnnotationValue extractValue(String valueName,
			TypeElement annotation, Element element) {
		return extractValue(valueName, extractValues(annotation, element));
	}

	protected String extractStringValue(String valueName,
			TypeElement annotation, Element element) {

		AnnotationValue value = extractValue(valueName, annotation, element);

		String stringValue = null;
		if (value != null) {
			stringValue = new StringAnnotationValueVisitor().visit(value);
		}

		return stringValue;
	}

	protected List<? extends AnnotationValue> extractArrayValue(
			String valueName, TypeElement annotation, Element element) {

		AnnotationValue value = extractValue(valueName, annotation, element);

		List<? extends AnnotationValue> arrayValue = null;
		if (value != null) {
			arrayValue = new ArrayAnnotationValueVisitor().visit(value);
		}

		return arrayValue;
	}

}
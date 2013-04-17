package org.frankees.annotation;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.frankees.annotation.reflection.StringAnnotationValueVisitor;
import org.frankees.builder.BuilderBuilder;
import org.frankees.builder.BuilderDescription;
import org.frankees.builder.TypeDescription;

public abstract class AbstractBuilderAnnotationProcessor extends
		AbstractProcessor {

	private ProcessingEnvironment env;

	public AbstractBuilderAnnotationProcessor() {
		super();
	}

	@Override
	public void init(ProcessingEnvironment env) {
		this.env = env;
	}

	protected ProcessingEnvironment getEnv() {
		return env;
	}

	protected void buildBuilder(BuilderDescription builderDescription,
			Element element) {
		try {
			String classContent = BuilderBuilder.build(builderDescription);

			JavaFileObject file = env.getFiler().createSourceFile(
					builderDescription.getBuilderTypeDescription().toString(),
					element);
			file.openWriter().append(classContent).close();
		} catch (IOException e) {
			env.getMessager().printMessage(
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

		Map<? extends ExecutableElement, ? extends AnnotationValue> values = extractValues(
				annotation, element);
		for (Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values
				.entrySet()) {
			if ("builderPackageName".equals(entry.getKey().getSimpleName()
					.toString())) {
				String builderPackageName = new StringAnnotationValueVisitor()
						.visit(entry.getValue());
				if (builderPackageName == null || builderPackageName.isEmpty()) {
					builderPackageName = builderDescription
							.getObjectTypeDescription().getPackageName();
				}
				builderDescription.getBuilderTypeDescription().setPackageName(
						builderPackageName);
			} else if ("builderClassSuffix".equals(entry.getKey()
					.getSimpleName().toString())) {
				String builderClassSuffix = new StringAnnotationValueVisitor()
						.visit(entry.getValue());
				builderDescription.getBuilderTypeDescription().setClassName(
						builderDescription.getObjectTypeDescription()
								.getClassName() + builderClassSuffix);
			}
		}
	}

	protected Map<? extends ExecutableElement, ? extends AnnotationValue> extractValues(
			TypeElement annotation, Element element) {

		Map<? extends ExecutableElement, ? extends AnnotationValue> values = Collections
				.emptyMap();

		for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
			if (getEnv().getTypeUtils().isSameType(annotation.asType(),
					annotationMirror.getAnnotationType())) {
				values = getEnv().getElementUtils()
						.getElementValuesWithDefaults(annotationMirror);
				continue;
			}
		}

		return values;
	}

}
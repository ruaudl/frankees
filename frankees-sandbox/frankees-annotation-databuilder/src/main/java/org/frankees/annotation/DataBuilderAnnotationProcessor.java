package org.frankees.annotation;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.frankees.annotation.reflection.BuilderDescriptionAnnotationValueVisitor;
import org.frankees.builder.BuilderDescription;

@SupportedAnnotationTypes("org.frankees.annotation.DataBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DataBuilderAnnotationProcessor extends
		AbstractBuilderAnnotationProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			for (Element element : roundEnv
					.getElementsAnnotatedWith(annotation)) {
				switch (element.getKind()) {
				case CLASS:
					break;
				case PACKAGE:
					break;
				default:
					continue;
				}

				Map<? extends ExecutableElement, ? extends AnnotationValue> values = extractValues(
						annotation, element);
				AnnotationValue value = null;
				for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values
						.entrySet()) {
					if ("value".equals(entry.getKey().getSimpleName()
							.toString())) {
						value = entry.getValue();
						continue;
					}
				}
				if (value == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR,
							"Class to create builder for is not defined",
							element);
					continue;
				}

				BuilderDescription builderDescription = new BuilderDescriptionAnnotationValueVisitor()
						.visit(value);

				customizeBuilder(builderDescription, annotation, element);
				buildBuilder(builderDescription, element);
			}
		}
		return true;
	}

}

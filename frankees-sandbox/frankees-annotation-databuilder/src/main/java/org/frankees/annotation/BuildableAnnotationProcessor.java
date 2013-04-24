package org.frankees.annotation;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import org.frankees.annotation.reflection.BuilderDescriptionElementVisitor;
import org.frankees.builder.BuilderDescription;

@SupportedAnnotationTypes("org.frankees.annotation.Buildable")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class BuildableAnnotationProcessor extends
		AbstractBuilderAnnotationProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			for (Element element : roundEnv
					.getElementsAnnotatedWith(annotation)) {
				if (!ElementKind.CLASS.equals(element.getKind())) {
					continue;
				}

				BuilderDescription builderDescription = new BuilderDescriptionElementVisitor()
						.visit(element);

				customizeBuilder(builderDescription, annotation, element);
				buildBuilder(builderDescription, element);
			}
		}
		return true;
	}
}

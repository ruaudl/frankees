package org.frankees.annotation;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

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

				AnnotationMirror mirror = extractMirror(annotation, element);
				BuilderDescription builderDescription = extractBuilder(mirror,
						element);
				if (builderDescription != null) {
					buildBuilder(builderDescription, element);
				}
			}
		}
		return true;
	}

}

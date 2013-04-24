package org.frankees.annotation;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.frankees.builder.BuilderDescription;

@SupportedAnnotationTypes("org.frankees.annotation.DataBuilders")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DataBuildersAnnotationProcessor extends
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

				Set<BuilderDescription> builderDescriptions = new HashSet<BuilderDescription>();

				AnnotationMirror mirror = extractMirror(annotation, element);
				builderDescriptions.addAll(extractBuilders(mirror, element));

				if (builderDescriptions.isEmpty()) {
					String beanPackageName = extractStringValue(
							"beanPackageName", mirror);
					String beanClassName = extractStringValue("beanClassName",
							mirror);

					builderDescriptions.addAll(extractBuilders(beanPackageName,
							beanClassName, element));
				}

				for (BuilderDescription builderDescription : builderDescriptions) {
					customizeBuilder(builderDescription, mirror);
					buildBuilder(builderDescription, element);
				}
				printWarning("DataBuilders: " + builderDescriptions, element);
			}
		}
		return true;
	}
}

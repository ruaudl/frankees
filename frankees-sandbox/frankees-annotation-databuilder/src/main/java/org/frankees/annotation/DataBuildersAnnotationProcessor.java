package org.frankees.annotation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.frankees.annotation.reflection.AnnotationMirrorAnnotationValueVisitor;
import org.frankees.annotation.reflection.BuilderDescriptionAnnotationValueVisitor;
import org.frankees.annotation.reflection.BuilderDescriptionsElementVisitor;
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

				List<? extends AnnotationValue> innerValues = extractArrayValue(
						"value", annotation, element);

				if (innerValues != null && !innerValues.isEmpty()) {
					for (AnnotationValue innerValue : innerValues) {
						AnnotationMirror annotationMirror = new AnnotationMirrorAnnotationValueVisitor()
								.visit(innerValue);
						AnnotationValue value = extractValue("value",
								annotationMirror);
						if (value == null) {
							processingEnv
									.getMessager()
									.printMessage(
											Kind.ERROR,
											"Class to create builder for is not defined",
											element);
							continue;
						}

						BuilderDescription description = new BuilderDescriptionAnnotationValueVisitor()
								.visit(value);
						if (description != null) {
							builderDescriptions.add(description);
						}
					}
				}
				processingEnv.getMessager().printMessage(Kind.ERROR,
						"DataBuilders: " + builderDescriptions, element);

				String beanPackageName = extractStringValue("beanPackageName",
						annotation, element);
				if (beanPackageName == null) {
					processingEnv.getMessager()
							.printMessage(Kind.ERROR,
									"No package defined to create builder for",
									element);
					continue;
				}
				PackageElement beanPackageElement = processingEnv
						.getElementUtils().getPackageElement(beanPackageName);

				if (builderDescriptions.isEmpty()) {
					String beanClassName = extractStringValue("beanClassName",
							annotation, element);
					if (beanClassName == null) {
						processingEnv.getMessager().printMessage(Kind.ERROR,
								"No class name defined to create builder for",
								element);
						continue;
					}

					builderDescriptions
							.addAll(new BuilderDescriptionsElementVisitor()
									.visit(beanPackageElement, beanClassName));
				}

				if (builderDescriptions == null
						|| builderDescriptions.isEmpty()) {
					processingEnv.getMessager().printMessage(Kind.ERROR,
							"No class found for package " + beanPackageName,
							element);
					continue;
				}

				for (BuilderDescription builderDescription : builderDescriptions) {
					customizeBuilder(builderDescription, annotation,
							element);
					buildBuilder(builderDescription, beanPackageElement);
				}
			}
		}
		return true;
	}
}

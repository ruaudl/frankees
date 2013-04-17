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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.frankees.annotation.reflection.BuilderDescriptionsElementVisitor;
import org.frankees.annotation.reflection.StringAnnotationValueVisitor;
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

				AnnotationValue beanPackageNameValue = null;
				Map<? extends ExecutableElement, ? extends AnnotationValue> values = extractValues(
						annotation, element);
				for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values
						.entrySet()) {
					if ("beanPackageName".equals(entry.getKey().getSimpleName()
							.toString())) {
						beanPackageNameValue = entry.getValue();
						continue;
					}
				}
				if (beanPackageNameValue == null) {
					getEnv().getMessager().printMessage(Kind.ERROR,
							"Class to create builder for is not defined",
							element);
					continue;
				}

				String beanPackageName = new StringAnnotationValueVisitor()
						.visit(beanPackageNameValue);
				PackageElement beanPackageElement = getEnv().getElementUtils()
						.getPackageElement(beanPackageName);
				Set<BuilderDescription> builderDescriptions = new BuilderDescriptionsElementVisitor()
						.visit(beanPackageElement);

				for (BuilderDescription builderDescription : builderDescriptions) {
					customizeBuilder(builderDescription, annotation,
							beanPackageElement);
					buildBuilder(builderDescription, beanPackageElement);
				}
			}
		}
		return true;
	}
}

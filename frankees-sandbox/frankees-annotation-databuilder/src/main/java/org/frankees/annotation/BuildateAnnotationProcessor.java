package org.frankees.annotation;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.frankees.builder.BuilderBuilder;
import org.frankees.builder.BuilderDescription;

@SupportedAnnotationTypes("org.frankees.annotation.Buildable")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class BuildateAnnotationProcessor extends AbstractProcessor {

	private Filer filer;
	private Messager messager;

	@Override
	public void init(ProcessingEnvironment env) {
		filer = env.getFiler();
		messager = env.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			for (Element element : roundEnv
					.getElementsAnnotatedWith(annotation)) {
				if (!ElementKind.CLASS.equals(element.getKind())) {
					continue;
				}

				JavaFileObject file = null;
				try {
					BuilderDescription builderDescription = BuilderBuilder
							.describe(element);

					builderDescription.setBuilderClassName(builderDescription
							.getObjectClassName() + "Builder");
					builderDescription.setBuilderPackageName(builderDescription
							.getObjectPackageName());

					String classContent = BuilderBuilder
							.build(builderDescription);

					file = filer.createSourceFile(
							builderDescription.getBuilderPackageName() + "."
									+ builderDescription.getBuilderClassName(),
							element);
					file.openWriter().append(classContent).close();
				} catch (IOException e) {
					messager.printMessage(
							Kind.ERROR,
							"Unable to create builder source class: "
									+ e.getMessage(), element);
				}
			}
		}
		return true;
	}

}

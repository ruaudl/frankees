package org.frankees.annotation;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.frankees.annotation.reflection.BuilderDescriptionAnnotationValueVisitor;
import org.frankees.builder.BuilderBuilder;
import org.frankees.builder.BuilderDescription;

@SupportedAnnotationTypes("org.frankees.annotation.DataBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DataBuilderAnnotationProcessor extends AbstractProcessor {

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
				switch (element.getKind()) {
				case CLASS:
					break;
				case PACKAGE:
					break;
				default:
					continue;
				}

				AnnotationValue value = null;
				List<? extends AnnotationMirror> mirrors = element
						.getAnnotationMirrors();
				String name = DataBuilder.class.getName();
				for (AnnotationMirror mirror : mirrors) {
					if (mirror.getAnnotationType().toString().equals(name)) {
						for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : mirror
								.getElementValues().entrySet()) {
							if ("value".equals(entry.getKey().getSimpleName()
									.toString())) {
								value = entry.getValue();
								break;
							}
						}
					}
				}
				if (value == null) {
					messager.printMessage(Kind.ERROR,
							"Class to create builder for is not defined",
							element);
					continue;
				}

				BuilderDescription builderDescription = new BuilderDescriptionAnnotationValueVisitor()
						.visit(value);
				try {
					String classContent = BuilderBuilder
							.build(builderDescription);

					JavaFileObject file = filer.createSourceFile(
							builderDescription.getBuilderTypeDescription()
									.toString(), element);
					file.openWriter().append(classContent).close();
				} catch (IOException e) {
					messager.printMessage(
							Kind.ERROR,
							"Unable to create builder source class: "
									+ builderDescription
											.getBuilderTypeDescription()
											.toString() + ". Cause: "
									+ e.getMessage(), element);
				}
			}
		}
		return true;
	}

}

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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

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

				JavaFileObject file = null;
				try {

					BuilderDescription builderDescription = new SimpleAnnotationValueVisitor6<BuilderDescription, Void>() {
						public BuilderDescription visitType(TypeMirror t, Void p) {
							return new SimpleTypeVisitor6<BuilderDescription, Void>() {
								@Override
								public BuilderDescription visitDeclared(
										DeclaredType t, Void p) {
									return BuilderBuilder.describe(t
											.asElement());
								}
							}.visit(t);
						};
					}.visit(value);

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

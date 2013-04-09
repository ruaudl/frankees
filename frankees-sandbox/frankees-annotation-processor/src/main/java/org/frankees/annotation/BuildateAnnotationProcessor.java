package org.frankees.annotation;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
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
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.frankees.builder.BuilderBuilder;
import org.frankees.builder.BuilderDescription;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class BuildateAnnotationProcessor extends AbstractProcessor {

	private Filer filer;
	private Messager messager;

	private class BuilderDescriptionVisitor extends
			SimpleElementVisitor6<BuilderDescription, Void> {
		@Override
		public BuilderDescription visitPackage(PackageElement e, Void p) {
			BuilderDescription description = new BuilderDescription();
			description.setObjectPackageName(e.getQualifiedName().toString());
			return description;
		}

		@Override
		public BuilderDescription visitExecutable(ExecutableElement e, Void p) {
			BuilderDescription description = new BuilderDescription();
			if (e.getSimpleName().toString().startsWith("get")) {
				String propertyName = e.getSimpleName().toString()
						.substring(3, 4).toLowerCase()
						+ e.getSimpleName().toString().substring(4);
				description.setProperties(Collections.singletonMap(
						propertyName, e.getReturnType().toString()));
			} else {
				description.setProperties(Collections
						.<String, String> emptyMap());
			}
			return description;
		}

		@Override
		public BuilderDescription visitType(TypeElement e, Void p) {
			BuilderDescription description = new BuilderDescription();
			description.setObjectClassName(e.getSimpleName().toString());

			BuilderDescription packageDescription = visit(e
					.getEnclosingElement());
			if (packageDescription != null) {
				description.setObjectPackageName(packageDescription
						.getObjectPackageName());
			}

			List<? extends Element> elements = e.getEnclosedElements();
			Map<String, String> properties = new HashMap<String, String>();
			for (Element element : elements) {
				BuilderDescription setterDescription = visit(element);
				if (setterDescription != null) {
					properties.putAll(setterDescription.getProperties());
				}
			}
			description.setProperties(properties);

			return description;
		}
	}

	@Override
	public void init(ProcessingEnvironment env) {
		filer = env.getFiler();
		messager = env.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getRootElements()) {
			if (element.getAnnotation(Buildable.class) == null
					|| !ElementKind.CLASS.equals(element.getKind())) {
				continue;
			}

			if (element.getSimpleName().toString().startsWith("T")) {
				messager.printMessage(Kind.WARNING, "Start with a T!", element);
			} else {
				messager.printMessage(Kind.WARNING, "Should start with a T!",
						element);
			}

			JavaFileObject file = null;
			try {
				BuilderDescription builderDescription = new BuilderDescriptionVisitor()
						.visit(element);
				builderDescription.setBuilderClassName(builderDescription
						.getObjectClassName() + "Builder");
				builderDescription.setBuilderPackageName(builderDescription
						.getObjectPackageName());

				String classContent = BuilderBuilder.build(builderDescription);

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

		return true;
	}

}

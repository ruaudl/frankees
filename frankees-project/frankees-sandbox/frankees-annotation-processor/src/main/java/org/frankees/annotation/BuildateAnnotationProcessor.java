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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("*")
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
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getRootElements()) {
			if (element.getAnnotation(Buildable.class) == null) {
				continue;
			}

			if (element.getSimpleName().toString().startsWith("T")) {
				messager.printMessage(Kind.WARNING, "Start with a T!", element);
			} else {
				messager.printMessage(Kind.WARNING, "Should start with a T!", element);
			}

			String builderClassName = element.getSimpleName() + "Builder";
			String builderPackageName = ((PackageElement) element.getEnclosingElement()).getQualifiedName().toString();

			String sillyClassContent = "package " + builderPackageName + ";\n" + "public class " + builderClassName
					+ " {\n" + "	public String foobar;\n" + "}";

			JavaFileObject file = null;

			try {
				file = filer.createSourceFile(builderPackageName + "." + builderClassName, element);
				file.openWriter().append(sillyClassContent).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

}

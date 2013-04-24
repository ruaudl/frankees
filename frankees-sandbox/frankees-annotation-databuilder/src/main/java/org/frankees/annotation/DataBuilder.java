package org.frankees.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.CLASS)
public @interface DataBuilder {

	Class<? extends Object> value();

	String builderClassSuffix() default "";

	String builderPackageName() default "";
}

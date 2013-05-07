package org.frankees.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.CLASS)
public @interface DataBuilders {

	Class<? extends Object>[] value() default {};

	String builderClassSuffix() default "";

	String builderPackageName() default "";

	String beanClassName() default ".*";

	String beanPackageName() default "";

	DataBuilder[] builders() default {};
}

package org.frankees.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Retention(RetentionPolicy.CLASS)
public @interface DataBuilders {

	String builderClassSuffix() default "Builder";
	
	String builderPackageName() default "";

	String beanClassName() default ".*";

	String beanPackageName() default "";
	
	DataBuilder[] value() default {};
}

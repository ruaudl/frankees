package org.frankees.tooling;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeUtils {

	private TypeUtils() {
	}

	public static boolean isPrimitive(Type type) {
		return Class.class.isAssignableFrom(type.getClass()) && ((Class<?>) type).isPrimitive();
	}

	public static boolean isEnum(Type type) {
		return Class.class.isAssignableFrom(type.getClass()) && ((Class<?>) type).isEnum();
	}

	public static boolean isGeneric(Type type) {
		return ParameterizedType.class.isAssignableFrom(type.getClass());
	}

	public static boolean isArray(Type type) {
		return Class.class.isAssignableFrom(type.getClass()) && ((Class<?>) type).isArray();
	}

	public static boolean isGenericArray(Type type) {
		return GenericArrayType.class.isAssignableFrom(type.getClass());
	}

	public static Class<?> getRawClass(Type type) {
		if (Class.class.isAssignableFrom(type.getClass())) {
			return (Class<?>) type;
		}
		if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
			return getRawClass(((ParameterizedType) type).getRawType());
		}
		if (GenericArrayType.class.isAssignableFrom(type.getClass())) {
			Class<?> rawClass = getRawClass(((GenericArrayType) type).getGenericComponentType());
			return Array.newInstance(rawClass, 0).getClass();
		}
		if (TypeVariable.class.isAssignableFrom(type.getClass())) {
			return getRawClass(((TypeVariable<?>) type).getBounds()[0]);
		}
		if (WildcardType.class.isAssignableFrom(type.getClass())) {
			return getRawClass(((WildcardType) type).getUpperBounds()[0]);
		}
		return null;
	}

	public static Type[] getTypeArguments(Type type) {
		if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
			return ((ParameterizedType) type).getActualTypeArguments();
		}
		return null;
	}
}

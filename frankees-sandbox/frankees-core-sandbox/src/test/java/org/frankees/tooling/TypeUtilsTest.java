package org.frankees.tooling;

import static org.fest.assertions.Assertions.*;
import static org.frankees.tooling.TypeUtils.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

public class TypeUtilsTest {

	public class Puppet<T, S extends Properties> {
		byte byteField;
		short shortField;
		int intField;
		long longField;
		char charField;
		float floatField;
		double doubleField;
		boolean booleanField;
		List<String> stringListField;
		Properties propertiesField;
		String[] stringArrayField;
		int[] intArrayField;
		List<String>[] stringListArrayField;
		T typeVariableField;
		S propertiesTypeVariableField;
		Set<?> wilcardTypeSetField;

		void voidMethod() {
		}
	}

	@Test
	public void testPrimitiveTypes() throws SecurityException, NoSuchFieldException {
		assertThat(isPrimitive(typeForField("byteField"))).isTrue();
		assertThat(isPrimitive(typeForField("shortField"))).isTrue();
		assertThat(isPrimitive(typeForField("intField"))).isTrue();
		assertThat(isPrimitive(typeForField("longField"))).isTrue();
		assertThat(isPrimitive(typeForField("charField"))).isTrue();
		assertThat(isPrimitive(typeForField("floatField"))).isTrue();
		assertThat(isPrimitive(typeForField("doubleField"))).isTrue();
		assertThat(isPrimitive(typeForField("booleanField"))).isTrue();
		assertThat(isPrimitive(void.class)).isTrue();
		assertThat(isPrimitive(typeForField("stringListField"))).isFalse();
		assertThat(isPrimitive(typeForField("propertiesField"))).isFalse();
		assertThat(isPrimitive(typeForField("stringArrayField"))).isFalse();
		assertThat(isPrimitive(typeForField("intArrayField"))).isFalse();
	}

	@Test
	public void testGenericTypes() throws SecurityException, NoSuchFieldException {
		assertThat(isGeneric(typeForField("stringListField"))).isTrue();
		assertThat(isGeneric(typeForField("propertiesField"))).isFalse();
		assertThat(isGeneric(typeForField("stringArrayField"))).isFalse();
		assertThat(isGeneric(typeForField("intArrayField"))).isFalse();
		assertThat(isGeneric(typeForField("stringListArrayField"))).isFalse();
	}

	@Test
	public void testArrayTypes() throws SecurityException, NoSuchFieldException {
		assertThat(isArray(typeForField("stringListField"))).isFalse();
		assertThat(isArray(typeForField("propertiesField"))).isFalse();
		assertThat(isArray(typeForField("stringArrayField"))).isTrue();
		assertThat(isArray(typeForField("intArrayField"))).isTrue();
		assertThat(isArray(typeForField("stringListArrayField"))).isFalse();
	}

	@Test
	public void testGenericArrayTypes() throws SecurityException, NoSuchFieldException {
		assertThat(isGenericArray(typeForField("stringListField"))).isFalse();
		assertThat(isGenericArray(typeForField("propertiesField"))).isFalse();
		assertThat(isGenericArray(typeForField("stringArrayField"))).isFalse();
		assertThat(isGenericArray(typeForField("intArrayField"))).isFalse();
		assertThat(isGenericArray(typeForField("stringListArrayField"))).isTrue();
	}

	@Test
	public void testClassRetrieval() throws SecurityException, NoSuchFieldException {
		assertThat(getRawClass(typeForField("stringListField"))).isEqualTo(List.class);
		assertThat(getRawClass(typeForField("propertiesField"))).isEqualTo(Properties.class);
		assertThat(getRawClass(typeForField("stringArrayField"))).isEqualTo(String[].class);
		assertThat(getRawClass(typeForField("intArrayField"))).isEqualTo(int[].class);
		assertThat(getRawClass(typeForField("stringListArrayField"))).isEqualTo(List[].class);
		assertThat(getRawClass(typeForField("typeVariableField"))).isEqualTo(Object.class);
		assertThat(getRawClass(typeForField("propertiesTypeVariableField"))).isEqualTo(Properties.class);
		assertThat(getRawClass(typeForField("wilcardTypeSetField"))).isEqualTo(Set.class);
		assertThat(getRawClass(typeParameterForField("wilcardTypeSetField"))).isEqualTo(Object.class);
	}

	public Type typeForField(String name) throws SecurityException, NoSuchFieldException {
		return Puppet.class.getDeclaredField(name).getGenericType();
	}

	public Type typeParameterForField(String name) throws SecurityException, NoSuchFieldException {
		Type genericType = Puppet.class.getDeclaredField(name).getGenericType();
		if (isGeneric(genericType)) {
			return ((ParameterizedType) genericType).getActualTypeArguments()[0];
		}
		return null;
	}
}

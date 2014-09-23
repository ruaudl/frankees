package org.frankees.making;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.frankees.tooling.BeanField;
import org.frankees.tooling.BeanUtils;
import org.frankees.tooling.TypeUtils;

public abstract class RecursiveMaker implements Maker {

	private int maxLevel = 1;

	public RecursiveMaker() {
		super();
	}

	public abstract <B> Boolean makeBoolean(BeanField<B> beanField);

	public abstract <B> Number makeNumber(BeanField<B> beanField);

	public abstract <B> Character makeChar(BeanField<B> beanField);

	public abstract <B> String makeString(BeanField<B> beanField);

	public abstract Boolean makeBoolean(String name);

	public abstract Number makeNumber(String name);

	public abstract Character makeChar(String name);

	public abstract String makeString(String name);

	@Override
	public Object make(String name, Type type) {
		return make(name, type, maxLevel);
	}

	public Object make(String name, Type type, int level) {
		Object bean = null;

		try {
			Class<?> rawClass = TypeUtils.getRawClass(type);
			if (TypeUtils.isPrimitive(type)) {
				if (boolean.class.equals(type)) {
					bean = makeBoolean(name);
				} else if (char.class.equals(type)) {
					bean = makeChar(name);
				} else {
					bean = makeNumber(name);
				}
			} else if (TypeUtils.isGeneric(type)) {
				Type[] typeArguments = TypeUtils.getTypeArguments(type);
				if (List.class.equals(rawClass) && typeArguments.length == 1) {
					bean = makeList(name, typeArguments[0], level);
				} else if (Set.class.equals(rawClass) && typeArguments.length == 1) {
					bean = makeSet(name, typeArguments[0], level);
				} else if (Map.class.equals(rawClass) && typeArguments.length == 2) {
					bean = makeMap(name, typeArguments[0], typeArguments[1], level);
				} else {
					// TODO
				}
			} else {
				if (Number.class.isAssignableFrom(rawClass)) {
					Constructor<?> constructor = rawClass.getConstructor(String.class);
					bean = constructor.newInstance(makeNumber(name).toString());
				} else if (String.class.isAssignableFrom(rawClass)) {
					bean = makeString(name);
				} else if (TypeUtils.isEnum(type)) {
					makeEnum(name, rawClass);
				} else {
					bean = rawClass.newInstance();
					if (level > 0) {
						Map<String, BeanField<?>> fields = BeanUtils.readFields(rawClass);
						for (Entry<String, BeanField<?>> field : fields.entrySet()) {
							field.getValue().writeValue(bean, make(field.getKey(), field.getValue().getFieldType(), level - 1));
						}
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@Override
	public <T> T makeBean(Class<T> beanClass) {
		return makeBean(beanClass, maxLevel);
	}

	public <T> T makeBean(Class<T> beanClass, int level) {
		T bean = null;

		try {
			bean = beanClass.newInstance();

			if (level > 0) {
				Map<String, BeanField<T>> fields = BeanUtils.readFields(beanClass);
				for (BeanField<T> field : fields.values()) {
					field.writeValue(bean, makeField(field, level - 1));
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return bean;
	}

	@Override
	public <B, F> F makeField(BeanField<B> field) {
		return makeField(field, maxLevel);
	}

	@SuppressWarnings("unchecked")
	public <B, F> F makeField(BeanField<B> field, int level) {
		F fieldValue = null;

		Class<?> fieldClass = field.getFieldClass();
		try {
			if (fieldClass.isPrimitive()) {
				if (boolean.class.equals(fieldClass)) {
					fieldValue = (F) makeBoolean(field);
				} else if (char.class.equals(fieldClass)) {
					fieldValue = (F) makeChar(field);
				} else {
					fieldValue = (F) makeNumber(field);
				}
			} else if (Number.class.isAssignableFrom(fieldClass)) {
				Constructor<F> constructor = (Constructor<F>) fieldClass.getConstructor(String.class);
				fieldValue = constructor.newInstance(makeNumber(field).toString());
			} else if (String.class.isAssignableFrom(fieldClass)) {
				fieldValue = (F) makeString(field);
			} else if (fieldClass.isEnum()) {
				Object[] enumConstants = fieldClass.getEnumConstants();
				if (enumConstants != null && enumConstants.length > 0) {
					fieldValue = (F) enumConstants[0];
				}
			} else if (Collection.class.equals(fieldClass) && field.isGeneric()) {
				fieldValue = (F) makeCollection(field, level);
			} else if (Map.class.equals(fieldClass) && field.isGeneric()) {
				fieldValue = (F) makeMap(field, level);
			} else {
				fieldValue = (F) makeBean(fieldClass, level);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return fieldValue;
	}

	@Override
	public <B> Collection<?> makeCollection(BeanField<B> field) {
		return makeCollection(field, maxLevel);
	}

	@SuppressWarnings("unchecked")
	public <B, C extends Collection<E>, E> C makeCollection(BeanField<B> field, int level) {
		C collection = null;
		Class<C> collectionClass = (Class<C>) field.getFieldClass();
		Class<E> elementClass = (Class<E>) ((ParameterizedType) field.getFieldType()).getActualTypeArguments()[0];

		if (collectionClass.isInterface()) {
			if (level > 0) {
				if (Set.class.isAssignableFrom(collectionClass)) {
					collection = collectionClass.cast(Collections.singleton(makeBean(elementClass, level - 1)));
				} else {
					collection = collectionClass.cast(Collections.singletonList(makeBean(elementClass, level - 1)));
				}
			} else {
				if (Set.class.isAssignableFrom(collectionClass)) {
					collection = collectionClass.cast(Collections.emptySet());
				} else {
					collection = collectionClass.cast(Collections.emptyList());
				}
			}
		} else {
			try {
				collection = collectionClass.newInstance();
				if (level > 0) {
					collection.add(makeBean(elementClass, level - 1));
				}
			} catch (InstantiationException e) {
				collection = null;
			} catch (IllegalAccessException e) {
				collection = null;
			}
		}
		return collection;
	}

	@Override
	public <B> Map<?, ?> makeMap(BeanField<B> field) {
		return makeMap(field, maxLevel);
	}

	@SuppressWarnings("unchecked")
	public <B, K extends Object, V extends Object, M extends Map<K, V>> M makeMap(BeanField<B> field, int level) {
		M map = null;

		Class<M> mapClass = (Class<M>) field.getFieldClass();
		ParameterizedType genericType = (ParameterizedType) field.getFieldType();
		Class<K> keyClass = (Class<K>) genericType.getActualTypeArguments()[0];
		Class<V> valueClass = (Class<V>) genericType.getActualTypeArguments()[1];

		if (mapClass.isInterface()) {
			if (level > 0) {
				map = mapClass.cast(Collections.singletonMap(makeBean(keyClass, level - 1), makeBean(valueClass, level - 1)));
			} else {
				map = mapClass.cast(Collections.emptyMap());
			}
		} else {
			try {
				map = mapClass.newInstance();
				if (level > 0) {
					map.put(makeBean(keyClass, level - 1), makeBean(valueClass, level - 1));
				}
			} catch (InstantiationException e) {
				map = null;
			} catch (IllegalAccessException e) {
				map = null;
			}
		}
		return map;
	}

	public <E> E makeEnum(String name, Class<E> enumClass) {
		E value = null;
		E[] enumConstants = enumClass.getEnumConstants();
		if (enumConstants != null && enumConstants.length > 0) {
			int index = makeNumber(name).intValue() % enumConstants.length;
			value = enumConstants[index];
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public <E> Set<E> makeSet(String name, Type elementType, int level) {
		Set<E> value = null;
		if (level > 0) {
			value = (Set<E>) Collections.singleton(make(name, elementType, level - 1));
		} else {
			value = Collections.emptySet();
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public <E> List<E> makeList(String name, Type elementType, int level) {
		List<E> value = null;
		if (level > 0) {
			value = (List<E>) Collections.singletonList(make(name, elementType, level - 1));
		} else {
			value = Collections.emptyList();
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> makeMap(String name, Type keyType, Type valueType, int level) {
		Map<K, V> value = null;
		if (level > 0) {
			value = (Map<K, V>) Collections.singletonMap(make(name, keyType, level - 1), make(name, valueType, level - 1));
		} else {
			value = Collections.emptyMap();
		}
		return value;
	}

	public RecursiveMaker withMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
		return this;
	}

}
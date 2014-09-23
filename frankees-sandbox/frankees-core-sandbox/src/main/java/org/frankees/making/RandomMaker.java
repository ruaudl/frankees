package org.frankees.making;

import java.util.Random;

import org.frankees.loading.Dataset;
import org.frankees.tooling.BeanField;

public class RandomMaker extends RecursiveMaker implements Maker {

	private Random random;

	public RandomMaker() {
		random = new Random();
	}

	@Override
	public <B> Boolean makeBoolean(BeanField<B> field) {
		return random.nextBoolean();
	}

	@Override
	public <B> Number makeNumber(BeanField<B> field) {
		return random.nextInt(100);
	}

	@Override
	public <B> Character makeChar(BeanField<B> field) {
		return (char) ('a' + random.nextInt(26));
	}

	@Override
	public <B> String makeString(BeanField<B> field) {
		int length = field.getFieldDescriptor().getName().length();
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = makeChar(field);
		}
		return new String(chars);
	}

	public RandomMaker from(Dataset dataset) {
		return this;
	}

	public RandomMaker withSeed(long seed) {
		random.setSeed(seed);
		return this;
	}

	@Override
	protected void makeEnum(String name, Class<?> enumClass) {
		Object value;
		Object[] enumConstants = enumClass.getEnumConstants();
		if (enumConstants != null && enumConstants.length > 0) {
			int index = makeNumber(name).intValue() % enumConstants.length;
			value = enumConstants[index];
		}
	}

}

package org.frankees.making;

import org.frankees.tooling.BeanField;

public class StaticMaker extends RecursiveMaker implements Maker {

	@Override
	public <B> Boolean makeBoolean(BeanField<B> field) {
		return field.getFieldDescriptor().getName().length() % 2 == 0;
	}

	@Override
	public <B> Number makeNumber(BeanField<B> field) {
		return field.getFieldDescriptor().getName().length();
	}

	@Override
	public <B> Character makeChar(BeanField<B> field) {
		return (char) ('a' + field.getFieldDescriptor().getName().length());
	}

	@Override
	public <B> String makeString(BeanField<B> field) {
		return field.getFieldDescriptor().getName().toUpperCase();
	}

	@Override
	public Boolean makeBoolean(String name) {
		return name.length() % 2 == 0;
	}

	@Override
	public Number makeNumber(String name) {
		return name.length();
	}

	@Override
	public Character makeChar(String name) {
		return (char) ('a' + name.length());
	}

	@Override
	public String makeString(String name) {
		return name.toUpperCase();
	}

}

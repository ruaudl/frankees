package org.frankees.loader.parsing;

import static org.fest.assertions.Assertions.*;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.frankees.loader.parsing.JSONLoaderHelper;
import org.frankees.loader.parsing.XMLLoaderHelper;
import org.frankees.loader.parsing.YMLLoaderHelper;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

public class ParsersTest {

	public static class Monster {

		private String name;

		private Short legsCount;

		private Integer frightLevel;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Short getLegsCount() {
			return legsCount;
		}

		public void setLegsCount(Short legsCount) {
			this.legsCount = legsCount;
		}

		public Integer getFrightLevel() {
			return frightLevel;
		}

		public void setFrightLevel(Integer frightLevel) {
			this.frightLevel = frightLevel;
		}
	}

	@Test
	public void testXmlParser() throws JAXBException, SAXException,
			IOException, ParserConfigurationException, IntrospectionException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, DOMException, InvocationTargetException {
		String filename = "dataset.xml";
		InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream(filename);

		Map<String, Monster> beans = XMLLoaderHelper.loadBeans(stream,
				Monster.class);

		Monster dracula = beans.get("dracula");
		assertThat(dracula).isNotNull();
		assertThat(dracula.getName()).isEqualTo("Dracula");

		Monster wolfman = beans.get("wolfman");
		assertThat(wolfman).isNotNull();
		assertThat(wolfman.getName()).isEqualTo("Wolfman");
	}

	@Test
	public void testJsonParser() throws IllegalArgumentException, DOMException,
			SAXException, IOException, ParserConfigurationException,
			IntrospectionException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		String filename = "dataset.json";
		InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream(filename);

		Map<String, Monster> beans = JSONLoaderHelper.loadBeans(stream,
				Monster.class);

		Monster dracula = beans.get("dracula");
		assertThat(dracula).isNotNull();
		assertThat(dracula.getName()).isEqualTo("Dracula");

		Monster wolfman = beans.get("wolfman");
		assertThat(wolfman).isNotNull();
		assertThat(wolfman.getName()).isEqualTo("Wolfman");
	}

	@Test
	public void testYamlParser() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			IntrospectionException, InstantiationException {
		String filename = "dataset.yml";
		InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream(filename);

		Map<String, Monster> beans = YMLLoaderHelper.loadBeans(stream,
				Monster.class);

		Monster dracula = beans.get("dracula");
		assertThat(dracula).isNotNull();
		assertThat(dracula.getName()).isEqualTo("Dracula");

		Monster wolfman = beans.get("wolfman");
		assertThat(wolfman).isNotNull();
		assertThat(wolfman.getName()).isEqualTo("Wolfman");
	}
}

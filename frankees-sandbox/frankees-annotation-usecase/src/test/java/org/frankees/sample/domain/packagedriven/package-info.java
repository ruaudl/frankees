@DataBuilders(builderClassSuffix = "Assembler", builderPackageName = "org.frankees.sample.domain.packagedriven.test", value = {
		@DataBuilder(PDMonster.class), @DataBuilder(PDCharacter.class) })
package org.frankees.sample.domain.packagedriven;

import org.frankees.annotation.DataBuilder;
import org.frankees.annotation.DataBuilders;


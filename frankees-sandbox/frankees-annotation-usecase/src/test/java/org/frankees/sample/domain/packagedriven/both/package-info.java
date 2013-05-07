@DataBuilders(builderClassSuffix = "Assembler", builderPackageName = "org.frankees.sample.domain.packagedriven.both", builders = {
		@DataBuilder(PDMonster.class), @DataBuilder(PDCharacter.class) })
package org.frankees.sample.domain.packagedriven.both;

import org.frankees.annotation.DataBuilder;
import org.frankees.annotation.DataBuilders;
import org.frankees.sample.domain.packagedriven.PDCharacter;
import org.frankees.sample.domain.packagedriven.PDMonster;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.frankees.maven.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.project.MavenProject;
import org.fest.assertions.Assertions;
import org.frankees.maven.model.Monster;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Guillaume
 */
public class DatabuilderMojoTest {

     private static final String TARGET_DIR = "." + File.separator + "target" + File.separator;

  private DatabuilderMojo databuilderMojo;
  private MavenProject mavenProject;

  @Before
  public void setUp() throws Exception {
    mavenProject = Mockito.mock(MavenProject.class);
    databuilderMojo = new DatabuilderMojo();
    databuilderMojo.project = mavenProject;
    databuilderMojo.packages = new String[]{"org.frankees.maven.model"};
    databuilderMojo.targetDir = TARGET_DIR;
  }

  @Test
  public void testExecute() throws Exception {
    List<String> classes = new ArrayList<String>();
    classes.add(Monster.class.getName());
    Mockito.when(mavenProject.getRuntimeClasspathElements()).thenReturn(classes);

    databuilderMojo.execute();

    // check that expected assertions file exist (we don't check the content we suppose the generator works).
    Assertions.assertThat(assertionsFileFor(Monster.class)).exists();
  }

  private static File assertionsFileFor(Class<?> clazz) {
    return new File(TARGET_DIR + clazz.getPackage().getName().replace('.', File.separatorChar) + File.separator
        + clazz.getSimpleName() + "Builder.java");
  }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.frankees.maven.mojo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.frankees.builder.BuilderBuilder;
import org.frankees.builder.BuilderDescription;
import org.frankees.maven.mojo.converter.DescriptionConverter;
import org.frankees.maven.mojo.util.LoadClassUtil;

/**
 * Goal which generates databuilder.
 * 
 * @goal databuilder
 * @phase generate-test-sources
 * @requiresDependencyResolution compile+runtime
 */
public class DatabuilderMojo extends AbstractMojo {
    
    /**
   * The Maven project to analyze.
   * 
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
    protected MavenProject project;
            
    /**
     * @parameter expression="${classes}
     */
    private String[] classes;
    
    /**
     * @parameter expression="${packages}
     */
    protected String[] packages;
    
    /**
   * Destination dir to store generated assertion source files. Defaults to
   * 'target/generated-test-sources/databuilders'.<br>
   * Your IDE should be able to pick up files from this location as sources automatically when generated.
   * 
   * @parameter default-value="${project.build.directory}/generated-test-sources/databuilders"
   */
  public String targetDir;
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            List<Class<?>> collectClasses = new ArrayList<Class<?>>();
            if (ArrayUtils.isNotEmpty(packages)) {
                collectClasses.addAll(LoadClassUtil.getClassesFromClassLoader(getProjectClassLoader(), packages));
            }
            if (ArrayUtils.isNotEmpty(classes)) {
                collectClasses.addAll(LoadClassUtil.getClassesFromClassLoader(getProjectClassLoader(), classes));
            }
            if (collectClasses.isEmpty()) {
                getLog().info("No classes to load");
                return;
            }
            DescriptionConverter converter = new DescriptionConverter();
            for (Class<?> clazz : collectClasses) {
                BuilderDescription builderDescription = converter.convert(clazz);
                if (builderDescription == null) {
                    getLog().error("Erreur lors la construction du builder pour la classe " + clazz.getName());
                    continue;
                }
                try {
                    String build = BuilderBuilder.build(builderDescription);
                    new File(targetDir).mkdirs();
                    createBuilderFile(build, builderDescription.getBuilderTypeDescription().getClassName(), builderDescription.getBuilderTypeDescription().getPackageName(), targetDir);
                } catch (IOException ex) {
                    getLog().error("Erreur lors de la construction de la classe du builder pour la classe " + clazz.getName(), ex);
                }
            }   
            project.addTestCompileSourceRoot(targetDir);
        } catch (DependencyResolutionRequiredException ex) {
            Logger.getLogger(DatabuilderMojo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(DatabuilderMojo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void fillBuilderJavaFile(String builderContent, File builderJavaFile) throws IOException {
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(builderJavaFile);
      fileWriter.write(builderContent);
    } finally {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
  }

  private File createBuilderFile(String assertionFileContent, String builderFileName, String builderPackageName, String targetDirectory)
      throws IOException {
        String path = builderPackageName.replace('.', File.separatorChar);
    File pathFile = new File(targetDir, path);
    pathFile.mkdirs();
    File builderJavaFile = new File(pathFile, builderFileName + ".java");
    builderJavaFile.createNewFile();
    fillBuilderJavaFile(assertionFileContent, builderJavaFile);
    return builderJavaFile;
  }
    
    private ClassLoader getProjectClassLoader() throws DependencyResolutionRequiredException, MalformedURLException {
    @SuppressWarnings("unchecked")
    List<String> runtimeClasspathElements = project.getRuntimeClasspathElements();
    URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
    for (int i = 0; i < runtimeClasspathElements.size(); i++) {
      runtimeUrls[i] = new File(runtimeClasspathElements.get(i)).toURI().toURL();
    }
    return new URLClassLoader(runtimeUrls, Thread.currentThread().getContextClassLoader());
  }
    
}

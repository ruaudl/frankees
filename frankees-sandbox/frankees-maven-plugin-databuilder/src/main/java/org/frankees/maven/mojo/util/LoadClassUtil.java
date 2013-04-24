package org.frankees.maven.mojo.util;

import static java.lang.reflect.Modifier.isPublic;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;


public final class LoadClassUtil {

  private static final String CLASS_SUFFIX = ".class";


  /**
   * Permet de récupérer les classes (contenues dans des packages selon le paramètre) à partir d'un class loader.
   * 
   * @param classLoader {@link ClassLoader} contenant les classes definies par le paramètre classOrPackageNames
   * @param classOrPackageNames Noms de classes ou de packages que l'on veut récupérer
   *          packages)
   * @return Liste des {@link Class}es trouvées
   * @throws RuntimeException Lancée en cas d'erreur lors de la récupération
   */
  public static List<Class<?>> getClassesFromClassLoader(ClassLoader classLoader, String... classOrPackageNames) {
    List<Class<?>> classes = new ArrayList<Class<?>>();
    for (String classOrPackageName : classOrPackageNames) {
      Class<?> clazz = getClassFromClassLoader(classOrPackageName, classLoader);
      if (clazz != null) {
        classes.add(clazz);
      } else {
        // c'est un package
        classes.addAll(getClassesInPackageFromClassLoader(classOrPackageName, classLoader));
      }
    }
    return classes;
  }

  /**
   * Récupère toutes les classes contenues dans un package.
   * 
   * @param packageName Nom du package dont on veut récupérer les classes
   * @param classLoader ClassLoader permettant de charger les classes
   * @return Liste des classes trouvées
   * @throws RuntimeException Lancée en cas d'erreur lors de la récupération
   */
  private static Set<Class<?>> getClassesInPackageFromClassLoader(String packageName, ClassLoader classLoader) {
    if (classLoader == null) {
      throw new IllegalArgumentException("Null class loader.");
    }
    // on récupère les classes contenues dans le classpath (mais pas dans des jars)
    Set<Class<?>> packageClasses = getPackageClassesFromClasspathFiles(packageName, classLoader);
    // on récupère les classes contenues dans les jars du classpath
    Set<Class<?>> packageClassesFromClasspathJars = getPackageClassesFromClasspathJars(packageName, classLoader);
    packageClasses.addAll(packageClassesFromClasspathJars);
    return packageClasses;
  }

  private static Set<Class<?>> getPackageClassesFromClasspathJars(String packageName, ClassLoader classLoader) {
    List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
    classLoadersList.add(ClasspathHelper.contextClassLoader());
    classLoadersList.add(ClasspathHelper.staticClassLoader());
    classLoadersList.add(classLoader);

    Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
        .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
        .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));
    Set<Class<?>> classesInPackage = reflections.getSubTypesOf(Object.class);
    Set<Class<?>> filteredClassesInPackage = new HashSet<Class<?>>();
    for (Class<?> classFromJar : classesInPackage) {
      if (isClassCandidateToDatabuilderGeneration(classFromJar)) {
        filteredClassesInPackage.add(classFromJar);
      }
    }
    return filteredClassesInPackage;
  }

  private static Set<Class<?>> getPackageClassesFromClasspathFiles(String packageName, ClassLoader classLoader) {
    try {
      String packagePath = packageName.replace('.', File.separatorChar);
      // Ask for all resources for the path
      Enumeration<URL> resources = classLoader.getResources(packagePath);
      Set<Class<?>> classes = new HashSet<Class<?>>();
      while (resources.hasMoreElements()) {
        File directory = new File(URLDecoder.decode(resources.nextElement().getPath(), "UTF-8"));
        if (directory.canRead()) {
          classes.addAll(getClassesInDirectory(directory, packageName, classLoader));
        }
      }
      return classes;
    } catch (UnsupportedEncodingException encex) {
      throw new RuntimeException(packageName + " does not appear to be a valid package (Unsupported encoding)", encex);
    } catch (IOException ioex) {
      throw new RuntimeException("IOException was thrown when trying to get all classes for " + packageName, ioex);
    }
  }

  /**
   * Get <b>public</b> classes in given directory (recursively).
   * <p>
   * Note that <b>anonymous</b> and <b>local</b> classes are excluded from the resulting list.
   * 
   * @param directory directory where to look for classes
   * @param packageName package name corresponding to directory
   * @param classLoader used classloader
   * @return
   * @throws UnsupportedEncodingException
   */
  private static List<Class<?>> getClassesInDirectory(File directory, String packageName, ClassLoader classLoader)
      throws UnsupportedEncodingException {
    List<Class<?>> classes = new ArrayList<Class<?>>();
    // Capture all the .class files in this directory
    // Get the list of the files contained in the package
    File[] files = directory.listFiles();
    for (File currentFile : files) {
      String currentFileName = currentFile.getName();
      if (isClass(currentFileName)) {
        // CHECKSTYLE:OFF
        try {
          // removes the .class extension
          String className = packageName + '.' + StringUtils.remove(currentFileName, CLASS_SUFFIX);
          Class<?> loadedClass = loadClass(className, classLoader);
          // we are only interested in public classes that are neither anonymous nor local
          if (isClassCandidateToDatabuilderGeneration(loadedClass)) {
            classes.add(loadedClass);
          }
        } catch (Throwable e) {
          // do nothing. this class hasn't been found by the loader, and we don't care.
        }
        // CHECKSTYLE:ON
      } else if (currentFile.isDirectory()) {
        // It's another package
        String subPackageName = packageName + ClassUtils.PACKAGE_SEPARATOR + currentFileName;
        // Ask for all resources for the path
        URL resource = classLoader.getResource(subPackageName.replace('.', File.separatorChar));
        File subDirectory = new File(URLDecoder.decode(resource.getPath(), "UTF-8"));
        List<Class<?>> classesForSubPackage = getClassesInDirectory(subDirectory, subPackageName, classLoader);
        classes.addAll(classesForSubPackage);
      }
    }
    return classes;
  }

  /**
   * @param loadedClass
   * @return
   */
  private static boolean isClassCandidateToDatabuilderGeneration(Class<?> loadedClass) {
    return loadedClass != null && isPublic(loadedClass.getModifiers()) && !loadedClass.isAnonymousClass()
        && !loadedClass.isLocalClass();
  }

  private static boolean isClass(String fileName) {
    return fileName.endsWith(CLASS_SUFFIX);
  }

  private static Class<?> getClassFromClassLoader(String className, ClassLoader classLoader) {
    try {
      return loadClass(className, classLoader);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  private static Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
    return Class.forName(className, true, classLoader);
  }

}

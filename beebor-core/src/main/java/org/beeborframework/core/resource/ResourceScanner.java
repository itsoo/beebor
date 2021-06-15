package org.beeborframework.core.resource;

import org.beeborframework.core.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ResourceScanner
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/12 15:17
 */
public class ResourceScanner {

    private static final String FILE_TYPE = "file";

    private static final String JAR_TYPE = "jar";

    private static final String CLASS_FILE = ".class";

    private static final String CLASS_OUTPUT_FOLDER = "classes/";

    private final Class<?> scannedClass;


    public ResourceScanner(Class<?> scannedClass) {
        this.scannedClass = scannedClass;
        this.init();
    }

    protected void init() {
        // ext
    }

    public Set<Class<?>> getClasses() {
        return getClasses(t -> !t.isAnnotation() && !t.isAnonymousClass());
    }

    public Set<Class<?>> getClasses(Predicate<Class<?>> predicate) {
        Set<Class<?>> result = new HashSet<>();
        String packageName = getPackageName(scannedClass.getCanonicalName());
        for (String className : getClassNames(packageName)) {
            try {
                Class<?> clazz = Class.forName(className);
                if (predicate.test(clazz)) {
                    result.add(clazz);
                }
            } catch (ClassNotFoundException ignore) {}
        }

        return result;
    }

    private String getPackageName(String canonicalName) {
        if (Objects.isNull(canonicalName) || canonicalName.trim().isEmpty()) {
            return null;
        }

        int i = canonicalName.lastIndexOf('.');
        return (i == -1) ? canonicalName : canonicalName.substring(0, i);
    }

    // process all class-file to class-name of package

    private static Set<String> getClassNames(String packageName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = getClassPathName(packageName);
        URL url = loader.getResource(packagePath);
        if (Objects.nonNull(url)) {
            String protocol = url.getProtocol();
            if (FILE_TYPE.equals(protocol)) {
                return getClassNamesFromDir(url.getPath(), packageName);
            } else if (JAR_TYPE.equals(protocol)) {
                try {
                    JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                    return getClassNamesFromJar(jarFile.entries(), packageName);
                } catch (Exception ignore) {}
            }
        } else {
            return getClassNamesFromJars(((URLClassLoader) loader).getURLs(), packageName);
        }

        return Collections.emptySet();
    }

    private static Set<String> getClassNamesFromDir(String filePath, String packageName) {
        Set<String> result = new HashSet<>();
        File[] files = (new File(filePath)).listFiles();
        if (Objects.isNull(files)) {
            return result;
        }

        for (File childFile : files) {
            if (childFile.isDirectory()) {
                result.addAll(getClassNamesFromDir(childFile.getPath(), packageName + '.' + childFile.getName()));
            } else {
                if (isClassFile(childFile.getName())) {
                    result.add(packageName + '.' + getClassName(childFile.getName()));
                }
            }
        }

        return result;
    }

    private static Set<String> getClassNamesFromJars(URL[] urls, String packageName) {
        Set<String> result = new HashSet<>();
        for (URL url : urls) {
            String classPath = url.getPath();
            if (classPath.endsWith(CLASS_OUTPUT_FOLDER)) {
                continue;
            }

            try {
                JarFile jarFile = new JarFile(classPath.substring(classPath.indexOf("/")));
                result.addAll(getClassNamesFromJar(jarFile.entries(), packageName));
            } catch (IOException ignore) {}
        }

        return result;
    }

    private static Set<String> getClassNamesFromJar(Enumeration<JarEntry> jarEntries, String packageName) {
        Set<String> result = new HashSet<>();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            if (!jarEntry.isDirectory()) {
                if (isClassFile(jarEntry.getName())) {
                    String entryName = getClassPackageName(jarEntry.getName());
                    if (entryName.startsWith(packageName)) {
                        result.add(getClassName(entryName));
                    }
                }
            }
        }

        return result;
    }

    private static String getClassPackageName(String pathName) {
        return StringUtils.replace(pathName, "/", ".");
    }

    private static String getClassPathName(String packageName) {
        return StringUtils.replace(packageName, ".", "/");
    }

    private static boolean isClassFile(String fileName) {
        return fileName.endsWith(CLASS_FILE) && !fileName.contains("$");
    }

    private static String getClassName(String fileName) {
        return StringUtils.replace(fileName, CLASS_FILE, "");
    }
}

package org.beeborframework.core.resource;

import lombok.SneakyThrows;
import org.beeborframework.core.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Environment
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 21:06
 */
public class Environment {

    /*** default properties file path */
    public static final String DEFAULT_PROPERTIES_FILE = "application.properties";

    /*** default server-port key */
    public static final String SERVER_PORT_KEY = "server.port";

    /*** application-context key */
    public static final String APPLICATION_CONTEXT_KEY = "server.application.context";

    /*** default port */
    public static final int DEFAULT_PORT = 8080;

    /*** default root resource path */
    public static final String DEFAULT_RESOURCE_PATH = "";

    private final Class<?> appClass;

    private final Properties properties;


    private Environment(Class<?> appClass) {
        this.appClass = appClass;
        this.properties = getDefaultProperties();
    }

    public static Environment getInstance(Class<?> appClass) {
        return new Environment(appClass);
    }

    public Class<?> getAppClass() {
        return appClass;
    }

    public int getServerPort() {
        return Integer.parseInt(getProperty(SERVER_PORT_KEY));
    }

    public String getApplicationContext() {
        return getProperty(APPLICATION_CONTEXT_KEY);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @SneakyThrows(IOException.class)
    private Properties getDefaultProperties() {
        Properties result = new Properties();
        // Load system env
        System.getenv().forEach(result::put);
        // Load default properties
        setDefaultEnv(result);
        // Load properties file env
        try (Reader reader = getDefaultPropertiesReader()) {
            result.load(reader);
            return result;
        }
    }

    private Reader getDefaultPropertiesReader() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE);
        Assert.notNull(is, "Cannot loaded default properties");
        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }

    private void setDefaultEnv(Properties properties) {
        // Set server port
        properties.put(SERVER_PORT_KEY, String.valueOf(DEFAULT_PORT));
        // Set application context
        properties.put(APPLICATION_CONTEXT_KEY, DEFAULT_RESOURCE_PATH);
    }
}

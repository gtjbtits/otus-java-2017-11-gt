package com.jbtits.otus.lecture16.ms.config;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

abstract public class MSConfiguration {
    private final String propertiesFileName;
    private final Properties properties;

    protected MSConfiguration(String propertiesFileName) {
        properties = new Properties();
        this.propertiesFileName = propertiesFileName;
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(propertiesFileName);
        if (stream == null) {
            throw new RuntimeException("Can\'t find properties file " + propertiesFileName);
        }
        try {
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getString(String key) {
        if (!getProperties().containsKey(key)) {
            throw new RuntimeException("Properties file " + propertiesFileName + " has no key " + key);
        }
        return (String) getProperties().get(key);
    }

    protected int getInt(String key) {
        return Integer.valueOf(getString(key));
    }

    protected Properties getProperties() {
        return properties;
    }
}

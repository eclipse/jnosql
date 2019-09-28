package org.eclipse.jnosql.artemis.configuration.keyvalue;

import jakarta.nosql.Settings;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.BucketManagerFactory;
import jakarta.nosql.keyvalue.KeyValueConfiguration;
import jakarta.nosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.artemis.configuration.ConfigurationException;
import org.eclipse.jnosql.artemis.configuration.SettingsConverter;
import org.eclipse.jnosql.artemis.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * Converter the {@link String} to {@link BucketManagerFactory} it will use the {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link KeyValueConfiguration}
 */
public class BucketManagerFactoryConverter implements Converter<BucketManagerFactory> {

    @Override
    public BucketManagerFactory convert(String value) {
        final SettingsConverter settingsConverter = BeanManagers.getInstance(SettingsConverter.class);
        Config config = BeanManagers.getInstance(Config.class);
        final Settings settings = settingsConverter.convert(value);
        String provider = value + ".provider";
        final Class<?> bucketClass = config.getValue(provider, Class.class);
        if (KeyValueConfiguration.class.isAssignableFrom(bucketClass)) {
            final Reflections reflections = BeanManagers.getInstance(Reflections.class);
            final KeyValueConfiguration configuration = (KeyValueConfiguration) reflections.newInstance(bucketClass);
            return configuration.get(settings);

        }
        throw new ConfigurationException("The class " + bucketClass + " is not valid to " + KeyValueConfiguration.class);
    }
}


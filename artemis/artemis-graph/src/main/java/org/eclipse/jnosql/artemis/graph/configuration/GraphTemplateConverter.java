package org.eclipse.jnosql.artemis.graph.configuration;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.artemis.configuration.SettingsConverter;
import org.eclipse.jnosql.artemis.graph.GraphTemplate;
import org.eclipse.jnosql.artemis.graph.GraphTemplateProducer;
import org.eclipse.jnosql.artemis.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * Converter the {@link String} to {@link GraphTemplate} it will use the {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link org.eclipse.jnosql.artemis.graph.GraphConfiguration}
 */
public class GraphTemplateConverter implements Converter<GraphTemplate> {

    @Override
    public GraphTemplate convert(String value) {

        Config config = BeanManagers.getInstance(Config.class);
        final Graph manager = config.getValue(value, Graph.class);
        GraphTemplateProducer producer = BeanManagers.getInstance(GraphTemplateProducer.class);
        return producer.get(manager);
    }
}

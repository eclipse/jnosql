package org.apache.diana.mongodb.document;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.async.client.MongoClients;
import com.mongodb.connection.ClusterSettings;
import org.apache.diana.api.document.DocumentCollectionManagerFactory;
import org.apache.diana.api.document.DocumentConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class MongoDBDocumentConfiguration implements DocumentConfiguration {

    private static final String FILE_CONFIGURATION = "diana-mongodb.properties";

    private static final Logger LOGGER = Logger.getLogger(MongoDBDocumentConfiguration.class.getName());

    private static final int DEFAULT_PORT = 27017;


    @Override
    public DocumentCollectionManagerFactory getManagerFactory(Map<String, String> configurations) {
        List<ServerAddress> servers = configurations.keySet().stream().filter(s -> s.startsWith("mongodb-server-host-")).map(configurations::get).map(HostPortConfiguration::new)
                .map(HostPortConfiguration::toServerAddress).collect(Collectors.toList());
        if (servers.isEmpty()) {
            return new MongoDBDocumentCollectionManagerFactory(new MongoClient(), MongoClients.create());
        }

        return new MongoDBDocumentCollectionManagerFactory(new MongoClient(servers), getAsyncMongoClient(servers));
    }

    private com.mongodb.async.client.MongoClient getAsyncMongoClient(List<ServerAddress> servers) {
        ClusterSettings clusterSettings = ClusterSettings.builder().hosts(servers).build();
        MongoClientSettings settings = MongoClientSettings.builder().clusterSettings(clusterSettings).build();
        return MongoClients.create(settings);
    }


    @Override
    public DocumentCollectionManagerFactory getManagerFactory() {
        try {
            Properties properties = new Properties();
            InputStream stream = MongoDBDocumentConfiguration.class.getClassLoader().getResourceAsStream(FILE_CONFIGURATION);
            properties.load(stream);
            Map<String, String> collect = properties.keySet().stream().collect(Collectors.toMap(Object::toString, s -> properties.get(s).toString()));
            return getManagerFactory(collect);
        } catch (IOException e) {
            LOGGER.warning("The file " + FILE_CONFIGURATION + " was not found using default configuration");
            return getManagerFactory(Collections.emptyMap());
        }
    }

    private class HostPortConfiguration {


        private final String host;

        private final int port;

        HostPortConfiguration(String value) {
            String[] values = value.split(":");
            if (values.length == 2) {
                host = values[0];
                port = Integer.valueOf(values[1]);
            } else {
                host = values[0];
                port = DEFAULT_PORT;
            }
        }

        public ServerAddress toServerAddress() {
            return new ServerAddress(host, port);
        }
    }
}

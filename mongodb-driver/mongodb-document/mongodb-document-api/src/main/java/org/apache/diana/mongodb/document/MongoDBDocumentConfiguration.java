package org.apache.diana.mongodb.document;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.apache.diana.api.document.DocumentCollectionManagerFactory;
import org.apache.diana.api.document.DocumentConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;


public class MongoDBDocumentConfiguration implements DocumentConfiguration {

    private static final String FILE_CONFIGURATION = "diana-mongodb.properties";

    private static final int DEFAULT_PORT = 27017;


    @Override
    public DocumentCollectionManagerFactory getManagerFactory(Map<String, String> configurations) {
        List<ServerAddress> servers = configurations.keySet().stream().filter(s -> s.startsWith("mongodb-server-host-")).map(configurations::get).map(HostPortConfiguration::new)
                .map(HostPortConfiguration::toServerAddress).collect(Collectors.toList());
        if (servers.isEmpty()) {
            return new MongoDBDocumentCollectionManagerFactory(new MongoClient());
        }
        MongoClient mongoClient = new MongoClient(servers);
        return new MongoDBDocumentCollectionManagerFactory(mongoClient);
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
            throw new RuntimeException("Cannot found " + FILE_CONFIGURATION + " file");
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

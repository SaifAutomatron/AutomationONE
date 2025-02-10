package Utilities.Database;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import Utilities.Excel.EnvironmentData;
import lombok.SneakyThrows;

public class MongoDB {

    private static MongoDB instance;
    private final HashMap<String, String> envMap;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private final String mongoHost;
    private final String databaseName;
    private final String user;
    private final String password;

    @SneakyThrows
    private MongoDB() {
        envMap = EnvironmentData.getInstance().getEnvironmentDataMap();
        mongoHost = envMap.get("MONGO_HOST");  
        databaseName = envMap.get("MONGO_DATABASE");
        user = envMap.get("MONGO_USER");
        password = envMap.get("MONGO_PASSWORD");
    }

    private void connect() {
        if (mongoClient == null) {
            String mongoUri;
            if (user != null && password != null && !user.isEmpty() && !password.isEmpty()) {
                mongoUri = String.format("mongodb://%s:%s@%s/%s?authSource=admin",
                        user, password, mongoHost, databaseName);
            } else {
                mongoUri = String.format("mongodb://%s/%s", mongoHost, databaseName);
            }

            mongoClient = MongoClients.create(mongoUri);
            database = mongoClient.getDatabase(databaseName);
        }
    }

    public static synchronized MongoDB getInstance() {
        if (instance == null) {
            instance = new MongoDB();
        }
        return instance;
    }

    @SneakyThrows
    public String getDocument(String collectionName, String documentID) {
        connect();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document doc = collection.find(Filters.eq("_id", documentID)).first();
        return doc != null ? doc.toJson() : null;
    }

    @SneakyThrows
    public void createDocument(String collectionName, Document document) {
        connect();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.insertOne(document);
    }

    @SneakyThrows
    public void deleteDocument(String collectionName, String documentID) {
        connect();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.deleteOne(Filters.eq("_id", documentID));
    }

    @SneakyThrows
    public List<Document> runQuery(String collectionName, Document queryFilter) {
        connect();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        List<Document> results = new ArrayList<>();
        collection.find(queryFilter).into(results);
        return results;
    }

    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
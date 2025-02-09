package Utilities.Database;

import java.util.HashMap;

import com.couchbase.client.java.*;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.InsertOptions;
import com.couchbase.client.java.query.QueryResult;

import Utilities.Excel.EnvironmentData;
import lombok.SneakyThrows;

public class CouchBase {

    private static CouchBase instance;
    private final HashMap<String, String> envMap;
    private Cluster cluster;
    private Bucket bucket;
    private Collection collection;
    private final String cbHost;
    private final String user;
    private final String password;

    @SneakyThrows
    private CouchBase() {
        envMap = EnvironmentData.getInstance().getEnvironmentDataMap();
        cbHost = envMap.get("CB_HOST");
        user = envMap.get("CB_USER");
        password = envMap.get("CB_PASSWORD");
    }

    public static synchronized CouchBase getInstance() {
        if (instance == null) {
            instance = new CouchBase();
        }
        return instance;
    }

    private void connect() {
        if (cluster == null) {
            cluster = Cluster.connect(cbHost, ClusterOptions.clusterOptions(user, password));
        }
    }

    @SneakyThrows
    public String getDocument(String bucketName, String documentID) {
        connect();
        bucket = cluster.bucket(bucketName);
        collection = bucket.defaultCollection();
        GetResult data = collection.get(documentID);
        return data.contentAsObject().toString();
    }

    @SneakyThrows
    public void createDocument(String bucketName, String docID, Object content) {
        connect();
        bucket = cluster.bucket(bucketName);
        collection = bucket.defaultCollection();
        collection.insert(docID, content, InsertOptions.insertOptions());
    }

    @SneakyThrows
    public void deleteDocument(String bucketName, String docID) {
        connect();
        bucket = cluster.bucket(bucketName);
        collection = bucket.defaultCollection();
        collection.remove(docID);
    }

    @SneakyThrows
    public String runN1QLQuery(String query) {
        connect();
        QueryResult result = cluster.query(query);
        return result.rowsAsObject().toString();
    }

    public void disconnect() {
        if (cluster != null) {
            cluster.disconnect();
        }
    }
}
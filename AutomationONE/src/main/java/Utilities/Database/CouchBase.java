package Utilities.Database;

import java.util.HashMap;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.InsertOptions;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.QueryResult;

import Utilities.Excel.EnvironmentData;
import lombok.SneakyThrows;

public class CouchBase {

	static CouchBase d;
	HashMap<String, String> envMap;
	String jsonString;
	Cluster cluster = null;
	Bucket bucket=null;
	Collection collection=null;
	String cbHost=null;
	String User=null;
	String Password=null;

	@SneakyThrows
	private CouchBase()
	{
		envMap = EnvironmentData.getInstance().getEnvironmentDataMap();
	}

	public static synchronized CouchBase getInstance()
	{
		if(d==null)
		{
			d=new CouchBase();
		}
		else
			return d;

		return d;
	}

	@SneakyThrows
	public String getDocument(String bucketName,String documentID) {
		
	cbHost=envMap.get("CB_HOST");
	User=envMap.get("CB_USER");
	Password=envMap.get("CB_PASSWORD");

		try {
			cluster = Cluster.connect(cbHost,User, Password);

			bucket = cluster.bucket(bucketName);

			collection = bucket.defaultCollection();

			GetResult data = collection.get(documentID);

			jsonString = data.contentAsObject().toString();

		} 

		finally
		{
			if(cluster!=null)
				cluster.disconnect();
		}

		return jsonString;
	}

	@SneakyThrows
	public void createDocument(String bucketName,String docID,Object content)
	{
		cbHost=envMap.get("CB_HOST");
		User=envMap.get("CB_USER");
		Password=envMap.get("CB_PASSWORD");
		try {
			cluster = Cluster.connect(cbHost,cbHost, Password);

			bucket = cluster.bucket(bucketName);
			
			collection = bucket.defaultCollection();

			collection.insert(docID, content, InsertOptions.insertOptions());
			
		}
		finally
		{
			if(cluster!=null)
				cluster.disconnect();
		}
	}
	
	
	@SneakyThrows
	public void deleteDocument(String bucketName,String docID)
	{
		cbHost=envMap.get("CB_HOST");
		User=envMap.get("CB_USER");
		Password=envMap.get("CB_PASSWORD");
		
		try {
			cluster = Cluster.connect(cbHost,User, Password);

			bucket = cluster.bucket(bucketName);
			
			collection = bucket.defaultCollection();

			collection.remove(docID);

		}
		finally
		{
			if(cluster!=null)
				cluster.disconnect();
		}
	}

	@SneakyThrows
	public String runN1QLQuery(String query)
	{
		cbHost=envMap.get("CB_HOST");
		User=envMap.get("CB_USER");
		Password=envMap.get("CB_PASSWORD");
		QueryResult result=null;
		try {
			cluster = Cluster.connect(cbHost,User,Password);

			result= cluster.query(query);
		}
		finally
		{
			if(cluster!=null)
				cluster.disconnect();
		}

		return  result.rowsAsObject().toString();
	}


}

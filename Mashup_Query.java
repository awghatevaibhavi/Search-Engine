/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vaibhavi
 */
public class Mashup_Query {
    
	//function querying database based on updated year
    static ArrayList<String> queryBasedOnYear(DBCollection dbCollection, String year) {
        BasicDBObject updatedYear = new BasicDBObject();
        updatedYear.put("updated", new BasicDBObject("$regex", year));
        DBCursor dc = dbCollection.find(updatedYear);
        ArrayList<String> result = new ArrayList<String>();
        while(dc.hasNext()){
            BasicDBObject res = (BasicDBObject) dc.next(); 
            result.add(res.getString("name"));
        }
        return result;
    }
    
    //function querying database based on tags 
    static ArrayList<String> queryBasedOnTags(DBCollection dbCollection, String tags) {
        BasicDBObject query1 = new BasicDBObject();
        List<BasicDBObject> query1_list = new ArrayList<BasicDBObject>();
        String[] tags_array = tags.split(",");
        for(int index = 0; index < tags_array.length; index++){
            query1_list.add(new BasicDBObject("Tags",tags_array[index]));
        }
        query1.put("$and", query1_list);
        DBCursor dc = dbCollection.find(query1);
        ArrayList<String> result = new ArrayList<String>();
        while(dc.hasNext()){
            BasicDBObject res = (BasicDBObject) dc.next(); 
            result.add(res.getString("name"));
        }
        return result;
    }
    
    //function querying database based on used APIs 
    static ArrayList<String> queryBasedOnApis(DBCollection dbCollection, String apis) {
    	BasicDBObject query1 = new BasicDBObject();
        List<BasicDBObject> query1_list = new ArrayList<BasicDBObject>();
    	String[] apis_array = apis.split(",");
        for(int index = 0; index < apis_array.length; index++){
            query1_list.add(new BasicDBObject("apis.api",apis_array[index]));
        }
        query1.put("$and", query1_list);
        DBCursor dc = dbCollection.find(query1);
        ArrayList<String> result = new ArrayList<String>();
        while(dc.hasNext()){
            BasicDBObject res = (BasicDBObject) dc.next(); 
            result.add(res.getString("name"));
        }
        return result;
    }
    
    //function querying database based on keywords 
    static ArrayList<String> queryBasedOnKeywords(DBCollection dbCollection, String keywords) {
        BasicDBObject keyword = new BasicDBObject();
        List<BasicDBObject> list_obj = new ArrayList<BasicDBObject>();
        String[] keywords_array = keywords.split(",");
        for(int index = 0; index < keywords_array.length; index++){
            list_obj.add(new BasicDBObject("summary", new BasicDBObject("$regex",keywords_array[index])));
            list_obj.add(new BasicDBObject("description", new BasicDBObject("$regex",keywords_array[index])));
            list_obj.add(new BasicDBObject("title", new BasicDBObject("$regex",keywords_array[index])));
        }
        keyword.put("$and", list_obj);
        DBCursor dc = dbCollection.find(keyword);
        ArrayList<String> result = new ArrayList<String>();
        while(dc.hasNext()){
            BasicDBObject res = (BasicDBObject) dc.next(); 
            result.add(res.getString("name"));
        }
        return result;
    }
}

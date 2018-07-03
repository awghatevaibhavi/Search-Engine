/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import java .io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 *
 * @author Vaibhavi
 */
public class Mashup_parser {
    // This function parses the mashup.txt file
    public static void main(String[] args) throws FileNotFoundException, IOException{
        //connection to mongodb
        DB db = (new MongoClient("localhost", 27017).getDB("local"));
        DBCollection dbCollection = db.getCollection("myChannel_mashup");
        FileReader reader = new FileReader("C:\\Users\\Vaibhavi\\Documents\\Spring 17\\Web services\\PA3\\mashup.txt");
        BufferedReader br = new BufferedReader(reader);
        String line;
        String[] mashup_headers = {"id","title","summary","rating","name","label","author","description","type","downloads","useCount","sampleUrl","dateModified","numComments","commentsUrl","Tags","apis","updated"};
        JSONObject mashup_obj = new JSONObject();
        String mashup_obj_string = null;
        while((line = br.readLine())!= null){
            //function call to parse the mashup file
            mashup_obj = parseMashupFile(mashup_obj, line, mashup_headers);
            mashup_obj_string = mashup_obj.toString();
            //JSON objects are being inserted into MongoDB
            dbCollection.insert((DBObject) JSON.parse(mashup_obj_string));
        }
    }

    private static JSONObject parseMashupFile(JSONObject mashup_obj, String line, String[] mashup_headers) {
    	//every passed line is split on $#$
    	String [] temp = line.split("\\$\\#\\$");
        
    	//if value is null, nothing is added to that particular field in JSON object
        for (int index = 0; index < temp.length; index++){
            if(temp[index].length() == 0){
                mashup_obj.put(mashup_headers[index],"");
            }
            else{
            	//fields for apis and urls are parsed
                String[] subelement = temp[index].split("\\#\\#\\#");
                JSONArray temp_tags = new JSONArray();
                JSONArray temp_apis = new JSONArray();
                for(int i = 0; i < subelement.length; i++){
                    String[] innerelement = subelement[i].split("\\$\\$\\$");
                    if(innerelement.length == 1){
                        temp_tags.add(innerelement[0]);
                    }
                    if(innerelement.length == 2){
                        JSONObject temp_obj = new JSONObject();
                        temp_obj.put("api", innerelement[0]);
                        temp_apis.add(temp_obj);
                        temp_obj.put("url", innerelement[1]);
                        temp_apis.add(temp_obj);
                    }
                }
                if(temp_tags.size() > 0){
                    mashup_obj.put(mashup_headers[index], temp_tags);
                }
                if(temp_apis.size() > 0){
                    mashup_obj.put(mashup_headers[index], temp_apis);
                }
            }
        }
        return mashup_obj;
    }
}

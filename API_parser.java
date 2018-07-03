
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Vaibhavi
 */
public class API_parser {
    public static void main(String[] args) throws FileNotFoundException, IOException{
    	//Establishing connection to database
        DB db = (new MongoClient("localhost", 27017).getDB("local"));
        DBCollection dbCollection = db.getCollection("myChannel_API");
        
        //reading input file
        FileReader reader = new FileReader("C:\\Users\\Vaibhavi\\Documents\\Spring 17\\Web services\\PA3\\api.txt");
        BufferedReader br = new BufferedReader(reader);
        String line;
        
        //specifying headers for JSON objcet
        String[] headers = {"id","title","summary","rating","name","label","author","description","type","downloads","useCount","sampleUrl","downloadUrl","dateModified","remoteFeed","numComments","commentsUrl","Tags","category","protocols","serviceEndpoint","version","wsdl","data formats","apigroups","example","clientInstall","authentication","ssl","readonly","VendorApiKits","CommunityApiKits","blog","forum","support","accountReq","commercial","provider","managedBy","nonCommercial","dataLicensing","fees","limits","terms","company","updated"};
        JSONObject API_obj = new JSONObject();
        String API_obj_string;
        
        //for every line, parsing function is called ans is inserted in database
        while((line = br.readLine())!= null){
            
            API_obj = parseAPIFile(API_obj, line, headers);
            API_obj_string = API_obj.toString();
            dbCollection.insert((DBObject) JSON.parse(API_obj_string));
        }
        
    }

    private static JSONObject parseAPIFile(JSONObject obj, String line, String[] headers) throws IOException {
        //every paased line is split on $#$
    	String [] temp = line.split("\\$\\#\\$");
        
    	//if value is null, nothing is added to that particular field in JSON object
        for (int index = 0; index < temp.length; index++){
            if(temp[index].length() == 0){
                obj.put(headers[index],"");
            }
            else{
            	//else th string is split on ### 
                String[] subelement = temp[index].split("\\#\\#\\#");
                //if field is not the tags field, it is added as it is to JSON object
                if(subelement.length == 1){
                    String substring = subelement[0];
                    obj.put(headers[index],substring);
                }
                else{
                	//else, multiple tags are added to same tags fields
                    JSONArray temp_array = new JSONArray();
                    for(String substring: subelement){
                        temp_array.add(substring);
                    }
                    obj.put(headers[index], temp_array);
                }
            }
        }
      return obj;  
    }

}

package api;
// Import Restlet Methods and Resources
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

// Import Driver for Postgres Connection
import java.io.IOException;
import java.rmi.server.ExportException;
import java.sql.*;


// Import Gson for Json Objects
import com.google.gson.*;

public class KnowledgeResource extends ServerResource {

    String postgreUser = "postgres";
    String postgrePwd = "1234";
    String postgreDatabase = "ka";
    String connection = "jdbc:postgresql://localhost:5432/" + postgreDatabase + "?user=" + postgreUser +
            "&password=" + postgrePwd;
    Connection c;


    @Get
    public String get_knowledge_by_id() {

        int id = Integer.parseInt(getAttribute("id"));
        String getquery =  "SELECT * FROM public.\"Knowledge\" where \"Base_Knowledge_ID\" = " + id + ";";
        JsonObject object = new JsonObject();

        try {

            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(connection);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(getquery);

            if (rs.next()) {
                String base_knowledge_id = rs.getString("Base_Knowledge_ID");
                String parent_knowledge_id = rs.getString("Parent_Knowledge_ID");
                String knowledge_id = rs.getString("Knowledge_ID");
                int object_id = rs.getInt("Object_ID");
                String user_id = rs.getString("User_ID");
                String state_id = rs.getString("Sate_ID");
                String knowledge_acceptance = rs.getString("Knowledge_Acceptance");
                String knowledge_rating = rs.getString("Knowledge_Rating");
                String knowledge_data = rs.getString("Knowledge_Data");
                String knowledge_type = rs.getString("Knowledge_Type");
                String knowledge_status = rs.getString("Knowledge_Status");



                String out = "{knowledge_id : " + knowledge_id +
                        "base_knowledge_id : " + base_knowledge_id +
                        "parent_knowledge_id : " + parent_knowledge_id +
                        ",object_id : " + object_id +
                        ",user_id : " + user_id +
                        ",state_id : " + state_id +
                        ",knowledge_acceptance : " + knowledge_acceptance +
                        ",knowledge_rating : " + knowledge_rating +
                        ",knowledge_data : " + knowledge_data +
                        ",knowledge_type : " + knowledge_type +
                        ",knowledge_status : " + knowledge_status+ "}";

                object = (JsonObject) JsonParser.parseString(out);
            }
            else {
                return "No Objects found for this ID";
            }

        } catch (Exception e) {
            System.out.println(e);
            return "Error!";
        }
        return object.toString();
    }

    @Post
    public String post_knowledge(Representation representation) throws IOException{

        JsonRepresentation jsonRepresentation = new JsonRepresentation(representation);
        JSONObject jsonObject = jsonRepresentation.getJsonObject();

        String query = "INSERT INTO public.\"Knowledge\" VALUES ('" + jsonObject.getString("base_knowledge_id") + "','" +
                jsonObject.getString("parent_knowledge_id") + "','" + jsonObject.getString("knowledge_id") +
                "','" + jsonObject.getString("object_id") + "','" + jsonObject.getString("user_id")+ "','" +
                jsonObject.getString("state_id")+ "','"+jsonObject.getString("knowledge_acceptance") + "','"+
                jsonObject.getString("knowledge_rating") +"','"+ jsonObject.getString("knowledge_data") + "','" +
                jsonObject.getString("knowledge_status") + "')";

        try {

            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(connection);
            PreparedStatement pstmt = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return "Error Inserting Data";
            }

        } catch (Exception e) {

            System.out.println(e);
            return "Error!";

        }

        return "Successfully Inserted";

    }

    @Delete
    public void bye()
    {
        System.out.println("Bye Bye, DELETE method called");
    }
}


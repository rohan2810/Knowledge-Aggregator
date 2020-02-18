// Import Restlet Methods and Resources
package api;
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

public class KAResources extends ServerResource {

    String postgreUser = "postgres";
    String postgrePwd = "1234";
    String postgreDatabase = "ka";
    String connection = "jdbc:postgresql://localhost:5432/" + postgreDatabase + "?user=" + postgreUser +
            "&password=" + postgrePwd;
    Connection c;
    @Get
    public String pingcheck(){
        return "API is up and running";
    }
    @Get
    public String get_object() {

//        String postgreUser = "postgres";
//        String postgrePwd = "1234";
//        String postgreDatabase = "ka";
//
//        String connection = "jdbc:postgresql://localhost:5432/" + postgreDatabase + "?user=" + postgreUser +
//                            "&password=" + postgrePwd;
        String getquery =  "SELECT * FROM public.\"Object\";";
//        Connection c;
        JsonArray object_array = new JsonArray();

        try {

            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(connection);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(getquery);

            int objectID;
            String objectName;
            String objectType;
            String objectDescription;
            String out;

            while (rs.next()) {
                objectID = rs.getInt("Object_ID");
                objectName = rs.getString("Object_Name");
                objectType = rs.getString("Object_Type");
                objectDescription = rs.getString("Object_Description");

                out = "{Object_ID : " + objectID + ",Object_Name : " + objectName + ",Object_Type : " + objectType +
                        ",Object_Description : " + objectDescription + "}";

                object_array.add(JsonParser.parseString(out));
            }

        } catch (Exception e) {

            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
            return "Postgres connection failed!";

        }

        return object_array.toString();
    }

    @Post
    public String post_object(Representation representation) throws IOException{

        JsonRepresentation jsonRepresentation = new JsonRepresentation(representation);
        JSONObject jsonObject = jsonRepresentation.getJsonObject();

        String query = "INSERT INTO public.\"Object\" VALUES ('" + jsonObject.getString("ID") + "','" +
                        jsonObject.getString("Name") + "','" + jsonObject.getString("Type") +
                        "','" + jsonObject.getString("Description") + "')";

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


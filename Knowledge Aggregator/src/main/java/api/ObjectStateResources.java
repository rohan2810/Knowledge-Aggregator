package api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.sql.*;

public class ObjectStateResources extends ServerResource {

    String postgreUser = "postgres";
    String postgrePwd = "1234";
    String postgreDatabase = "ka";
    String connection = "jdbc:postgresql://localhost:5432/" + postgreDatabase + "?user=" + postgreUser +
            "&password=" + postgrePwd;
    Connection c;

    @Get
    public String get_object_state_by_id() {

        int id = Integer.parseInt(getAttribute("id"));
        String getquery =  "SELECT * FROM public.\"Object_State\" where \"Object_ID\" = " + id + ";";
        JsonObject object = new JsonObject();

        try {

            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(connection);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(getquery);

            if (rs.next()) {
                int objectID = rs.getInt("Object_ID");
                String objectStatus = rs.getString("Object_Status");
                String objectSignature = rs.getString("Object_Signature");

                String out = "{Object_ID : " + objectID + ",Object_Status : " + objectStatus + ",Object_Signature : "
                                + objectSignature + "}";

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
    public String post_object_state(Representation representation) throws IOException {

        JsonRepresentation jsonRepresentation = new JsonRepresentation(representation);
        JSONObject jsonObject = jsonRepresentation.getJsonObject();

        String query = "INSERT INTO public.\"Object_State\" VALUES ('" + jsonObject.getString("ID") + "','" +
                jsonObject.getString("Status") + "','" + jsonObject.getString("Signature") +
                "','" + jsonObject.getString("Object_ID") + "')";

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

}

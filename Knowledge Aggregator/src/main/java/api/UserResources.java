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

public class UserResources extends ServerResource {

    String postgreUser = "postgres";
    String postgrePwd = "1234";
    String postgreDatabase = "ka";
    String connection = "jdbc:postgresql://localhost:5432/" + postgreDatabase + "?user=" + postgreUser +
            "&password=" + postgrePwd;
    Connection c;

    @Get
    public String get_user_by_id() {

        Character id = getAttribute("id").charAt(0);
        String getquery =  "SELECT * FROM public.\"User\" where \"User_ID\" = '" + id + "';";
        JsonObject object = new JsonObject();

        try {

            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(connection);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(getquery);

            if (rs.next()) {
                int objectID = rs.getInt("User_ID");
                String userRole = rs.getString("User_Role");
                String userDetails = rs.getString("User_Details");

                String out = "{User_ID : " + objectID + ",User_Role : " + userRole + ",User_Details : "
                        + userDetails + "}";

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
    public String post_user(Representation representation) throws IOException {

        JsonRepresentation jsonRepresentation = new JsonRepresentation(representation);
        JSONObject jsonObject = jsonRepresentation.getJsonObject();

        String query = "INSERT INTO public.\"User\" VALUES ('" + jsonObject.getString("ID") + "','" +
                jsonObject.getString("Role") + "','" + jsonObject.getString("Details") + "')";

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

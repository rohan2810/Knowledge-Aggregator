// Import Restlet Methods and Resources
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

// Import Driver for Postgres Connection
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;;

// Import Gson for Json Objects
import com.google.gson.*;

public class KAResources extends ServerResource
{
    @Get
    public String hello()
    {
        String postgreUser = "postgres";
        String postgrePwd = "1234";
        String postgreDatabase = "ka";

        String connection = "jdbc:postgresql://localhost:5432/" + postgreDatabase + "?user=" + postgreUser +
                            "&password=" + postgrePwd;
        String getquery =  "SELECT * FROM public.\"Object\";";
        Connection c;
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

    @Delete
    public void bye()
    {
        System.out.println("Bye Bye, DELETE method called");
    }
}


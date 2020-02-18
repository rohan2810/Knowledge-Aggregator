import org.restlet.Server;
import org.restlet.data.Protocol;

public class KAServer {

    public static void main(String[] args) throws Exception {

        Server server = new Server(Protocol.HTTP, 8080, KAResources.class);
        server.start();
    }
}
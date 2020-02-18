
package api;

import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.*;

public class KAServer extends Application
{
    public static void startup()
    {
        try {
            Component server = new Component() ;
            server.getServers().add(Protocol.HTTP, 8080) ;
            server.getDefaultHost().attach(new KAServer()) ;
            server.start() ;
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public Restlet createInboundRoot()
    {
        Router router = new Router(getContext()) ;
        router.attach( "/", KAResources.class ) ;
        router.attach( "/feedback", KAResources.class ) ;

        return router;
    }
}
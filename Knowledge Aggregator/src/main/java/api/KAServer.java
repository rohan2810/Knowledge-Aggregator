
package api;

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
        router.attach( "/objects", ObjectResources.class ) ;
        router.attach( "/object/{id}", ObjectResources.class ) ;
        router.attach( "/object", ObjectResources.class ) ;
        router.attach( "/object_state/{id}", ObjectStateResources.class ) ;
        router.attach( "/object_state", ObjectStateResources.class ) ;
        router.attach( "/user/{id}", UserResources.class ) ;
        router.attach( "/user", UserResources.class ) ;
        router.attach( "/feedback", ObjectResources.class ) ;

        return router;
    }
}
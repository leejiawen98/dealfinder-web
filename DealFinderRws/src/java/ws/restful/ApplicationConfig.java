package ws.restful;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;



@javax.ws.rs.ApplicationPath("Resources")
public class ApplicationConfig extends Application
{
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        
        resources.add(MultiPartFeature.class);
        
        return resources;
    }

    
    
    private void addRestResourceClasses(Set<Class<?>> resources){
        resources.add(ws.restful.AdminResource.class);
        resources.add(ws.restful.BusinessResource.class);
        resources.add(ws.restful.CategoryResource.class);
        resources.add(ws.restful.CorsFilter.class);
        resources.add(ws.restful.CreditCardResource.class);
        resources.add(ws.restful.CustomerResource.class);
        resources.add(ws.restful.DealResource.class);
        resources.add(ws.restful.FavouriteResource.class);
        resources.add(ws.restful.RedemptionResource.class);
        resources.add(ws.restful.ReviewResource.class);
        resources.add(ws.restful.SaleTransactionResource.class);
        resources.add(ws.restful.TagResource.class);
    }    
}

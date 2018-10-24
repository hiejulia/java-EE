/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.steeplesoft.monumentum.model.User;
import com.steeplesoft.monumentum.mongo.Collection;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.HttpHeaders;
import org.bson.Document;


@RequestScoped
public class UserProducer {

    @Inject
    private KeyGenerator keyGenerator;
    @Inject
    HttpServletRequest req;
    @Inject
    @Collection("users")
    private MongoCollection<Document> users;
    // colllections : user 

    // Get user 
    @Produces
    public User getUser() {
        String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.contains("Bearer")) {
            String token = authHeader.substring("Bearer".length()).trim();
            Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(keyGenerator.getKey()).parseClaimsJws(token);
            return getUser(parseClaimsJws.getBody().getSubject());
        } else {
            return null;
        }
    }
    /**
     * 
     * + String authHeader = req.getHeader 
     * + Jws - 
     * return getUser
     * 
     */


    private User getUser(String email) {
        // Doc - users.find - email- first()
        Document doc = users.find(new BasicDBObject("email", email)).first();
        if (doc != null) {
            return new User(doc);// retrun 
        } else {
            return null;
        }
    }
    
}

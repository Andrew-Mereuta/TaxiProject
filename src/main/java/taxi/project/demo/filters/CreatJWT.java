package taxi.project.demo.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import taxi.project.demo.entities.Client;
import taxi.project.demo.serializers.ClientSerializer;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Map;

public class CreatJWT {

    private JWTInfo jwtInfo = new JWTInfo();

    public String createJWT(Client client) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Client.class, new ClientSerializer());
        mapper.registerModule(module);
        String payload = mapper.writeValueAsString(client);

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        SecretKey secretKey = Keys.hmacShaKeyFor(jwtInfo.getSecretKey().getBytes());

        String jws = Jwts.builder()
                            .setHeader(Map.of("alg", "HmacSHA256"))
                            .setPayload(payload)
                            .signWith(secretKey)
                            .compact();
        return "Bearer " + jws;
    }
}
//    Item myItem = new Item(1, "theItem", new User(2, "theUser"));
//    ObjectMapper mapper = new ObjectMapper();
//
//    SimpleModule module = new SimpleModule();
//    module.addSerializer(Item.class, new ItemSerializer());
//    mapper.registerModule(module);
//
//    String serialized = mapper.writeValueAsString(myItem);
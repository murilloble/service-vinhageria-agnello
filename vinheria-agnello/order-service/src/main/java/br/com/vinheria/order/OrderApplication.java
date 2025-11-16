package br.com.vinheria.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}

@RestController
class OrderController {
    private static final String SECRET = System.getenv().getOrDefault("JWT_SECRET", "vinheria-secret-key-which-is-long-enough");
    private static final String PRODUCT_URL = System.getenv().getOrDefault("PRODUCT_SERVICE_URL","http://product-service:3001");

    private void validate(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No token");
        }
        String token = authHeader.substring(7);
        Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes())).build().parseClaimsJws(token);
    }

    @GetMapping("/orders")
    public Map<String,Object> orders(@RequestHeader(value = "Authorization", required = false) String auth) {
        validate(auth);
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", auth);
        HttpEntity<String> ent = new HttpEntity<>(headers);
        ResponseEntity<List> resp = rt.exchange(PRODUCT_URL + "/products", HttpMethod.GET, ent, List.class);
        List items = resp.getBody();
        Map<String,Object> order = Map.of("id",42, "items", List.of(items.get(0)));
        return Map.of("orders", List.of(order));
    }
}

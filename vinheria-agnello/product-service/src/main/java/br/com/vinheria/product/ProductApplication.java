package br.com.vinheria.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.*;

@SpringBootApplication
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}

@RestController
class AuthController {
    private static final String SECRET = System.getenv().getOrDefault("JWT_SECRET", "vinheria-secret-key-which-is-long-enough");

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody Map<String,String> payload) {
        String user = payload.getOrDefault("user", "demo");
        Date now = new Date();
        Date exp = new Date(now.getTime() + 3600_000L); // 1h
        String token = Jwts.builder()
                .setSubject(user)
                .claim("role", "product")
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
        return Collections.singletonMap("token", token);
    }
}

@RestController
class ProductController {
    private static final String SECRET = System.getenv().getOrDefault("JWT_SECRET", "vinheria-secret-key-which-is-long-enough");

    private void validate(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No token"); 
        }
        String token = authHeader.substring(7);
        Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes())).build().parseClaimsJws(token);
    }

    @GetMapping("/products")
    public List<Map<String,Object>> products(@RequestHeader(value = "Authorization", required = false) String auth) {
        validate(auth);
        return List.of(
            Map.of("id",1,"name","Cabernet Sauvignon","price",89.9),
            Map.of("id",2,"name","Malbec Reserva","price",69.5)
        );
    }
}

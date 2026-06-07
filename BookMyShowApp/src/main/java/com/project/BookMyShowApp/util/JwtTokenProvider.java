package com.project.BookMyShowApp.util;

import com.project.BookMyShowApp.model.Role;
import com.project.BookMyShowApp.model.User;
import com.project.BookMyShowApp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Autowired
    private UserRepository userRepository;
    private static final String SECRET_KEY_STRING="any-key#123wueoqueoqu9032oqeyuoqewewewefor-jwt3334";
    private final Key SECRET_KEY= Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

    @Autowired
    private UserDetailsService userDetailsService; // optional for auth reconstruction



    public String generateToken(User user)
    {
//        User user = userRepository.findByEmail(username).get();
        String roles = user.getRoles().stream()
                .map(Role::getName) // ROLE_USER etc
                .collect(Collectors.joining(","));

        String token= Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(SECRET_KEY)
                .compact();
        System.out.println("Generated Token"+token);
        return token;

    }


    public boolean validateToken(String token)
    {
        try{
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        }
        catch (ExpiredJwtException e)
        {
            System.out.println("Token Expired "+e.getMessage());
            return false;
        }
        catch (SignatureException e)
        {
            System.out.println("Invalid JWT Signature "+e.getMessage());
            return false;
        }
        catch (Exception e)
        {
            System.out.println("JWT exception "+e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
                .parseClaimsJws(token).getBody();

        String email = claims.getSubject();
        // load user details via your UserDetailsService
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Alternatively, build authorities from token:
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String roles = claims.get("roles", String.class);
        if (roles != null && !roles.isBlank()) {
            for (String role : roles.split(",")) {
                authorities.add(new SimpleGrantedAuthority(role.trim()));
            }
        }

        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    public String resolveToken(HttpServletRequest req) {
        String bearer = req.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
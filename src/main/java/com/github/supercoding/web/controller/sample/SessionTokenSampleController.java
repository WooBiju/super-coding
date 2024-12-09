package com.github.supercoding.web.controller.sample;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SessionTokenSampleController {

    @GetMapping("/set-session")
    public String setSession(HttpSession session) {
        session.setAttribute("user","조인성");
        session.setAttribute("gender","남자");
        session.setAttribute("job","배우");
        return "Session Set successfully";
    }

    @GetMapping("/set-session2")
    public String setSession2(HttpSession session) {
        session.setAttribute("user","차은우");
        session.setAttribute("gender","남자");
        session.setAttribute("job","배우");
        return "Session Set successfully";
    }

    @GetMapping("/get-session")
    public String getSession(HttpSession session) {
       String user = (String) session.getAttribute("user");
       String gender = (String) session.getAttribute("gender");
       String job = (String) session.getAttribute("job");
       return String.format("안녕하세요, 직업: %s 성별: %s인 %s 입니다.",job,gender,user);
    }

    @GetMapping("/generate-token")
    public String generateToken(HttpServletResponse response) {
        String jwt = Jwts.builder()
                .setSubject("token1")
                .claim("user","조인성")
                .claim("gender","남자")
                .claim("job","배우")
                .compact();
        response.addHeader("Token" , jwt);
        return "JWT set Successfully";

    }

    @GetMapping("/generate-token2")
    public String generateToken2(HttpServletResponse response) {
        String jwt = Jwts.builder()
                .setSubject("token1")
                .claim("user","차은우")
                .claim("gender","남자")
                .claim("job","배우")
                .compact();
        response.addHeader("Token" , jwt);
        return "JWT set Successfully";

    }

    @GetMapping("/show-token")
    public String showToken(@RequestHeader("Token") String token) {
        Claims claims = Jwts.parser()
                .parseClaimsJws(token)
                .getBody();

        String user = (String) claims.get("user");
        String gender = (String) claims.get("gender");
        String job = (String) claims.get("job");
        return String.format("안녕하세요, 직업: %s 성별: %s인 %s 입니다.",job,gender,user);
    }


}

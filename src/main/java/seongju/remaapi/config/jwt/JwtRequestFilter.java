package seongju.remaapi.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import seongju.remaapi.service.jwt.JwtUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        //request 헤더의 Authorization 가져오기
        final String requestTokenHeader =
                request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        //토큰, username 가져오기
        //JWT Token is in the form "Bearer token". RemoveBearer word and get
        //only the Token
        if(
                requestTokenHeader != null
                && requestTokenHeader.startsWith("Bearer ")
        ){
            jwtToken = requestTokenHeader.substring(7);
            try{
                //username 가져오기
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e){
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e){
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        //토큰 검사
        //Once we get the token validate it.
        if(
                username != null
                && SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            //if token is valid configure Spring Security to manually set
            //authentication
            if(jwtTokenUtil.validateToken(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        //전처리
        chain.doFilter(request, response);
        //후처리
    }
}

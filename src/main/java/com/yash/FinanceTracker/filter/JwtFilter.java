package com.yash.FinanceTracker.filter;

import com.yash.FinanceTracker.domain.User;
import com.yash.FinanceTracker.services.user.UserService;
import com.yash.FinanceTracker.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader= request.getHeader("Authorization");

        String userName= null;
        String jwt= null;

        if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer "))
        {
            jwt= authorizationHeader.substring(7);
            userName= jwtUtil.extractUserName(jwt);
        }

        if(userName!=null)
        {
            User user= userService.findUserByUserName(userName);
            if(jwtUtil.validateToken(jwt))
            {
                UsernamePasswordAuthenticationToken auth= new UsernamePasswordAuthenticationToken(user,null);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request,response);
    }
}

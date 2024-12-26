package com.yash.FinanceTracker.oauth;

import com.yash.FinanceTracker.constants.UserRoles;
import com.yash.FinanceTracker.domain.User;
import com.yash.FinanceTracker.repository.UserRepository;
import com.yash.FinanceTracker.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        try
        {
            DefaultOAuth2User oAuth2User= (DefaultOAuth2User) authentication.getPrincipal();
            System.out.println("USer get Name "+ oAuth2User.getName());
            System.out.println(oAuth2User.getAttributes().toString());
            System.out.println(oAuth2User.getAuthorities().toString());

            //now save the details in the database
            String userName= oAuth2User.getAttribute("name");
            String userEmail= oAuth2User.getAttribute("email");
            String userSub= oAuth2User.getAttribute("sub");

            User user= new User();
            user.setUserId(userSub);
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            user.getUserRolesSet().add(UserRoles.USER);

            userRepository.save(user);

            String jwtToken= jwtUtil.generateToken(oAuth2User);
            System.out.println("JWT Token is \n"+ jwtToken);
            response.setContentType("application/json");
            response.getWriter().write("{\"auth-token\":\"" + jwtToken + "\"}");
            new DefaultRedirectStrategy().sendRedirect(request,response,"/api/investment/get-all-investments");
        }
        catch(Exception e)
        {
            throw e;
        }
    }
}

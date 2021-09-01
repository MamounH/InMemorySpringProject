package com.atypon.springproject.security;

import com.atypon.springproject.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@SessionAttributes("id")
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication ) throws IOException {

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        request.getSession().setAttribute("id",userDetails.getUsername());
        request.getSession().setAttribute("role",userDetails.getRole());

        redirectUser(response, userDetails);

    }

    private void redirectUser(HttpServletResponse response, UserPrincipal userDetails) throws IOException {
        String redirectURL;
        Role userRole = Role.valueOf(userDetails.getRole());

        switch (userRole){
            case ADMIN -> redirectURL = "/Admin/Users";
            case EDITOR -> redirectURL = "/Editor/Books";
            case VIEWER -> redirectURL = "/Viewer/Books";
            default -> redirectURL = "/login";

        }
        response.sendRedirect(redirectURL);
    }


}

package com.atypon.springproject.controllers.login;

import com.atypon.springproject.entity.Role;
import com.atypon.springproject.security.AuthenticationFacade;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LoginServlet {


    private AuthenticationFacade authFacade;

    @GetMapping({"", "/"})
    public String doGet() {

        Authentication authentication = authFacade.getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (!authentication.isAuthenticated() || roles.contains("ROLE_ANONYMOUS")) {
            return "login";
        }
        return directUserToHomePage();

    }

    public String directUserToHomePage(){

        Role userRole = Role.valueOf(authFacade.getPrincipalUser().getRole());

        switch (userRole){
            case ADMIN -> {return "redirect:/Admin/Users";}
            case EDITOR -> {return "redirect:/Editor/Books";}
            case VIEWER -> {return "redirect:/Viewer/Books";}
            default -> {return "redirect:/login";}
        }
    }



    @RequestMapping(value = "login", method = RequestMethod.GET)
    protected String showLogin() {
        return "/login";
    }


}

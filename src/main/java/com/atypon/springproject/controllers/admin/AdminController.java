package com.atypon.springproject.controllers.admin;


import com.atypon.springproject.accountconfig.UserAccountConfig;
import com.atypon.springproject.dao.UserDao;
import com.atypon.springproject.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
@RequestMapping("Admin")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {

    private UserDao usersDao;
    private UserAccountConfig userAccountConfig;

    @GetMapping(value = "Users")
    protected String ShowUsers(ModelMap modelMap)  {
        modelMap.addAttribute("list", usersDao.getAll());
        return"/Admin/Users";
    }


    @GetMapping(value = "AddUser")
    protected String showAddUserForm(ModelMap modelMap)  {
        modelMap.addAttribute("user", User.builder().build());
        return "/Admin/AddUser";
    }

    @PostMapping(value = "AddUser")
    protected String addNewUser(@ModelAttribute @Valid User user, BindingResult result) {

        if (usersDao.userExists(user.getEmail())){
            result.rejectValue("email", "error.user", "An account already exists for this email.");
            return "/Admin/AddUser";
        }else{
            return userAccountConfig.addUser(user,result);
        }
    }


    @GetMapping(value = "UpdateUser")
    protected String showUpdateUserForm(@RequestParam String email, ModelMap modelMap) {
        modelMap.addAttribute("user",usersDao.getUser(email));
        return "/Admin/UpdateUser";

    }

    @PostMapping(value = "UpdateUser")
    protected String updateUser(@ModelAttribute @Valid User user,
                                BindingResult result, HttpServletRequest req) {
        String oldEmail = req.getParameter("oldEmail");
        if (isValidEmail(user,oldEmail,result)){
            return userAccountConfig.updateUser(user,oldEmail,result);
        } else
        {
            return "/Admin/UpdateUser";
        }
    }

    private boolean isValidEmail(User user,String oldEmail, BindingResult result){
        if (usersDao.userExists(user.getEmail())) {
            if(!usersDao.getUser(oldEmail).getEmail().equalsIgnoreCase(user.getEmail())) {
                result.rejectValue("email", "error.user", "An account already exists for this email.");
                return false;
            }
        }
        return true;
    }


    @GetMapping(value = "DeleteUser")
    protected String deleteUser(@RequestParam String email) {
        usersDao.deleteUser(email);
        return "redirect:/Admin/Users";
    }
}

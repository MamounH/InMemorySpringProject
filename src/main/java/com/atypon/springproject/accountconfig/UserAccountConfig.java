package com.atypon.springproject.accountconfig;

import com.atypon.springproject.dao.UserDao;
import com.atypon.springproject.entity.User;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserAccountConfig {


    private UserDao userRepo;
    private PasswordEncoder passwordEncoder;


    public String addUser(User user, BindingResult result){

        if (result.hasErrors()){
            return "/Admin/AddUser";
        } else {
            return saveUser(user);
        }
    }


    public String updateUser(User user, String email, BindingResult result){
        if (result.hasErrors()){
            return "/Admin/UpdateUser";
        } else {
            return updateUser(user,email);
        }
    }

    @NotNull
    private String saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.createUser(user);
        return "redirect:/Admin/Users";
    }

    @NotNull
    private String updateUser(User user, String email) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.update(user,email);
        return "redirect:/Admin/Users";
    }


}


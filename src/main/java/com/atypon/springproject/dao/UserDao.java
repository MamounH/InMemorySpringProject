package com.atypon.springproject.dao;


import com.atypon.springproject.entity.User;

import java.util.List;

public interface UserDao {

      User getUser(String username);
      void updateUser(User user, String email);
      List<User> getAll();
      void deleteUser(String username);
      void createUser(User user) ;
      boolean userExists(String username);

    }

package com.atypon.springproject.dao;


import com.atypon.springproject.entity.User;

import java.util.List;

public interface UserDao {

      User get(String username);
      void update(User user, String email);
      List<User> getAll();
      void remove(String username);
      void createUser(User user) ;
      boolean userExists(String username);

    }

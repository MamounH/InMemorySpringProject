package com.atypon.springproject.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class User {


    @NotEmpty(message = "* Please provide user Email")
    @Email(message = "* Please provide a valid Email")
    String email;

    @NotEmpty(message = "* Please provide user First Name")
    String fName;

    @NotEmpty(message = "* Please provide user Last Name")
    String lName;

    @Length(min = 8, message = "* User password must have at least 8 characters")
    @NotEmpty(message = "* Please provide user password")
    String password;

    @NotEmpty(message = "* user cannot be created without an assigned role")
    String role;

    @Override
    public String toString() {
        return fName + "," + lName + "," + email + "," + password + ","
                  + role +"\n";
    }



}
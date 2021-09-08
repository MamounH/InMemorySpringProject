package com.atypon.springproject.dao.daoImp;

import com.atypon.springproject.dao.UserDao;
import com.atypon.springproject.entity.User;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UserDaoImp implements UserDao {



    private static final String DBPATH = "src/main/resources/users.txt";
    private static final String TEMPPATH = "src/main/resources/userstemp.txt";
    private final Logger logger = Logger.getLogger("UserDao");

    File file = new File(DBPATH);
    File newFile = new File(TEMPPATH);


    @Override
    public List<User> getAll(){

        List<User> users = new ArrayList<>();

        try (FileReader fileReader = new FileReader(DBPATH);
             BufferedReader bufferedReader= new BufferedReader(fileReader)) {
            loadUsers(users, bufferedReader);
        } catch (Exception e) {
            logError(e);
        }
        return users;

    }

    @Override
    public User get(String username){
        try(Scanner scanner =new Scanner(new File(DBPATH)) ){
            return retrieveUserFromDB(username, scanner);
        } catch (Exception e) {
            logError(e);
        }
        return new User();
    }

    @Override
    public void update(User user, String email){

        try(FileReader fileReader =new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            FileWriter fileWriter = new FileWriter(newFile)) {
            updateExistingRecord(user,email, bufferedReader, fileWriter);
        } catch (Exception e) {
            logError(e);
        }

        handleTempFile();
    }

    @Override
    public void remove(String username) {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader =new BufferedReader(fileReader);
             FileWriter fileWriter=new FileWriter(newFile) ){

            deleteUserFromDB(username, bufferedReader, fileWriter);
        } catch (Exception e) {
            logError(e);
        }
        handleTempFile();
    }

    @Override
    public boolean userExists(String username){
        boolean isFound = false;
        try (Scanner scanner =new Scanner(new FileReader(DBPATH)) ){
            while(scanner.hasNextLine() && !isFound) {
                isFound = scanner.nextLine().contains(username);
            }
        }
        catch(IOException e) {
            logError(e);
        }

        return isFound;
    }



    private void loadUsers(List<User> users, BufferedReader bufferedReader) throws IOException {

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");

            User user =  User.builder().fName(data[0]).lName(data[1]).email(data[2])
                    .role(data[4]).build();
            users.add(user);
        }
    }


    private void updateExistingRecord(User user,String email, BufferedReader bufferedReader, FileWriter fileWriter) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");

            if (email.equals(data[2])) {
                fileWriter.write(user.toString());
            } else {
                fileWriter.write(line + "\n");
            }
        }
    }

    @Override
    public void createUser(User user) {
        saveUser(user);
    }

    private void saveUser(User user) {

        try (FileWriter fileWriter = new FileWriter(DBPATH, true)){
            addUserToDb(user, fileWriter);
        } catch (Exception e){
            logError(e);
        }
    }


    private User retrieveUserFromDB(String username, Scanner scanner) {
        String fName;
        String lName;
        String tempUsername;
        String tempPassword;
        String tempRole;
        scanner.useDelimiter("[,\n]");
        while (scanner.hasNext()) {
            fName=scanner.next();
            lName=scanner.next();
            tempUsername = scanner.next();
            tempPassword = scanner.next();
            tempRole= scanner.next();
            if (tempUsername.trim().equalsIgnoreCase(username.trim())) {

                return User.builder().fName(fName).lName(lName).email(tempUsername)
                                .password(tempPassword).role(tempRole).build();
            }
        }
        return  User.builder().build();
    }

    private void deleteUserFromDB(String username, BufferedReader bufferedReader, FileWriter fileWriter) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");
            if (username.equals(data[2])) {
                System.out.println("Successfully Deleted : " + username);
            } else {
                fileWriter.write(line + "\n");
            }
        }
        fileWriter.flush();
    }

    private void addUserToDb(User user, FileWriter fileWriter) throws IOException {
        fileWriter.write(user.toString());
        fileWriter.flush();
    }


    private void handleTempFile()  {
        try {
            Files.delete(file.toPath());
            newFile.renameTo(file);
        } catch (Exception e){
            logError(e);
        }
    }

    private void logError (Exception e){
        logger.log(Level.SEVERE,e.getMessage());
    }

}

package com.atypon.springproject.dao.daoImp;

import com.atypon.springproject.dao.BooksDao;
import com.atypon.springproject.entity.Book;

import java.io.*;
import java.nio.file.Files;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BooksDaoImp<K,V> implements BooksDao<K,V> {

    private static final String DBPATH = "src/main/resources/bookDetails.csv";

    private static final String TEMPPATH = "src/main/resources/temp.txt";

    private final Logger logger = Logger.getLogger("DataBase Log");

    File file = new File(DBPATH);

    File newFile = new File(TEMPPATH);


    @Override
    public TreeMap<K, V> loadRecords() {

        TreeMap<Integer, V> map = new TreeMap<>();

        try (FileReader fileReader = new FileReader(DBPATH);
             BufferedReader bufferedReader= new BufferedReader(fileReader)) {
            loadDBIntoMap( map, bufferedReader);
        } catch (Exception e) {
            logError(e);
        }
        return (TreeMap<K, V>) map;
    }


    @Override
    public boolean recordIsAdded(V value) {
        synchronized (this){

            try(FileWriter fileWriter = new FileWriter(DBPATH, true) ) {
                addNewRecordToDB(value, fileWriter);
                return true;
            } catch (Exception e) {
                logError(e);
                return false;
            }
        }
    }


    @Override
    public boolean recordIsDeleted(K key)  {

        synchronized (this){
          try (FileReader fileReader =new FileReader(file);
               BufferedReader bufferedReader=new BufferedReader(fileReader);
               FileWriter fileWriter = new FileWriter(newFile)){

              deleteRecordFromDB(key, bufferedReader, fileWriter);
          } catch (Exception e) {
                logError(e);
                return false;
            }
        return handleTempFile(file, newFile);
        }
    }


    @Override
    public boolean recordIsUpdated(V value, K key)  {
        synchronized (this) {

            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader);
                 FileWriter fileWriter = new FileWriter(newFile)) {

                updateExistingRecord(value, key, bufferedReader, fileWriter);
            } catch (Exception e) {
                logError(e);
                return false;
            }

            return handleTempFile(file, newFile);
        }
    }


    private void loadDBIntoMap(TreeMap<Integer, V> map, BufferedReader bufferedReader) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");
            Book book =  Book.builder().ID(Integer.parseInt(data[0])).name(data[1]).subject(data[2])
                    .author(data[3]).publisher(data[4]).year(data[5]).build();
            map.put( book.getID(),(V)book);
        }
    }


    private void addNewRecordToDB(V value, FileWriter fileWriter) throws IOException {
        fileWriter.write(value.toString());
        fileWriter.flush();
    }


    private void updateExistingRecord(V value, K key, BufferedReader bufferedReader, FileWriter fileWriter) throws IOException {
        System.out.println("Updating record with key value of:"+key);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");

            if (key.equals(Integer.parseInt(data[0]))) {
                    fileWriter.write(value.toString());
            } else {
                fileWriter.write(line + "\n");
            }
        }
    }

    private void deleteRecordFromDB(K key, BufferedReader bufferedReader, FileWriter fileWriter) throws IOException {
        System.out.println("deleting record with key value of:"+key);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");

            if (key.equals(Integer.parseInt(data[0]))) {
                logger.log(Level.INFO,"Record Found, Deleting Record....");
            } else {
                fileWriter.write(line + "\n");
            }
        }
        fileWriter.flush();
    }

    private boolean handleTempFile(File file, File newFile)  {
        try {
            Files.delete(file.toPath());
            newFile.renameTo(file);
            return true;
        }
        catch (Exception e){
            logError(e);
            return false;
        }
    }

    private void logError (Exception e){
        logger.log(Level.SEVERE,e.getMessage());
    }


}





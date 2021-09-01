package com.atypon.springproject.database;

import com.atypon.springproject.entity.Book;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class BooksTableTest {

    BooksTable<Integer,Book> booksTable;

    @Before
    public void setUp(){
         booksTable = BooksTable.getInstance();
    }

    @Test
    public void testConcurrentOps(){
        Map<String, Book> books;
        try {

            // thread to add record
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Book book=  Book.builder().ID(99).name("test").author("test").subject("test")
                                        .publisher("test").year("test").build();
                        booksTable.add(book,book.getID());
                        assertTrue(booksTable.recordExists(99));
                        booksTable.remove(99);
                        assertFalse(booksTable.recordExists(99));
                    } catch (Exception e) {
                        fail("Error on locking in 1st Thread:"+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }).start();

            // thread to add record
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Book book= Book.builder().ID(1000).name("test").author("test").subject("test")
                                .publisher("test").year("test").build();
                        booksTable.add(book,book.getID());
                        assertTrue(booksTable.recordExists(1000));
                        booksTable.remove(1000);
                        assertFalse(booksTable.recordExists(1000));
                    } catch (Exception e) {
                        fail("Error on locking in 2nd Thread:"+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }).start();

            // thread to read all records
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Map<Integer, Book> books= booksTable.getAll();
                        assertTrue(books.size()== booksTable.getDBSize());
                    } catch (Exception e) {
                        fail("Error on locking in 3rd Thread:"+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            fail("An error occurred while manipulating inMemory database:" +e.getMessage());
        }


    }

    @Test
    public void testUpdateRecord(){

        Book book=  Book.builder().ID(0).name("test").author("test").subject("test")
                .publisher("test").year("test").build();

        booksTable.add(book,book.getID());

        Book updatedBook=  Book.builder().ID(0).name("t").author("t").subject("tt")
                .publisher("tst").year("tet").build();


        booksTable.update(updatedBook,book.getID());

        assertEquals(updatedBook, booksTable.get(book.getID()));
        booksTable.remove(book.getID());

    }





}




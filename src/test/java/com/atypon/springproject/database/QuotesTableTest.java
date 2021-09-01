package com.atypon.springproject.database;

import com.atypon.springproject.entity.Quote;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class QuotesTableTest {


    QuotesTable<Integer, Quote> quotesTable;

    @Before
    public void setUp(){
        quotesTable = QuotesTable.getInstance();
    }

    @Test
    public void testConcurrentOps(){
        try {

            // thread to add record
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Quote quote = Quote.builder().id(1000).quote("test").bookId(3).build();
                        quotesTable.add(quote,quote.getId());
                        assertTrue(quotesTable.recordExists(1000));
                        quotesTable.remove(1000);
                        assertFalse(quotesTable.recordExists(1000));
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
                        Quote quote = Quote.builder().id(2000).quote("test").bookId(3).build();
                        quotesTable.add(quote,quote.getId());
                        assertTrue(quotesTable.recordExists(2000));
                        quotesTable.remove(2000);
                        assertFalse(quotesTable.recordExists(2000));

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
                        Map<Integer, Quote> quoteMap= quotesTable.getAll();
                        assertTrue(quoteMap.size()== quotesTable.getDBSize());
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

        Quote quote =  Quote.builder().id(00).quote("test").bookId(3).build();

        quotesTable.add(quote,quote.getId());

        Quote updatedQuote =  Quote.builder().id(00).quote("testing").bookId(4).build();


        quotesTable.update(updatedQuote,quote.getId());

        assertEquals(updatedQuote, quotesTable.get(quote.getId()));
        quotesTable.remove(quote.getId());

    }




}
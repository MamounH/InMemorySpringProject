package com.atypon.springproject.database;

import com.atypon.springproject.dao.LibraryDao;


public class IDGenerator {

    private int bookId;
    private int quoteId;


    public IDGenerator(int bookId , int quoteId) {
        this.bookId=bookId;
        this.quoteId=quoteId;
    }


    protected synchronized int getNewBookId(){
        bookId = bookId +1;
        return bookId;
    }

    protected synchronized int getNewQuoteId(){
        quoteId = quoteId + 1;
        return quoteId;
    }


}

package com.atypon.springproject.database;

import com.atypon.springproject.dao.LibraryDao;
import com.atypon.springproject.dao.daoImp.BooksDaoImp;
import com.atypon.springproject.dao.daoImp.QuotesDaoImp;
import com.atypon.springproject.entity.Book;
import com.atypon.springproject.entity.Quote;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.*;

public class InMemoryDBTest {

    LibraryDao<Integer,Book> bookLibraryDao = new BooksDaoImp<>();
    LibraryDao<Integer,Quote> QuoteLibraryDao = new QuotesDaoImp<>();

    Table<Integer,Book> booksTable;
    Table<Integer, Quote> quotesTable;

    InMemoryDB inMemoryDB;

    @Before
    public void setUp(){
        booksTable = new Table<>(bookLibraryDao);
        quotesTable = new Table<>(QuoteLibraryDao);
        inMemoryDB = InMemoryDB.getInstance();
    }
    @Test
    public void getAllBooksTest() {
        assertEquals(booksTable.getDBSize(),inMemoryDB.getAllBooks().size());

    }

    @Test
    public void getAllQuotesTest() {
        assertEquals(quotesTable.getDBSize(),inMemoryDB.getAllQuotes().size());
    }

    @Test
    public void getBookTest() {
        assertEquals(booksTable.get(2),inMemoryDB.getBook(2));
    }

    @Test
    public void getQuoteTest() {
        assertEquals(quotesTable.get(2),inMemoryDB.getQuote(2));
    }

    @Test
    public void getBookQuotesTest() {
        TreeMap<Integer,Quote> quotes = inMemoryDB.getBookQuotes(8);
        assertEquals(8,quotes.get(4).getBookId());
    }

    @Test
    public void bookIsAddedTest() {
        Book book= Book.builder().name("test").author("test").subject("test")
                .publisher("test").year("test").build();

      assertTrue(inMemoryDB.bookIsAdded(book));
      assertTrue(booksTable.recordExists(booksTable.getAll().lastKey()));
      inMemoryDB.removeBook(booksTable.getAll().lastKey());
    }

    @Test
    public void quoteIsAddedTest() {
        Quote quote =  Quote.builder().quote("test").bookId(3).build();
        assertTrue(inMemoryDB.quoteIsAdded(quote));
        assertTrue(quotesTable.recordExists(quotesTable.getAll().lastKey()));
        inMemoryDB.removeQuote(quotesTable.getAll().lastKey());
    }

    @Test
    public void bookIsUpdated() {
        int id = (Integer) inMemoryDB.getAllBooks().lastEntry().getKey();
        Book book= Book.builder().ID(id).name("test").author("test").subject("test")
                .publisher("test").year("test").build();

        inMemoryDB.bookIsAdded(book);

        Book updatedBook= Book.builder().ID(id).name("t").author("t").subject("tt")
                .publisher("tst").year("tet").build();

        assertTrue(inMemoryDB.bookIsUpdated(updatedBook));


        assertEquals(updatedBook, inMemoryDB.getBook(id));
        inMemoryDB.removeBook(id);

    }

    @Test
    public void quoteIsUpdated() {

        int id = (Integer) inMemoryDB.getAllQuotes().lastEntry().getKey();


        Quote quote =  Quote.builder().id(id).quote("test").bookId(3).build();

        inMemoryDB.quoteIsAdded(quote);

        Quote updatedQuote =  Quote.builder().id(id).quote("testing").bookId(4).build();


        assertTrue(inMemoryDB.quoteIsUpdated(updatedQuote));

        assertEquals(updatedQuote, inMemoryDB.getQuote(id));
        inMemoryDB.removeQuote(id);

    }


}
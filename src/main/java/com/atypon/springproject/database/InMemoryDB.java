package com.atypon.springproject.database;

import com.atypon.springproject.entity.Book;
import com.atypon.springproject.entity.Quote;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.TreeMap;

@Repository
public class InMemoryDB {

    private BooksTable<Integer , Book> booksTable;
    private QuotesTable<Integer , Quote> quotesTable;

    public InMemoryDB(){
        booksTable= BooksTable.getInstance();
        quotesTable=QuotesTable.getInstance();
    }


    public TreeMap getAllBooks(){
        return booksTable.getAll();
    }

    public TreeMap getAllQuotes(){
        assignBookNames();
        return quotesTable.getAll();

    }

    private void assignBookNames() {
        Set<Integer> keys = quotesTable.getAll().keySet();
        for(Integer k:keys) {
            quotesTable.getAll().get(k).setBookName(booksTable.get(quotesTable.get(k).getBookId()).getName());
        }
    }

    public Book getBook(int key){
        return booksTable.get(key);
    }

    public Quote getQuote(int key){
        return quotesTable.get(key);
    }

    public TreeMap getBookQuotes(int id) {

        TreeMap<Integer,Quote> quotes = new TreeMap<>();


        Set<Integer> keys = quotesTable.getAll().keySet();
        for(Integer k:keys) {
            if (quotesTable.get(k).getBookId()==id){
                quotes.put(quotesTable.get(k).getId(),quotesTable.get(k));
            }

        }
        return quotes;
    }


    public boolean bookIsAdded(Book book){

            int id = booksTable.get(booksTable.getAll().lastEntry().getKey()).getID() + 1;
            book.setID(id);
            booksTable.add(book,book.getID());
        return booksTable.recordExists(book.getID());
    }

    public boolean quoteIsAdded(Quote quote){

            quote.setId(quotesTable.get((Integer) getAllQuotes().lastEntry().getKey()).getId() + 1);
            quotesTable.add(quote,quote.getId());
        return quotesTable.recordExists(quote.getId());
    }


    public boolean bookIsUpdated(Book book){

            booksTable.update(book,book.getID());
        return booksTable.recordExists(book.getID());
    }

    public boolean quoteIsUpdated(Quote quote){

            quotesTable.update(quote,quote.getId());
        return quotesTable.recordExists(quote.getId());
    }

    public void removeBook(int key) {
        booksTable.remove(key);
        if (quotesTable.bookQuotesAreRemoved(key)){
                quotesTable.getAll().entrySet().removeIf(entry -> entry.getValue().getBookId() == key);
        }
    }

    public void removeQuote(int key) {
        quotesTable.remove(key);
    }


}

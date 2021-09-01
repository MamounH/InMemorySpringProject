package com.atypon.springproject.controllers.editor;


import com.atypon.springproject.database.InMemoryDB;
import com.atypon.springproject.entity.Book;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BooksController {


    private InMemoryDB inMemoryDB;


    @GetMapping(value = "Editor/Books")
    protected String showBooks(ModelMap modelMap)  {
        modelMap.addAttribute("list", inMemoryDB.getAllBooks());
        return"/Editor/Books";
    }


    @GetMapping(value = "Editor/AddBook")
    protected String showAddPage(ModelMap modelMap)  {
        modelMap.addAttribute("book", Book.builder().build());
        return"/Editor/AddBook";
    }

    @PostMapping(value = "Editor/AddBook")
    protected String AddBook(@ModelAttribute @Valid Book book, BindingResult bindingResult)  {

        if (bindingResult.hasErrors()){
            return "Editor/AddBook";
        } else {
            inMemoryDB.bookIsAdded(book);
            return "redirect:/Editor/Books";
        }
    }



    @GetMapping(value = "Editor/UpdateBook")
    protected String showUpdatePage(ModelMap modelMap, @RequestParam int ID)  {
        modelMap.addAttribute("book",inMemoryDB.getBook(ID));
        return"/Editor/UpdateBook";
    }

    @PostMapping(value = "Editor/UpdateBook")
    protected String updateBook(@ModelAttribute @Valid Book book, BindingResult result)  {

        if (result.hasErrors()){
            return "/Editor/UpdateBook";
        } else {
            inMemoryDB.bookIsUpdated(book);
            return "redirect:/Editor/Books";
        }
    }

    @GetMapping(value = "Editor/DeleteBook")
    protected String deleteBook(@RequestParam int ID)  {
        inMemoryDB.removeBook(ID);
        return"redirect:/Editor/Books";
    }




}

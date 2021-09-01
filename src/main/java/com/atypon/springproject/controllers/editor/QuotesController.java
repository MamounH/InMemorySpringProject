package com.atypon.springproject.controllers.editor;

import com.atypon.springproject.database.InMemoryDB;
import com.atypon.springproject.entity.Quote;
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
public class QuotesController {

    private InMemoryDB inMemoryDB;


    @GetMapping(value = "Editor/EQuotes")
    protected String showQuotes(ModelMap modelMap)  {
        modelMap.addAttribute("list", inMemoryDB.getAllQuotes());
        return"/Editor/EQuotes";
    }



    @GetMapping(value = "Editor/AddQuote")
    protected String showAddPage(ModelMap modelMap)  {
        modelMap.addAttribute("list",inMemoryDB.getAllBooks());
        modelMap.addAttribute("quote",Quote.builder().build());
        return"/Editor/AddQuote";
    }

    @PostMapping(value = "Editor/AddQuote")
    protected String AddQuote(@ModelAttribute @Valid Quote quote, BindingResult bindingResult)  {

        if (bindingResult.hasErrors()){
            return "/Editor/AddQuote";
        } else {
            inMemoryDB.quoteIsAdded(quote);
            return "redirect:/Editor/EQuotes";
        }
    }





    @GetMapping(value = "Editor/UpdateQuote")
    protected String showUpdatePage(ModelMap modelMap, @RequestParam int ID)  {
        modelMap.addAttribute("quote",inMemoryDB.getQuote(ID));
        return"/Editor/UpdateQuote";
    }

    @PostMapping(value = "Editor/UpdateQuote")
    protected String updateQuote(@ModelAttribute @Valid Quote quote, BindingResult bindingResult)  {


        if (bindingResult.hasErrors()){
            return "/Editor/UpdateQuote";
        } else {
            inMemoryDB.quoteIsUpdated(quote);
            return "redirect:/Editor/EQuotes";

        }
    }

    @GetMapping(value = "Editor/DeleteQuote")
    protected String deleteQuote(@RequestParam int ID)  {
        inMemoryDB.removeQuote(ID);
        return"redirect:/Editor/EQuotes";
    }


}

package com.atypon.springproject.controllers.viewer;

import com.atypon.springproject.database.InMemoryDB;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("Viewer")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ViewerController {

    private InMemoryDB inMemoryDB;


    @GetMapping(value = "Books")
    protected String showBooks(ModelMap modelMap)  {
        modelMap.addAttribute("list", inMemoryDB.getAllBooks());
        return"/Viewer/Books";
    }

    @GetMapping(value = "Quotes")
    protected String showQuotes(ModelMap modelMap)  {
        modelMap.addAttribute("list", inMemoryDB.getAllQuotes());
        return"/Viewer/Quotes";
    }

    @GetMapping(value = "BookQuotes")
    protected String showBookQuotes(@RequestParam int ID, ModelMap modelMap)  {
        modelMap.addAttribute("list", inMemoryDB.getBookQuotes(ID));
        return"/Viewer/BookQuotes";
    }




}

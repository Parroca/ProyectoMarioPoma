package cat.institutmarianao.proyecto.controllers;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import cat.institutmarianao.proyecto.service.AdService;
import cat.institutmarianao.proyecto.service.FavoriteService;
import cat.institutmarianao.proyecto.service.UserService;
import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.User;
import org.springframework.data.domain.Sort;

@Controller
public class HomeController {

    @Autowired
    private AdService adService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/home")
    public String home(Model model, Principal principal,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "dateDesc") String sort) {
        Pageable pageable;

        switch (sort) {
            case "dateAsc":
                pageable = PageRequest.of(page, 24, Sort.by("id").ascending());
                break;
            case "priceAsc":
                pageable = PageRequest.of(page, 24, Sort.by("price").ascending());
                break;
            case "priceDesc":
                pageable = PageRequest.of(page, 24, Sort.by("price").descending());
                break;
            default: 
                pageable = PageRequest.of(page, 24, Sort.by("id").descending());
                break;
        }

        Page<Item> itemsPage;
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            itemsPage = adService.findAllByUsuarioNotAndOcultoFalse(user, pageable); 
            model.addAttribute("user", user);
        } else {
            itemsPage = adService.findAllByOcultoFalse(pageable); 
        }

        model.addAttribute("itemsPage", itemsPage);
        model.addAttribute("sort", sort); 
        return "home";
    }


    @GetMapping("/home/item/{id}")
    public String getItemDetails(@PathVariable("id") long id, Model model, Principal principal) {
       
        Item item = adService.getById(id);
        if (item != null) {
        	
            model.addAttribute("item", item);

            List<Item> similarItems = adService.findSimilarItems(item);
            model.addAttribute("similarItems", similarItems);
            
            boolean isFavorite = false;
            if (principal != null) {
                User user = userService.findByUsername(principal.getName());
                isFavorite = favoriteService.isItemInFavorites(user, item);
                model.addAttribute("user", user);
            }
            
            model.addAttribute("isFavorite", isFavorite);

            return "anuncios/item";
        } else {
            
            return "redirect:/home";
        }
    }
    
    @PostMapping("/search")
    public String searchItems(@RequestParam("nombre") String nombre, Model model, Principal principal, @RequestParam(defaultValue = "0") int page) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "redirect:/home"; 
        }

        Pageable pageable = PageRequest.of(page, 24); 
        Page<Item> itemsPage;

        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            itemsPage = adService.searchByNameAndUsuarioNotAndOcultoFalse(nombre, user, pageable);  
            model.addAttribute("user", user);
        } else {
            itemsPage = adService.searchByNameAndOcultoFalse(nombre, pageable); 
        }

        model.addAttribute("itemsPage", itemsPage);
        if (itemsPage.isEmpty()) {
            model.addAttribute("message", "Objeto no encontrado");
        }

        return "homeSearch";
    }
    
    @GetMapping("/searchCategory")
    public String searchItemsByCategory(@RequestParam(value = "categoria", required = false) String categoria, 
                                         @RequestParam(defaultValue = "0") int page, 
                                         @RequestParam(defaultValue = "dateDesc") String sort, 
                                         Model model, 
                                         Principal principal) {
        if (categoria == null || categoria.isEmpty()) {
            return "redirect:/home";
        }

        Pageable pageable;
        switch (sort) {
            case "dateAsc":
                pageable = PageRequest.of(page, 24, Sort.by("id").ascending());
                break;
            case "priceAsc":
                pageable = PageRequest.of(page, 24, Sort.by("price").ascending());
                break;
            case "priceDesc":
                pageable = PageRequest.of(page, 24, Sort.by("price").descending());
                break;
            default: 
                pageable = PageRequest.of(page, 24, Sort.by("id").descending());
                break;
        }

        Page<Item> itemsPage;
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            itemsPage = adService.findAllByCategoryAndUsuarioNotAndOcultoFalse(Item.Categoria.valueOf(categoria), user, pageable);
            model.addAttribute("user", user);
        } else {
            itemsPage = adService.findAllByCategoryAndOcultoFalse(Item.Categoria.valueOf(categoria), pageable);
        }

        model.addAttribute("itemsPage", itemsPage);
        model.addAttribute("categoria", categoria);
        model.addAttribute("sort", sort); 
        if (itemsPage.isEmpty()) {
            model.addAttribute("message", "No se encontraron objetos en esta categoría");
        }

        return "homeSearch";
    }
}

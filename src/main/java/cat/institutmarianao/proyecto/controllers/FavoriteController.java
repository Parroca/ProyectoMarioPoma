package cat.institutmarianao.proyecto.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.User;
import cat.institutmarianao.proyecto.service.AdService;
import cat.institutmarianao.proyecto.service.FavoriteService;
import cat.institutmarianao.proyecto.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdService adService;

    @GetMapping("/favorite")
    public String viewFavorites(Model model, Principal principal, 
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "dateDesc") String sort) {
        if (principal == null) {
            return "redirect:/login"; 
        }

        String username = principal.getName();
        Pageable pageable;

        switch (sort) {
            case "dateAsc":
                pageable = PageRequest.of(page, 12, Sort.by("id").ascending());
                break;
            case "priceAsc":
                pageable = PageRequest.of(page, 12, Sort.by("item.price").ascending());
                break;
            case "priceDesc":
                pageable = PageRequest.of(page, 12, Sort.by("item.price").descending());
                break;
            default: 
                pageable = PageRequest.of(page, 12, Sort.by("id").descending());
                break;
        }

        Page<Item> favoriteItems = favoriteService.getFavoriteItems(username, pageable);

        if (favoriteItems.isEmpty()) {
            model.addAttribute("message", "Todavía no has agregado ningún artículo a favoritos");
        } else {
            model.addAttribute("favoriteItems", favoriteItems);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", favoriteItems.getTotalPages());
        model.addAttribute("sort", sort);
        return "favorite";
    }

    @PostMapping("/favorite/add")
    public String addFavorite(@RequestParam Long itemId, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; 
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);
        Item item = adService.getById(itemId);

        if (user != null && item != null) {
            favoriteService.addToFavorites(user, item);
        }

        return "redirect:/home/item/" + itemId;
    }

    @PostMapping("/favorite/remove")
    public String removeFavorite(@RequestParam Long itemId, @RequestParam String redirectUrl, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; 
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);
        Item item = adService.getById(itemId);

        if (user != null && item != null) {
            favoriteService.removeFromFavorites(user, item);
        }

        return "redirect:" + redirectUrl;
    }
}

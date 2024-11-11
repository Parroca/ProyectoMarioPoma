package cat.institutmarianao.proyecto.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import cat.institutmarianao.proyecto.service.TransactionService;
import org.springframework.transaction.annotation.Transactional;

import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.Message;
import cat.institutmarianao.proyecto.model.User;
import cat.institutmarianao.proyecto.repository.MessageRepository;
import cat.institutmarianao.proyecto.service.AdService;
import cat.institutmarianao.proyecto.service.ChatService;
import cat.institutmarianao.proyecto.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private AdService adService;
    @Autowired
    private UserService userService;
    
    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TransactionService transactionService;
    
    private static final Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @GetMapping("/users")
    public String listUsers(Model model) {
    	List<User> users = userService.getAllUsers().stream()
    	        .filter(user -> user.getRole().equals("ROLE_USER"))
    	        .collect(Collectors.toList());
    	    model.addAttribute("users", users);
    	    return "admin/users";
    }

    @GetMapping("/items")
    public String listItems(Model model) {
        List<Item> items = adService.findAll();
        model.addAttribute("items", items);
        return "admin/items";
    }

    @GetMapping("/user/{username}/anuncios")
    public String listUserItems(@PathVariable("username") String username, Model model) {
        User user = userService.findByUsername(username);
        if (user != null) {
            List<Item> items = adService.findAllByUsuario(user);
            model.addAttribute("items", items);
            model.addAttribute("user", user);
            model.addAttribute("isAdminView", true);
        }
        return "anuncios";
    }

    @Transactional
    @GetMapping("/deleteUser/{username}")
    public String deleteUser(@PathVariable String username) {
        User user = userService.findByUsername(username);

        if (user != null) {
            adService.deleteItemsByUser(user);

            transactionService.deleteTransactionsByUser(user);

            List<Message> userMessages = messageRepository.findBySender(user);
            if (!userMessages.isEmpty()) {
                messageRepository.deleteAll(userMessages);
            }

            chatService.deleteChatsByUser(username);

            userService.deleteByUsername(username);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/deleteItem/{id}")
    public String deleteItem(@PathVariable Long id) {
        adService.deleteById(id);
        return "redirect:/admin/items";
    }
    
    @GetMapping("/denuncias")
    public String listarDenuncias(Model model) {
        List<Item> denunciados = adService.findByDenunciaEstado(Item.DenunciaEstado.DENUNCIADO);
        model.addAttribute("denunciados", denunciados);
        return "admin/denuncias";
    }
    
    @PostMapping("/ocultar/{id}")
    public String ocultarProducto(@PathVariable Long id) {
        Item item = adService.getById(id);
        if (item != null) {
            item.setOculto(true);
            item.setDenunciaEstado(Item.DenunciaEstado.NO_DENUNCIADO); 
            adService.save(item);
        }
        return "redirect:/admin/denuncias";
    }
    
    @PostMapping("/desocultar/{id}")
    public String desocultarProducto(@PathVariable Long id) {
        Item item = adService.getById(id);
        if (item != null) {
            item.setOculto(false); 
            adService.save(item);
        }
        return "redirect:/admin/productosOcultos"; 
    }
    
    @PostMapping("/deleteDenuncia/{id}")
    public String deleteDenuncia(@PathVariable Long id) {
        Item item = adService.getById(id);
        if (item != null) {
            item.setDenunciaEstado(Item.DenunciaEstado.NO_DENUNCIADO); 
            adService.save(item);
        }
        return "redirect:/admin/denuncias"; 
    }
    
    @PostMapping("/vetarUsuario/{username}")
    public String vetarUsuario(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            user.setVetado(true);
            userService.save(user);
            
            List<Item> items = adService.findAllByUsuario(user);
            for (Item item : items) {
                item.setOculto(true);
                adService.save(item);
            }
        }
        return "redirect:/admin/users";
    }
    
    @PostMapping("/desvetarUsuario/{username}")
    public String desvetarUsuario(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            user.setVetado(false); 
            userService.save(user);

            List<Item> items = adService.findAllByUsuario(user);
            for (Item item : items) {
                item.setOculto(false);
                adService.save(item);
            }
        }
        return "redirect:/admin/usuariosVetados"; 
    }
    
    @GetMapping("/productosOcultos")
    public String listarProductosOcultos(Model model) {
        List<Item> productosOcultos = adService.findOcultos();
        model.addAttribute("productosOcultos", productosOcultos);
        return "admin/productosOcultos";
    }
    
    @GetMapping("/usuariosVetados")
    @PreAuthorize("hasRole('ADMIN')")
    public String listarUsuariosVetados(Model model) {
        List<User> usuariosVetados = userService.findVetados();
        model.addAttribute("usuariosVetados", usuariosVetados);
        return "admin/usuariosVetados";
    }
}

package cat.institutmarianao.proyecto.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.transaction.annotation.Transactional;

import cat.institutmarianao.proyecto.model.User;
import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.Address;
import cat.institutmarianao.proyecto.model.Transaction;
import cat.institutmarianao.proyecto.service.AdService;
import cat.institutmarianao.proyecto.service.TransactionService;
import cat.institutmarianao.proyecto.service.UploadFileService;
import cat.institutmarianao.proyecto.service.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping(value = "/usuario")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UploadFileService uploadFileService;
    
    @Autowired
    private AdService adService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal, 
                            @RequestParam(defaultValue = "0") int page) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            
            if (user.getImageName() == null || user.getImageName().isEmpty()) {
                user.setImageName("default.jpg");
            }
            
            Pageable pageable = PageRequest.of(page, 4);
            Page<Item> itemsPage = adService.findPaginatedByUsuarioAndOcultoFalse(user, pageable);

            if (itemsPage == null) {
                itemsPage = Page.empty(pageable);
            }

            model.addAttribute("user", user);
            model.addAttribute("itemsPage", itemsPage);  
            model.addAttribute("isOwner", true); 
            model.addAttribute("isAdmin", user.getRole().equals("ROLE_ADMIN"));
        } else {
            model.addAttribute("isOwner", false);
            model.addAttribute("isAdmin", false);
        }
        return "/usuario/perfil";
    }
    
    @GetMapping("/perfil/{username}")
    public String userProfile(@PathVariable("username") String username, 
                              @RequestParam(defaultValue = "0") int page, 
                              Model model, Principal principal) {
        User user = userService.findByUsername(username);
        
        if (user.getImageName() == null || user.getImageName().isEmpty()) {
            user.setImageName("default.jpg");
        }
        
        boolean isOwner = false;
        boolean isAdmin = false;

        if (principal != null) {
            User currentUser = userService.findByUsername(principal.getName());
            if (currentUser.getUsername().equals(username)) {
                isOwner = true;
            }
            if (currentUser.getRole().equals("ROLE_ADMIN")) {
                isAdmin = true;
            }
        }

        Pageable pageable = PageRequest.of(page, 4);
        Page<Item> itemsPage = adService.findPaginatedByUsuarioAndOcultoFalse(user, pageable);

        if (itemsPage == null) {
   
            itemsPage = Page.empty(pageable);
        }

        model.addAttribute("user", user);
        model.addAttribute("itemsPage", itemsPage);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isAdmin", isAdmin);

        return "/usuario/perfil";
    }

    @GetMapping("/editar/{username}")
    public ModelAndView editarPerfil(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            ModelAndView mav = new ModelAndView("usuario/editar");
            mav.addObject("user", user);
            return mav;
        }
        return new ModelAndView("redirect:/usuario/perfil");
    }

    @PostMapping("/actualizar")
    public String actualizarPerfil(@ModelAttribute User user, @RequestParam("img") MultipartFile imageFile) {
        User existingUser = userService.findByUsername(user.getUsername());

        if (existingUser != null) {
            existingUser.setFullname(user.getFullname());
            existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            if (!imageFile.isEmpty()) {
                try {
                    String imageName = uploadFileService.saveImg(imageFile);
                    existingUser.setImage(imageFile.getBytes());
                    existingUser.setImageName(imageName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            userService.save(existingUser);
        }

        return "redirect:/usuario/perfil";
    }
    
    
    @Transactional
    @GetMapping("/eliminar")
    public String eliminarCuenta() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByUsername(username);

        if (user != null) {
            adService.deleteItemsByUser(user);

            transactionService.deleteTransactionsByUser(user);

            userService.deleteByUsername(username);
            SecurityContextHolder.clearContext();
        }

        return "redirect:/logout";
    }
    
    @GetMapping("/cerrar")
    public String cerrarSesion() {
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
    
    @GetMapping("/direccion")
    public String showAddressForm(Principal principal, Model model, HttpSession session) {
        User user = userService.findByUsername(principal.getName());
        Address address = user.getAddress();
        if (address == null) {
            address = new Address();
        }
        model.addAttribute("address", address);
        model.addAttribute("redirectAfterAddress", session.getAttribute("redirectAfterAddress"));
        return "usuario/direccion";
    }

    @PostMapping("/direccion")
    public String saveAddress(Principal principal,
                              @RequestParam("street") String street,
                              @RequestParam("number") int number,
                              @RequestParam("floor") Integer floor,
                              @RequestParam("door") String door,
                              @RequestParam("city") String city,
                              @RequestParam("province") String province,
                              @RequestParam("postalCode") String postalCode,
                              @RequestParam("country") String country,
                              HttpSession session) {
        User user = userService.findByUsername(principal.getName());
        Address address = user.getAddress();
        if (address == null) {
            address = new Address();
        }
        address.setStreet(street);
        address.setNumber(number);
        address.setFloor(floor);
        address.setDoor(door);
        address.setCity(city);
        address.setProvince(province);
        address.setPostalCode(postalCode);
        address.setCountry(country);

        user.setAddress(address);
        userService.save(user);

        String redirectUrl = (String) session.getAttribute("redirectAfterAddress");
        session.removeAttribute("redirectAfterAddress");

        if (redirectUrl != null) {
            return "redirect:" + redirectUrl;
        } else {
            return "redirect:/usuario/perfil";
        }
    }
    
    @GetMapping("/transacciones")
    public String getUserTransactions(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Transaction> transactions = transactionService.findTransactionsBySeller(user);
        model.addAttribute("transactions", transactions);
        model.addAttribute("user", user);
        return "usuario/transacciones";
    }
    
    @GetMapping("/compras")
    public String getUserPurchases(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Transaction> purchases = transactionService.findTransactionsByBuyer(user);
        model.addAttribute("purchases", purchases);
        model.addAttribute("user", user);
        return "usuario/compras";
    }
    
    @PostMapping("/transacciones/ocultar/{transactionId}")
    public String hideTransactionForSeller(@PathVariable Long transactionId) {
        transactionService.hideTransactionForSeller(transactionId);
        return "redirect:/usuario/transacciones";
    }

    @PostMapping("/compras/ocultar/{transactionId}")
    public String hideTransactionForBuyer(@PathVariable Long transactionId) {
        transactionService.hideTransactionForBuyer(transactionId);
        return "redirect:/usuario/compras";
    }

}

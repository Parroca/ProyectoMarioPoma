package cat.institutmarianao.proyecto.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.User;
import cat.institutmarianao.proyecto.service.UploadFileService;
import cat.institutmarianao.proyecto.service.AdService;
import cat.institutmarianao.proyecto.service.UserService;
import cat.institutmarianao.proyecto.service.TransactionService;
import cat.institutmarianao.proyecto.model.Transaction;
import cat.institutmarianao.proyecto.model.Address;

@Controller
@RequestMapping("/anuncios")
public class AdController {

	@Autowired
	private AdService adService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private UploadFileService uploadFileService;
	 
	@Autowired
	private TransactionService transactionService;
	
	 @GetMapping("")
	 public String anuncios(Model model) {
	     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     String username = ((UserDetails) authentication.getPrincipal()).getUsername();
	     User user = userService.findByUsername(username);

	     if (user != null) {
	         List<Item> items = adService.findAllByUsuario(user);
	         model.addAttribute("items", items);
	         model.addAttribute("user", user);
	         model.addAttribute("isAdminView", false); 
	     }
	     
	     return "anuncios/anuncios";
	 }
	
	 @GetMapping("/admin/{username}")
	 @PreAuthorize("hasRole('ROLE_ADMIN')")
	 public String adminAnuncios(@PathVariable("username") String username, Model model) {
	     User user = userService.findByUsername(username);

	     if (user != null) {
	         List<Item> items = adService.findAllByUsuario(user);
	         model.addAttribute("items", items);
	         model.addAttribute("user", user); 
	         model.addAttribute("isAdminView", true); 
	         model.addAttribute("adminViewing", true);
	     }

	     return "anuncios/anuncios";
	 }
	 
	@GetMapping("/crear")
	public ModelAndView crear() {
		ModelAndView mav = new ModelAndView("anuncios/crear");
        mav.addObject("item", new Item());
        return mav;
	}

	@PostMapping("/guardar")
    public String save(@ModelAttribute Item item, @RequestParam("img") MultipartFile imageFile, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "anuncios/crear";
        }

        item.setUsuario(user);

        if (!imageFile.isEmpty()) {
            try {
                String imageName = uploadFileService.saveImg(imageFile);
                item.setImage(imageFile.getBytes()); 
                item.setImageName(imageName); 
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Error al guardar la imagen.");
                return "anuncios/crear";
            }
        } else {
            item.setImageName("default.jpg"); 
        }

        try {
            adService.save(item);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar el item.");
            return "anuncios/crear";
        }

        return "redirect:/anuncios";
    }
	
	@GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Item item = adService.getById(id);
        if (item != null) {
            ModelAndView mav = new ModelAndView("anuncios/editar");
            mav.addObject("item", item);
            return mav;
        }
        return new ModelAndView("redirect:/anuncios");
    }

	@PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Item item, @RequestParam("img") MultipartFile imageFile, Model model) {
        System.out.println("Updating item: " + item);

        Item existingItem = adService.getById(item.getId());
        if (existingItem != null) {
            if (imageFile.isEmpty()) {
                item.setImage(existingItem.getImage());
                item.setImageName(existingItem.getImageName());
            } else {
                if (!"default.jpg".equals(existingItem.getImageName())) {
                    try {
                        uploadFileService.deleteImg(existingItem.getImageName());
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("error", "Error al eliminar la imagen anterior.");
                        return "anuncios/editar";
                    }
                }
                try {
                    String imageName = uploadFileService.saveImg(imageFile);
                    item.setImage(imageFile.getBytes());
                    item.setImageName(imageName);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("error", "Error al guardar la nueva imagen.");
                    return "anuncios/editar";
                }
            }
            item.setUsuario(existingItem.getUsuario()); 
            try {
                adService.update(item);
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error", "Error al actualizar el item.");
                return "anuncios/editar";
            }
        } else {
            model.addAttribute("error", "El item no existe.");
            return "anuncios/editar";
        }
        return "redirect:/anuncios";
    }


	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable("id") Long id) {
	    Item item = adService.getById(id);
	    if (item != null) {
	        if (!"default.jpg".equals(item.getImageName())) {
	            try {
	                uploadFileService.deleteImg(item.getImageName());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        adService.deleteById(id);
	    }
	    return "redirect:/anuncios";
	}
	
	@PostMapping("/confirm-purchase/{itemId}")
    public String confirmPurchase(@PathVariable("itemId") Long itemId, @ModelAttribute Address address, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByUsername(username);

        if (user != null) {
            Item item = adService.getById(itemId);
            if (item != null) {
                Transaction transaction = new Transaction();
                transaction.setBuyer(user);
                transaction.setItem(item);
                transactionService.createTransaction(transaction, address);

                return "transaction/purchase_success";
            }
        }

        return "/home"; 
    }
	
	 @GetMapping("/{id}")
	 public String getItem(@PathVariable("id") Long id, Model model) {
	     Item item = adService.getById(id);
	     if (item != null) {
	         model.addAttribute("item", item);
	         return "usuario/item"; 
	      } else {
	    	 return "error/404"; 
	      	}
	 }
	 
	 @PostMapping("/denunciar/{id}")
	 public String denunciarProducto(@PathVariable Long id, Model model) {
	     Item item = adService.getById(id);
	     if (item != null) {
	         item.setDenunciaEstado(Item.DenunciaEstado.DENUNCIADO);
	         adService.save(item);
	         model.addAttribute("successMessage", "El producto ha sido denunciado y será revisado por un administrador.");
	     } else {
	         model.addAttribute("errorMessage", "El producto no existe.");
	     }
	     return "redirect:/home";
	 }

}

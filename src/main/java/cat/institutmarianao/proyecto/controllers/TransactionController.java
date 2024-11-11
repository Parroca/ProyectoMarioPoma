package cat.institutmarianao.proyecto.controllers;

import cat.institutmarianao.proyecto.model.Address;
import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.Transaction;
import cat.institutmarianao.proyecto.model.User;
import cat.institutmarianao.proyecto.service.AdService;
import cat.institutmarianao.proyecto.service.TransactionService;
import cat.institutmarianao.proyecto.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TransactionController {

    @Autowired
    private AdService adService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/purchase/confirm")
    public String showConfirmPurchase(@RequestParam("itemId") long itemId, Principal principal, Model model) {
        try {
            User user = userService.findByUsername(principal.getName());
            Hibernate.initialize(user.getAddress());  
            Item item = adService.getById(itemId);
            Address address = user.getAddress();

            if (item == null) {
                throw new Exception("Item or address not found");
            }
            
            if (address == null) {
                return "redirect:/usuario/direccion";
            }

            model.addAttribute("item", item);
            model.addAttribute("user", user);
            model.addAttribute("address", address);

            return "transaction/compra";
        } catch (Exception e) {
            logger.error("Error during purchase confirmation", e);
            return "error";
        }
    }

    @PostMapping("/purchase/confirm")
    public String confirmPurchase(@RequestParam("itemId") long itemId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            Item item = adService.getById(itemId);
            User user = userService.findByUsername(principal.getName());
            Address address = user.getAddress();

            if (item != null && address != null) {
                User buyer = userService.findByUsername(principal.getName());
                User seller = item.getUsuario();

                Transaction transaction = new Transaction();
                transaction.setBuyer(buyer);
                transaction.setSeller(seller);
                transaction.setItem(item);

                Transaction savedTransaction = transactionService.createTransaction(transaction, address);

                return "redirect:/purchase/success?transactionId=" + savedTransaction.getId();
            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            logger.error("Error during purchase confirmation", e);
            return "redirect:/";
        }
    }

    @GetMapping("/purchase/success")
    public String purchaseSuccess(@RequestParam("transactionId") Long transactionId, Model model) {
        try {
            Transaction transaction = transactionService.getTransactionById(transactionId);
            
            if (transaction == null) {
                throw new Exception("Transaction not found");
            }

            model.addAttribute("transaction", transaction);
            
            return "transaction/purchase_success";
        } catch (Exception e) {
            logger.error("Error retrieving transaction", e);
            return "redirect:/error"; 
        }
    }
}

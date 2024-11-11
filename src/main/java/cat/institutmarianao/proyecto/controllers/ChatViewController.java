package cat.institutmarianao.proyecto.controllers;

import cat.institutmarianao.proyecto.model.Chat;
import cat.institutmarianao.proyecto.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;
import java.util.stream.Collectors;
import cat.institutmarianao.proyecto.model.Message;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import cat.institutmarianao.proyecto.service.UploadFileService;

import java.util.List;

@Controller
public class ChatViewController {

    @Autowired
    private ChatService chatService;
    
    @Autowired
    private UploadFileService uploadFileService;


    @GetMapping("/chats")
    public String getChatsPage(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<Chat> chats = chatService.getChatsByUserId(userDetails.getUsername());
        
        if (chats.isEmpty()) {
            model.addAttribute("message", "No tienes chats activos en este momento.");
        }
        
        model.addAttribute("chats", chats);
        return "chats";
    }

    @GetMapping("/chat/{id}")
    public String getChatPage(@PathVariable Long id, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        chatService.markMessagesAsRead(id, userDetails.getUsername());

        List<Message> messages = chatService.getMessagesByChatId(id).stream()
                                            .sorted(Comparator.comparing(Message::getTimestamp))
                                            .collect(Collectors.toList());
                                            
        model.addAttribute("chatId", id);
        model.addAttribute("messages", messages);
        return "chat";
    }
    
    @GetMapping("/chats/start/{username}")
    public String startChat(@PathVariable String username, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String currentUsername = userDetails.getUsername();
        Chat chat = chatService.createChat(currentUsername, username);

        return "redirect:/chat/" + chat.getId(); 
    }
    
    @PostMapping("/chat/{chatId}/messages")
    public String sendMessage(@PathVariable Long chatId, 
                              @RequestParam(value = "messageContent", required = false) String messageContent,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              Authentication authentication) throws IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String currentUsername = userDetails.getUsername();

        if ((messageContent == null || messageContent.trim().isEmpty()) && (imageFile == null || imageFile.isEmpty())) {
            return "redirect:/chat/" + chatId + "?error=emptyMessage";
        }

        String imageName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageName = uploadFileService.saveImg(imageFile);
        }

        chatService.sendMessage(chatId, currentUsername, messageContent, imageName);

        return "redirect:/chat/" + chatId;
    }
    
}
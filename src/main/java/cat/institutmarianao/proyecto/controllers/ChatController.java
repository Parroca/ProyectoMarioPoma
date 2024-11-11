package cat.institutmarianao.proyecto.controllers;

import cat.institutmarianao.proyecto.model.Chat;
import cat.institutmarianao.proyecto.model.Message;
import cat.institutmarianao.proyecto.service.ChatService;
import cat.institutmarianao.proyecto.service.UploadFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;
    
    @Autowired
    private UploadFileService uploadFileService;


    @PostMapping("/create")
    public Chat createChat(@RequestParam String user1Id, @RequestParam String user2Id) {
        return chatService.createChat(user1Id, user2Id);
    }

    @GetMapping("/user/{userId}")
    public List<Chat> getChatsByUserId(@PathVariable String userId) {
        return chatService.getChatsByUserId(userId);
    }

    @PostMapping("/{chatId}/messages")
    public Message sendMessage(@PathVariable Long chatId, 
                               @RequestParam String senderId, 
                               @RequestParam String content, 
                               @RequestParam(required = false) MultipartFile imageFile) throws IOException {
        String imageName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageName = uploadFileService.saveImg(imageFile);
        }

        return chatService.sendMessage(chatId, senderId, content, imageName);
    }

    @GetMapping("/{chatId}/messages")
    public List<Message> getMessagesByChatId(@PathVariable Long chatId) {
        return chatService.getMessagesByChatId(chatId);
    }
}
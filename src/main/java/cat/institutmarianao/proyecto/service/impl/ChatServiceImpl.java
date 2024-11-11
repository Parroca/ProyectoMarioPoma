package cat.institutmarianao.proyecto.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.institutmarianao.proyecto.model.Chat;
import cat.institutmarianao.proyecto.model.Message;
import cat.institutmarianao.proyecto.model.User;
import cat.institutmarianao.proyecto.repository.ChatRepository;
import cat.institutmarianao.proyecto.repository.MessageRepository;
import cat.institutmarianao.proyecto.repository.UserRepository;
import cat.institutmarianao.proyecto.service.ChatService;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;
   
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Chat createChat(String user1Id, String user2Id) {
        User user1 = userRepository.findByUsername(user1Id);
        User user2 = userRepository.findByUsername(user2Id);

        List<Chat> existingChats = chatRepository.findByUser1UsernameOrUser2Username(user1Id, user2Id);
        for (Chat chat : existingChats) {
            if ((chat.getUser1().getUsername().equals(user1Id) && chat.getUser2().getUsername().equals(user2Id)) ||
                (chat.getUser2().getUsername().equals(user1Id) && chat.getUser1().getUsername().equals(user2Id))) {
                return chat;
            }
        }

        Chat chat = new Chat();
        chat.setUser1(user1);
        chat.setUser2(user2);

        return chatRepository.save(chat);
    }

    @Override
    public List<Chat> getChatsByUserId(String userId) {
        return chatRepository.findByUser1UsernameOrUser2Username(userId, userId);
    }

    @Override
    public Message sendMessage(Long chatId, String senderId, String content, String imageName) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        User sender = userRepository.findByUsername(senderId);

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(new Date());
        message.setImageName(imageName); 

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }
    
    @Override
    public void deleteChatsByUser(String username) {
        chatRepository.deleteByUser1UsernameOrUser2Username(username, username);
    }
    
    @Override
    public void markMessagesAsRead(Long chatId, String userId) {
        List<Message> messages = messageRepository.findByChatId(chatId);
        for (Message message : messages) {
            if (!message.isRead() && !message.getSender().getUsername().equals(userId)) {
                message.setRead(true);
                messageRepository.save(message);
            }
        }
    }
}
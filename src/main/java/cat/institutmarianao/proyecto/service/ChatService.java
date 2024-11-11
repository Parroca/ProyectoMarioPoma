package cat.institutmarianao.proyecto.service;

import cat.institutmarianao.proyecto.model.Chat;
import cat.institutmarianao.proyecto.model.Message;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.List;

public interface ChatService {
    Chat createChat(String user1Id, String user2Id);
    List<Chat> getChatsByUserId(String userId);
    Message sendMessage(Long chatId, String senderId, String content, String imageName) throws IOException;
    List<Message> getMessagesByChatId(Long chatId);
    void deleteChatsByUser(String username);
    public void markMessagesAsRead(Long chatId, String userId);
}
package cat.institutmarianao.proyecto.repository;

import cat.institutmarianao.proyecto.model.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cat.institutmarianao.proyecto.model.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @EntityGraph(attributePaths = {"chat", "sender"})
    List<Message> findByChatId(Long chatId);
    List<Message> findBySender(User sender);
}
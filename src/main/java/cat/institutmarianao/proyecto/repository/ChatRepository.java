package cat.institutmarianao.proyecto.repository;

import cat.institutmarianao.proyecto.model.Chat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @EntityGraph(attributePaths = {"user1", "user2", "messages"})
    List<Chat> findByUser1UsernameOrUser2Username(String user1Username, String user2Username);
    void deleteByUser1UsernameOrUser2Username(String user1Username, String user2Username);
}
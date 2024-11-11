package cat.institutmarianao.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;

import cat.institutmarianao.proyecto.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	void deleteByUsername(String username);
	List<User> findByVetado(boolean vetado);
	
	@EntityGraph(attributePaths = {"items"})
	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findByUsername(String username);
}

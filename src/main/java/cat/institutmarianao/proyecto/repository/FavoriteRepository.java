package cat.institutmarianao.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.proyecto.model.Favorite;
import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
 
    List<Favorite> findByUserAndItem(User user, Item item);
    
    @Query("SELECT f FROM Favorite f WHERE f.user = :user")
    Page<Favorite> findByUser(@Param("user") User user, Pageable pageable);
}

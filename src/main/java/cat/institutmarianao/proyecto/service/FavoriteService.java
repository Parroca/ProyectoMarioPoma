package cat.institutmarianao.proyecto.service;

import java.util.List;
import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {
	Page<Item> getFavoriteItems(String username, Pageable pageable);
    void addToFavorites(User user, Item item);
    void removeFromFavorites(User user, Item item);
    boolean isItemInFavorites(User user, Item item);
}

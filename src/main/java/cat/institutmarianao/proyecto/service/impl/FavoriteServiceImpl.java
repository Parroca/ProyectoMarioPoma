package cat.institutmarianao.proyecto.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.institutmarianao.proyecto.model.Favorite;
import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.User;
import cat.institutmarianao.proyecto.repository.FavoriteRepository;
import cat.institutmarianao.proyecto.repository.UserRepository;
import cat.institutmarianao.proyecto.service.FavoriteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<Item> getFavoriteItems(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return Page.empty();
        }
        Page<Favorite> favorites = favoriteRepository.findByUser(user, pageable);
        return favorites.map(Favorite::getItem);
    }

    @Override
    public void addToFavorites(User user, Item item) {
        if (!isItemInFavorites(user, item)) {
            Favorite favorite = new Favorite(user, item);
            favoriteRepository.save(favorite);
        }
    }

    @Override
    public void removeFromFavorites(User user, Item item) {
        List<Favorite> favorites = favoriteRepository.findByUserAndItem(user, item);
        for (Favorite favorite : favorites) {
            favoriteRepository.delete(favorite);
        }
    }

    @Override
    public boolean isItemInFavorites(User user, Item item) {
        List<Favorite> favorites = favoriteRepository.findByUserAndItem(user, item);
        return !favorites.isEmpty();
    }
}

package cat.institutmarianao.proyecto.service;

import java.util.List;

import cat.institutmarianao.proyecto.model.Address;
import cat.institutmarianao.proyecto.model.User;

public interface UserService {
	User findByUsername(String username);
    void deleteByUsername(String username);
    void deleteUser(String username);
    List<User> getAllUsers();
    void save(User user);
    void update(User user);
    List<User> findAll();
    Address getUserAddress(String username);
    List<User> findVetados();
}

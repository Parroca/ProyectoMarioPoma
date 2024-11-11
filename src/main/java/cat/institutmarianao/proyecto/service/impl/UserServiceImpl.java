package cat.institutmarianao.proyecto.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cat.institutmarianao.proyecto.model.Address;
import cat.institutmarianao.proyecto.model.User;
import cat.institutmarianao.proyecto.repository.TransactionRepository;
import cat.institutmarianao.proyecto.repository.UserRepository;
import cat.institutmarianao.proyecto.service.AdService;
import cat.institutmarianao.proyecto.service.TransactionService;
import cat.institutmarianao.proyecto.service.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private AdService adService;

    
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {

            transactionService.deleteTransactionsByUser(user);
            
            adService.deleteItemsByUser(user);

            userRepository.delete(user);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
    
    @Override
    @Transactional
    public void deleteUser(String username) {
        adService.deleteItemsByUser(username);
        transactionService.deleteTransactionsByUser(userRepository.findByUsername(username));
        userRepository.deleteByUsername(username);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);  
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (user.isVetado()) {  
            throw new UsernameNotFoundException("El usuario está vetado");
        }
        return user;
    }
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    public Address getUserAddress(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? user.getAddress() : null;
    }
    
    @Override
    public List<User> findVetados() {
        return userRepository.findByVetado(true);
    }

}

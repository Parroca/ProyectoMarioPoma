package cat.institutmarianao.proyecto.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.Item.Categoria;
import cat.institutmarianao.proyecto.model.Item.DenunciaEstado;
import cat.institutmarianao.proyecto.repository.AdRepository;
import cat.institutmarianao.proyecto.repository.UserRepository;
import cat.institutmarianao.proyecto.service.AdService;
import jakarta.validation.constraints.Positive;
import cat.institutmarianao.proyecto.model.User;

@Service
public class AdServiceImpl implements AdService {
	
	@Autowired
	private AdRepository adrepository;

	@Autowired
    private UserRepository userRepository;
	
	@Override
    public List<Item> findAll() {
        return adrepository.findAll();
    }

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return adrepository.findAll(pageable);
    }

    @Override
    public Page<Item> findAllByCategoryAndOcultoFalse(Categoria category, Pageable pageable) {
        return adrepository.findByCategoriaAndOcultoFalse(category, pageable);
    }

    @Override
    public Page<Item> findAllByUsuarioNotAndOcultoFalse(User usuario, Pageable pageable) {
        return adrepository.findByUsuarioNotAndOcultoFalse(usuario, pageable);
    }

    @Override
    public Item getById(Long id) {
        Optional<Item> optionalItem = adrepository.findById(id);
        return optionalItem.orElse(null);
    }

    @Override
    public Item getByName(String name) {
         Optional<Item> optionalItem = adrepository.findByName(name);
         return optionalItem.orElse(null);
    }

    @Override
    public Item save(Item item) {
        return adrepository.save(item);
    }

    @Override
    public void deleteById(@Positive Long id) {
        adrepository.deleteById(id);
    }

    @Override
    public void update(Item item) {
        adrepository.save(item);
    }
    
    @Override
    public List<Item> findAllByUsuario(User usuario) {
        return adrepository.findAllByUsuario(usuario);
    }
    
    @Override
    public Page<Item> findAllByUsuarioNot(User usuario, Pageable pageable) {
        return adrepository.findByUsuarioNotAndOcultoFalse(usuario, pageable);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<Item> searchByNameAndOcultoFalse(String name, Pageable pageable) {
        return adrepository.findByNameContainingIgnoreCaseAndOcultoFalse(name, pageable);
    }

    @Override
    public Page<Item> searchByNameAndUsuarioNotAndOcultoFalse(String name, User usuario, Pageable pageable) {
        return adrepository.findByNameContainingIgnoreCaseAndUsuarioNotAndOcultoFalse(name, usuario, pageable);
    }
    
    
    @Override
    public void deleteItemsByUser(User usuario) {
        adrepository.deleteByUsuario(usuario);
    }

    @Transactional
    @Override
    public void deleteItemsByUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            deleteItemsByUser(user);
        }
    }
    
    @Override
    public List<Item> findByDenunciaEstado(DenunciaEstado denunciaEstado) {
        return adrepository.findByDenunciaEstado(denunciaEstado);
    }
    
    @Override
    public List<Item> findOcultos() {
        return adrepository.findByOculto(true);
    }
    
    @Override
    public Page<Item> findAllByCategoryAndUsuarioNotAndOcultoFalse(Categoria category, User usuario, Pageable pageable) {
        return adrepository.findByCategoriaAndUsuarioNotAndOcultoFalse(category, usuario, pageable);
    }

    @Override
    public Page<Item> findAllByOcultoFalse(Pageable pageable) {
        return adrepository.findByOcultoFalse(pageable);
    }
    
    @Override
    public Page<Item> findPaginatedByUsuario(User usuario, Pageable pageable) {
        if (usuario == null) {
            
            return Page.empty(pageable);
        }
        return adrepository.findByUsuario(usuario, pageable);
    }
    
    @Override
    public List<Item> findSimilarItems(Item item) {
        return adrepository.findTop5ByCategoriaAndIdNotAndOcultoFalse(item.getCategoria(), item.getId());
    }
    
    @Override
    public Page<Item> findPaginatedByUsuarioAndOcultoFalse(User usuario, Pageable pageable) {
        return adrepository.findByUsuarioAndOcultoFalse(usuario, pageable);
    }
    
    /*
    @Override
    public void rateItem(long itemId, double rating) {
        Item item = adrepository.findById(itemId).orElse(null);
        if (item != null) {
            item.setRating(rating);
            adrepository.save(item);
        }
    }*/
}

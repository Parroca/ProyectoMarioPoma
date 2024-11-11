package cat.institutmarianao.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.Item.Categoria;
import cat.institutmarianao.proyecto.model.Item.DenunciaEstado;
import cat.institutmarianao.proyecto.model.User;
import jakarta.validation.constraints.Positive;

public interface AdService {
	List<Item> findAll();
	
    Page<Item> findAll(Pageable pageable);
    
    Page<Item> findAllByCategoryAndOcultoFalse(Categoria category, Pageable pageable); 
    
    Page<Item> findAllByUsuarioNotAndOcultoFalse(User usuario, Pageable pageable);  
    
    Item getById(Long id);
    
    Item getByName(String name);
    
    Item save(Item item);
    
    void deleteById(@Positive Long id);
    
    void update(Item item);
    
    List<Item> findAllByUsuario(User usuario);
    
    Page<Item> findAllByUsuarioNot(User usuario, Pageable pageable);
    
    User findByUsername(String username);
    
    Page<Item> searchByNameAndOcultoFalse(String name, Pageable pageable);  
    
    Page<Item> searchByNameAndUsuarioNotAndOcultoFalse(String name, User usuario, Pageable pageable);  
    
    void deleteItemsByUser(User usuario); 
    
    void deleteItemsByUser(String username);
    
    List<Item> findByDenunciaEstado(DenunciaEstado denunciaEstado);
    
    List<Item> findOcultos();
    
    List<Item> findSimilarItems(Item item);
    
    Page<Item> findPaginatedByUsuarioAndOcultoFalse(User usuario, Pageable pageable);
    
    Page<Item> findAllByCategoryAndUsuarioNotAndOcultoFalse(Categoria category, User usuario, Pageable pageable);
    
    Page<Item> findAllByOcultoFalse(Pageable pageable);
    
    Page<Item> findPaginatedByUsuario(User usuario, Pageable pageable);
	 
	 /*void rateItem(long itemId, double rating);*/
}

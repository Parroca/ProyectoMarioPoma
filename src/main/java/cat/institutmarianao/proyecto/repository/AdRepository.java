package cat.institutmarianao.proyecto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import cat.institutmarianao.proyecto.model.Item;
import cat.institutmarianao.proyecto.model.Item.Categoria;
import cat.institutmarianao.proyecto.model.Item.DenunciaEstado;
import cat.institutmarianao.proyecto.model.User;

@Repository
public interface AdRepository extends JpaRepository<Item, Long> {
		
	    Page<Item> findByCategoriaAndOcultoFalse(Item.Categoria category, Pageable pageable);
	    
	    Optional<Item> findByName(String name);
	    
	    Page<Item> findByUsuarioNotAndOcultoFalse(User usuario, Pageable pageable);

	    Page<Item> findByCategoriaAndUsuarioNotAndOcultoFalse(Categoria category, User usuario, Pageable pageable);
	    
	    List<Item> findAllByUsuario(User usuario);
	    
	    Page<Item> findByNameContainingIgnoreCaseAndOcultoFalse(String name, Pageable pageable);
	    
	    Page<Item> findByNameContainingIgnoreCaseAndUsuarioNotAndOcultoFalse(String name, User usuario, Pageable pageable);
	    
	    @Transactional
	    @Modifying
	    void deleteByUsuario(User usuario);
	    
	    List<Item> findByDenunciaEstado(DenunciaEstado denunciaEstado);
	    
	    List<Item> findByOculto(boolean oculto);
	    
	    Page<Item> findByOcultoFalse(Pageable pageable);
	    
	    Page<Item> findByUsuario(User usuario, Pageable pageable);
	    
	    List<Item> findTop5ByCategoriaAndIdNotAndOcultoFalse(Item.Categoria categoria, Long id);
	    
	    Page<Item> findByUsuarioAndOcultoFalse(User usuario, Pageable pageable);
}

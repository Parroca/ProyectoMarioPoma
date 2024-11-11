package cat.institutmarianao.proyecto.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "item")
@Data
public class Item implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String description;
	private double price;

	public enum Categoria {
		Hogar, Deporte, Coches, Motos, Barcos, Moda, Electrónica, Informática, Otros
	}

	public enum Estado {
		nuevo, seminuevo, usado
	}
	
	public enum DenunciaEstado {
	    NO_DENUNCIADO, DENUNCIADO
	}
	
	private boolean oculto = false;
	
	@Enumerated(EnumType.STRING)
    private Categoria categoria;
	
	@Enumerated(EnumType.STRING)
	private DenunciaEstado denunciaEstado = DenunciaEstado.NO_DENUNCIADO;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] image;
	
	@Column(name = "image_name")
	private String imageName = "default.jpg";;
	
	@ManyToOne
	private User usuario;
	
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Favorite> favorites = new ArrayList<>();
	
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
	
	//Para futura implementacion de añadir valoracion a los items
	/*@Column(name = "rating")
	 private double rating;*/

	private static final long serialVersionUID = 1L;
	
	@Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageName='" + imageName + '\'' +
                ", categoria=" + categoria +
                /*", rating=" + rating +*/
                '}';
    }
}

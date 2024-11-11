package cat.institutmarianao.proyecto.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Address implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String street;
	private int number;
	private Integer floor;
	private String door;
	private String city;
	private String province;
	private String postalCode;
	private String country;
	
	@OneToOne(mappedBy = "address")
    private User user;
	
	 public String getFullAddress() {
	        return street + " " + number + (floor != null ? ", floor " + floor : "") +
	               (door != null ? ", door " + door : "") + ", " + city + ", " + province +
	               ", " + postalCode + ", " + country;
	    }

}
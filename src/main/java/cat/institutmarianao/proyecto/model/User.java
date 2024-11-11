package cat.institutmarianao.proyecto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name= "users")
@Data
public class User implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String username;
	private String role;
	private String password;
	private String fullname;
	private String email;
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] image;
	
	@Column(name = "image_name")
	private String imageName;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;

	@OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> boughtTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> soldTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();
    
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> sentMessages = new ArrayList<>();
    
    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chatsAsUser1 = new ArrayList<>();

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chatsAsUser2 = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();
    
    private boolean vetado = false;
	
	 public User() {}

	    public User(String username, String role, String password, String fullname, String email, Address address, List<Item> items) {
	        this.username = username;
	        this.role = role;
	        this.password = password;
	        this.fullname = fullname;
	        this.email = email;
	        this.address = address;
	        this.items = items;
	    }

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "', fullname='" + fullname + "', email='" + email + "', imageName='" + imageName + "'}";
    }
}
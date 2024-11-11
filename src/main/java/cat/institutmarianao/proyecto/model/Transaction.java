package cat.institutmarianao.proyecto.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import jakarta.persistence.CascadeType;

@Entity
@Data
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "buyer_username", referencedColumnName = "username")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "seller_username", referencedColumnName = "username")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Date transactionDate;
    
    @Column(name = "hidden_for_buyer", nullable = false)
    private boolean hiddenForBuyer = false;

    @Column(name = "hidden_for_seller", nullable = false)
    private boolean hiddenForSeller = false;
}
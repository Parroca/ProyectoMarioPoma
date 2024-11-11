package cat.institutmarianao.proyecto.model;

import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import cat.institutmarianao.proyecto.model.User;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;
    
    @PrePersist
    protected void onCreate() {
        this.timestamp = new Date(); 
    }
    
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;
    
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
    
    @Lob
	@Column(columnDefinition = "LONGBLOB")
    private byte[] image;
    
    private String imageName;
    
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
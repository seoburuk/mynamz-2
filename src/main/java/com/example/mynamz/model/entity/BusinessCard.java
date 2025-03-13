package com.example.mynamz.model.entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import com.example.mynamz.model.entity.User; 

@Entity
@Table(name = "business_cards")
public class BusinessCard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String company;
    
    @Column(nullable = false)
    private String position;
    
    @Column(nullable = false)
    private String phone;
    
    private String email;
    
    private String department;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "is_owner")
    private boolean isOwner = false;
    
    @Column(name = "owner_username")
    private String ownerUsername;
    
    @Column(name = "description", length = 255)
    private String description;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // 기존 Getter/Setter
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCompany() { return company; }
    public String getPosition() { return position; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isOwner() { return isOwner; }
    public String getOwnerUsername() { return ownerUsername; }
    public User getUser() { return user; }
    
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCompany(String company) { this.company = company; }
    public void setPosition(String position) { this.position = position; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartment(String department) { this.department = department; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setOwner(boolean isOwner) { this.isOwner = isOwner; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }
    public void setUser(User user) { this.user = user; }
    
    
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
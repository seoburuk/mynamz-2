package com.example.mynamz.model.dto;

import com.example.mynamz.model.entity.BusinessCard;

public class BusinessCardDTO {
    private Long id;
    private String name;
    private String company;
    private String position;
    private String phone;
    private String email;
    private String department;
    private Long userId;
    private String description;
    // 기본 생성자
    public BusinessCardDTO() {
    }

    // 모든 필드 생성자
    // 모든 필드 생성자 수정
// 모든 필드 생성자 수정
public BusinessCardDTO(Long id, String name, String company, String position,
                      String phone, String email, String department, String description) {  // description 파라미터 추가
    this.id = id;
    this.name = name;
    this.company = company;
    this.position = position;
    this.phone = phone;
    this.email = email;
    this.department = department;
    this.description = description;  // description 설정
}

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // 유효성 검사
    public boolean isValid() {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (company == null || company.trim().isEmpty()) {
            return false;
        }
        // 전화번호 형식 검사 (000-0000-0000)
        if (phone == null || !phone.matches("\\d{3}-\\d{4}-\\d{4}")) {
            return false;
        }
        // 이메일 형식 검사 (선택적)
        if (email != null && !email.isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return false;
            }
        }
        return true;
    }

    // Entity 변환 메서드
    public BusinessCard toEntity() {
        BusinessCard card = new BusinessCard();
        card.setId(this.id);
        card.setName(this.name);
        card.setCompany(this.company);
        card.setPosition(this.position);
        card.setPhone(this.phone);
        card.setEmail(this.email);
        card.setDepartment(this.department);
        card.setDescription(this.description);
        return card;
    }

    // Entity로부터 DTO 생성
    public static BusinessCardDTO fromEntity(BusinessCard card) {
        if(card == null) {
            return null;
        }
        BusinessCardDTO dto = new BusinessCardDTO();
        dto.setId(card.getId());
        dto.setName(card.getName());
        dto.setCompany(card.getCompany());
        dto.setPosition(card.getPosition());
        dto.setPhone(card.getPhone());
        dto.setEmail(card.getEmail());
        dto.setDepartment(card.getDepartment());
        dto.setDescription(card.getDescription()); 
        dto.setUserId(card.getUser() != null ? card.getUser().getId() : null);  // userId 설정
        return dto;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
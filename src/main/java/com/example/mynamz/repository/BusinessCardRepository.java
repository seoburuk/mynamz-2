package com.example.mynamz.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.mynamz.model.entity.BusinessCard;
import com.example.mynamz.model.entity.User;

@Repository
public interface BusinessCardRepository extends JpaRepository<BusinessCard, Long> {
	 @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BusinessCard b " +
	           "WHERE b.ownerUsername = :ownerUsername AND b.user = :user")
	    boolean existsByOwnerUsernameAndUser(@Param("ownerUsername") String ownerUsername, @Param("user") User user);
	
   
    
	@Query("SELECT b FROM BusinessCard b WHERE b.user = :user AND b.isOwner = true ORDER BY b.createdAt DESC")
    List<BusinessCard> findMyCards(@Param("user") User user);
    
    @Query("SELECT COUNT(b) FROM BusinessCard b WHERE b.user = :user AND b.isOwner = true")
    int countMyCards(@Param("user") User user);
    
    BusinessCard findByUserAndIsOwnerTrue(User user);
    
    @Query("SELECT b FROM BusinessCard b WHERE b.user.id = :userId AND b.isOwner = false")
    List<BusinessCard> findReceivedCards(@Param("userId") Long userId);
    
    @Query("SELECT b FROM BusinessCard b WHERE b.description LIKE %:keyword% AND b.user.id = :userId AND b.isOwner = false")
    List<BusinessCard> searchByDescription(@Param("keyword") String keyword, @Param("userId") Long userId);

	

    // 전체 검색에 description 포함
    @Query("SELECT b FROM BusinessCard b WHERE b.user.id = :userId AND b.isOwner = false " +
           "AND (b.name LIKE %:keyword% OR b.company LIKE %:keyword% OR b.department LIKE %:keyword% " +
           "OR b.position LIKE %:keyword% OR b.phone LIKE %:keyword% OR b.email LIKE %:keyword% " +
           "OR b.description LIKE %:keyword%)")
    List<BusinessCard> searchByKeyword(@Param("keyword") String keyword, @Param("userId") Long userId);
	
	@Query("SELECT b FROM BusinessCard b WHERE b.company LIKE %:company% AND b.user.id = :userId AND b.isOwner = false")
    List<BusinessCard> searchByCompanyReceived(
        @Param("company") String company,
        @Param("userId") Long userId
    );

    // 이름으로 검색 (받은 명함 중에서)
    @Query("SELECT b FROM BusinessCard b WHERE b.name LIKE %:name% AND b.user.id = :userId AND b.isOwner = false")
    List<BusinessCard> searchByNameReceived(
        @Param("name") String name,
        @Param("userId") Long userId
    );
    
    // 내 명함 존재 여부 확인
    boolean existsByOwnerUsernameAndIsOwnerTrue(String username);

    
    // 3. 받은 명함 검색 메서드
    
    
    @Query("SELECT b FROM BusinessCard b WHERE b.name LIKE %:keyword% AND b.user.id = :userId AND b.isOwner = false")
    List<BusinessCard> searchByName(@Param("keyword") String keyword, @Param("userId") Long userId);
    
    @Query("SELECT b FROM BusinessCard b WHERE b.company LIKE %:keyword% AND b.user.id = :userId AND b.isOwner = false")
    List<BusinessCard> searchByCompany(@Param("keyword") String keyword, @Param("userId") Long userId);
    
    @Query("SELECT b FROM BusinessCard b WHERE b.department LIKE %:keyword% AND b.user.id = :userId AND b.isOwner = false")
    List<BusinessCard> searchByDepartment(@Param("keyword") String keyword, @Param("userId") Long userId);
    
    // 4. 기타 유틸리티 메서드
    Optional<BusinessCard> findByOwnerUsername(String username);

    @Query("SELECT b FROM BusinessCard b WHERE b.user.id = :userId")
    List<BusinessCard> findByUserId(@Param("userId") Long userId);
    
    
}
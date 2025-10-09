package com.sosyalmedia.customerservice.repository;

import com.sosyalmedia.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    // Şirket adına göre bul
    Optional<Customer> findByCompanyName(String companyName);

    // Şirket adında arama yap (LIKE)
    List<Customer> findByCompanyNameContainingIgnoreCase(String companyName);

    // Sektöre göre listele
    List<Customer> findBySector(String sector);

    // Status'e göre listele
    List<Customer> findByStatus(Customer.CustomerStatus status);

    // Üyelik paketine göre
    List<Customer> findByMembershipPackage(String membershipPackage);

    // ID ile tüm ilişkileri yükle
    @Query("SELECT DISTINCT c FROM Customer c " +
            "LEFT JOIN FETCH c.targetAudience " +
            "LEFT JOIN FETCH c.contacts " +
            "LEFT JOIN FETCH c.socialMedia " +
            "LEFT JOIN FETCH c.seo " +
            "LEFT JOIN FETCH c.apiKey " +
            "LEFT JOIN FETCH c.media " +
            "WHERE c.id = :id AND c.deleted = false")
    Optional<Customer> findByIdWithAllRelations(@Param("id") Long id);

    // Şirket adı ile tüm ilişkileri yükle
    @Query("SELECT c FROM Customer c " +
            "LEFT JOIN FETCH c.targetAudience " +
            "LEFT JOIN FETCH c.contacts " +
            "LEFT JOIN FETCH c.socialMedia " +
            "LEFT JOIN FETCH c.seo " +
            "LEFT JOIN FETCH c.apiKey " +
            "LEFT JOIN FETCH c.media " +
            "WHERE c.companyName = :companyName AND c.deleted = false")
    Optional<Customer> findByCompanyNameWithAllRelations(@Param("companyName") String companyName);

    // Silinmiş kayıtları getir
    @Query(value = "SELECT * FROM customers WHERE deleted = true", nativeQuery = true)
    List<Customer> findAllDeleted();

    // Hard Delete (gerçekten sil)
    @Modifying
    @Query("DELETE FROM Customer c WHERE c.id = :id")
    void hardDelete(@Param("id") Long id);

    // Restore (geri getir)
    @Modifying
    @Transactional
    @Query(value = "UPDATE customers SET deleted = false, deleted_at = null WHERE id = :id", nativeQuery = true)
    void restore(@Param("id") Long id);
}
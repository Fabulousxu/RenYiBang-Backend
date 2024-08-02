package com.renyibang.serviceapi.repository;

import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.enums.ServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Override
    Page<Service> findAll(Pageable pageable);

    @Query("SELECT s FROM Service s WHERE " +
            "(LOWER(s.title) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :searchText, '%'))) AND " +
            "s.price BETWEEN :priceLow AND :priceHigh AND " +
            "s.createdAt BETWEEN :beginDateTime AND :endDateTime AND " +
            "s.status != :status")
    Page<Service> searchServices(@Param("searchText") String searchText,
                           @Param("priceLow") long priceLow,
                           @Param("priceHigh") long priceHigh,
                           @Param("beginDateTime") LocalDateTime beginDateTime,
                           @Param("endDateTime") LocalDateTime endDateTime,
                           @Param("status") ServiceStatus status,
                           Pageable pageable);

    Page<Service> findByPriceBetweenAndCreatedAtBetweenAndStatusNot(@Param("priceLow") long priceLow,
                                                     @Param("priceHigh") long priceHigh,
                                                     @Param("beginDateTime") LocalDateTime beginDateTime,
                                                     @Param("endDateTime") LocalDateTime endDateTime,
                                                     @Param("status") ServiceStatus status,
                                                     Pageable pageable);

    Page<Service> findByOwnerId(long userId, Pageable pageable);

    @Query("SELECT s FROM Service s WHERE s.serviceId IN " +
            "(SELECT sa.service.serviceId FROM ServiceAccess sa WHERE sa.accessorId = :userId)")
    Page<Service> findByAccessorId(@Param("userId") long userId, Pageable pageable);
}
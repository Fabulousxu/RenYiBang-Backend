package com.renyibang.serviceapi.repository;

import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.entity.ServiceAccess;
import com.renyibang.serviceapi.entity.ServiceCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAccessRepository extends JpaRepository<ServiceAccess, Long> {
    boolean existsByAccessorIdAndService(long accessorId, Service service);
    ServiceAccess findByServiceAndAccessorId(Service service, long accessorId);
}
package com.renyibang.serviceapi.repository;

import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.entity.ServiceCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCollectRepository extends JpaRepository<ServiceCollect, Long> {
    boolean existsByCollector_idAndAndService(long collectorId, Service service);

    ServiceCollect findByServiceAndCollector_id(Service service, long collectorId);
}
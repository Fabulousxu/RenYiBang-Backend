package com.renyibang.serviceapi.dao.daoImpl;

import com.renyibang.moduleapi.clients.UserClient;
import com.renyibang.serviceapi.dao.ServiceDao;
import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.entity.ServiceAccess;
import com.renyibang.serviceapi.entity.ServiceCollect;
import com.renyibang.serviceapi.enums.ServiceStatus;
import com.renyibang.serviceapi.repository.ServiceAccessRepository;
import com.renyibang.serviceapi.repository.ServiceCollectRepository;
import com.renyibang.serviceapi.repository.ServiceRepository;
import com.renyibang.serviceapi.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ServiceDaoImpl implements ServiceDao {
  @Autowired ServiceRepository serviceRepository;

  @Autowired ServiceCollectRepository serviceCollectRepository;

  @Autowired ServiceAccessRepository serviceAccessRepository;

  @Autowired UserClient userClient;

    @Override
    public Service findById(long serviceId) {
        return serviceRepository.findById(serviceId).orElse(null);
    }

    @Override
    public Page<Service> searchServiceByPaging(String keyword, Pageable pageable, LocalDateTime beginDateTime, LocalDateTime endDateTime, long priceLow, long priceHigh)
    {
        if(!keyword.isEmpty())
        {
            return serviceRepository.searchServices(keyword, priceLow, priceHigh, beginDateTime, endDateTime, ServiceStatus.DELETE, pageable);
        }
        else
        {
            return serviceRepository.findByPriceBetweenAndCreatedAtBetweenAndStatusNot(priceLow, priceHigh, beginDateTime, endDateTime, ServiceStatus.DELETE, pageable);
        }
    }

    @Override
    public String collectServiceByServiceId(long serviceId, long collectorId)
    {
        try{
            if(!userClient.getUserExist(collectorId))
            {
                return "用户不存在！";
            }

            Service service = serviceRepository.findById(serviceId).orElse(null);
            if(service == null)
            {
                return "服务不存在！";
            }

            if(service.getStatus() == ServiceStatus.DELETE)
            {
                return "该服务已被删除！";
            }

            if(serviceCollectRepository.existsByCollector_idAndAndService(collectorId, service))
            {
                return "用户已收藏该服务！";
            }

            service.setCollectedNumber(service.getCollectedNumber() + 1);
            ServiceCollect serviceCollect = new ServiceCollect();
            serviceCollect.setCollector_id(collectorId);
            serviceCollect.setService(service);
            serviceCollect.setCreatedAt(LocalDateTime.now());

            serviceCollectRepository.save(serviceCollect);

            return "收藏成功！";
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    @Override
    public String uncollectServiceByServiceId(long serviceId, long uncollectorId)
    {
        try{
            if(!userClient.getUserExist(uncollectorId))
            {
                return "用户不存在！";
            }

            Service service = serviceRepository.findById(serviceId).orElse(null);
            if(service == null)
            {
                return "服务不存在！";
            }

            if(service.getStatus() == ServiceStatus.DELETE)
            {
                return "该服务已被删除！";
            }

            ServiceCollect serviceCollect = serviceCollectRepository.findByServiceAndCollector_id(service, uncollectorId);
            if(serviceCollect == null)
            {
                return "用户未收藏该服务！";
            }

            service.setCollectedNumber(service.getCollectedNumber() - 1);
            serviceCollectRepository.delete(serviceCollect);

            return "取消收藏成功！";
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    @Override
    public String accessServiceByServiceId(long serviceId, long accessorId)
    {
        try{
            Service service = serviceRepository.findById(serviceId).orElse(null);
            if(service == null)
            {
                return "服务不存在！";
            }

            if(service.getStatus() == ServiceStatus.DELETE)
            {
                return "该服务已被删除！";
            }
            else if (service.getStatus() == ServiceStatus.REMOVE)
            {
                return "该服务已被下架！";
            }

            if(service.getOwner_id() == accessorId)
            {
                return "不能接取自己发布的服务！";
            }

            if(!userClient.getUserExist(accessorId))
            {
                return "用户不存在！";
            }

            if(ServiceAccessRepository.existsByAccessor_idAndService(accessorId, service))
            {
                return "用户已经接取该服务！";
            }

            if(!service.accessNotFull())
            {
                return "该服务接取已达上限！";
            }

            if(service.getStatus() == ServiceStatus.DELETE)
            {
                return "该服务已被删除！";
            }
            else if (service.getStatus() == ServiceStatus.REMOVE)
            {
                return "该服务已被下架！";
            }

            ServiceAccess serviceAccess = new ServiceAccess();
            serviceAccess.setAccessor_id(accessorId);
            serviceAccess.setService(service);
            serviceAccess.setCreatedAt(LocalDateTime.now());

            serviceAccessRepository.save(serviceAccess);
            return "接取服务成功！";
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    @Override
    public String unaccessServiceByServiceId(long serviceId, long unaccessorId)
    {
        try{
            Service service = serviceRepository.findById(serviceId).orElse(null);
            if(service == null)
            {
                return "服务不存在！";
            }

            if(service.getStatus() == ServiceStatus.DELETE)
            {
                return "该服务已被删除！";
            }

            if(!userClient.getUserExist(unaccessorId))
            {
                return "用户不存在！";
            }

            ServiceAccess serviceAccess = serviceAccessRepository.findByServiceAndAccessor_id(service, unaccessorId);
            if(serviceAccess == null)
            {
                return "用户未接取该服务！";
            }

            serviceAccessRepository.delete(serviceAccess);
            return "取消接取服务成功！";
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    @Override
    public String publishService(long userId, String title, String description, long price, List<String> requestImages)
    {
        try {
            if(!userClient.getUserExist(userId))
            {
                return "用户不存在！";
            }

            String imagesURL = ImageUtil.mergeImages(requestImages);

            Service service = new Service();
            service.setOwner_id(userId);
            service.setTitle(title);
            service.setDescription(description);
            service.setPrice(price);
            service.setImages(imagesURL);
            service.setCreatedAt(LocalDateTime.now());

            serviceRepository.save(service);
            return "服务发布成功！";
        }
        catch (Exception e)
        {
            throw e;
        }
    }
}




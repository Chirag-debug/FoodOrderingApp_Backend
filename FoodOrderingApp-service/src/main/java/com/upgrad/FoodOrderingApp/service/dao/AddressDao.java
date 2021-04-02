package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AddressEntity saveAddress(final AddressEntity address)
    {
        entityManager.persist(address);
        return address;
    }

    public List<AddressEntity> getAddressesByCustomerUuid(final String customerUuid)
    {
        try {
           return  entityManager.createNamedQuery("getAllAddressByCustomerUuid", AddressEntity.class).setParameter("customerUuid",customerUuid).getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }

    public AddressEntity getAddressByAddressId(String addressId){
        try{
            AddressEntity addressEntity = entityManager.createNamedQuery("getAddressByUud",AddressEntity.class).setParameter("uuid", addressId).getSingleResult();
            return addressEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

    public AddressEntity deleteAddress(final AddressEntity addressEntity)
    {
        entityManager.remove(addressEntity);
        return addressEntity;
    }


}

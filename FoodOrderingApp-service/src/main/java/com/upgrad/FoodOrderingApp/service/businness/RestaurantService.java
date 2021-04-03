package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;


    public List<RestaurantEntity> restaurantsByRating() {
        return restaurantDao.getAllRestaurants();
    }


    public List<RestaurantEntity> restaurantsByName(final String restaurantName)
            throws RestaurantNotFoundException {
        if (restaurantName == null || restaurantName == "") {
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }

        List<RestaurantEntity> restaurantEntities = restaurantDao.getRestaurantByName(restaurantName);

        if (restaurantEntities == null) {
            return new ArrayList<>();
        } else {
            return restaurantEntities;
        }
    }


    public List<RestaurantEntity> restaurantByCategory(final String categoryUuid)
            throws CategoryNotFoundException {
        if (categoryUuid == null) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

        CategoryEntity categoryEntity = categoryDao.getCategoryById(categoryUuid);
        if (categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        return categoryEntity.getRestaurants();
    }


    public RestaurantEntity restaurantByUUID(final String restaurantId)
            throws RestaurantNotFoundException {

        if (restaurantId == null) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }


        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantId);

        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        } else {
            return restaurantEntity;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, Double customerRating)
            throws InvalidRatingException {

        if (customerRating == null || !(customerRating >= 1 && customerRating <= 5)) {
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }

        Double oldRestaurantRating = restaurantEntity.getCustomerRating();
        Integer oldCustomersRatingCount = restaurantEntity.getNumberCustomersRated();
        restaurantEntity.setNumberCustomersRated(oldCustomersRatingCount + 1);

        restaurantEntity.setCustomerRating(calculateAvgRating(oldRestaurantRating, customerRating, oldCustomersRatingCount));

        RestaurantEntity updatedRestaurantEntity = restaurantDao.updateRestaurantRating(restaurantEntity);
        return updatedRestaurantEntity;
    }


    private Double calculateAvgRating(Double oldRestaurantRating, Double customerRating, Integer oldCustomersRatingCount) {
        Double newCustomerRating = ((oldRestaurantRating * oldCustomersRatingCount) + customerRating) / (oldCustomersRatingCount + 1);
        Double truncatedDouble = BigDecimal.valueOf(newCustomerRating)
                .setScale(1, RoundingMode.FLOOR)
                .doubleValue();
        return truncatedDouble;
    }
}
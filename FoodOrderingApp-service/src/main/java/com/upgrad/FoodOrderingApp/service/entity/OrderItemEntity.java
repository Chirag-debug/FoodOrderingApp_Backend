package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "order_item")
@NamedQueries({
        // first query changed
        @NamedQuery(name = "fetchItemDetails",query = "SELECT o FROM OrderItemEntity o WHERE o.orderEntity = :orders ORDER BY o.itemEntity.itemName ASC"),
        @NamedQuery(name = "getItemsByOrders",query = "SELECT o FROM OrderItemEntity o WHERE o.orderEntity = :ordersEntity"),

})
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER) //changed here
    @JoinColumn(name = "order_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // added here
    @NotNull                                    // added here
    private OrderEntity orderEntity;


    @ManyToOne(fetch = FetchType.EAGER) //changed here
    @JoinColumn(name = "item_id")
    @NotNull                            // added here
    private ItemEntity itemEntity;

    @Column(name = "Quantity")
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ItemEntity getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(ItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Column(name = "price")
    private Integer price;

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }
}

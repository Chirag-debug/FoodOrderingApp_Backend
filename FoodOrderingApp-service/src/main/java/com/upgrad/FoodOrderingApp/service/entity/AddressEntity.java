package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "ADDRESS")
@NamedQueries({
        @NamedQuery(name = "getAllAddressByCustomerUuid", query = "SELECT distinct a FROM AddressEntity a join a.customer c where c.uuid=:customerUuid"),
        @NamedQuery(name = "getAddressByUud", query = "SELECT a from AddressEntity a where a.uuid = :uuid"),
})
public class AddressEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "flat_buil_number")
    @Size(max = 255)
    @NotNull
    private String flatBuilNumber;

    @Column(name = "locality")
    @Size(max = 255)
    @NotNull
    private String locality;

    @Column(name = "city")
    @Size(max = 30)
    @NotNull
    private String city;

    @Column(name = "pincode")
    @Size(max = 30)
    @NotNull
    private String pincode;

    @Column(name = "active")
    private Integer active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id", nullable = false)
    private StateEntity state;


    @ManyToOne
    @JoinTable(
            name = "customer_address",
            joinColumns = @JoinColumn(name = "customer_id")
    )
    private CustomerEntity customer;

    public AddressEntity()
    {

    }



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



    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public String getFlatBuilNumber() {
        return flatBuilNumber;
    }

    public void setFlatBuilNumber(String flatBuilNumber) {
        this.flatBuilNumber = flatBuilNumber;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AddressEntity(@Size(max = 200) @NotNull String uuid, @Size(max = 255) @NotNull String flatBuilNumber, @Size(max = 255) @NotNull String locality, @Size(max = 30) @NotNull String city, @Size(max = 30) @NotNull String pincode, StateEntity state) {
        this.uuid = uuid;
        this.flatBuilNumber = flatBuilNumber;
        this.locality = locality;
        this.city = city;
        this.pincode = pincode;
        this.state = state;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this,obj).isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}

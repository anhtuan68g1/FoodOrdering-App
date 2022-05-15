/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.aptech.sem4prj.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author gengh
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM Users u"),
        @NamedQuery(name = "User.findById", query = "SELECT u FROM Users u WHERE u.id = :id"),
        @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM Users u WHERE u.phone = :phone"),
        @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
        @NamedQuery(name = "User.findByFullName", query = "SELECT u FROM Users u WHERE u.fullName = :fullName"),
        @NamedQuery(name = "User.findByBirthday", query = "SELECT u FROM Users u WHERE u.birthday = :birthday"),
        @NamedQuery(name = "User.findByGender", query = "SELECT u FROM Users u WHERE u.gender = :gender"),
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
//        @NamedQuery(name = "User.findByAvatar", query = "SELECT u FROM User u WHERE u.image = :image"),
        @NamedQuery(name = "User.findByCreatedAt", query = "SELECT u FROM Users u WHERE u.createdAt = :createdAt"),
        @NamedQuery(name = "User.findByModifiedAt", query = "SELECT u FROM Users u WHERE u.modifiedAt = :modifiedAt")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic(optional = false)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "fullname")
    private String fullName;
    @Basic(optional = false)
    @Column(name = "birthday")
    private String birthday;
    @Basic(optional = false)
    @Column(name = "gender")
    private boolean gender;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Column(name = "image")
    private String image;
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Column(name = "modified_at")
    @Temporal(TemporalType.DATE)
    private Date modifiedAt;
    @OneToMany(mappedBy = "usersId")
//    @JsonManagedReference(value = "cartitems")
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private Collection<CartItems> cartItemsCollection;
    @OneToMany(mappedBy = "usersId")
//    @JsonManagedReference(value = "useraddress")
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    private Collection<UserAddress> userAddressCollection;
    @OneToMany(mappedBy = "usersId")
//    @JsonManagedReference(value = "favouriteproducts")
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    private Collection<FavouriteProduct> favouriteProductCollection;
    @OneToMany(mappedBy = "usersId")
//    @JsonManagedReference(value = "orders")
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    private Collection<Order> order1Collection;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    private String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }



    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isGender() {
        return gender;
    }


    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Users() {
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Users(Integer id, String phone, String password, String fullName, String birthday, boolean gender, String email, String image, boolean enabled, String provider) {
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.image = image;
        this.enabled = enabled;
        this.provider = provider;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @XmlTransient
    public Collection<CartItems> getCartItemsCollection() {
        return cartItemsCollection;
    }

    public void setCartItemsCollection(Collection<CartItems> cartItemsCollection) {
        this.cartItemsCollection = cartItemsCollection;
    }

    @XmlTransient
    public Collection<UserAddress> getUserAddressCollection() {
        return userAddressCollection;
    }

    public void setUserAddressCollection(Collection<UserAddress> userAddressCollection) {
        this.userAddressCollection = userAddressCollection;
    }

    @XmlTransient
    public Collection<FavouriteProduct> getFavouriteProductCollection() {
        return favouriteProductCollection;
    }

    public void setFavouriteProductCollection(Collection<FavouriteProduct> favouriteProductCollection) {
        this.favouriteProductCollection = favouriteProductCollection;
    }

    @XmlTransient
    public Collection<Order> getOrder1Collection() {
        return order1Collection;
    }

    public void setOrder1Collection(Collection<Order> order1Collection) {
        this.order1Collection = order1Collection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vn.aptech.semprj.entity.User[ id=" + id + " ]";
    }

}

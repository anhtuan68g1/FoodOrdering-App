/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.aptech.sem4prj.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author gengh
 */
@Entity
@Table(name = "cart_items")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "CartItems.findAll", query = "SELECT c FROM CartItems c"),
        @NamedQuery(name = "CartItems.findById", query = "SELECT c FROM CartItems c WHERE c.id = :id"),
        @NamedQuery(name = "CartItems.findByQuantity", query = "SELECT c FROM CartItems c WHERE c.quantity = :quantity"),
        @NamedQuery(name = "CartItems.findByTotal", query = "SELECT c FROM CartItems c WHERE c.total = :total")})
public class CartItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "quantity")
    private Integer quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private Double total;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne
    @JsonBackReference(value = "productscartitems")
    private Product productId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    @JsonBackReference(value= "users")
    private Users usersId;

    public CartItems() {
    }

    public CartItems(Integer id) {
        this.id = id;
    }

    public CartItems(Integer id, String s, String s1, double v) {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Users getUserId() {
        return usersId;
    }

    public void setUserId(Users usersId) {
        this.usersId = usersId;
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
        if (!(object instanceof CartItems)) {
            return false;
        }
        CartItems other = (CartItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vn.aptech.semprj.entity.CartItems[ id=" + id + " ]";
    }

}

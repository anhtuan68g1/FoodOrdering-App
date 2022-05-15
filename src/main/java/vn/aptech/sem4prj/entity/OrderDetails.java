/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.aptech.sem4prj.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name = "order_details")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "OrderDetails.findAll", query = "SELECT o FROM OrderDetails o"),
        @NamedQuery(name = "OrderDetails.findById", query = "SELECT o FROM OrderDetails o WHERE o.id = :id"),
        @NamedQuery(name = "OrderDetails.findByQuantity", query = "SELECT o FROM OrderDetails o WHERE o.quantity = :quantity"),
        @NamedQuery(name = "OrderDetails.findByTotal", query = "SELECT o FROM OrderDetails o WHERE o.total = :total")})
public class OrderDetails implements Serializable {

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
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @ManyToOne
    @JsonBackReference(value = "orders")
    private Order orderId;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne
    @JsonBackReference(value = "productsorder")
    private Product productId;

    public OrderDetails() {
    }

    public OrderDetails(Integer id) {
        this.id = id;
    }

    public OrderDetails(Integer id, Order orderId, Product productId, Integer quantity, Double total) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.total = total;
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

    public Order getOrderId() {
        return orderId;
    }

    public void setOrderId(Order orderId) {
        this.orderId = orderId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
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
        if (!(object instanceof OrderDetails)) {
            return false;
        }
        OrderDetails other = (OrderDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vn.aptech.semprj.entity.OrderDetails[ id=" + id + " ]";
    }

}

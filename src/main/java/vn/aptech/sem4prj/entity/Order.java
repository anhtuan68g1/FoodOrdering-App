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
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author gengh
 */
@Entity
@Table(name = "orders")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Order1.findAll", query = "SELECT o FROM Order o"),
        @NamedQuery(name = "Order1.findById", query = "SELECT o FROM Order o WHERE o.id = :id"),
        @NamedQuery(name = "Order1.findByPayment", query = "SELECT o FROM Order o WHERE o.payment = :payment"),
        @NamedQuery(name = "Order1.findByStatus", query = "SELECT o FROM Order o WHERE o.status = :status"),
        @NamedQuery(name = "Order1.findByOrderDate", query = "SELECT o FROM Order o WHERE o.orderDate = :orderDate")})
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "payment")
    private String payment;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "status")
    private String status;
    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;
    private Double total_price;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Order(Integer id, Users usersId, String payment, String status, Date orderDate, Double total_price, String address) {
        this.id = id;
        this.usersId = usersId;
        this.payment = payment;
        this.status = status;
        this.orderDate = orderDate;
        this.total_price = total_price;
        this.address = address;
    }

    public Double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Double total_price) {
        this.total_price = total_price;
    }

    @OneToMany(mappedBy = "orderId")
//    @JsonManagedReference(value = "orderdetails")
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    private Collection<OrderDetails> orderDetailsCollection;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    @JsonBackReference(value = "users")
    private Users usersId;

    public Order() {
    }

    public Order(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @XmlTransient
    public Collection<OrderDetails> getOrderDetailsCollection() {
        return orderDetailsCollection;
    }

    public void setOrderDetailsCollection(Collection<OrderDetails> orderDetailsCollection) {
        this.orderDetailsCollection = orderDetailsCollection;
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
        if (!(object instanceof Order)) {
            return false;
        }
        Order other = (Order) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vn.aptech.semprj.entity.Order1[ id=" + id + " ]";
    }

}

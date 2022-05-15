/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.aptech.sem4prj.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author gengh
 */
@Entity
@Table(name = "administrator")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Administrator.findAll", query = "SELECT a FROM Administrator a"),
        @NamedQuery(name = "Administrator.findById", query = "SELECT a FROM Administrator a WHERE a.id = :id"),
        @NamedQuery(name = "Administrator.findByUsername", query = "SELECT a FROM Administrator a WHERE a.username = :username"),
        @NamedQuery(name = "Administrator.findByPassword", query = "SELECT a FROM Administrator a WHERE a.password = :password"),
        @NamedQuery(name = "Administrator.findByFullname", query = "SELECT a FROM Administrator a WHERE a.fullname = :fullname"),
        @NamedQuery(name = "Administrator.findByBirthday", query = "SELECT a FROM Administrator a WHERE a.birthday = :birthday"),
        @NamedQuery(name = "Administrator.findByGender", query = "SELECT a FROM Administrator a WHERE a.gender = :gender"),
        @NamedQuery(name = "Administrator.findByEmail", query = "SELECT a FROM Administrator a WHERE a.email = :email"),
//        @NamedQuery(name = "Administrator.findByAvatar", query = "SELECT a FROM Administrator a WHERE a.avatar = :avatar"),
        @NamedQuery(name = "Administrator.findByStatus", query = "SELECT a FROM Administrator a WHERE a.status = :status"),
        @NamedQuery(name = "Administrator.findByCreatedAt", query = "SELECT a FROM Administrator a WHERE a.createdAt = :createdAt"),
        @NamedQuery(name = "Administrator.findByModifiedAt", query = "SELECT a FROM Administrator a WHERE a.modifiedAt = :modifiedAt")})
public class Administrator implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "fullname")
    private String fullname;
    @Basic(optional = false)
    @Column(name = "birthday")
    private Date birthday;
    @Basic(optional = false)
    @Column(name = "gender")
    private boolean gender;
    @Column(name = "image", nullable = true, length = 500)
    private String image;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @Column(name = "email")
    private String email;

    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Column(name = "modified_at")
    @Temporal(TemporalType.DATE)
    private Date modifiedAt;



    public Administrator() {
    }

    public Administrator(Integer id) {
        this.id = id;
    }

    public Administrator(Integer id, String username, String password, String fullname, Date birthday, boolean gender, String image, String email, boolean status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.image = image;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {

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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreated_at(Date createdAt){
//        createdAt = Calendar.getInstance().getTime();
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt){
//        modifiedAt = Calendar.getInstance().getTime();
        this.modifiedAt = modifiedAt;
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
        if (!(object instanceof Administrator)) {
            return false;
        }
        Administrator other = (Administrator) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vn.aptech.semprj.entity.Administrator[ id=" + id + " ]";
    }

//    @Transient
//    public String getAvatar() {
//        if (avatar == null || id == null) return null;
//
//        return "/user-photos/" + id + "/" + avatar;
//    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import util.security.CryptographicHelper;

/**
 *
 * @author yeerouhew
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(min = 4, max = 32)
    private String username;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    private String password;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String email;
    @Column(nullable = false, length = 8)
    @NotNull
    @Size(max = 8)
    @Pattern(regexp = "^[89]\\d{7}$")
    private String mobileNum;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SaleTransaction> saleTransactions;

    public User() {
        this.saleTransactions = new ArrayList<>();
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
    }

    public User(String username, String password, String email, String mobileNum) {
        this();
        this.username = username;
        this.email = email;
        this.mobileNum = mobileNum;

        setPassword(password);
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.User[ id=" + id + " ]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        if (password != null) {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        } else {
            this.password = null;
        }
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public List<SaleTransaction> getSaleTransactions() {
        return saleTransactions;
    }

    public void setSaleTransactions(List<SaleTransaction> saleTransactions) {
        this.saleTransactions = saleTransactions;
    }

}

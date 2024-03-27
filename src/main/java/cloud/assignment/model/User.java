package cloud.assignment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

import jakarta.persistence.*;
//import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;


@Entity
public class User {
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    //@GeneratedValue(generator = "uniid")
    //@GenericGenerator(name="uniid",strategy = "uuid2")
    @UuidGenerator
    private String ID;
    @JsonFormat(without = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @Column(nullable = false)
    private String first_name;
    @JsonFormat(without = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
    @Column(nullable = false)
    private String last_name;
    @Pattern(regexp = "^[^@]+@[^@]+\\.com$")
    @Column(unique = true, nullable = false)
    private String username;
    //@JsonIgnore
    @Pattern(regexp = "^[^\\s].*$")
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @CreationTimestamp
    private Date account_created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @CreationTimestamp
    private Date account_updated;

    public User(String newUser, String password, String first, String last) {
        this.username=newUser;
        this.password=password;
        this.first_name=first;
        this.last_name=last;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public Date getAccount_created() {
        return account_created;
    }

    public void setAccount_created(Date account_created) {
        this.account_created = account_created;
    }

    public Date getAccount_updated() {
        return account_updated;
    }

    public void setAccount_updated(Date account_updated) {
        this.account_updated = account_updated;
    }

    public User() {
    }

    public User(String ID, String first_name, String last_name, String username, String password, Date account_created, Date account_updated) {
        this.ID = ID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.account_created = account_created;
        this.account_updated = account_updated;
    }
}

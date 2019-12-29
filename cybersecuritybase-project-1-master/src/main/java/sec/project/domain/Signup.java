package sec.project.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;

@Entity
public class Signup extends AbstractPersistable<Long> {

    private String name;
    private String address;
    private String user;

    public Signup() {
        super();
    }

    public Signup(String name, String address, String user) {
        this();
        this.name = name;
        this.address = address;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

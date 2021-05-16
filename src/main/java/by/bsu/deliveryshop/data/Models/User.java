package by.bsu.deliveryshop.data.Models;

import by.bsu.deliveryshop.annotations.CsvOrder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@MappedSuperclass
public class User {
    @Column(name = "name")
    protected String name;
    @Column(name = "password")
    protected String password;

    public User() {
        name = null;
        password = null;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @CsvOrder(position = 2)
    public  String getName() {
        return name;
    }

    @CsvOrder(position = 3)
    public String getPassword(){
        return password;
    }
}

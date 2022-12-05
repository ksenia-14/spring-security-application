package com.example.springsecurityapplication.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private Product product;

    @ManyToOne(optional = false)
    private Person person;

    private float price;

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }

    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }


    public Order() {
    }

    public Order(String number, Product product, Person person, float price) {
        this.product = product;
        this.person = person;
        this.price = price;
    }
}

package com.example.springsecurityapplication.models;

import javax.persistence.*;

@Entity
@Table(name = "product_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Person person;

    @OneToOne
    private Product product;

    public Cart(Person person, Product product) {
        this.person = person;
        this.product = product;
    }

    public Cart() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person personId) {
        this.person = personId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

package com.example.springsecurityapplication.models;

import com.sun.istack.NotNull;
import org.aspectj.bridge.IMessage;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Product")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 4, max = 30, message = "Поле должно содержать от 4 до 30 символов")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 4, max = 30, message = "Поле должно содержать от 4 до 30 символов")
    @Column(name = "seller")
    private String seller;

//    @NotEmpty(message = "Поле не может быть пустым")
//    @Size(min = 1, max = 30, message = "Поле должно содержать от 4 до 30 символов")
//    @Range(min=1, max=90, message = "Поле должно содержать от 1 до 30 символов")
    @Min(value=1, message="Минимально возможное значение 1.00")
    @Column(name = "price")
    private double price;

//    @ManyToOne(optional = false)
//    private Category category;

    @NotEmpty(message = "Поле не может быть пустым")
    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;


    public Product() {
    }

    public Product(String title, String seller, double price, String category) {
        this.title = title;
        this.seller = seller;
        this.price = price;
        this.category = category;
    }

    public Product(String title, String seller, double price, String category, String description) {
        this.title = title;
        this.seller = seller;
        this.price = price;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}

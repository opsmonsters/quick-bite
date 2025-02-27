package com.opsmonsters.quick_bite.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name", nullable = false)
    private String name;
    @Column (name="imageUrl")
    private String imageUrl;

    @Column(name = "mrp", nullable = false)
    private Double mrp = 0.0;

    @Column(name = "discount", nullable = false)
    private Double discount = 0.0;

    @Column(name = "description")
    private String description;

    @Column(name = "about")
    private String about;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Product() {}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMrp() {
        return (mrp != null) ? mrp : 0.0;
    }

    public void setMrp(Double mrp) {
        this.mrp = (mrp != null) ? mrp : 0.0;
    }

    public Double getDiscount() {
        return (discount != null) ? discount : 0.0;
    }

    public void setDiscount(Double discount) {
        this.discount = (discount != null) ? discount : 0.0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getStock() {
        return (stock != null) ? stock : 0;
    }

    public void setStock(Integer stock) {
        this.stock = (stock != null) ? stock : 0;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getProducts().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getProducts().remove(this);
    }


    public Double getPrice() {
        return Math.max(getMrp() - getDiscount(), 0.0);
    }
}

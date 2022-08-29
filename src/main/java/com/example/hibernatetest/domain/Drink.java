package com.example.hibernatetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "drinks")
public class Drink  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drink_gen")
    @SequenceGenerator(name = "drink_gen", sequenceName = "drink_id_sequence", initialValue = 10001)
    @Column(name = "drink_id")
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name;

    @NonNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NonNull
    @Column(name = "amount", nullable = false)
    private Long availableAmount;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "brand_id")
    @JsonIgnoreProperties({"drinks"})
    private Brand brand;

    @JsonIgnore
    @OneToOne(targetEntity = ImageDTO.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private ImageDTO image;

    @JsonIgnore
    @Transient
    @OneToMany(mappedBy = "drink")
    private List<OrderItem> orderItems;

    @ElementCollection(targetClass = ItemCategories.class)
    @JoinTable(name = "item_categories", joinColumns = @JoinColumn(name = "drink_id"))
    @Column(name = "item_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<ItemCategories> itemCategories;

    public ImageDTO getImage() {
        return this.image;
    }

    public void setImage(String imageName) throws IOException {
        this.image = new ImageDTO(imageName);
    }

    @Data
    public static class DrinkForBrandDTO{
        private Long id;
        private String name;
        private Double price;
        private Long availableAmount;

        public DrinkForBrandDTO build(Drink drink) {
            this.id = drink.id;
            this.name = drink.name;
            this.price = drink.price;
            this.availableAmount = drink.availableAmount;
            return this;
        }
    }

    @Data
    public static class DrinksForSearchResultDTO{
        private Collection<Drink> drinks;
        private Collection<String> tags;

        public DrinksForSearchResultDTO build(Collection<Drink> drinks, Collection<String> tags) {
            this.drinks = drinks;
            this.tags = tags;
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drink drink = (Drink) o;
        return Objects.equals(id, drink.id) && name.equals(drink.name) && Objects.equals(brand, drink.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brand);
    }
}

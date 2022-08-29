package com.example.hibernatetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "brand_gen")
    @SequenceGenerator(name = "brand_gen", sequenceName = "brand_id_sequence")
    @Column(name = "brand_id")
    private Long id;
    @NonNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(
            mappedBy = "brand",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnoreProperties({"brand"})
    private Set<Drink> drinks = new HashSet<>();

    public void addDrink(Drink drink){
        drinks.add(drink);
        drink.setBrand(this);
    }

    public void removeDrink(Drink drink){
        if (drink != null)
            drink.setBrand(null);
        drinks.remove(drink);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brand brand = (Brand) o;
        return Objects.equals(id, brand.id) && name.equals(brand.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
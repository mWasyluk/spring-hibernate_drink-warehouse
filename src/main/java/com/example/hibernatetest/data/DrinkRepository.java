package com.example.hibernatetest.data;

import com.example.hibernatetest.domain.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {
    Collection<Drink> findByNameContainsIgnoreCase(String content);
//    Collection<Drink> findAll (Sort name);
}
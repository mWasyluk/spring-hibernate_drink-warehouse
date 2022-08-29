package com.example.hibernatetest.controller;

import com.example.hibernatetest.data.BrandRepository;
import com.example.hibernatetest.domain.Brand;
import com.example.hibernatetest.domain.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class BrandController {

    @Autowired
    private BrandRepository brandRepository;

    @GetMapping("/brands")
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> all = brandRepository.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/brands")
    public ResponseEntity<Brand> addBrand(@RequestBody Brand brand) {
        brand.getDrinks().forEach(brand::addDrink);
        Optional<Brand> optional = brandRepository.findByName(brand.getName());
        return optional.map(
                        (value) -> new ResponseEntity<>(value, HttpStatus.CONFLICT))
                .orElseGet(
                        () -> new ResponseEntity<>(brandRepository.save(brand), HttpStatus.CREATED));
    }

    @GetMapping("/brands/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable("id") Long id) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        return optionalBrand
                .map( brand -> new ResponseEntity<Brand>(optionalBrand.get(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Transactional
    @PostMapping("brands/{id}")
    public ResponseEntity<?> addDrinkToBrandByBrandId(@PathVariable("id") Long id, @RequestBody Drink drink) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (optionalBrand.isPresent()) {
            Brand brand = optionalBrand.get();
            Optional<Drink> any = brand.getDrinks().stream().filter((element -> element.getName().equals(drink.getName()))).findAny();
            if (any.isPresent())
            {
                return new ResponseEntity<>(any.get(),HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(brandRepository.save(brand), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/brands/{id}/drinks")
    public ResponseEntity<Set<Drink.DrinkForBrandDTO>> getAllDrinksByBrandId(@PathVariable Long id){
        return brandRepository.findById(id)
                .map(val -> {
                    Set<Drink.DrinkForBrandDTO> collect = val.getDrinks().stream()
                            .map(drink -> new Drink.DrinkForBrandDTO().build(drink))
                            .collect(Collectors.toSet());
                    return new ResponseEntity<>( collect, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

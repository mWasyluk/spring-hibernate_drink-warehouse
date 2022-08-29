package com.example.hibernatetest.controller;

import com.example.hibernatetest.data.BrandRepository;
import com.example.hibernatetest.data.DrinkRepository;
import com.example.hibernatetest.domain.Brand;
import com.example.hibernatetest.domain.Drink;
import com.example.hibernatetest.domain.ImageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@Slf4j
public class DrinkController {
    @Autowired
    private DrinkRepository drinkRepository;
    @Autowired
    private BrandRepository brandRepository;

    @GetMapping("/drinks")
    @CrossOrigin("/**")
    public ResponseEntity<?> getAllDrinks(@RequestParam(name = "search", required = false) String searchQuery ){
        if ( searchQuery == null || searchQuery.trim().isEmpty()) {
            log.info("No search parameter found. Sending all drinks as response.");
            Drink.DrinksForSearchResultDTO build = new Drink.DrinksForSearchResultDTO().build(drinkRepository.findAll(Sort.by("name")), new ArrayList<>());
            return new ResponseEntity<>(build, HttpStatus.OK);
        }
        char[] spaceEqual = new char [] {' ', '.', '-'};

        Set<Drink> combinedDrinksSet = new HashSet<>();
        Set<String> combinedUsedTags = new TreeSet<>();
        for (char charact: spaceEqual) {
            if (searchQuery.contains(charact + "")) {
                String[] split = searchQuery.split("[,.\\- ]++");
                log.info( "split : " + Arrays.toString(split));
                Collection<Drink> byNameContainsIgnoreCaseMain = drinkRepository.findByNameContainsIgnoreCase(split[0]);
                combinedDrinksSet.addAll(byNameContainsIgnoreCaseMain);
                combinedUsedTags.add(split[0]);
                log.info("add tag: " + split[0]);

                Arrays.stream(split).skip(1).forEach(part -> {
                    Collection<Drink> byNameContainsIgnoreCase = drinkRepository.findByNameContainsIgnoreCase(part);
                    combinedDrinksSet.retainAll(byNameContainsIgnoreCase);
                    combinedUsedTags.add(part);
                    log.info("add tag: " + part);
                });
            }
        }
        if (combinedDrinksSet.isEmpty()){
            Collection<Drink> byNameContainsIgnoreCaseFullQuery = drinkRepository.findByNameContainsIgnoreCase(searchQuery);
            combinedDrinksSet.addAll(byNameContainsIgnoreCaseFullQuery);
            if (combinedUsedTags.isEmpty())
                combinedUsedTags.add(searchQuery);
        }
        Collection<Drink> sorted = combinedDrinksSet.stream().sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).collect(Collectors.toList());
        log.info("Sent " + sorted.size() + " drinks as response to /drinks?search=" + searchQuery);
        Drink.DrinksForSearchResultDTO build = new Drink.DrinksForSearchResultDTO().build(sorted, combinedUsedTags);
        return new ResponseEntity<>(build, HttpStatus.OK);
    }

    @GetMapping("/drinks/{id}")
    public ResponseEntity<Drink> getDrinkById(@PathVariable Long id){
        return drinkRepository.findById(id)
                .map(val -> new ResponseEntity<>( val, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/drinks")
    public ResponseEntity<?> postDrink(@RequestBody Drink drink){
        //TODO:
        String brandName = drink.getBrand().getName();
        Optional<Brand> optionalBrand = brandRepository.findByName(brandName);
        if (optionalBrand.isPresent()){
            optionalBrand.get().addDrink(drink);
            brandRepository.save(optionalBrand.get());
            return new ResponseEntity<>(drink, HttpStatus.OK);
        }
        if (drink.getBrand() != null){
            drink.getBrand().addDrink(drink);
            return new ResponseEntity<>(brandRepository.save(drink.getBrand()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        //TODO: add proper message that the Brand has not been found.
    }

    private final ImageDTO noImageAvailableImage = new ImageDTO("no_image.jpg");

    @GetMapping(value = "/drinks/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageByDrinkId(@PathVariable Long id) {
        Optional<Drink> byId = drinkRepository.findById(id);
        if (byId.isPresent() && byId.get().getImage() != null ){
            Drink drink = byId.get();
            byte[] imageBytes = drink.getImage().getImageBytes();
            return new ResponseEntity<>(imageBytes, HttpStatus.OK);
        }
        return new ResponseEntity<>(noImageAvailableImage.getImageBytes(), HttpStatus.NOT_FOUND);
    }
}

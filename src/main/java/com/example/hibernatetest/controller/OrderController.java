package com.example.hibernatetest.controller;

import com.example.hibernatetest.data.ClientRepository;
import com.example.hibernatetest.data.DrinkRepository;
import com.example.hibernatetest.data.OrderRepository;
import com.example.hibernatetest.domain.Order;
import com.example.hibernatetest.domain.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
public class OrderController {
    @Autowired
    private DrinkRepository drinkRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository cLientRepository;

    @GetMapping("/orders")
    public ResponseEntity<Collection<Order>> getAllOrders(){
        return new ResponseEntity<>(orderRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        return orderRepository.findById(id)
                .map(val -> new ResponseEntity<>( val, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/orders/{id}/change-status")
    public ResponseEntity<Order> patchOrderStatusByOrderId(@PathVariable Long id, @RequestParam (name = "status") String stringStatus ){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.map( val -> {
            if(!stringStatus.trim().isEmpty())
                for (OrderStatus status : OrderStatus.values()){
                    if (status.name().equals(stringStatus)){
                        val.setStatus(status);
                        return new ResponseEntity<>(orderRepository.save(val), HttpStatus.ACCEPTED);
                    }
                }
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(orderRepository.save(val), HttpStatus.OK);
        }).orElseGet( () -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    @PostMapping("/drinks")
//    public ResponseEntity<?> postDrink(@RequestBody Drink drink){
//        //TODO:
//        String brandName = drink.getBrand().getName();
//        Optional<Brand> optionalBrand = brandRepository.findByName(brandName);
//        if (optionalBrand.isPresent()){
//            optionalBrand.get().addDrink(drink);
//            brandRepository.save(optionalBrand.get());
//            return new ResponseEntity<>(drink, HttpStatus.OK);
//        }
//        if (drink.getBrand() != null){
//            drink.getBrand().addDrink(drink);
//            return new ResponseEntity<>(brandRepository.save(drink.getBrand()), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        //TODO: add proper message that the Brand has not been found.
//    }
}

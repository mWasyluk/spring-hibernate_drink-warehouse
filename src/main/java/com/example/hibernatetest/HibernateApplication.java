package com.example.hibernatetest;

import com.example.hibernatetest.data.BrandRepository;
import com.example.hibernatetest.data.ClientRepository;
import com.example.hibernatetest.data.DrinkRepository;
import com.example.hibernatetest.data.OrderRepository;
import com.example.hibernatetest.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
@Slf4j
public class HibernateApplication {

	public static void main(String[] args) {
		SpringApplication.run(HibernateApplication.class, args);
	}

	@Autowired
	private DrinkRepository drinkRepository;
	@Autowired
	private BrandRepository brandRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ClientRepository clientRepository;

	@Bean
	public CommandLineRunner run(){
		return (arg) -> {
			//Add 2 brands
			Brand brandCocaCola = new Brand("The Coca-Cola Company");
			Brand brandPepsi = new Brand("PepsiCo Inc.");
			Brand brandZbyszko = new Brand("Zbyszko Company S.A");
			Brand brandPolmlek = new Brand("Polmlek Sp.z.o.o.");


			//Add 3 drinks
			Drink drink = new Drink("Coca-cola 500ml, butelka, opakowanie 12 szt.", 36.00, 325L);
			Drink drink2 = new Drink("Coca-cola 1.75l, butelka, opakowanie 6 szt.", 31.20, 278L);
			Drink drink3 = new Drink("Pepsi 500ml, puszka, opakowanie 4 szt.", 12.80, 156L);
			Drink drink4 = new Drink("Zbyszko 3 Pomarańcze 1.5l, butelka, opakowanie 6 szt.", 19.80, 347L);
			Drink drink5 = new Drink("Zbyszko 3 Limonki 1.75l, butelka, opakowanie 6 szt.", 19.80, 247L);
			Drink drink6 = new Drink("Sprite 330ml, puszka, opakowanie 24 szt.", 62.40, 123L);
			Drink drink7 = new Drink("Sprite 500ml, butelka, opakowanie 12 szt.", 45.60, 223L);
			Drink drink8 = new Drink("Mirinda 500ml, butelka, opakowanie 12 szt.", 42.40, 186L);
			Drink drink9 = new Drink("Fortuna Sok Multiwitamina 1l, opakowanie 12 szt.", 48.40, 193L);
			Drink drink10 = new Drink("Fortuna Sok 100% Jabłko 300ml, szkło, opakowanie 15 szt.", 32.20, 151L);


			drink.setImage("coca_cola_12.jpg");
			drink2.setImage("coca_cola_1_75_6.jpg");
			drink3.setImage("pepsi_500ml_4.jpg");
			drink5.setImage("zbyszko_limonki_1_75l.jpg");
			drink6.setImage("sprite_330ml_24.jpg");
			drink7.setImage("sprite_500ml.jpg");
			drink8.setImage("mirinda_500ml.jpg");
			drink9.setImage("fortuna_multiwitamina_1l_12.jpg");
			drink10.setImage("fortuna_jablko_100_300ml_15.jpg");

			drink.setItemCategories(new HashSet<ItemCategories>(Arrays.asList(
					ItemCategories.BUTELKA,
					ItemCategories.NAPOJE_GAZOWANE
			)));
			drink2.setItemCategories(new HashSet<ItemCategories>(Arrays.asList(
					ItemCategories.BUTELKA,
					ItemCategories.NAPOJE_GAZOWANE
			)));
			drink3.setItemCategories(new HashSet<ItemCategories>(Arrays.asList(
					ItemCategories.PUSZKA,
					ItemCategories.NAPOJE_GAZOWANE
			)));
			drink4.setItemCategories(new HashSet<ItemCategories>(Arrays.asList(
					ItemCategories.BUTELKA,
					ItemCategories.NAPOJE_GAZOWANE
			)));
			drink5.setItemCategories(new HashSet<ItemCategories>(Arrays.asList(
					ItemCategories.BUTELKA,
					ItemCategories.NAPOJE_GAZOWANE
			)));
			drink6.setItemCategories(new HashSet<ItemCategories>(Arrays.asList(
					ItemCategories.PUSZKA,
					ItemCategories.NAPOJE_GAZOWANE
			)));
			drink7.setItemCategories(new HashSet<ItemCategories>(Arrays.asList(
					ItemCategories.BUTELKA,
					ItemCategories.NAPOJE_GAZOWANE
			)));
			drink8.setItemCategories(new HashSet<ItemCategories>(Arrays.asList(
					ItemCategories.BUTELKA,
					ItemCategories.NAPOJE_GAZOWANE
			)));
			drink9.setItemCategories(new HashSet<ItemCategories>(Arrays.asList(
					ItemCategories.KARTONOWE_OPAKOWANIE,
					ItemCategories.SOKI_I_NEKTARY
			)));
			drink10.setItemCategories(new HashSet<ItemCategories>(Arrays.asList(
					ItemCategories.BUTELKA,
					ItemCategories.SOKI_I_NEKTARY
			)));

			//Assign drinks to brands
			brandCocaCola.addDrink(drink);
			brandCocaCola.addDrink(drink2);
			brandPepsi.addDrink(drink3);
			brandZbyszko.addDrink(drink4);
			brandZbyszko.addDrink(drink5);
			brandCocaCola.addDrink(drink6);
			brandCocaCola.addDrink(drink7);
			brandPepsi.addDrink(drink8);
			brandPolmlek.addDrink(drink9);
			brandPolmlek.addDrink(drink10);

			//Save brands to database
			brandRepository.save(brandCocaCola);
			brandRepository.save(brandPepsi);
			brandRepository.save(brandZbyszko);
			brandRepository.save(brandPolmlek);

			//1st client
			//Create address, assign it to new client
			Address address = new Address("Warszawa", "Puławska", "116/10", "02-620");
			Client client = new Client("Marek", "Wasyluk");
			client.addAddress(address);

			//Save client
			Client savedClient = clientRepository.save(client);

			//Create new order for the client and save it to database
			Order order = new Order(savedClient);
			Order savedOrder = orderRepository.save(order);
			log.info("Order has been saved in database.");

			//Create new OrderItem with drinks, assign it to the order
			OrderItem orderItem = new OrderItem(drinkRepository.findById(10001l).get(), 5);
			savedOrder.addOrderItem(orderItem);

			//Save updated order
			orderRepository.save(savedOrder);
			log.info("Order has been updated with new OrderItem.");

			//2nd client
			//Create address, assign it to new client
			Address address2 = new Address("Biała Podlaska", "Kopernika", "23", "21-520");
			Address address3 = new Address("Warszawa", "Al. Lotników", "112/12", "02-520");
			Client client2 = new Client("Adam", "Alboniedam");
			client2.addAddress(address2);
			client2.addAddress(address3);

			//Save client
			Client savedClient2 = clientRepository.save(client2);

			//Create new order for the client
			Order order2 = new Order(savedClient2);
			Order order3 = new Order(savedClient2);

			//Save order to database
			Order savedOrder2 = orderRepository.save(order2);
			log.info("Order2 has been saved in database.");
			Order savedOrder3 = orderRepository.save(order3);
			log.info("Order3 has been saved in database.");

			//Create new OrderItems with drinks, assign them to the orders
			OrderItem orderItem2 = new OrderItem(drinkRepository.findById(10002L).get(), 12);
			savedOrder2.addOrderItem(orderItem2);
			OrderItem orderItem3 = new OrderItem(drinkRepository.findById(10003L).get(), 23);
			savedOrder2.addOrderItem(orderItem3);
			OrderItem orderItem4 = new OrderItem(drinkRepository.findById(10001L).get(), 19);
			savedOrder3.addOrderItem(orderItem4);

			savedOrder2.setStatus(OrderStatus.DELIVERED);

			//Save updated orders
			orderRepository.save(savedOrder2);
			log.info("Order2 has been updated with  new OrderItem.");
			orderRepository.save(savedOrder3);
			log.info("Order3 has been updated with new OrderItem.");
		};
	}
}

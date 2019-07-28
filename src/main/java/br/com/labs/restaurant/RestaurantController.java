package br.com.labs.restaurant;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

	private RestaurantRepository repository;

	public RestaurantController(RestaurantRepository repository) {
		this.repository = repository;
	}
}

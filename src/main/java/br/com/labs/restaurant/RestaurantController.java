package br.com.labs.restaurant;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.labs.category.Category;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

	private RestaurantRepository restaurantRepository;

	public RestaurantController(RestaurantRepository repository) {
		this.restaurantRepository = repository;
	}

	@PostMapping
	public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
		restaurantRepository.save(restaurant);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(restaurant.getId())
				.toUri();

		return ResponseEntity.created(location).body(restaurant);
	}

	@GetMapping
	public ResponseEntity<Page<Restaurant>> index(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		return ResponseEntity.ok(restaurantRepository.findAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Restaurant> show(@PathVariable("id") Integer id) {
		return restaurantRepository.findById(id).map(restaurant -> ResponseEntity.ok().body(restaurant))
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Restaurant> update(@Valid @RequestBody Restaurant restaurant,
			@PathVariable("id") Integer id) {
		return restaurantRepository.findById(id).map(r -> {
			
			r.setName(restaurant.getName());
			r.setDescription(restaurant.getDescription());
			
			restaurantRepository.save(r);
			
			return ResponseEntity.ok().body(r);
		}).orElse(ResponseEntity.notFound().build());
	}
}

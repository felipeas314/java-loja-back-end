package br.com.labs.store;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/restaurant")
public class StoreController {

	private StoreRepository restaurantRepository;

	public StoreController(StoreRepository repository) {
		this.restaurantRepository = repository;
	}

	@PostMapping
	public ResponseEntity<Store> create(@Valid @RequestBody Store restaurant) {
		restaurantRepository.save(restaurant);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(restaurant.getId())
				.toUri();

		return ResponseEntity.created(location).body(restaurant);
	}

	@GetMapping
	public ResponseEntity<Page<Store>> index(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		return ResponseEntity.ok(restaurantRepository.findAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Store> show(@PathVariable("id") Integer id) {
		return restaurantRepository.findById(id).map(restaurant -> ResponseEntity.ok().body(restaurant))
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Store> update(@Valid @RequestBody Store restaurant,
			@PathVariable("id") Integer id) {
		return restaurantRepository.findById(id).map(r -> {

			r.setName(restaurant.getName());
			r.setDescription(restaurant.getDescription());

			restaurantRepository.save(r);

			return ResponseEntity.ok().body(r);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> destroy(@PathVariable("id") Integer id) {
		return restaurantRepository.findById(id)
				.map(restaurant -> {
					restaurantRepository.delete(restaurant);
					return ResponseEntity.ok().build();
				}).orElse(ResponseEntity.notFound().build());
	}
}

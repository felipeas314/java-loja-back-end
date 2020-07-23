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

	private StoreRepository storeRepository;

	public StoreController(StoreRepository repository) {
		this.storeRepository = repository;
	}

	@PostMapping
	public ResponseEntity<Store> create(@Valid @RequestBody Store store) {
		storeRepository.save(store);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(store.getId())
				.toUri();

		return ResponseEntity.created(location).body(store);
	}

	@GetMapping
	public ResponseEntity<Page<Store>> index(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		return ResponseEntity.ok(storeRepository.findAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Store> show(@PathVariable("id") Integer id) {
		return storeRepository.findById(id).map(store -> ResponseEntity.ok().body(store))
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Store> update(@Valid @RequestBody Store store,
			@PathVariable("id") Integer id) {
		return storeRepository.findById(id).map(s -> {

			s.setName(store.getName());
			s.setDescription(store.getDescription());

			storeRepository.save(s);

			return ResponseEntity.ok().body(s);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> destroy(@PathVariable("id") Integer id) {
		return storeRepository.findById(id)
				.map(store -> {
					storeRepository.delete(store);
					return ResponseEntity.ok().build();
				}).orElse(ResponseEntity.notFound().build());
	}
}

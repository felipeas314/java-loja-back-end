package br.com.labs.category;

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
@RequestMapping("/category")
public class CategoryController {

	private CategoryRepository categoryRepository;

	public CategoryController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@GetMapping
	public ResponseEntity<Page<Category>> index(@PageableDefault(page = 0, size = 20) Pageable pageable) {
		return ResponseEntity.ok(categoryRepository.findAll(pageable));
	}

	@PostMapping
	public ResponseEntity<Category> create(@Valid @RequestBody Category category) {

		categoryRepository.save(category);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(category.getId())
				.toUri();

		return ResponseEntity.created(location).body(category);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> show(@PathVariable("id") Integer id) {
		return categoryRepository.findById(id).map(category -> ResponseEntity.ok().body(category))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Category> update(@Valid @RequestBody Category category,@PathVariable("id") Integer id){
		return categoryRepository.findById(id)
				.map(c -> {
					
					c.setDescription(category.getDescription());
					c.setName(category.getName());
					
					return ResponseEntity.ok(c);
				}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> destroy(@PathVariable("id") Integer id){
		return categoryRepository.findById(id)
				.map(category -> {
					categoryRepository.deleteById(id);
					return ResponseEntity.ok().build();
				}).orElse(ResponseEntity.notFound().build());
	}
}

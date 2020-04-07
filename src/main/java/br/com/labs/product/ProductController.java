package br.com.labs.product;

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
@RequestMapping("/product")
public class ProductController {

	private ProductRepository productRepository;

	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@GetMapping
	public ResponseEntity<Page<Product>> index(@PageableDefault(page = 0, size = 20) Pageable pageable) {
		return ResponseEntity.ok(productRepository.findAll(pageable));
	}

	@PostMapping
	public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
		productRepository.save(product);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId())
				.toUri();

		return ResponseEntity.created(location).body(product);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> show(@PathVariable("id") Integer id) {
		return productRepository.findById(id).map(product -> ResponseEntity.ok(product))
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> update(@PathVariable("id") Integer id, Product product) {
		return productRepository.findById(id).map(p -> {

			p.setId(id);
			p.setName(product.getName());
			p.setCategory(product.getCategory());
			p.setDescription(product.getDescription());
//			p.setStore(product.getStore());
			p.setPrice(product.getPrice());

			productRepository.save(p);

			return ResponseEntity.ok(p);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		return productRepository.findById(id).map(product -> {
			productRepository.delete(product);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}

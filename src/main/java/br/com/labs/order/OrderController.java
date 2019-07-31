package br.com.labs.order;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {

	private OrderRepository orderRepository;

	public OrderController(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Mono<Order> create(@Valid @RequestBody Order order) {
		return orderRepository.save(order);
	}

	@GetMapping
	public Flux<Order> index() {
		return orderRepository.findAll();
	}
}

package br.com.labs.order;

import br.com.labs.product.Product;

public class Item {

	private Product product;

	private int quantity;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getTotalPrice() {
		return product.getPrice().doubleValue()*quantity;
	}
}

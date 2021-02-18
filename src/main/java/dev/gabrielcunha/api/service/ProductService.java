package dev.gabrielcunha.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import dev.gabrielcunha.api.entity.Product;
import dev.gabrielcunha.api.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Transactional
	public Product save(final Product product) {
		
		boolean isProductValid = isProductValid(product); 
		
		if (!isProductValid) { 
			return null;
		}
		
		Optional<Product> productFromDataBase = productRepository.findBySku(product.getSku());
		
		if (productFromDataBase.isPresent()) {
			
		}
		Product createdProduct = productRepository.save(product);
		
		return createdProduct;
	}
	
	public List<Product> findAll() {
		return productRepository.findAll();
	}
	
	public Product find(Long id) {
		Optional<Product> productFromDataBase = productRepository.findById(id);
		
		return productFromDataBase.orElse(null);
	}	
	
	
	public Product findBySku(Product product) {
		
		Optional<Product> productFromDataBase = productRepository.findBySku(product.getSku());
		
		return productFromDataBase.orElse(null);
	}
	

	public boolean isProductValid(Product product) {
		
		boolean isObjectEmpty = ObjectUtils.isEmpty(product);
		
		boolean isSkuEmpty = ObjectUtils.isEmpty(product.getSku());
		
		boolean isNegativeValue = product.getValue().compareTo(BigDecimal.ZERO) < 0;
		
		if (isObjectEmpty || isSkuEmpty || isNegativeValue) {
			return false;
		}
		return true;
	}
}

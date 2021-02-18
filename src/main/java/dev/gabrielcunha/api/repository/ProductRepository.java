package dev.gabrielcunha.api.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.gabrielcunha.api.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

//	@Query("select p from Product p where p.sku = :sku")
	Optional<Product> findBySku(String sku);
	
	Product findBySkuAndValue(String sku, BigDecimal value);
	
//	List<Product> findByNameIgnoreCase(String name, Sort sort);
	
}

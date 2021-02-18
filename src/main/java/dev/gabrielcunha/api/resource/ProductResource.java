package dev.gabrielcunha.api.resource;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.gabrielcunha.api.entity.Product;
import dev.gabrielcunha.api.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Class to create, read, update or delete a resource.", tags = {"Product"})
@RestController
@RequestMapping("/products")
public class ProductResource {
	
	@Autowired
	private ProductService productService;
	
	@ApiOperation(value="Create a new resource from scratch.")	
	@ApiResponses(value = { 
	        @ApiResponse(code = 201, message = "Resource created."), 
	        @ApiResponse(code = 400, message = "Bad Request - Invalid resource."), 
	        @ApiResponse(code = 409, message = "Conflict - Resource already exists.") })		
	@PostMapping
	public ResponseEntity<Product> create(@RequestBody Product product) {
		
		boolean isValid = productService.isProductValid(product);
		
		if (!isValid) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		Product productFromDataBase = productService.findBySku(product);
		
		if (!ObjectUtils.isEmpty(productFromDataBase)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		Product productCreated = productService.save(product);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
											 .path("/{id}")
											 .buildAndExpand(product.getId()).toUri();
		
		return ResponseEntity.created(uri).body(productCreated);
	}
	
	@ApiOperation(value="List all resources")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sucesso.", response=List.class )  })
	@GetMapping
	public List<Product> findAll() throws Exception{
		
		return productService.findAll();
		
	}	
	
    @ApiParam(name = "id",
			  value = "Resource id to find. Can not be empty",
			  example = "72",
			  required = true)	
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Success.", response = Product.class),
	        @ApiResponse(code = 404, message = "Resource not found.") 
	})	
	@GetMapping("/{id}")
	public ResponseEntity<Product> find(@PathVariable Long id) {
		
		Product fabricante = productService.find(id);
		
		if (!ObjectUtils.isEmpty(fabricante)) {
			
			return ResponseEntity.ok(fabricante);
			
		} else {
			
			return ResponseEntity.notFound().build();
		}
	}	
	

}

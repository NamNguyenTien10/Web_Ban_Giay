package com.spring.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query(value = "SELECT p FROM Product p WHERE p.category.name = :cate")
	public List<Product> findAllByCategoryName(@Param("cate") String categoryName);

	@Query(value = "SELECT p FROM Product p WHERE p.name LIKE :keyword")
	List<Product> findByKeyword(@Param("keyword") String keyword);

	public Product getProductById(Integer productId);

	@Query(value = "SELECT p FROM Product p WHERE p.price BETWEEN ?1 AND ?2")
	Page<Product> findByPrice( Integer min, Integer max, Pageable pageable);
	
	@Query("SELECT p FROM Product p ORDER BY p.price ASC")
	Page<Product> findAllProductsShortByPriceASC(Pageable pageable);

	@Query("SELECT p FROM Product p ORDER BY p.price DESC")
	Page<Product> findAllProductsShortByPriceDESC(Pageable pageable);
	
	@Query("SELECT p FROM Product p ORDER BY p.name ASC")
	Page<Product> findAllProductsShortByName(Pageable pageable);

}

package org.solteh.repository;

import org.solteh.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByCode(String code);

	@Override
	List<Product> findAll();

	@Override
	Page<Product> findAll(Pageable pageable);

	@Query(value = "SELECT prod.* FROM products prod LEFT OUTER JOIN orderdetail_products p2 ON prod.Code=p2.products_KEY AND p2.products >=1 group by prod.code order by p2.products DESC LIMIT 3", nativeQuery = true)
	List<Product> find();
}

package org.solteh.repository;

import org.solteh.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByCode(String code);

	@Override
	List<Product> findAll();

	@Override
	Page<Product> findAll(Pageable pageable);

}

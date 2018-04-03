package org.solteh.repository;

import org.solteh.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByCode(String code);

    @Override
    List<Product> findAll();
}

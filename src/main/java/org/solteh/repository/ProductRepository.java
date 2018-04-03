package org.solteh.repository;

import org.solteh.entity.Product;
import org.solteh.model.ProductInfo;
import org.solteh.pagination.PaginationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByCode(String code);

    @Override
    List<Product> findAll();

    @Query("select p from Product p where p.name like ?4 ")
    PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,
                                                String likeName);
}

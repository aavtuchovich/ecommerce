package org.solteh.repository;

import org.solteh.entity.Product;
import org.solteh.model.ProductInfo;
import org.solteh.pagination.PaginationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProduct(String code);

    ProductInfo findProductInfo(String code);

    PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,
                                                String likeName);

    PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage);
}

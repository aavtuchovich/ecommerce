package org.solteh.repository;

import org.solteh.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    String FIND_TOP_PRODUCTS_WITH_LIMIT = "SELECT prod.* FROM products prod LEFT OUTER JOIN orderdetail_products p2 ON prod.Code=p2.products_KEY AND p2.products >=1 group by prod.code order by p2.products DESC LIMIT 3";
    String SELECT_TOPSALES_PRODUCTS_WITH_PAGINATION = "SELECT prod.* FROM products prod INNER JOIN orderdetail_products p2  ON prod.Code= p2.products_KEY AND  p2.products>=1 group by  prod.code  \n-- #pageable\n";
    String SELECT_COUNT_FROM_TOPSALES = "SELECT count(products) FROM products prod INNER JOIN orderdetail_products p2  ON prod.Code= p2.products_KEY AND  p2.products>=1 group by  prod.code";
    String SELECT_COUNT_SALES_FROM_PRODUCT = "SELECT SUM(p2.products) FROM products prod INNER JOIN orderdetail_products p2  ON prod.Code= p2.products_KEY AND prod.code=:#{#product.code} AND p2.products>=1 group by  prod.code";
    String SELECT_MAX_SALES = "SELECT MAX(reg_sum) FROM (select sum(p1.products) as reg_sum FROM orderdetail_products p1 group by p1.products_key) p2";

    Product findByCode(String code);

    @Override
    List<Product> findAll();

    @Override
    Page<Product> findAll(Pageable pageable);

    @Query(value = FIND_TOP_PRODUCTS_WITH_LIMIT, nativeQuery = true)
    List<Product> find();

    @Query(value = SELECT_TOPSALES_PRODUCTS_WITH_PAGINATION,
            countQuery = SELECT_COUNT_FROM_TOPSALES, nativeQuery = true)
    Page<Product> findTopSales(Pageable pageable);

    @Query(value = SELECT_COUNT_SALES_FROM_PRODUCT, nativeQuery = true)
    List<BigDecimal> getSalesCountFromProduct(@Param("product") Product product);

    @Query(value = SELECT_MAX_SALES, nativeQuery = true)
    List<BigDecimal> getMaxAmountProductFromOrders();
}

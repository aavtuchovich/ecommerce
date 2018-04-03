package org.solteh.repository;

import org.solteh.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("Select max(o.id) from Order o")
    int getMaxOrderNum();

    @Override
    List<Order> findAll();

    List<Order> findOrdersByUserId(long id);

    @Override
    Order getOne(Long aLong);
}

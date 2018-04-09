package org.solteh.repository;

import org.solteh.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	@Override
	List<Order> findAll();

	List<Order> findOrdersByUserId(long id);

	@Query(value = "select count(*),Order_Date from orders group by DATE_FORMAT(Order_Date, '%Y-%m-%d');",nativeQuery = true)
	List<Object[]> findOrdersWithGroupByDate();

	@Override
	Order getOne(Long aLong);
}

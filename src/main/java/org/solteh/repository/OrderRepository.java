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

	@Override
	Order getOne(Long aLong);
}

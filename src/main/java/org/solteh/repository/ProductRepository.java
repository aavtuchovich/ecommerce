package org.solteh.repository;

import org.solteh.model.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByCode(String code);

	@Override
	List<Product> findAll();

	@Override
	Page<Product> findAll(Pageable pageable);


}

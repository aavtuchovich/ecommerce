package org.solteh.repository;

import org.solteh.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}

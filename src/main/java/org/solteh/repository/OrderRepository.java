package org.solteh.repository;

import org.solteh.entity.Order;
import org.solteh.model.OrderDetailInfo;
import org.solteh.model.OrderInfo;
import org.solteh.pagination.PaginationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    int getMaxOrderNum();

    PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage);

    Order findOrder(String orderId);

    OrderInfo getOrderInfo(String orderId);

    List<OrderDetailInfo> listOrderDetailInfos(String orderId);
}

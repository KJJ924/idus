package me.jaejoon.idus.order.repository;

import java.util.List;
import me.jaejoon.idus.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/06
 */

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByConsumer(String orderer);
}

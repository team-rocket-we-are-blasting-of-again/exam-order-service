package com.teamrocket.Template.repository;

import com.teamrocket.Template.dto.OrderDTO;
import com.teamrocket.Template.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

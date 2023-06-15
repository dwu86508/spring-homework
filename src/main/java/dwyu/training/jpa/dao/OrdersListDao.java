package dwyu.training.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import dwyu.training.jpa.entity.Orders;
import dwyu.training.jpa.entity.OrdersList;

public interface OrdersListDao extends JpaRepository<OrdersList, Long>{

}

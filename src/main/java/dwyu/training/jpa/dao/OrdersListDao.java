package dwyu.training.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import dwyu.training.jpa.entity.Orders;

public interface OrdersListDao extends JpaRepository<Orders, Long>{

}

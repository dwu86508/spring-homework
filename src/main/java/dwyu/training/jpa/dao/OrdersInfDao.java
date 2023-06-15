package dwyu.training.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import dwyu.training.jpa.entity.OrdersInf;

public interface OrdersInfDao extends JpaRepository<OrdersInf, Long>{

}
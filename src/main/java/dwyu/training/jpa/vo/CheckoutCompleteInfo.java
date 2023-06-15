package dwyu.training.jpa.vo;

import java.util.List;

import dwyu.training.jpa.entity.Goods;
import dwyu.training.jpa.entity.OrdersInf;
import dwyu.training.jpa.entity.OrdersList;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckoutCompleteInfo {
	
	private OrderCustomer orderCustomer;
	
	private OrdersInf ordersInf;
	
	private List<OrdersList> ordersList;

}

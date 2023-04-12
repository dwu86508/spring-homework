package dwyu.training.jpa.vo;

import java.util.List;

import dwyu.training.jpa.entity.Goods;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckoutCompleteInfo {
	
	private OrderCustomer orderCustomer;
	
	private List<Goods> orderGoodsList;

}

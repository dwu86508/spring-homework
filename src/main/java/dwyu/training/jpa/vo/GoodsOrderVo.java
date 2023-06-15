package dwyu.training.jpa.vo;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data

public class GoodsOrderVo {


	private long goodsID;
	
	private String goodsName;

	private String description;	

	private int price;

	private int quantity;

	private String imageName;

	private String status;
	
	private List<OrderVo> orderVos;
	
}

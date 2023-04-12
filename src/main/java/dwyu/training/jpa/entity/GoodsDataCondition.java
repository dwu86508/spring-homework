package dwyu.training.jpa.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class GoodsDataCondition {
	
	private Integer goodsID;
	
	private String goodsName;
	
	private Integer startPrice;
	
	private Integer endPrice;
	
	private String priceSort;
	
	private Integer quantity;
	
	private String status;
	
	
}

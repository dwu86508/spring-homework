package dwyu.training.jpa.entity;

import java.util.List;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class FrontGoodsInfo {
	
	
		
	private List<Goods> goodsList;
	
	private GenericPageable genericPageable;
		
	
}

package dwyu.training.jpa.entity;

import java.util.List;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class GoodsDataInfo {
	
	private List<Object> goodsList;
	
	private GenericPageable genericPageable;
	
}

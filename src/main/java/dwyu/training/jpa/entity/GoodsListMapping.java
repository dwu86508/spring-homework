package dwyu.training.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import lombok.Data;

@SqlResultSetMapping(
	    name = "GoodsListMapping",
	    entities={
	        @EntityResult(
		        entityClass = dwyu.training.jpa.entity.GoodsListMapping.class,
		        fields = {
		        	@FieldResult(name="goodsID", column="GOODS_ID"),
		            @FieldResult(name="goodsName",  column="GOODS_NAME"),
		        	@FieldResult(name="goodsPrice",  column="PRICE"),
		        	@FieldResult(name="goodsQuantity",  column="QUANTITY"),
		        	@FieldResult(name="goodsImageName",  column="IMAGE_NAME"),
		        	@FieldResult(name="goodsStatus",  column="STATUS")
		        	
		        }
	        )
	    }
	)


@Data
@Entity
public class GoodsListMapping {
	@Id
	private Long goodsID;
	
	private String goodsName;
		
	private Integer goodsPrice;
	
	private Integer goodsQuantity;

	private String goodsImageName;

	private String goodsStatus;
}

package dwyu.training.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SqlResultSetMapping(
	    name = "GoodsReportSalesList",
	    entities={
	        @EntityResult(
		        entityClass = dwyu.training.jpa.entity.GoodsReportSalesList.class,
		        fields = {		        	
		        	@FieldResult(name="goodsName", column="GOODS_NAME"),
		            @FieldResult(name="goodsBuyPrice",  column="GOODS_BUY_PRICE"),
		        	@FieldResult(name="orderID",  column="ORDER_ID"),
		        	@FieldResult(name="orderDate",  column="ORDER_DATE"),
		        	@FieldResult(name="buyQuantity",  column="BUY_QUANTITY"),
		        	@FieldResult(name="customerName",  column="CUSTOMER_NAME")
		        }
	        )
	    }
	)


@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@ToString
@Table(name = "BEVERAGE_ORDER")
public class GoodsReportSalesList {	
	
	@Column(name = "GOODS_Name")
	private String goodsName;
		
	@Column(name = "GOODS_BUY_PRICE")
	private Integer goodsBuyPrice;
	
	@Id
	@Column(name = "ORDER_ID")
	private Long orderID;
	
	@Column(name = "ORDER_DATE")
	private String orderDate;
	
	@Column(name = "BUY_QUANTITY")
	private Integer buyQuantity;	
	
	@Column(name = "CUSTOMER_Name")
	private String customerName;

}

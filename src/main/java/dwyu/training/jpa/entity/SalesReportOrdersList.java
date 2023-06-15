package dwyu.training.jpa.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SqlResultSetMapping(
	    name = "SalesReportOrdersList",
	    entities={
	        @EntityResult(
		        entityClass = dwyu.training.jpa.entity.SalesReportOrdersList.class,
		        fields = {
		        	@FieldResult(name="listNum",  column="LIST_NUM"),
		        	@FieldResult(name="goodsID", column="GOODS_ID"),
		        	@FieldResult(name="goodsName", column="GOODS_NAME"),
		            @FieldResult(name="goodsBuyPrice",  column="GOODS_BUY_PRICE"),
		        	@FieldResult(name="buyQuantity",  column="BUY_QUANTITY")
		        }
	        )
	    }
	)


@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@ToString
@Table(name = "BEVERAGE_ORDER_List")
public class SalesReportOrdersList {
	
	@Id
	@Column(name = "LIST_NUM")
	private Long listNum;

	@Column(name = "GOODS_ID")
	private String goodsID;
	
//	@Transient
//	private String goodsName;
//	@Column(name = "GOODS_Name")
//	private String goodsName;
		
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GOODS_ID", referencedColumnName = "GOODS_ID", insertable = false, updatable = false)
	private Goods goods;
	
	@Column(name = "GOODS_BUY_PRICE")
	private Integer goodsBuyPrice;	
	
	@Column(name = "BUY_QUANTITY")
	private Integer buyQuantity;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY) // EAGER(在查詢時立刻載入關聯的物件)
	@JoinColumn(name = "INF_NUM")
	private OrdersInf ordersInf;
	
	

}

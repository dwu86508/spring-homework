package dwyu.training.jpa.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SqlResultSetMapping(
	    name = "SalesReportOrdersInf",
	    entities={
	        @EntityResult(
		        entityClass = dwyu.training.jpa.entity.SalesReportOrdersInf.class,
		        fields = {
		        	@FieldResult(name="infNum",  column="INF_NUM"),
		        	@FieldResult(name="orderDate", column="ORDER_DATE"),
		        	@FieldResult(name="customerName", column="CUSTOMER_NAME"),
		            @FieldResult(name="orderTotal",  column="ORDER_TOTAL"),
		        	@FieldResult(name="discount",  column="DISCOUNT")
		        }
	        )
	    }
	)


@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@ToString
@Table(name = "BEVERAGE_ORDER_Inf")
public class SalesReportOrdersInf {

	@Id
	@Column(name = "INF_NUM")
	private Long infNum;

	@Column(name = "ORDER_DATE")
	private String orderDate;
	
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
		
	@Column(name = "ORDER_TOTAL")
	private Integer orderTotal;	
	
	@Column(name = "DISCOUNT")
	private Integer discount;
	
	
	@OneToMany(
		// 透過mappedBy來連結雙向之間的關係，所對映的物件欄位名稱(除非關係是單向的，否則此參數必須)
		mappedBy = "ordersInf",
		cascade = {CascadeType.PERSIST}, orphanRemoval = true,
		fetch = FetchType.EAGER // LAZY(只在用到時才載入關聯的物件)		
	)
	@OrderBy(value = "listNum")
	private List<SalesReportOrdersList> orderList;
}

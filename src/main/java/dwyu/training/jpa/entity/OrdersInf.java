package dwyu.training.jpa.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"infNum"})
@ToString
@Entity
@Table(name = "BEVERAGE_ORDER_INF")
public class OrdersInf {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BEVERAGE_ORDER_INF_SEQ_GEN")
    @SequenceGenerator(name = "BEVERAGE_ORDER_INF_SEQ_GEN", sequenceName = "BEVERAGE_ORDER_INF_SEQ", allocationSize = 1)
	@Column(name = "INF_NUM")
	private long infNum;
	
	@Column(name = "ORDER_DATE")
	private LocalDateTime orderDate;
	
	@Column(name = "CUSTOMER_ID")
	private String customerID;	
	
	@Column(name = "ORDER_TOTAL")
	private long orderTotal;
	
	@Column(name = "DISCOUNT")
	private long discount;
	
	@JsonIgnore
	@OneToMany(
		// 透過mappedBy來連結雙向之間的關係，所對映的物件欄位名稱(除非關係是單向的，否則此參數必須)
		mappedBy = "ordersInf",
		cascade = {CascadeType.PERSIST}, orphanRemoval = true,
		fetch = FetchType.LAZY // LAZY(只在用到時才載入關聯的物件)		
	)
	@OrderBy(value = "listNum")
	private List<OrdersList> orderList;
	
	@JsonIgnore
	@OneToMany(
		// 透過mappedBy來連結雙向之間的關係，所對映的物件欄位名稱(除非關係是單向的，否則此參數必須)
		mappedBy = "ordersInf",
		cascade = {CascadeType.PERSIST}, orphanRemoval = true,
		fetch = FetchType.LAZY // LAZY(只在用到時才載入關聯的物件)		
	)
	@OrderBy(value = "listNum")
	private List<PointLog> pointLog;
	
}

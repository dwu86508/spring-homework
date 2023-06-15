package dwyu.training.jpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"listNum"})
@ToString
@Entity
@Table(name = "BEVERAGE_ORDER_LIST")
public class OrdersList {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BEVERAGE_ORDER_LIST_SEQ_GEN")
    @SequenceGenerator(name = "BEVERAGE_ORDER_LIST_SEQ_GEN", sequenceName = "BEVERAGE_ORDER_LIST_SEQ", allocationSize = 1)
	@Column(name = "LIST_NUM")
	private long listNum;
	
	@Column(name = "GOODS_BUY_PRICE")
	private long goodsBuyPrice;
	
	@Column(name = "BUY_QUANTITY")
	private long buyQuantity;
	
	@ManyToOne(fetch = FetchType.EAGER) // EAGER(在查詢時立刻載入關聯的物件)
	@JoinColumn(name = "GOODS_ID")
	private Goods goods;
	
//	@JsonIgnore	
	@ManyToOne(fetch = FetchType.EAGER) // EAGER(在查詢時立刻載入關聯的物件)
	@JoinColumn(name = "INF_NUM")
	private OrdersInf ordersInf;
}

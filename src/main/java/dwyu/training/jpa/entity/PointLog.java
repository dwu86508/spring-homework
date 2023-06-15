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
@EqualsAndHashCode(of = {"plNum"})
@ToString
@Entity
@Table(name = "BEVERAGE_MEMBER_POINTS_LOG")
public class PointLog {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BEVERAGE_MEMBER_POINTS_LOG_SEQ_GEN")
    @SequenceGenerator(name = "BEVERAGE_MEMBER_POINTS_LOG_SEQ_GEN", sequenceName = "BEVERAGE_MEMBER_POINTS_LOG_SEQ", allocationSize = 1)
	@Column(name = "PL_NUM")
	private long plNum;	
	
	@Column(name = "CUSTOMER_ID")
	private String customerID;
	
	@ManyToOne(fetch = FetchType.EAGER) // EAGER(在查詢時立刻載入關聯的物件)
	@JoinColumn(name = "ORDER_INF_NUM")
	private OrdersInf ordersInf;
	
	@Column(name = "DATETIME")
	private LocalDateTime datetime;	

	@Column(name = "POINT_RECORD")
	private long pointRecord;
	
}

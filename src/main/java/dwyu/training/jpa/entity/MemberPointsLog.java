package dwyu.training.jpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@Table(name = "BEVERAGE_MEMBER_POINTS_LOG")
public class MemberPointsLog {

	@Id
	@Column(name = "PL_NUM")
	private long plNum;
	
	@Column(name = "CUSTOMER_ID")
	private String customerID;
	
	@Column(name = "ORDER_INF_NUM")
	private long infNum;	
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "DATETIME")
	private LocalDateTime dateTime;
		
	@Column(name = "POINT_RECORD")
	private long point_record;
	
}

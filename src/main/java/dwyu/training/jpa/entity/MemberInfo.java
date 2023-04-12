package dwyu.training.jpa.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@Table(name = "BEVERAGE_MEMBER")
public class MemberInfo {
	
	@Id
	@Column(name = "IDENTIFICATION_NO")
	private String memberID;
	
	@Column(name = "PASSWORD")
	private String memberPWD;
	
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	
}

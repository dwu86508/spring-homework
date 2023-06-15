package dwyu.training.jpa.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderCustomer {
	
	private String cusName;
	
	private String mobileNumber;
	
	private String homeNumber;
	
	private String orderAddr;
	
	private long points;
	

}

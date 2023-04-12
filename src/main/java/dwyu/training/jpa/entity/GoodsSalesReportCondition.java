package dwyu.training.jpa.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class GoodsSalesReportCondition {
	
	private String startDate;
	
	private String endDate;

}

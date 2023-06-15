package dwyu.training.jpa.entity;

import java.util.List;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class SalesReportInf {
	
	private List<SalesReportOrdersInf> goodsReportSalesList;
	
	private GenericPageable genericPageable;
	
}

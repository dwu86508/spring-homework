package dwyu.training.jpa.entity;

import java.util.List;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class GoodsReportSalesInfo {
	
	private List<GoodsReportSalesList> goodsReportSalesList;
	
	private GenericPageable genericPageable;
}

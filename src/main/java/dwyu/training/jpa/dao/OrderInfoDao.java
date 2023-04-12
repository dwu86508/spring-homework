package dwyu.training.jpa.dao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import dwyu.training.jpa.entity.GenericPageable;
import dwyu.training.jpa.entity.GoodsReportSalesInfo;
import dwyu.training.jpa.entity.GoodsReportSalesList;
import dwyu.training.jpa.entity.GoodsSalesReportCondition;

@Repository
public class OrderInfoDao {
	
	@PersistenceContext(name = "oracleEntityManager")
    private EntityManager entityManager;
	
	public GoodsReportSalesInfo queryOrderList(GoodsSalesReportCondition condition,GenericPageable genericPageable) {
		StringBuilder sqlStart = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		sqlStart.append("SELECT ORDER_ID,ORDER_DATE,GOODS_BUY_PRICE,BUY_QUANTITY,CUSTOMER_NAME,GOODS_NAME FROM (");
		sb.append(" SELECT ROWNUM ROW_NUM,O.ORDER_ID,O.ORDER_DATE,O.GOODS_BUY_PRICE,O.BUY_QUANTITY,M.CUSTOMER_NAME,G.GOODS_NAME FROM BEVERAGE_ORDER O  ");
		sb.append(" LEFT JOIN BEVERAGE_GOODS G ON O.GOODS_ID=G.GOODS_ID ");
		sb.append(" LEFT JOIN BEVERAGE_MEMBER M ON O.CUSTOMER_ID=M.IDENTIFICATION_NO ");
		sb.append(" WHERE O.ORDER_DATE BETWEEN TO_DATE( ?1 || ' 00:00:00','YYYY-mm-DD HH24:MI:SS') AND TO_DATE( ?2 || ' 23:59:59','YYYY-mm-DD HH24:MI:SS') ");
		sb.append(" ORDER BY O.ORDER_ID  ");
		sqlStart.append(sb);
		sqlStart.append(") WHERE ROW_NUM BETWEEN ?3 AND ?4 ");
		
		int endNum = genericPageable.getCurrentPageNo()*genericPageable.getPageDataSize();
		int startNum = endNum-genericPageable.getPageDataSize()+1;
		Query query  = entityManager.createNativeQuery(sqlStart.toString(), "GoodsReportSalesList");
		query.setParameter(1, condition.getStartDate());
		query.setParameter(2, condition.getEndDate());
		query.setParameter(3, startNum);
		query.setParameter(4, endNum);		
		Stream<GoodsReportSalesList> goodsReportSalesListMappingStream = query.getResultStream().map(s -> (GoodsReportSalesList)s);
		List<GoodsReportSalesList> goodsReportSalesListMapping =  goodsReportSalesListMappingStream.collect(Collectors.toList());
		
		Query queryCount  = entityManager.createNativeQuery(sb.toString(), "GoodsReportSalesList");
		queryCount.setParameter(1, condition.getStartDate());
		queryCount.setParameter(2, condition.getEndDate());		
		Stream<GoodsReportSalesList> goodsReportSalesListMappingStreamCount = queryCount.getResultStream().map(s -> (GoodsReportSalesList)s);
		List<GoodsReportSalesList> goodsReportSalesListMappingCount =  goodsReportSalesListMappingStreamCount.collect(Collectors.toList());	
		genericPageable.setDatTotalSize(goodsReportSalesListMappingCount.size());
		GoodsReportSalesInfo goodsReportSalesInfo = GoodsReportSalesInfo.builder()
				.goodsReportSalesList(goodsReportSalesListMapping)
				.genericPageable(genericPageable)
				.build();
		
		
		return goodsReportSalesInfo;
	}
	
	
}

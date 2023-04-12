package dwyu.training.jpa.dao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import dwyu.training.jpa.entity.GenericPageable;
import dwyu.training.jpa.entity.GoodsDataCondition;
import dwyu.training.jpa.entity.GoodsDataInfo;
import dwyu.training.jpa.entity.GoodsListMapping;

@Repository
public class GoodsListDao {	
	
	@PersistenceContext(name = "oracleEntityManager")
    private EntityManager entityManager;
	
	public GoodsDataInfo queryGoodsSearch(GoodsDataCondition condition,GenericPageable genericPageable){	
		
		StringBuilder sqlStart = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		sqlStart.append("SELECT GOODS_ID,GOODS_NAME,PRICE,QUANTITY,IMAGE_NAME,STATUS FROM ");
		sb.append("(SELECT ROWNUM ROW_NUM, GOODS_ID,GOODS_NAME,PRICE,QUANTITY,IMAGE_NAME,STATUS FROM BEVERAGE_GOODS B WHERE GOODS_ID=GOODS_ID  ");
			if(condition.getGoodsID()!=null) {
				sb.append(" AND B.GOODS_ID=" + condition.getGoodsID());
			}
			if(condition.getGoodsName()!=null  ) {
				sb.append(" AND LOWER(B.GOODS_NAME) like '%" + condition.getGoodsName().toLowerCase() +"%'" );
			}
			if(condition.getStartPrice()!= null) {
				sb.append(" AND B.PRICE >= " + condition.getStartPrice());
			}
			if(condition.getEndPrice()!= null) {
				sb.append(" AND B.PRICE <= " + condition.getEndPrice());
			}
			if(condition.getQuantity()!=null) {
				sb.append(" AND QUANTITY < " + condition.getQuantity());
			}
			if(condition.getStatus()!=null) {
				sb.append(" AND STATUS = " + condition.getStatus());				
			}
			if(condition.getPriceSort()!=null) {
				if(condition.getPriceSort().equals("asc")) {
					sb.append(" ORDER BY B.PRICE asc");
		        }else if(condition.getPriceSort().equals("desc")) {
		        	sb.append(" ORDER BY B.PRICE desc");
		        }				
			}
			sb.append(" )");
			sqlStart.append(sb);
			sqlStart.append(" WHERE ROW_NUM BETWEEN ?1 AND ?2 ");
			
		int endNum = genericPageable.getCurrentPageNo()*genericPageable.getPageDataSize();
		int startNum = endNum-genericPageable.getPageDataSize()+1;
		Query query  = entityManager.createNativeQuery(sqlStart.toString(), "GoodsListMapping");
		query.setParameter(1, startNum);
		query.setParameter(2, endNum);
		Stream<GoodsListMapping> goodsListMappingStream = query.getResultStream().map(s -> (GoodsListMapping)s);
		List<Object> goodsListMapping = goodsListMappingStream.collect(Collectors.toList());
	
		Query queryCount  = entityManager.createNativeQuery(sb.toString(), "GoodsListMapping");	
		Stream<GoodsListMapping> goodsListMappingStreamCount = queryCount.getResultStream().map(s -> (GoodsListMapping)s);
		List<GoodsListMapping> goodsListMappingCount = goodsListMappingStreamCount.collect(Collectors.toList());
		genericPageable.setDatTotalSize(goodsListMappingCount.size());
		GoodsDataInfo goodsDataInfo = GoodsDataInfo.builder().goodsList(goodsListMapping).genericPageable(genericPageable).build();
		return goodsDataInfo;
	}
	
}

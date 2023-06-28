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
		
			if (condition.getPriceSort() != null) {
			    sb.append(" (SELECT ROW_NUMBER() OVER (ORDER BY B.PRICE "+condition.getPriceSort()+" ) ROW_NUM ,");
			} else {
			    sb.append(" (SELECT ROW_NUMBER() OVER (ORDER BY B.GOODS_ID ASC) ROW_NUM ,");
			}
			sb.append(" GOODS_ID,GOODS_NAME,PRICE,QUANTITY,IMAGE_NAME,STATUS FROM BEVERAGE_GOODS B WHERE GOODS_ID=GOODS_ID ");
			if(condition.getGoodsID()!=null) {
				sb.append(" AND B.GOODS_ID= (:goodsID) ");
			}
			if (condition.getGoodsName() != null) {
			    sb.append(" AND LOWER(B.GOODS_NAME) LIKE (:goodsName) ");
			}
			if (condition.getStartPrice() != null) {
			    sb.append(" AND B.PRICE >= (:startPrice)");
			}
			if (condition.getEndPrice() != null) {
			    sb.append(" AND B.PRICE <= (:endPrice) ");
			}
			if (condition.getQuantity() != null) {
			    sb.append(" AND B.QUANTITY < (:quantity) ");
			}
			if (condition.getStatus() != null && Integer.parseInt(condition.getStatus()) <= 1) {
			    sb.append(" AND B.STATUS = (:status) ");
			}
			
			sb.append(" )");
			sqlStart.append(sb);
			sqlStart.append(" WHERE ROW_NUM BETWEEN (:startNum) AND (:endNum) ");
			
		int endNum = genericPageable.getCurrentPageNo()*genericPageable.getPageDataSize();
		int startNum = endNum-genericPageable.getPageDataSize()+1;
		Query query  = entityManager.createNativeQuery(sqlStart.toString(), "GoodsListMapping");			
		if(condition.getGoodsID()!=null) {
			query.setParameter("goodsID", condition.getGoodsID());
		}
		if (condition.getGoodsName() != null) {
			query.setParameter("goodsName", "%" + condition.getGoodsName().toLowerCase() + "%");
		}
		if (condition.getStartPrice() != null) {
			query.setParameter("startPrice", condition.getStartPrice());
		}
		if (condition.getEndPrice() != null) {
			query.setParameter("endPrice", condition.getEndPrice());
		}
		if (condition.getQuantity() != null) {
			query.setParameter("quantity", condition.getQuantity());
		}
		if (condition.getStatus() != null && Integer.parseInt(condition.getStatus()) <= 1) {
			query.setParameter("status", condition.getStatus());
		}
		query.setParameter("startNum", startNum);
		query.setParameter("endNum", endNum);
		Stream<GoodsListMapping> goodsListMappingStream = query.getResultStream().map(s -> (GoodsListMapping)s);
		List<Object> goodsListMapping = goodsListMappingStream.collect(Collectors.toList());
	
		Query queryCount  = entityManager.createNativeQuery(sb.toString(), "GoodsListMapping");		
		if(condition.getGoodsID()!=null) {
			queryCount.setParameter("goodsID", condition.getGoodsID());
		}
		if (condition.getGoodsName() != null) {
			queryCount.setParameter("goodsName", "%" + condition.getGoodsName().toLowerCase() + "%");
		}
		if (condition.getStartPrice() != null) {
			queryCount.setParameter("startPrice", condition.getStartPrice());
		}
		if (condition.getEndPrice() != null) {
			queryCount.setParameter("endPrice", condition.getEndPrice());
		}
		if (condition.getQuantity() != null) {
			queryCount.setParameter("quantity", condition.getQuantity());
		}
		if (condition.getStatus() != null && Integer.parseInt(condition.getStatus()) <= 1) {
			queryCount.setParameter("status", condition.getStatus());
		}
		Stream<GoodsListMapping> goodsListMappingStreamCount = queryCount.getResultStream().map(s -> (GoodsListMapping)s);
		List<GoodsListMapping> goodsListMappingCount = goodsListMappingStreamCount.collect(Collectors.toList());
		genericPageable.setDatTotalSize(goodsListMappingCount.size());
		GoodsDataInfo goodsDataInfo = GoodsDataInfo.builder().goodsList(goodsListMapping).genericPageable(genericPageable).build();
		return goodsDataInfo;
	}
	
}

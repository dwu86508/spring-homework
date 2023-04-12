package dwyu.training.jpa.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dwyu.training.jpa.dao.GoodsInfoDao;
import dwyu.training.jpa.dao.OrdersListDao;
import dwyu.training.jpa.entity.GenericPageable;
import dwyu.training.jpa.entity.Goods;
import dwyu.training.jpa.entity.GoodsDataInfo;
import dwyu.training.jpa.entity.Orders;
import dwyu.training.jpa.vo.CheckoutCompleteInfo;
import dwyu.training.jpa.vo.GoodsVo;
import dwyu.training.jpa.vo.MemberInfoVo;
import dwyu.training.jpa.vo.OrderCustomer;

@Service
public class FrontendService {

	@Autowired
	private GoodsInfoDao goodsInfoDao;
	
	@Autowired
	private OrdersListDao ordersListDao;
	
	@Autowired
	private BackendService backendService;
	
	public GoodsDataInfo queryGoodsData(String searchKeyword ,GenericPageable genericPageable){
		
		if(searchKeyword==null) {
			searchKeyword="";
		}
		int pageNo = genericPageable.getCurrentPageNo() == 0 ? 1 : genericPageable.getCurrentPageNo();
		genericPageable.setCurrentPageNo(pageNo);
		//取得符合商品總數量
		List<Goods> goodsListCount= goodsInfoDao.findByGoodsNameContainingIgnoreCase(searchKeyword);		
		genericPageable.setDatTotalSize(goodsListCount.size());		
		Pageable pageable = PageRequest.of(genericPageable.getCurrentPageNo()-1,genericPageable.getPageDataSize());
		List<Goods> goodsList= goodsInfoDao.findByGoodsNameContainingIgnoreCase(pageable,searchKeyword);
		List<Object> goodsListMapping = goodsList.stream().collect(Collectors.toList());
		
		//計算頁碼
		GenericPageable newGenericPageable = backendService.calculatePage(genericPageable);
		GoodsDataInfo goodsDataInfo = GoodsDataInfo.builder()
				.goodsList(goodsListMapping)
				.genericPageable(newGenericPageable)
				.build();
		return goodsDataInfo;
	}
	
	@Transactional
	public CheckoutCompleteInfo checkoutGoods(MemberInfoVo sessionMemberInfo,OrderCustomer customer,List<GoodsVo> cartGoods){
		LocalDateTime datetime = LocalDateTime.now();
		Map<Long,Integer> buyInfo = new LinkedHashMap<>();		
		for(GoodsVo goods : cartGoods) {
			Integer quantity = buyInfo.get(goods.getGoodsID());
			buyInfo.put(goods.getGoodsID(), (quantity==null) ? 1 : ++quantity);			
		}
		//取得資料庫內商品資訊
		List<Goods> goodsInfo = goodsInfoDao.findByGoodsIDIn(buyInfo.keySet());		
		Map<Long, Goods> buyGoodsInf = new LinkedHashMap<>();
		for(Goods goods : goodsInfo) {			
			buyGoodsInf.put(goods.getGoodsID(), goods);
		}
		
		List<Goods> goodsBuyInfo = new ArrayList<>();
		List<Orders> orders = new ArrayList<>();
		buyInfo.forEach((goodsID,quantity)->{
			Goods goodInfo = Goods.builder()
					.goodsID(goodsID)
					.goodsName(buyGoodsInf.get(goodsID).getGoodsName())
					.goodsDescription(buyGoodsInf.get(goodsID).getGoodsDescription())
					.goodsPrice(buyGoodsInf.get(goodsID).getGoodsPrice())
					.goodsImageName(buyGoodsInf.get(goodsID).getGoodsImageName())
					.goodsStatus(buyGoodsInf.get(goodsID).getGoodsStatus())
					.build();
			Orders order = Orders.builder()
					.orderDate(datetime)					
					.customerID(sessionMemberInfo.getMemberID())
					.goodsBuyPrice(buyGoodsInf.get(goodsID).getGoodsPrice())
					.goods(buyGoodsInf.get(goodsID))
					.build();
			int goodsQuantity = buyGoodsInf.get(goodsID).getGoodsQuantity() - quantity;
			if(goodsQuantity>=0) {					
				buyGoodsInf.get(goodsID).setGoodsQuantity(goodsQuantity);
				//存入正確購資訊
				goodInfo.setGoodsQuantity(quantity);
				order.setBuyQuantity(quantity);
				goodsBuyInfo.add(goodInfo);
				orders.add(order);
			//如果商品庫存小於購買數量	
			}else {
				//如果庫存數量大於零
				if(buyGoodsInf.get(goodsID).getGoodsQuantity()>0) {					
					//存入正確購資訊
					goodInfo.setGoodsQuantity(buyGoodsInf.get(goodsID).getGoodsQuantity());
					order.setBuyQuantity(buyGoodsInf.get(goodsID).getGoodsQuantity());
					goodsBuyInfo.add(goodInfo);
					orders.add(order);
					//更新新庫存
					buyGoodsInf.get(goodsID).setGoodsQuantity(0);
				}
			}				
		});
		//計算總金額
		ordersListDao.saveAll(orders);
		return CheckoutCompleteInfo.builder().orderCustomer(customer).orderGoodsList(goodsBuyInfo).build();
	}
}

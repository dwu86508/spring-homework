package dwyu.training.jpa.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import dwyu.training.jpa.dao.GoodsInfoDao;
import dwyu.training.jpa.dao.MemberDao;
import dwyu.training.jpa.dao.OrdersInfDao;
import dwyu.training.jpa.dao.OrdersListDao;
import dwyu.training.jpa.entity.GenericPageable;
import dwyu.training.jpa.entity.Goods;
import dwyu.training.jpa.entity.GoodsDataInfo;
import dwyu.training.jpa.entity.MemberInfo;
import dwyu.training.jpa.entity.Orders;
import dwyu.training.jpa.entity.OrdersInf;
import dwyu.training.jpa.entity.OrdersList;
import dwyu.training.jpa.entity.PointLog;
import dwyu.training.jpa.vo.CheckoutCompleteInfo;
import dwyu.training.jpa.vo.GoodsVo;
import dwyu.training.jpa.vo.MemberInfoVo;
import dwyu.training.jpa.vo.OrderCustomer;
import dwyu.training.jpa.vo.ShoppingCarGoods;


@Service
public class FrontendService {

	@Autowired
	private GoodsInfoDao goodsInfoDao;
	
	@Autowired
	private OrdersInfDao ordersInfDao;	
	
	@Autowired
	private MemberDao memberDao; 
	
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
	public CheckoutCompleteInfo checkoutGoods(MemberInfoVo sessionMemberInfo,OrderCustomer customer,List<ShoppingCarGoods> cartGoods){
		LocalDateTime datetime = LocalDateTime.now();
		
		//取得顧客資訊
		Optional<MemberInfo> optMemberInfo = memberDao.findById(sessionMemberInfo.getMemberID());
		MemberInfo memberInfo = null;
		if(optMemberInfo.isPresent()) {
			memberInfo = optMemberInfo.get();			
		}
		
		Map<Long,Integer> buyInfo = new LinkedHashMap<>();		
		for(ShoppingCarGoods goods : cartGoods) {			
			buyInfo.put(goods.getGoodsID(), goods.getBuyQuantity());			
		}
		//取得資料庫內商品資訊
		List<Goods> goodsInfo = goodsInfoDao.findByGoodsIDIn(buyInfo.keySet());		
		Map<Long, Goods> buyGoodsInf = new LinkedHashMap<>();
		for(Goods goods : goodsInfo) {			
			buyGoodsInf.put(goods.getGoodsID(), goods);
		}
		
		List<Goods> goodsBuyInfo = new ArrayList<>();
		List<OrdersList> ordersList = new ArrayList<>();
		//建立訂單資訊
		OrdersInf ordersInf = OrdersInf.builder()
				.orderDate(datetime)
				.customerID(sessionMemberInfo.getMemberID())
				.build();
		
		//取得訂單編號
		long listNum = ordersInf.getInfNum();
		buyInfo.forEach((goodsID,quantity)->{
			Goods goodInfo = Goods.builder()
					.goodsID(goodsID)
					.goodsName(buyGoodsInf.get(goodsID).getGoodsName())
					.goodsDescription(buyGoodsInf.get(goodsID).getGoodsDescription())
					.goodsPrice(buyGoodsInf.get(goodsID).getGoodsPrice())
					.goodsImageName(buyGoodsInf.get(goodsID).getGoodsImageName())
					.goodsStatus(buyGoodsInf.get(goodsID).getGoodsStatus())
					.build();
			OrdersList orderList = OrdersList.builder()
					.listNum(listNum)
					.goods(buyGoodsInf.get(goodsID))
					.goodsBuyPrice(buyGoodsInf.get(goodsID).getGoodsPrice())
					.ordersInf(ordersInf)
					.build();
			int goodsQuantity = buyGoodsInf.get(goodsID).getGoodsQuantity() - quantity;
			if(goodsQuantity>=0) {					
				buyGoodsInf.get(goodsID).setGoodsQuantity(goodsQuantity);
				//存入正確購資訊
				goodInfo.setGoodsQuantity(quantity);
				orderList.setBuyQuantity(quantity);
				goodsBuyInfo.add(goodInfo);
				ordersList.add(orderList);
			//如果商品庫存小於購買數量	
			}else {
				//如果庫存數量大於零
				if(buyGoodsInf.get(goodsID).getGoodsQuantity()>0) {					
					//存入正確購資訊
					goodInfo.setGoodsQuantity(buyGoodsInf.get(goodsID).getGoodsQuantity());
					orderList.setBuyQuantity(buyGoodsInf.get(goodsID).getGoodsQuantity());
					goodsBuyInfo.add(goodInfo);
					ordersList.add(orderList);
					//更新新庫存
					buyGoodsInf.get(goodsID).setGoodsQuantity(0);
				}
			}
		});
		//計算訂單總金額
		long total = ordersList.stream().mapToLong(g ->  g.getBuyQuantity() * g.getGoodsBuyPrice()).sum();
		ordersInf.setOrderTotal(total);
		//計算可折抵點數
		long discount = 0;
		discount = customer.getPoints() < total ? customer.getPoints() :  total;
		ordersInf.setDiscount(discount);
		//更新會員點數
		long pointAdd = total / 20;		
		memberInfo.setPoints(memberInfo.getPoints()-discount+pointAdd);		
		//會員點數異動紀錄
		List<PointLog> pointLogs = new ArrayList<>();
		if(pointAdd > 0) {
			PointLog pointLog = PointLog.builder()
					.customerID(memberInfo.getMemberID())
					.ordersInf(ordersInf)
					.datetime(datetime)
					.pointRecord(pointAdd)
					.build();
			pointLogs.add(pointLog);
		}
		
		if(discount > 0) {
			PointLog pointLog = PointLog.builder()
					.customerID(memberInfo.getMemberID())
					.ordersInf(ordersInf)
					.datetime(datetime)
					.pointRecord(-discount)
					.build();
			pointLogs.add(pointLog);
		}
		ordersInf.setPointLog(pointLogs);
		//購買商品清單
		ordersInf.setOrderList(ordersList);
		ordersInfDao.save(ordersInf);
		return CheckoutCompleteInfo.builder().orderCustomer(customer).ordersInf(ordersInf).ordersList(ordersList).build();
	}
}

package dwyu.training.jpa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dwyu.training.jpa.dao.GoodsInfoDao;
import dwyu.training.jpa.entity.Goods;
import dwyu.training.jpa.entity.Orders;
import dwyu.training.jpa.vo.GoodsOrderVo;
import dwyu.training.jpa.vo.OrderVo;


@Service
public class GoodsOrderService {

	@Autowired
	private GoodsInfoDao goodsInfoDao; 
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Goods createGoodsOrder(GoodsOrderVo goodsOrderVo) {
		Goods goods = Goods.builder()
				.goodsName(goodsOrderVo.getGoodsName())
				.goodsDescription(goodsOrderVo.getDescription())
				.goodsPrice(goodsOrderVo.getPrice())
				.goodsQuantity(goodsOrderVo.getQuantity())
				.goodsImageName(goodsOrderVo.getImageName())
				.goodsStatus(goodsOrderVo.getStatus())
				.build();
		
		List<Orders> orders = new ArrayList<>();
		for(OrderVo orderVo : goodsOrderVo.getOrderVos()){
			Orders order = Orders.builder()
					.orderDate(orderVo.getOrderDate())
					.customerID(orderVo.getCustomerID())
					.buyQuantity(orderVo.getBuyQuantity())
					.goodsBuyPrice(orderVo.getGoodsBuyPrice())
					.goods(goods)
					.build();
			orders.add(order);
		}
		goods.setOrders(orders);
		
		// 透過儲存「一」的那方Geography CascadeType.ALL屬性，就可以連同「多」的那方StoreInfo一併儲存!
		goods = goodsInfoDao.save(goods);
		
		return goods;
	}
	
	
	@Transactional
	public Goods updateGoodsOrder(GoodsOrderVo goodsOrderVo) {
		Goods goods = null;
		Optional<Goods> optGoods = goodsInfoDao.findById(goodsOrderVo.getGoodsID());
		if(optGoods.isPresent()){
			goods = optGoods.get();
			List<Orders> newOrdersInfos = new ArrayList<>();
			List<Orders> dbOrdersInfos = goods.getOrders();
			for(OrderVo orderVo : goodsOrderVo.getOrderVos()){
				Orders orders = Orders.builder().orderID(orderVo.getOrderID()).build();
//				storeInfo = (dbStoreInfos.contains(storeInfo)) ? dbStoreInfos.get(dbStoreInfos.indexOf(storeInfo)) : storeInfo;
				if(dbOrdersInfos.contains(orders)) {
					orders = dbOrdersInfos.get(dbOrdersInfos.indexOf(orders));
				}				
				orders.setOrderDate((orderVo.getOrderDate()==null) ? orders.getOrderDate() : orderVo.getOrderDate());
				orders.setCustomerID((orderVo.getCustomerID()==null) ?orders.getCustomerID() : orderVo.getCustomerID());
				orders.setGoodsBuyPrice((orderVo.getGoodsBuyPrice()==0) ?orders.getGoodsBuyPrice() : orderVo.getGoodsBuyPrice());
				orders.setBuyQuantity((orderVo.getBuyQuantity()==0) ?orders.getBuyQuantity() : orderVo.getBuyQuantity());
				orders.setGoods(goods);
				newOrdersInfos.add(orders);
			}
			dbOrdersInfos.clear();
			dbOrdersInfos.addAll(newOrdersInfos);
		}
		goods.setGoodsDescription((goodsOrderVo.getDescription()==null) ? goods.getGoodsDescription() : goodsOrderVo.getDescription());
		goods.setGoodsImageName((goodsOrderVo.getGoodsName()==null) ? goods.getGoodsImageName() : goodsOrderVo.getGoodsName());
		goods.setGoodsName((goodsOrderVo.getGoodsName()==null) ? goods.getGoodsName() : goodsOrderVo.getGoodsName());
		goods.setGoodsPrice((goodsOrderVo.getPrice()==0) ? goods.getGoodsPrice() : goodsOrderVo.getPrice());
		goods.setGoodsQuantity((goodsOrderVo.getQuantity()==0) ? goods.getGoodsQuantity() : goodsOrderVo.getQuantity());
		goods.setGoodsStatus((goodsOrderVo.getStatus()==null) ? goods.getGoodsStatus() : goodsOrderVo.getStatus());
		return goods;
	}
	
	
}

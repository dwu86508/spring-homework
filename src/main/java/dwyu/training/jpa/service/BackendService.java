package dwyu.training.jpa.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dwyu.training.jpa.dao.GoodsInfoDao;
import dwyu.training.jpa.dao.GoodsListDao;
import dwyu.training.jpa.dao.OrderInfoDao;
import dwyu.training.jpa.entity.GenericPageable;
import dwyu.training.jpa.entity.Goods;
import dwyu.training.jpa.entity.GoodsDataCondition;
import dwyu.training.jpa.entity.GoodsDataInfo;
import dwyu.training.jpa.entity.GoodsReportSalesInfo;
import dwyu.training.jpa.entity.GoodsSalesReportCondition;
import dwyu.training.jpa.entity.SalesReportInf;
import dwyu.training.jpa.vo.GoodsVo;


@Service
public class BackendService {
	
	private static Logger logger = LoggerFactory.getLogger(BackendService.class);
	
	@Autowired
	private GoodsInfoDao goodsInfoDao; 
	
	@Autowired
	private GoodsListDao goodsListDao;
	
	@Autowired
	private OrderInfoDao orderInfoDao; 
		
	public List<Goods> queryAllGoods(){
		
		return goodsInfoDao.findAll();
	}
	
	public Goods queryGoodsByID(Long goodsID){
		
		return goodsInfoDao.findById(goodsID).get();
	}
	
	public GenericPageable calculatePage(GenericPageable genericPageable) {
		
		//頁碼
		int pageNo = genericPageable.getCurrentPageNo();
		//商品總數量
		int itemsAmount = genericPageable.getDatTotalSize();
		//每頁商品數量
		int pageDataSize = genericPageable.getPageDataSize();
		//頁碼按鈕數量
		int pageIconSize = genericPageable.getPagesIconSize();
		
		int pagePageDataAmount = pageDataSize*pageIconSize;
		//最後一頁
		int lastPage = itemsAmount%pageDataSize==0 ? itemsAmount/pageDataSize : (itemsAmount/pageDataSize)+1;
		lastPage = itemsAmount<=pageDataSize ? 0 : lastPage;
		//計算頁中頁
		int pagePage = pageNo/pageIconSize==0 ? 1 : (int)Math.ceil((double)pageNo/pageIconSize);
		List<Integer> pageination= new ArrayList<>();
		for(int i = pagePage*pageIconSize-(pageIconSize-1); i <=pagePage*pageIconSize ; i++) {
			if(i<=lastPage) {
				pageination.add(i);
			}
		}
		
		genericPageable.setEndPageNo(lastPage);
		genericPageable.setPageination(pageination);
		
		return genericPageable;
	}
	
	public GoodsDataInfo queryGoodsData(GoodsDataCondition condition,GenericPageable genericPageable){
		
		int pageNo = genericPageable.getCurrentPageNo() == 0 ? 1 : genericPageable.getCurrentPageNo();
		genericPageable.setCurrentPageNo(pageNo);
		GoodsDataInfo goodsDataInfo = goodsListDao.queryGoodsSearch(condition , genericPageable);
		//頁碼計算
		GenericPageable newGenericPageable = calculatePage(goodsDataInfo.getGenericPageable());
		goodsDataInfo.setGenericPageable(newGenericPageable);
		return goodsDataInfo;
	}
	
	public Goods createGoods(GoodsVo goodsVo) throws IOException{	

		MultipartFile file = goodsVo.getFile();
		String fileName = file.getOriginalFilename();		
		Files.copy(file.getInputStream(), Paths.get("/home/VendingMachine/DrinksImage").resolve(fileName));
		goodsVo.setFile(null);
		Goods goods = Goods.builder()
			.goodsName(goodsVo.getGoodsName())
			.goodsDescription(goodsVo.getDescription())
			.goodsPrice(goodsVo.getPrice())
			.goodsQuantity(goodsVo.getQuantity())
			.goodsImageName(fileName)
			.goodsStatus(goodsVo.getStatus()).build();
		return goodsInfoDao.save(goods);
	}
	
	@Transactional
	public Goods updateGoods(GoodsVo goodsVo) throws IOException{
		Optional<Goods> optGoodsInfo = goodsInfoDao.findById(goodsVo.getGoodsID());
		MultipartFile file = goodsVo.getFile();
		String fileName = optGoodsInfo.get().getGoodsImageName();
		if(file!=null) {
			Files.delete(Paths.get("/home/VendingMachine/DrinksImage").resolve(fileName));
			fileName = file.getOriginalFilename();
			Files.copy(file.getInputStream(), Paths.get("/home/VendingMachine/DrinksImage").resolve(fileName));
			goodsVo.setFile(null);
		}
		Goods goodsInfo = null;
		if(optGoodsInfo.isPresent()) {
			goodsInfo = optGoodsInfo.get();
			goodsInfo.setGoodsName(goodsVo.getGoodsName());
			goodsInfo.setGoodsDescription(goodsVo.getDescription());
			goodsInfo.setGoodsPrice(goodsVo.getPrice());
			goodsInfo.setGoodsQuantity(goodsVo.getQuantity());
			goodsInfo.setGoodsImageName(fileName);
			goodsInfo.setGoodsStatus(goodsVo.getStatus());
		}
		return goodsInfo;
	}
	
	public GoodsReportSalesInfo queryGoodsSales(GoodsSalesReportCondition condition,GenericPageable genericPageable){
		//避免頁碼為0出錯
		int pageNo = genericPageable.getCurrentPageNo() == 0 ? 1 : genericPageable.getCurrentPageNo();
		genericPageable.setCurrentPageNo(pageNo);
		//取得資料庫符合訂單資料
		GoodsReportSalesInfo goodsReportSalesInfo = orderInfoDao.queryOrderList(condition,genericPageable);
		//頁碼計算
		GenericPageable newGenericPageable = calculatePage(goodsReportSalesInfo.getGenericPageable());
		goodsReportSalesInfo.setGenericPageable(newGenericPageable);
		return goodsReportSalesInfo;
	}
	
	public SalesReportInf queryGoodsSalesNew(GoodsSalesReportCondition condition,GenericPageable genericPageable){
		//避免頁碼為0出錯
		int pageNo = genericPageable.getCurrentPageNo() == 0 ? 1 : genericPageable.getCurrentPageNo();
		genericPageable.setCurrentPageNo(pageNo);
		//取得資料庫符合訂單資料
		SalesReportInf goodsReportSalesInfo = orderInfoDao.queryOrderListNew(condition,genericPageable);
		//頁碼計算
		GenericPageable newGenericPageable = calculatePage(goodsReportSalesInfo.getGenericPageable());
		goodsReportSalesInfo.setGenericPageable(newGenericPageable);
		return goodsReportSalesInfo;
	}
	
}

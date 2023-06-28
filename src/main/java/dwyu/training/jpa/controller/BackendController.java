package dwyu.training.jpa.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import dwyu.training.jpa.entity.GenericPageable;
import dwyu.training.jpa.entity.Goods;
import dwyu.training.jpa.entity.GoodsDataCondition;
import dwyu.training.jpa.entity.GoodsDataInfo;
import dwyu.training.jpa.entity.GoodsReportSalesInfo;
import dwyu.training.jpa.entity.GoodsSalesReportCondition;
import dwyu.training.jpa.entity.SalesReportInf;
import dwyu.training.jpa.service.BackendService;
import dwyu.training.jpa.vo.GoodsVo;


@CrossOrigin
@RestController
@RequestMapping("/ecommerce/BackendController")
public class BackendController {
		
	@Autowired
	private BackendService backendService;
	
	@ApiOperation(value = "購物網-後臺-查詢商品列表")
	@GetMapping(value = "/queryGoodsData")
	public ResponseEntity<GoodsDataInfo> queryGoodsData(@RequestParam(required = false) Integer goodsID, 
			 @RequestParam(required = false) String goodsName, @RequestParam(required = false) String priceSort,
			 @RequestParam(required = false) Integer startPrice, @RequestParam(required = false) Integer endPrice, 
			 @RequestParam(required = false) Integer quantity, @RequestParam String status,
			 @RequestParam int currentPageNo, @RequestParam int pageDataSize, @RequestParam int pagesIconSize) {

		GoodsDataCondition condition = GoodsDataCondition.builder().goodsID(goodsID).goodsName(goodsName)
				.startPrice(startPrice).endPrice(endPrice).priceSort(priceSort).quantity(quantity).status(status).build();		
		GenericPageable genericPageable = GenericPageable.builder().currentPageNo(currentPageNo)
				.pageDataSize(pageDataSize).pagesIconSize(pagesIconSize).build();
				
		GoodsDataInfo goodsDataInfo = backendService.queryGoodsData(condition, genericPageable);		
		
		return ResponseEntity.ok(goodsDataInfo);
	}

	@ApiOperation(value = "購物網-後臺-商品訂單查詢(一個商品對應到多筆訂單)")
	@GetMapping(value = "/queryGoodsSales")
	public ResponseEntity<GoodsReportSalesInfo> queryGoodsSales(
			 @RequestParam String startDate, @RequestParam String endDate,  
			 @RequestParam int currentPageNo, @RequestParam int pageDataSize, @RequestParam int pagesIconSize) {
		/*
		 startDate:2022/09/19
		 endDate:2022/09/19
		 currentPageNo:1
		 pageDataSize: 3
		 pagesIconSize: 3
		 */	
		GoodsSalesReportCondition condition = GoodsSalesReportCondition.builder().startDate(startDate).endDate(endDate).build();
		
		GenericPageable genericPageable = GenericPageable.builder().currentPageNo(currentPageNo)
				.pageDataSize(pageDataSize).pagesIconSize(pagesIconSize).build();
		
		GoodsReportSalesInfo goodsReportSalesInfo = backendService.queryGoodsSales(condition, genericPageable);
		
		return ResponseEntity.ok(goodsReportSalesInfo); 
	}
	
	@ApiOperation(value = "購物網-後臺-商品訂單查詢(一個商品對應到多筆訂單)")
	@GetMapping(value = "/queryGoodsSalesNew")
	public ResponseEntity<SalesReportInf> queryGoodsSalesNew(
			 @RequestParam String startDate, @RequestParam String endDate,  
			 @RequestParam int currentPageNo, @RequestParam int pageDataSize, @RequestParam int pagesIconSize) {
		/*
		 startDate:2022/09/19
		 endDate:2022/09/19
		 currentPageNo:1
		 pageDataSize: 3
		 pagesIconSize: 3
		 */	
		GoodsSalesReportCondition condition = GoodsSalesReportCondition.builder().startDate(startDate).endDate(endDate).build();
		
		GenericPageable genericPageable = GenericPageable.builder().currentPageNo(currentPageNo)
				.pageDataSize(pageDataSize).pagesIconSize(pagesIconSize).build();
		
		SalesReportInf goodsReportSalesInfo = backendService.queryGoodsSalesNew(condition, genericPageable);
		
		return ResponseEntity.ok(goodsReportSalesInfo); 
	}
	
	@ApiOperation(value = "購物網-後臺-商品新增作業")
	@PostMapping(value = "/createGoods", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Goods> createGoods(@ModelAttribute GoodsVo goodsVo) throws IOException {
		
		Goods goods = backendService.createGoods(goodsVo);
		
		return ResponseEntity.ok(goods);
	}
	
	@ApiOperation(value = "購物網-後臺-商品維護作業-查詢全部商品清單")
	@GetMapping(value = "/queryAllGoods")
	public ResponseEntity<List<Goods>> queryAllGoods() {
		
		List<Goods> goodsDatas = backendService.queryAllGoods();
		
		return ResponseEntity.ok(goodsDatas);
	}
	
	@ApiOperation(value = "購物網-後臺-商品維護作業-查詢單一商品資料")
	@GetMapping(value = "/queryGoodsByID")
	public ResponseEntity<Goods> queryGoodsByID(@RequestParam long goodsID){
		
		Goods goods = backendService.queryGoodsByID(goodsID);
		
		return ResponseEntity.ok(goods);
	}
	
	@ApiOperation(value = "購物網-後臺-商品維護作業-更新商品資料")
	@PostMapping(value = "/updateGoods", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Goods> updateGoods(@ModelAttribute GoodsVo goodsVo) throws IOException {
		
		Goods goods = backendService.updateGoods(goodsVo);
		
		return ResponseEntity.ok(goods);
	}
	
}

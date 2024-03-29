package dwyu.training.jpa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dwyu.training.jpa.entity.GenericPageable;
import dwyu.training.jpa.entity.GoodsDataInfo;
import dwyu.training.jpa.service.FrontendService;
import dwyu.training.jpa.vo.CheckoutCompleteInfo;
import dwyu.training.jpa.vo.GoodsVo;
import dwyu.training.jpa.vo.MemberInfoVo;
import dwyu.training.jpa.vo.OrderCustomer;
import dwyu.training.jpa.vo.ShoppingCarGoods;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:8086"}, allowCredentials = "true")
@RestController
@RequestMapping("/FrontendController")
public class FrontendController {
	
	private static Logger logger = LoggerFactory.getLogger(FrontendController.class);
	
	@Autowired
	private HttpSession httpSession;

	@Resource
	private MemberInfoVo sessionMemberInfo;

	@Resource(name = "sessionCartGoods")
	private List<GoodsVo> cartGoods;
	
	@Resource(name = "sessionCartGoodsNew")
	private Map<Long,ShoppingCarGoods> cartGoodsNew;
	
	@Autowired
	private FrontendService frontendService;
	
	@ApiOperation(value = "購物網-前臺-查詢商品列表")
	@GetMapping(value = "/queryGoodsData")
	public ResponseEntity<GoodsDataInfo> queryGoodsData(@RequestParam(required = false) String searchKeyword,
			 @RequestParam int currentPageNo, @RequestParam int pageDataSize, @RequestParam int pagesIconSize) {
	
		GenericPageable genericPageable = GenericPageable.builder().currentPageNo(currentPageNo)
				.pageDataSize(pageDataSize).pagesIconSize(pagesIconSize).build();
		 
		GoodsDataInfo goodsDataInfo = frontendService.queryGoodsData(searchKeyword, genericPageable);
	
		
		return ResponseEntity.ok(goodsDataInfo);
	}
	
	@ApiOperation(value = "購物網-前臺-結帳購物車商品")
	@PostMapping(value = "/checkoutGoods")
	public ResponseEntity<CheckoutCompleteInfo> checkoutGoods(@RequestBody OrderCustomer customer) {
		
		logger.info("HttpSession checkoutGoods:" + httpSession.getId());
		logger.info("CheckoutGoods:" + sessionMemberInfo.toString());
		
		List<ShoppingCarGoods> shoppingCar= new ArrayList<>(cartGoodsNew.values());
		CheckoutCompleteInfo checkoutCompleteInfo = frontendService.checkoutGoods(sessionMemberInfo, customer, shoppingCar);
		cartGoods.clear();
		return ResponseEntity.ok(checkoutCompleteInfo);
	}

}

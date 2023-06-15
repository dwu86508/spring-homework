package dwyu.training.jpa.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dwyu.training.jpa.entity.Goods;
import dwyu.training.jpa.entity.Orders;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;



@Data

public class GoodsVo {
	
	private long goodsID;
	
	private String goodsName;
	
	private String description;
	
	private int price;
	
	private int quantity;
	
	private MultipartFile file;
	
	private String imageName;
	
	private String status;
	
}

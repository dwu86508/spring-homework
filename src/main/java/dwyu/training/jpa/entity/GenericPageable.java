package dwyu.training.jpa.entity;

import java.util.List;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class GenericPageable {
	
	private int currentPageNo;
	
	private int pageDataSize;
	
	private int datTotalSize;
	
	private int pagesIconSize;
	
	private List<Integer> pageination;
	
	private int endPageNo;
	
}

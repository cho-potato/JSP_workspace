package gallery.domain;

import lombok.Data;

// lombok 이용, getter/setter 쉽고 편하게 
// 주석은 주석인데 프로그램에 사용하는 주석 : 어노테이션
@Data
public class Gallery {
	private int gallery_idx;
	private String title;
	private String writer;
	private String content;
	private String regdate;
	private int hit;
	private String filename;
	
}

<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.IOException"%>
<%@page import="gallery.domain.Gallery"%>
<%@page import="gallery.util.FileManager"%>
<%@page import="java.io.File"%>
<%@page import="gallery.repository.GalleryDAO"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%! GalleryDAO galleryDAO = new GalleryDAO();%>
<%
	// 파라미터를 받아 오라클에 넣기
	request.setCharacterEncoding("utf-8");
	
	// 클라이언트가 파일을 포함하여 인코딩 된 형식으로 요청을 시도할 때는 
	// 기존 텍스트 파라미터를 받을 때 사용한 getParameter 메서드로는 바이너리 파일을 포함한 기타 파라미터도 받을 수 없다
	// 해결 : 업로드 라이브러리를 이용해야 한다
	
	// 우리가 서블릿에서 사용했던 ServletContext 인터페이스는 JSP에서 내장객체로 지원하고 있다
	// 따라서 서블릿을 할 줄 모르는 개발자는 내장객체를 이용할 수 있다
	String savePath = application.getRealPath("/data/");
	
	
	// String savePath = "C:/jsp_workspace/GalleryProject/src/main/webapp/data/"; 
	int maxSize = 1024 * 1024 * 5; // 5MB 제한 
	MultipartRequest multi = null;
	
		try {
			multi = new MultipartRequest(request, savePath, maxSize, "UTF-8");
			// 이미 생성자에서 업로드가 완료되었기 때문에 생성된 파일을 대상으로 파일명을 바꾸자
		
			// 업로드 된 파일의 레퍼런스 얻기
			File file = multi.getFile("file"); // html에서의 컴포넌트 이름
			long time = System.currentTimeMillis(); // 파일명에 사용할 숫자
			String ext = FileManager.getExt(file.getName()); // 파일명
		
			// 기존 파일 to 새 파일
			// file.renameTo(new File("바꿀 파일 명"));
			file.renameTo(new File(savePath + time + "." + ext));
		
			String title = multi.getParameter("title");
			// String title = request.getParameter("title");
			String writer = multi.getParameter("writer");
			// String writer = request.getParameter("writer");
			String content = multi.getParameter("content");
			// String content = request.getParameter("content");
		
			// String file = request.getParameter("file"); // file은 바이너리이기 때문에 String이 될 수 없음
		
			// DTO에 담기
			Gallery gallery = new Gallery();
			gallery.setTitle(title);
			gallery.setWriter(writer);
			gallery.setContent(content);
			gallery.setFilename(time + "." + ext); // 파일명 채우기
			
			// DAO insert 호출
			int result = galleryDAO.insert(gallery);
			
			out.print("<script>");
			if(result > 0) {
				out.print("alert('업로드 성공');");
				out.print("location.href='/gallery/list.jsp';");
			}
				out.print("</script>");
			
			
			// out.print(title + "<br>");
			// out.print(writer + "<br>");
			// out.print(content + "<br>");
		} catch (IOException e) {
			
			out.print("<script>");
			out.print("alert('파일의 크기는 5MB 이하로 제한되어 있습니다');");
			out.print("history.back();");
			out.print("</script>");
		}
%>
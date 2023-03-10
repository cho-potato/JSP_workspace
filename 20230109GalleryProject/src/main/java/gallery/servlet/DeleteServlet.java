package gallery.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gallery.repository.GalleryDAO;

public class DeleteServlet extends HttpServlet {
	GalleryDAO galleryDAO = new GalleryDAO();
	ServletContext context; // 어플리케이션의 전반적인 컨텍스트(행위 달성을 위한 부가 정보)

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 요청 파라미터 받기
		// delete gallery where gallery_idx=5 형식이니까 인코딩 필요 없음
		response.setContentType("text/html;charset=UTF-8"); // 이 문서의 속성을 정하는 것. 인코딩과는 별개

		String gallery_idx = request.getParameter("gallery_idx");
		String filename = request.getParameter("filename");

		System.out.println("gallery_idx is " + gallery_idx);

		// 로컬 하드의 파일을 제어하기 위해서는 자바의 File 클래스를 이용
		// 디렉토리 경로가 하드 내에서만 적용가능한 경로이기 때문에 자바가 원하는 방향으로 가보자
		context = request.getServletContext(); // 웹 어플리케이션의 루트만 주어져도 루트를 기준으로 하드 디스크의 풀 경로를 조사할 수 있음

		String app_path = context.getRealPath("/data");
		System.out.println("현재 어플리케이션의 data 디렉토리의 실제 위치는 " + app_path);
		
//		File file = new File("C:/jsp_workspace/GalleryProject/src/main/webapp/data");
		File file = new File(app_path + "/" + filename);
		System.out.println(app_path + "/" + filename);

		PrintWriter out = response.getWriter();

		if (file.delete()) { // 파일이 삭제 되었다면
			int result = galleryDAO.delete(Integer.parseInt(gallery_idx));

			out.print("<script>");
			if (result > 0) {
				out.print("alert('삭제완료');");
				out.print("location.href='/gallery/list.jsp';");
			} else {
				out.print("alert('삭제실패');");
				out.print("history.back();");
			}
			out.print("</script>");
		} 

		// DB삭제가 트랜잭션이지만
		// DB삭제 + 파일 삭제까지 이루어져야 진정한 삭제 ,,,
		galleryDAO.delete(Integer.parseInt(gallery_idx));
	}
}

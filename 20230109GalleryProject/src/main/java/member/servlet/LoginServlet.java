package member.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.domain.Member;
import member.repository.MemberDAO;

public class LoginServlet extends HttpServlet{
	MemberDAO memberDAO = new MemberDAO();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		// 클라이언트가 최초로 접속을 하게 될 경우, 서버에서는 세션이 생성되고 
		// 생성된 세션에는 고유 값인 아이디가 발급된다
		// JSP 내장개게 중 세션을 표현한 객체가 바로 session 내장객체이다
		// session의 JavaEE API 자료형은 HttpSession이다
		
		 // 개발자가 생성하는 것이 아니라 시스템이 관리
		HttpSession session=request.getSession();
		String sid = session.getId();
		System.out.println("이 요청에 의해 생성된 세션 아이디 " + sid);
		
		// 영문으로만 전송되므로 인코딩 처리는 안함
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String name = request.getParameter("name");
		
		Member member = new Member();
		member.setId(id);
		member.setPass(pass);
		
		Member result = memberDAO.select(member);
		
		PrintWriter out = response.getWriter();
		
		out.print("<script>");
		if(result == null) { // 인증 실패
			out.print("alert('로그인 정보가 올바르지 않습니다');");
			out.print("history.back();");
		} else { // 인증 성공
			// 세션 객체에 DTO 심기
			session.setAttribute("member", result);
			out.print("alert('로그인 성공');");
			out.print("location.href='/gallery/list.jsp';");
		}
		out.print("</script>");
	}

}

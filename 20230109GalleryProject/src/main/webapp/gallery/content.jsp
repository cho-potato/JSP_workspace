<%@page import="gallery.domain.Gallery"%>
<%@page import="gallery.repository.GalleryDAO"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%!GalleryDAO galleryDAO = new GalleryDAO();%>
<%
	request.setCharacterEncoding("UTF-8");
	
	// String gallery_idx = request.getParameter("gallery_idx");
	 int gallery_idx = Integer.parseInt(request.getParameter("gallery_idx"));
	out.print(gallery_idx);
	
	// String sql = "select * from gallery where gallery_idx=?" + gallery_idx;
	// out.print(sql);
	// select * from gallery where gallery_idx;
	
	Gallery gallery = galleryDAO.select(gallery_idx);

	// Gallery gallery = galleryDAO.select(Integer.parseInt(gallery_idx));
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
<script>
	function edit() {
		if (confirm("수정?")) {
			$("form").attr("method", "post");
			let v = $("input[type='file']").val();
			if (true) { // 이미지를 업로드 하기 원하는 경우
				$("form").attr("action", "/gallery/edit");
				$("form").attr("enctype", "multipart/form-data");
			} else { // 텍스트만 수정하기를 원하는 경우
				$("form").attr("action", "/gallery/update");
			}
			$("form").submit();
		}
	}
	function del() {
		if (confirm("삭제?")) {
			// 삭제 요청을 받는 서버측 기술은 디자인이 요구되지 않으므로 서블릿으로 처리해도 무방
			// enctype = encoding
			// 폼태크 전송시 개발자가 아무 것도 명시하지 않으면 텍스트 전송이 기본값
			// 텍스트 전송에 사용되는 인코딩 타입은 application/x-www-form-urlencode 이고,
			// 이 방식은 바이너리 파일 전송이 불가능하다
			// 따라서 개발자가 파일도 함께 전송하려면 form태그의 전송 인코딩 방식을 multipart/form-data로 작성해야 한다
			$("form").attr({
				"action" : "/gallery/del",
				"method" : "post",
			});
			$("form").submit();
		}
	}
	$(function() {
		$($("input[type='button']")[0]).click(function() {
			edit();
		});
		$($("input[type='button']")[1]).click(function() {
			del();
		});
		$($("input[type='button']")[2]).click(function() {
			location.href="/gallery/list.jsp";
		});
	});
</script>
</head>
<body bgcolor="yellow">
	<form>
		<table>
			<tr>
				<td><input type="hidden" name="gallery_idx"
					value="<%=gallery.getGallery_idx()%>"></td>
				<td><input type="hidden" name="filename"
					value="<%=gallery.getFilename()%>"></td>
			</tr>
			<tr>
				<td><input type="text" name="title"
					value="<%=gallery.getTitle()%>"></td>
			</tr>
			<tr>
				<td><input type="text" name="writer"
					value="<%=gallery.getWriter()%>"></td>
			</tr>
			<tr>
				<td><textarea name="content" style="height: 200px"><%=gallery.getContent()%></textarea></td>
			</tr>
			<tr>
				<td><img src="/data/<%=gallery.getFilename()%>" width="500px">
				</td>
			</tr>
			<tr>
				<td><input type="file" name="file"></td>
			</tr>
			<tr>
				<td>
				<input type="button" value="수정"> 
						<input type="button" value="삭제">
						<input type="button" value="목록">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
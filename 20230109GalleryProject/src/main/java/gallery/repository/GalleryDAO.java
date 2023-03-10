package gallery.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gallery.domain.Gallery;
import gallery.pool.PoolManager;

// Gallery 테이블에 대한 CRUD만을 수행 
public class GalleryDAO {
	PoolManager pool = PoolManager.getInstance();

	// insert
	public int insert(Gallery gallery) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		conn = pool.getConnection(); // 접속이 아니라 대여 (접속은 되어있는 상태임, 속도 향상을 위해)

		String sql = "insert into gallery(gallery_idx, title, writer, content, filename)";
		sql += " values(seq_gallery.nextval, ?, ?, ?, ?)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, gallery.getTitle());
			pstmt.setString(2, gallery.getWriter());
			pstmt.setString(3, gallery.getContent());
			pstmt.setString(4, gallery.getFilename());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.release(conn, pstmt);
		}
		return result;
	}

	// 모든 레코드 가져오기
	public List selectAll() {
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		conn = pool.getConnection();
		String sql = "select * from gallery order by gallery_idx desc";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Gallery gallery = new Gallery();
				gallery.setGallery_idx(rs.getInt("gallery_idx"));
				gallery.setTitle(rs.getString("title"));
				gallery.setWriter(rs.getString("writer"));
				gallery.setContent(rs.getString("content"));
				gallery.setRegdate(rs.getString("regdate"));
				gallery.setFilename(rs.getString("filename"));
				gallery.setHit(rs.getInt("hit"));
				
				list.add(gallery);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 상세보기
	public Gallery select(int gallery_idx) {
		Gallery gallery = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = pool.getConnection(); // 접속객체 대여
			String sql = "select * from gallery where gallery_idx=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, gallery_idx);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				gallery = new Gallery();

				gallery.setGallery_idx(rs.getInt("gallery_idx"));
				gallery.setTitle(rs.getString("title"));
				gallery.setWriter(rs.getString("writer"));
				gallery.setContent(rs.getString("content"));
				gallery.setRegdate(rs.getString("regdate"));
				gallery.setHit(rs.getInt("hit"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.release(conn, pstmt, rs);
			return gallery;
		}
	}

	// 레코드 한 건 삭제하기
	public int delete(int gallery_idx) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		conn = pool.getConnection(); // 대여

		String sql = "delete from gallery where gallery_idx=?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, gallery_idx);

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.release(conn, pstmt);
		}
		return result;
	}

	public int update(Gallery gallery) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		conn = pool.getConnection();

		String sql = "update gallery set title=? writer=? content=?";
		sql += ", filename=? where gallery_idx=?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, gallery.getTitle());
			pstmt.setString(2, gallery.getWriter());
			pstmt.setString(3, gallery.getContent());
			pstmt.setString(4, gallery.getFilename());
			pstmt.setInt(5, gallery.getGallery_idx());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			pool.release(conn, pstmt);
		}
		return result;
	}
}

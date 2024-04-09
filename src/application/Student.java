package application;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import lombok.Setter;
import util.database.DbConnect;

@Setter
public class Student {
	private String stdId = null;
	private String stdName = null;
	private int attendanceTime = 0;
	private static DbConnect dc = new DbConnect();

//	public Student(String stdId, String stdName, int attendanceTime) {
//		this.stdId = stdId;
//		this.stdName = stdName;
//		this.attendanceTime = attendanceTime;
//	}

	// 로그인
	public void login(String id, String pw) {
		String sql = "SELECT * FROM student WHERE std_id = ? AND std_pw = ?";
		PreparedStatement pstmt;
		try {
			pstmt = dc.conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			dc.rs = dc.execute(pstmt);
			if (dc.rs.next()) {
				this.stdId = dc.rs.getString("std_id");
				this.stdName = dc.rs.getString("std_Name");
				this.attendanceTime = dc.rs.getInt("attendance_time");
			} else { // 로그인 실패
				System.out.println("아이디나 패스워드를 확인하세요");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 로그아웃
	public void logout() {
		dc.close();
		stdId = null;
		stdName = null;
		attendanceTime = 0;
		dc = null;
	}

	// 입실 기록하기
	public void checkin() {
		// attendance 테이블에 insert
		String sql = "INSERT INTO attendance VALUES(seq_aid.nextval, " + stdId + ", sysdate, null)";
		PreparedStatement pstmt;
		try {
			pstmt = dc.conn.prepareStatement(sql);
			int rows = dc.update(pstmt);
			if (rows == 1) {
				System.out.println(stdName + "님 입실을 완료하였습니다.");
			} else {
				System.out.println("입실 실패.  관리자에게 문의하세요");
			}
			dc.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 퇴실 기록하기
	public void checkout(int atdId, int atdTime) {
		// attendance 테이블에 insert
		String sql1 = "UPDATE attendance SET checkout_time = sysdate WHERE atd_id = ?";
		String sql2 = "UPDATE student SET attendance_time = attendance_time + ?  WHERE STD_ID = ?";
		PreparedStatement pstmt1;
		PreparedStatement pstmt2;
		try {
			Date dt = new Date();
			pstmt1 = dc.conn.prepareStatement(sql1);
			pstmt1.setInt(1, atdId);
			dc.update(pstmt1);

			pstmt2 = dc.conn.prepareStatement(sql2);
			if (dc.update(pstmt1) == 1 && dc.update(pstmt2) == 1) {
				System.out.println(stdName + "님 퇴실을 완료하였습니다.");
			} else {
				System.out.println("퇴실 실패. 관리자에게 문의하세요.");
			}
			dc.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 출석기록하기
	public void attendance() {
		// 오늘 출석 기록이 있는지 확인
		String sql = "SELECT atd_id, ROUND((sysdate - checkin_time)*24*60) AS atd_time  FROM attendance"
				+ " WHERE TO_CHAR(checkin_time, 'YYYYMMDD') = TO_CHAR(sysdate, 'YYYYMMDD') AND std_id = ?";
		PreparedStatement pstmt;
		try {
			pstmt = dc.conn.prepareStatement(sql);
			pstmt.setString(1, stdId);
			dc.rs = dc.execute(pstmt);
			if (dc.rs.next()) { // 있으면 퇴실 기록
				checkout(dc.rs.getInt("atd_id"), dc.rs.getInt("atd_time"));
			} else { // 로그인 실패
				checkin(); // 없으면 입실기록
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 시험 성적 확인
	public void listTest() {
		// 오늘 출석 기록이 있는지 확인
		String sql = "SELECT std_id, test_name, score FROM test WHERE std_id = ?";
		PreparedStatement pstmt;
		try {
			pstmt = dc.conn.prepareStatement(sql);
			pstmt.setString(1, stdId);
			dc.rs = dc.execute(pstmt);
			System.out.println("--------------------------------------------------------");
			System.out.println("--------------------------------------------------------");
			while (dc.rs.next()) {
				System.out.printf(dc.rs.getString("std_id") + "  |  " + dc.rs.getString("test_name") + "    |   "
						+ dc.rs.getInt("score"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

package util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbConnect {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	private String DB_HOST = "localhost";
	private String DB_USER = "shinhan";
	private String DB_PW = "shinhan1234";

	public DbConnect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver"); // 드라이버 로드
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" + DB_HOST + ":1521:xe", DB_USER, DB_PW); // DB 커넥트
			System.out.println("DB 접속 성공");
			stmt = conn.createStatement(); // sql 실행할 객체(statement)
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void execute() {

	}

}

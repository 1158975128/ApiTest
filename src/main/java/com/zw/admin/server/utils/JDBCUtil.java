package com.zw.admin.server.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {

	static Connection conn = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	static String url = "jdbc:mysql://47.103.97.21:3307/fuliye_web?user=root&password=root&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false";
	static String url2 = "jdbc:mysql://47.103.97.21:3307/fuliye_test?user=root&password=root&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false";

	/**
	 * 建表
	 *
	 * @param tabName 表名
	 * @return
	 */
	public static String create(String tabName) {

		// MySQL的JDBC连接语句
		// URL编写格式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
		// String url = "jdbc:mysql://localhost:3306/student?user=root&password=123456";
		String str = "";
		// 数据库执行的语句
		String sql = "create table if not exists " + tabName
				+ "(id bigint(11) NOT NULL AUTO_INCREMENT,`value` text,`create_time` datetime DEFAULT NULL,"
				+ "  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;";

		// 查询语句
		// String cmd = "select * from stuinfo;";
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载驱动
			conn = DriverManager.getConnection(url); // 获取数据库连接
			stmt = conn.createStatement(); // 创建执行环境
			stmt.execute(sql); // 执行SQL语句

		} catch (ClassNotFoundException e) {
			str = "加载驱动异常";
			e.printStackTrace();
		} catch (SQLException e) {
			str = "数据库异常";
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close(); // 关闭结果数据集
				}
				if (stmt != null) {
					stmt.close(); // 关闭执行环境
				}
				if (conn != null) {
					conn.close(); // 关闭数据库连接
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 通用查询
	 *
	 * @param sql 查询语句
	 */
	public static void query(String sql) {
		// MySQL的JDBC连接语句
		// URL编写格式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
		// String url = "jdbc:mysql://localhost:3306/student?user=root&password=123456";
		// String url =
		// "jdbc:mysql://api.fftai.com:3307/fuliye_web?user=root&password=root&useSSL=false&autoReconnect=true";

		// 数据库执行的语句
		// String sql = "insert into stuinfo values('201307020010','zhangsan',21);";//
		// 插入一条记录
		// String sql = "create table stuinfo(id char(12),name char(20),age
		// int);";//创建一个表

		// 查询语句
		// String cmd = "select * from stuinfo;";
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载驱动
			conn = DriverManager.getConnection(url); // 获取数据库连接
			stmt = conn.createStatement(); // 创建执行环境
			// 读取数据
			rs = stmt.executeQuery(sql); // 执行查询语句，返回结果数据集
			rs.last(); // 将光标移到结果数据集的最后一行，用来下面查询共有多少行记录
			System.out.println("共有" + rs.getRow() + "行记录：");
			rs.beforeFirst(); // 将光标移到结果数据集的开头
			while (rs.next()) { // 循环读取结果数据集中的所有记录
				// System.out.println(rs.getRow() + "、 学号:" + rs.getString("id") + "\t姓名:" +
				// rs.getString("name") + "\t年龄:"
				// + rs.getInt("age"));
			}
		} catch (ClassNotFoundException e) {
			System.out.println("加载驱动异常");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("数据库异常");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close(); // 关闭结果数据集
				}
				if (stmt != null) {
					stmt.close(); // 关闭执行环境
				}
				if (conn != null) {
					conn.close(); // 关闭数据库连接
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 其他数据库查询
	 *
	 * @param sql
	 * @return
	 */
	public static List<Map<String, Object>> queryOther(String sql) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// MySQL的JDBC连接语句
		// URL编写格式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
		// String url =
		// "jdbc:mysql://api.fftai.com:3307/fuliye_test?user=root&password=root&useSSL=false";
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载驱动
			conn = DriverManager.getConnection(url2); // 获取数据库连接
			stmt = conn.createStatement(); // 创建执行环境
			// 读取数据
			rs = stmt.executeQuery(sql); // 执行查询语句，返回结果数据集
			rs.last(); // 将光标移到结果数据集的最后一行，用来下面查询共有多少行记录
			// System.out.println("共有" + rs.getRow() + "行记录：");
			rs.beforeFirst(); // 将光标移到结果数据集的开头

			ResultSetMetaData md = rs.getMetaData();

			int columnCount = md.getColumnCount();

			while (rs.next()) {

				Map<String, Object> map = new HashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {

					map.put(md.getColumnName(i), rs.getObject(i));

				}

				list.add(map);

			}
		} catch (ClassNotFoundException e) {
			System.out.println("加载驱动异常");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("数据库异常");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close(); // 关闭结果数据集
				}
				if (stmt != null) {
					stmt.close(); // 关闭执行环境
				}
				if (conn != null) {
					conn.close(); // 关闭数据库连接
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}

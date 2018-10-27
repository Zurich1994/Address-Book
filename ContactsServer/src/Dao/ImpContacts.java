package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImpContacts {
	public List<Contact> getAllContacts(String user) {
		List<Contact> contacts = new ArrayList<Contact>();
		String sql = "select * from u" + user;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		conn = DbUtils.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt("del") == 0) {
					Contact temp = new Contact();
					temp.setName(rs.getString("name"));
					temp.setDate(rs.getDate("date"));
					temp.setCompany(rs.getString("company"));
					temp.setLocation(rs.getString("location"));
					temp.setTel_1(rs.getString("tel_1"));
					temp.setTel_2(rs.getString("tel_2"));
					temp.setRemark(rs.getString("remark"));
					contacts.add(temp);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contacts;
	}
	public List<Contact> getAllServerContacts(String user)
	{
		List<Contact> contacts = new ArrayList<Contact>();
		String sql = "select * from u" + user;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		conn = DbUtils.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
					Contact temp = new Contact();
					temp.setName(rs.getString("name"));
					temp.setDate(rs.getDate("date"));
					temp.setCompany(rs.getString("company"));
					temp.setLocation(rs.getString("location"));
					temp.setTel_1(rs.getString("tel_1"));
					temp.setTel_1(rs.getString("tel_2"));
					temp.setRemark(rs.getString("remark"));
					contacts.add(temp);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contacts;
	}
	public int[] unifyContacts(List<Contact> unifiedContacts, String user) {
		int[] result = null;
		boolean delResult = false;
		String sql_del = "delete from u" + user;// 清空服务器联系人表中的原有数据 更新为同步后数据
		String columns = "(name,tel_1,tel_2,location,date,company,remark,del,picture)";
		String sql_unify = "insert into u" + user + columns
				+ " values(?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		conn = DbUtils.getConnection();
		try {
			ps = conn.prepareStatement(sql_del);
			delResult = ps.execute();// 原来数据的清除结果
				ps = null;
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(sql_unify);				
				for (Contact c : unifiedContacts) {
					ps.setString(1, c.getName());
					ps.setString(2, c.getTel_1());
					ps.setString(3, c.getTel_2());
					ps.setString(4, c.getLocation());
					ps.setDate(5, c.getDate());
					ps.setString(6, c.getCompany());
					ps.setString(7, c.getRemark());
					ps.setInt(8, c.getDel());
					ps.setString(9, c.getPicture());
					ps.addBatch();
				}
				result = ps.executeBatch();
				conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return result;
		}
	}
}

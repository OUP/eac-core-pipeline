package com.oup.eac.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.oup.eac.util.ConnectionUtil;

public class BatchScheduleDao {
	
	private static final Logger LOG = Logger.getLogger(BatchScheduleDao.class);
	
	public Date getLastTriggerDate(int BatchId){
		Date date = null;
		Connection con = ConnectionUtil.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("SELECT LAST_SUCCESS_DATE FROM BATCH_JOB_TIME WHERE BATCH_JOB_ID = ?");
			ps.setInt(1, BatchId);
			
			ResultSet rs = ps.executeQuery();
			LOG.info("getLastTriggerDate query executed");
			while(rs.next()){
				date = rs.getTimestamp(1);
				break;
			}
		} catch (SQLException e) {
			LOG.error("getLastTriggerDate : ",e);
			e.printStackTrace();
		}
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in Batch schedule dao finally : ",e);
			}
		}
		return date;
		
	}
	
	public boolean updateTriggerDate(int BatchId){
		Connection con = ConnectionUtil.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE BATCH_JOB_TIME SET LAST_JOB_EXECUTE_DATE = CURRENT_TIMESTAMP WHERE BATCH_JOB_ID = ?");
			ps.setInt(1, BatchId);
			if(ps.executeUpdate() > 0){
				con.commit();
				return true;
			}
		} catch (SQLException e) {
			LOG.error("Error in Batch schedule dao finally : ",e);
		}
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in Batch schedule dao finally : ",e);
			}
		}
		return false;
	}
	
	public boolean updateSuccessDate(int BatchId){
		Connection con = ConnectionUtil.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE BATCH_JOB_TIME SET LAST_SUCCESS_DATE = CURRENT_TIMESTAMP WHERE BATCH_JOB_ID = ?");
			ps.setInt(1, BatchId);
			if(ps.executeUpdate() > 0){
				con.commit();
				return true;
			}
		} catch (SQLException e) {
			LOG.error("Error in Batch schedule dao finally : ",e);
		}
		finally{
			try {
				
				con.close();
			} catch (SQLException e) {
				LOG.error("Error in Batch schedule dao finally : ",e);
			}
		}
		return false;
	}
}

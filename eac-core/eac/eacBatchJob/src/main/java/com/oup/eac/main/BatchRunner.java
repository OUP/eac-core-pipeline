package com.oup.eac.main;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.oup.eac.dao.BatchScheduleDao;
import com.oup.eac.job.CMDPDataExportJob;
import com.oup.eac.job.OrgUnitExportJob;
import com.oup.eac.job.OxedMalaysiaDataExportJob;
import com.oup.eac.util.BatchJobProperties;

public class BatchRunner {
	
	private static final Logger LOG = Logger.getLogger(BatchRunner.class);
	
	private static final BatchScheduleDao batchSchedule = new BatchScheduleDao();
	
	public static void main(String[] args) {
		
		String job = args[0];
		LOG.info("Batch Job Trigger for : "+job);
		if("CMDP".equals(job)){
			CMDPDataExportJob cmdpDataExportJob = new CMDPDataExportJob();
			boolean b = batchSchedule.updateTriggerDate(1);
			LOG.info("Last trigger date updated : "+b);
			DateTime fromDate = new DateTime(batchSchedule.getLastTriggerDate(1));
			LOG.info("CMDP fromDate : "+fromDate);
			DateTime toDate = new DateTime();
			LOG.info("CMDP toDate : "+toDate);
			try {
				cmdpDataExportJob.ftpCMDPData(fromDate, toDate);
				b = batchSchedule.updateSuccessDate(1);
				LOG.info("Last trigger date updated : "+b);
			} catch (Exception e) {
				LOG.error("Exception in CMDP Export runner : " , e);
			}
			LOG.info("CMDP Export Complete");
			
		}
		else if("OXED".equals(job)){
			OxedMalaysiaDataExportJob exportJob = new OxedMalaysiaDataExportJob();
			boolean b = batchSchedule.updateTriggerDate(2);
			LOG.info("Last trigger date updated : "+b);
			DateTime fromDate = new DateTime(batchSchedule.getLastTriggerDate(2));
			LOG.info("OXED fromDate : "+fromDate);
			DateTime toDate = new DateTime();
			LOG.info("OXED toDate : "+toDate);
			String emailTO = BatchJobProperties.OXED_ADMIN_EMAIL;
			String emailBcc = BatchJobProperties.EAC_ADMIN_EMAIL;
			try {
				exportJob.eMailDataExport("OXED", fromDate, toDate, emailTO, emailBcc);
				b = batchSchedule.updateSuccessDate(2);
				LOG.info("Last trigger date updated : "+b);
			} catch (Exception e) {
				LOG.error("Exception in OXED Export runner : " , e);
			}
			LOG.info("OXED Export Complete");
		}
		else if("MALAYSIA".equals(job)){
			OxedMalaysiaDataExportJob exportJob = new OxedMalaysiaDataExportJob();
			boolean b = batchSchedule.updateTriggerDate(3);
			LOG.info("Last trigger date updated : "+b);
			DateTime fromDate = new DateTime(batchSchedule.getLastTriggerDate(3));
			LOG.info("MALAYSIA fromDate : "+fromDate);
			DateTime toDate = new DateTime();
			LOG.info("MALAYSIA toDate : "+toDate);
			String emailTO = BatchJobProperties.FAJAR_ADMIN_EMAIL;
			String emailBcc = BatchJobProperties.EAC_ADMIN_EMAIL;
			try {
				exportJob.eMailDataExport("MALAYSIA", fromDate, toDate, emailTO, emailBcc);
				b = batchSchedule.updateSuccessDate(3);
				LOG.info("Last trigger date updated : "+b);
			} catch (Exception e) {
				LOG.error("Exception in MALAYSIA Export runner : " , e);
			}
			LOG.info("MALAYSIA Export Complete");
			
		}
		else if("ORGUNIT".equals(job)){
			OrgUnitExportJob exportJob = new OrgUnitExportJob();
			boolean b = batchSchedule.updateTriggerDate(4);
			LOG.info("Last trigger date updated : "+b);
			DateTime fromDate = new DateTime(batchSchedule.getLastTriggerDate(4));
			LOG.info("ORGUNIT fromDate : "+fromDate);
			DateTime toDate = new DateTime();
			LOG.info("ORGUNIT toDate : "+toDate);
			String emailTO = BatchJobProperties.ORG_UNIT_ADMIN_EMAIL;
			String emailBcc = BatchJobProperties.EAC_ADMIN_EMAIL;
			try {
				exportJob.emailOrgUnitUsageReport(fromDate, toDate, emailTO, emailBcc);
				b = batchSchedule.updateSuccessDate(4);
				LOG.info("Last trigger date updated : "+b);
			} catch (Exception e) {
				LOG.error("Exception in ORGUNIT Export runner : " , e);
			}
			LOG.info("ORGUNIT Export Complete");
		}
		else{
			LOG.info("Please enter a valid argument.[CMDP,OXED,MALAYSIA,ORGUNIT]");
		}
	}
}

package cz.muni.fi.sdipr.visualizationofozone.downloading.impl;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

@Singleton
@Startup
public class DownloadScheduler {

	private final static Logger LOGGER = Logger.getLogger(DownloadScheduler.class.toString());
	private final static String TIMER_NAME = "Update_DB_timer";

	@Resource
	TimerService timerService;

	@EJB
	DownloadManager downloadManager;

	@PostConstruct
	public void scheduleDbUpdate() {
		LOGGER.info("Scheduling database update.");

		ScheduleExpression schedule = new ScheduleExpression();
		// schedule.second("0");
		schedule.minute("0");
		schedule.hour("0");

		TimerConfig timerConfig = new TimerConfig(TIMER_NAME, false);

		Timer timer = timerService.createCalendarTimer(schedule, timerConfig);
	}

	@Timeout
	public void programmaticTimeout(Timer timer) {
		LOGGER.info("Scheduler executing database update.");
		downloadManager.refreshDatabase();
	}
}

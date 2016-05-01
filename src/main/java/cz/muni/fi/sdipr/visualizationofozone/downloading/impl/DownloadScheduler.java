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

import cz.muni.fi.sdipr.visualizationofozone.configuration.Configuration;

@Singleton
@Startup
public class DownloadScheduler {

	private final static Logger LOGGER = Logger.getLogger(DownloadScheduler.class.toString());
	private final static String TIMER_NAME = "Update_DB_timer";

	@Resource
	TimerService timerService;

	@EJB
	DownloadManager downloadManager;

	@EJB
	Configuration config;

	@PostConstruct
	public void scheduleDbUpdate() {
		LOGGER.info("Scheduling database update.");

		ScheduleExpression schedule = this.loadScheduleExpression();

		TimerConfig timerConfig = new TimerConfig(TIMER_NAME, false);

		Timer timer = timerService.createCalendarTimer(schedule, timerConfig);
	}

	@Timeout
	public void programmaticTimeout(Timer timer) {
		LOGGER.info("Scheduler executing database update.");
		downloadManager.refreshDatabase();
	}

	private ScheduleExpression getDefaultScheduleExpression() {
		ScheduleExpression schedule = new ScheduleExpression();
		schedule.minute("0");
		schedule.hour("0");
		LOGGER.info("Default timer will be set to everyday midnight.");
		return schedule;
	}

	private ScheduleExpression loadScheduleExpression() {
		String expresion = config.getPropertyValue(Configuration.DM_TIMER_PROPERTY);

		if (expresion == null) {
			LOGGER.severe("Download scheduler could not find '" + Configuration.DM_TIMER_PROPERTY
					+ "' property, download task should by scheduled to default time.");
			return this.getDefaultScheduleExpression();
		}

		String[] splitedExpression = expresion.split(" ");
		ScheduleExpression schedule = new ScheduleExpression();

		if (splitedExpression.length != 7) {
			LOGGER.severe("Can't parse schedule expression, download task should by scheduled to default time.");
			return this.getDefaultScheduleExpression();
		}
		schedule.second(splitedExpression[0]);
		schedule.minute(splitedExpression[1]);
		schedule.hour(splitedExpression[2]);
		schedule.dayOfMonth(splitedExpression[3]);
		schedule.month(splitedExpression[4]);
		schedule.dayOfWeek(splitedExpression[5]);
		schedule.year(splitedExpression[6]);

		return schedule;
	}
}

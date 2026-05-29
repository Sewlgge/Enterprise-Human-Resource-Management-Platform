package com.company.management.task;

import com.company.management.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 考勤定时任务
 * - 服务启动后：初始化当天考勤表 + 更新前一天打卡状态
 * - 每日凌晨0点：生成当天考勤表 + 更新前一天打卡状态
 */
@Component
@Slf4j
public class AttendanceTask {

    @Autowired
    private AttendanceService attendanceService;

    /**
     * 服务启动完成后，立即初始化当天考勤表
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("===== 服务启动，初始化当日考勤表 =====");
        try {
            attendanceService.initDailyAttendance();
        } catch (Exception e) {
            log.error("初始化考勤表失败", e);
        }

        log.info("===== 服务启动，更新前一天打卡状态 =====");
        try {
            attendanceService.updatePreviousDayStatus();
        } catch (Exception e) {
            log.error("更新前一天打卡状态失败", e);
        }
    }

    /**
     * 每日凌晨 0:00 自动生成当天考勤表
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyInitAttendance() {
        log.info("===== 定时任务：初始化当日考勤表 =====");
        try {
            attendanceService.initDailyAttendance();
        } catch (Exception e) {
            log.error("定时初始化考勤表失败", e);
        }

        log.info("===== 定时任务：更新前一天打卡状态 =====");
        try {
            attendanceService.updatePreviousDayStatus();
        } catch (Exception e) {
            log.error("定时更新前一天状态失败", e);
        }
    }
}

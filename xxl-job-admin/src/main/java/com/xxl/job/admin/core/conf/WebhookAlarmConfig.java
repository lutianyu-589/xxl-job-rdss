package com.xxl.job.admin.core.conf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.admin.core.alarm.JobAlarm;
import com.xxl.job.admin.core.alarm.impl.WebhookAlarm;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "xxl.job.webhook")
public class WebhookAlarmConfig {
    String url;
    String token;
    @Bean
    JobAlarm getWebhookAlarm(
            @Autowired
            XxlJobGroupDao xxlJobGroupDao
    ){
        WebhookAlarm webhookAlarm = new WebhookAlarm();
        webhookAlarm.setJumpToken(token);
        webhookAlarm.setUrl(url);
        webhookAlarm.setXxlJobGroupDao(xxlJobGroupDao);
        webhookAlarm.setObjectMapper(new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
        return webhookAlarm;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.xxl.job.admin.core.alarm.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.admin.core.alarm.JobAlarm;
import com.xxl.job.admin.core.model.WebhookRequest;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WebhookAlarm implements JobAlarm {
    private static final Logger log = LoggerFactory.getLogger(WebhookAlarm.class);
    private static final String JUMP_AUTH_HEADER="Jumpcloud-allowToken";
    protected String url;
    protected String jumpToken;
    private XxlJobGroupDao xxlJobGroupDao;
    private ObjectMapper objectMapper;
    @Override
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog) {
        boolean alarmResult = true;
        // 如果联系人为空那么就不发告警
        if(info.getAlarmContacts() == null || info.getAlarmContacts().isEmpty()) {
            log.info("联系人为空，不发送告警");
            return alarmResult;
        }
        WebhookRequest requestBody = getRequestBody(info, jobLog);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url)
                    .setHeader("Content-Type","application/json")
                    .setHeader(JUMP_AUTH_HEADER, jumpToken)
                    .setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)))
                    .build();
            alarmResult = httpclient.execute(httpPost, response -> {
                int responseCode = response.getCode();
                String reasonPhrase = response.getReasonPhrase();
                System.out.println(responseCode + " " + reasonPhrase);
                if (responseCode != 200) {
                    log.error("告警发送失败，原因{}", reasonPhrase);
                    return false;
                }
                final HttpEntity responseEntity = response.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                Map<String, Object> responseMap = objectMapper.readValue(responseString, Map.class);
                boolean success = (boolean) responseMap.get("success");
                String message = (String) responseMap.get("message");
                if(success) {
                    log.info("告警发送成功，返回消息{}", message);
                }else{
                    log.error("告警发送失败，返回消息{}", message);
                    return false;
                }
                EntityUtils.consume(responseEntity);
                return true;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return alarmResult;
    }

    private WebhookRequest getRequestBody(XxlJobInfo info, XxlJobLog jobLog) {
        WebhookRequest webhookRequest = new WebhookRequest();
        webhookRequest.setAlarmContacts(info.getAlarmContacts());
        webhookRequest.setExecutorAddress(jobLog.getExecutorAddress());
        webhookRequest.setExecutorId(info.getJobGroup());
        webhookRequest.setJobId(jobLog.getJobId());
        webhookRequest.setJobDesc(info.getJobDesc());
        webhookRequest.setAppName(xxlJobGroupDao.load(webhookRequest.getExecutorId()).getAppname());
        webhookRequest.setTriggerCode(jobLog.getTriggerCode());
        webhookRequest.setTriggerMsg(jobLog.getTriggerMsg());
        webhookRequest.setTriggerTime(jobLog.getTriggerTime());
        webhookRequest.setHandleCode(jobLog.getHandleCode());
        webhookRequest.setHandleMsg(jobLog.getHandleMsg());
        webhookRequest.setHandleTime(jobLog.getHandleTime());
        return webhookRequest;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setJumpToken(String jumpToken) {
        this.jumpToken = jumpToken;
    }

    public void setXxlJobGroupDao(XxlJobGroupDao xxlJobGroupDao) {
        this.xxlJobGroupDao = xxlJobGroupDao;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}

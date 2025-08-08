package com.xxl.job.admin.core.model;

import com.xxl.job.admin.core.enums.CqJobRelation;

public class XxlJobCqJob {
    private String jobName;
    private String cqNo;
    private CqJobRelation rel;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCqNo() {
        return cqNo;
    }

    public void setCqNo(String cqNo) {
        this.cqNo = cqNo;
    }

    public CqJobRelation getRel() {
        return rel;
    }

    public void setRel(CqJobRelation rel) {
        this.rel = rel;
    }
}

package com.xxl.job.admin.core.model;

import java.time.LocalDate;
import java.util.List;

public class XxlJobCq {
    private String cqNo;
    private String cqName;
    private LocalDate prodDate;
    private List<XxlJobCqJob> jobList;

    public String getCqNo() {
        return cqNo;
    }

    public void setCqNo(String cqNo) {
        this.cqNo = cqNo;
    }

    public String getCqName() {
        return cqName;
    }

    public void setCqName(String cqName) {
        this.cqName = cqName;
    }

    public LocalDate getProdDate() {
        return prodDate;
    }

    public void setProdDate(LocalDate prodDate) {
        this.prodDate = prodDate;
    }

    public List<XxlJobCqJob> getJobList() {
        return jobList;
    }

    public void setJobList(List<XxlJobCqJob> jobList) {
        this.jobList = jobList;
    }
}

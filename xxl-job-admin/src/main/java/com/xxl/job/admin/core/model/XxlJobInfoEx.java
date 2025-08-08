package com.xxl.job.admin.core.model;

import com.xxl.job.admin.core.enums.CqJobRelation;

public class XxlJobInfoEx extends XxlJobInfo {
    CqJobRelation cqJobRelation;

    public XxlJobInfoEx(XxlJobInfo job,CqJobRelation relation) {
        super(job);
        this.cqJobRelation=relation;
    }

    public CqJobRelation getCqJobRelation() {
        return cqJobRelation;
    }

    public void setCqJobRelation(CqJobRelation cqJobRelation) {
        this.cqJobRelation = cqJobRelation;
    }
}

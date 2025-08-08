package com.xxl.job.admin.core.enums;

public enum CqJobRelation {
    ADD("add"),
    DELETE("delete"),
    UPDATE("update");
    final String relType;

    CqJobRelation(String relType) {
        this.relType = relType;
    }
}

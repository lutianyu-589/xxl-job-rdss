package com.xxl.job.core.handler.impl;

import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.log.XxlJobFileAppender;
import com.xxl.job.core.util.ScriptUtil;

import java.io.File;

public class MicroBatchJobHandler extends IJobHandler {
    private int jobId;
    private String jobName;
    private GlueTypeEnum glueType;

    public MicroBatchJobHandler(int jobId, String jobName, GlueTypeEnum glueType) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.glueType = glueType;
    }

    @Override
    public void execute() throws Exception {
        if (GlueTypeEnum.MICROBATCH!=glueType) {
            XxlJobHelper.handleFail("glueType["+ glueType +"] invalid.");
            return;
        }

        // cmd
        String cmd = glueType.getCmd();
        String scriptFileName="${HOME}/mb/spark/bin/start_spark_etl_job.sh";
        // log file
        String logFileName = XxlJobContext.getXxlJobContext().getJobLogFileName();

        // script params：0=param、1=分片序号、2=分片总数
        String[] scriptParams = new String[3];
        scriptParams[0] = jobName.toUpperCase();
        scriptParams[1] = XxlJobHelper.getJobParam();
        // invoke
        XxlJobHelper.log("----------- script file:"+ scriptFileName +" -----------");
        int exitValue = ScriptUtil.execToFile(cmd, scriptFileName, logFileName, scriptParams);

        if (exitValue == 0) {
            XxlJobHelper.handleSuccess();
            return;
        } else {
            XxlJobHelper.handleFail("script exit value("+exitValue+") is failed");
            return ;
        }

    }
}

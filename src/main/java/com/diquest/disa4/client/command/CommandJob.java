package com.diquest.disa4.client.command;

import com.diquest.disa4.client.model.JobStatus;
import com.diquest.frame.util.msg.Message;
import com.diquest.frame.util.msg.type.LongT;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandJob extends BaseCommand {

    private static final ObjectMapper mapper = new ObjectMapper();

    public CommandJob() {
    }

    public CommandJob(String ip) {
        super(ip);
    }

    public CommandJob(String ip, int port) {
        super(ip, port);
    }

    public CommandJob(String ip, int port, int timeout) {
        super(ip, port, timeout);
    }

    /**
     * 진행 중인 작업식별번호 조회
     *
     * @return 작업식별번호
     */
    public Long getJobId() {
        Message response = execute("", null); // TODO: 추후 DisaManager 와 DisaEngine 정리되면 reflector 추가  ex) com.diquest.infochatter.msg.job.ExtNetMsgGetJobId

        return response == null ? null : response.getLong(0);
    }

    /**
     * 진행 중인 작업상태 조회
     *
     * @return 작업상태
     */
    public JobStatus getJobStatus() {
        Long jobId = getJobId();

        return jobId == null ? null : getJobStatus(jobId);
    }

    /**
     * 작업상태 조회
     *
     * @param jobId 작업식별번호
     * @return 작업상태
     */
    public JobStatus getJobStatus(long jobId) {
        if (jobId <= 0)
            return null;

        try {
            Message request = new Message(1);
            request.setLong(0, jobId);
            // TODO: 추후 DisaManager 와 DisaEngine 정리되면 reflector 추가  ex) com.diquest.infochatter.msg.job.ExtNetMsgJobProgressReport
            Message response = execute("", request);
            if (response == null)
                return null;

            String jobStatusJson = response.get(0) == null ? null : response.getString(0);
            if (jobStatusJson == null)
                return null;

            JobStatus jobStatus = mapper.readValue(jobStatusJson, JobStatus.class);
            jobStatus.setJobId(jobId);

            return jobStatus;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 대기 중인 작업식별번호 조회
     *
     * @return 대기중인 작업식별번호 목록
     */
    public List<Long> getWaitingJob() {
        Message response = execute("", null); // TODO: 추후 DisaManager 와 DisaEngine 정리되면 reflector 추가  ex)com.diquest.infochatter.msg.job.ExtNetMsgGetWaitingJob

        return response == null || response.get(0) == null ? null : this.asLongList(response.getList(0));
    }

    public static List<Long> asLongList(List list) {
        if (list != null && list.size() != 0) {
            List<Long> result = new ArrayList();
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
                Object o = i$.next();
                if (o instanceof LongT) {
                    result.add(((LongT)o).value);
                }
            }

            return result.size() == 0 ? null : result;
        } else {
            return null;
        }
    }
}

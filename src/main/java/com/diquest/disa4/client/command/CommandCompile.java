package com.diquest.disa4.client.command;

import com.diquest.disa4.client.model.JobStatus;
import com.diquest.frame.util.msg.Message;

/**
 * 컴파일
 */
public class CommandCompile extends BaseCommand {

    public CommandCompile() {
    }

    public CommandCompile(String ip) {
        super(ip);
    }

    public CommandCompile(String ip, int port) {
        super(ip, port);
    }

    public CommandCompile(String ip, int port, int timeout) {
        super(ip, port, timeout);
    }

    /**
     * 전체 컴파일
     *
     * @return 작업상태
     */
    public JobStatus compileAll() {
        return compile("COMPILE_ALL_DIC", null);
    }

    /**
     * 컴파일
     *
     * @param target 컴파일 대상
     * @param categoryName 카테고리
     * @return 작업상태
     */
    public JobStatus compile(String target, String categoryName) {
        Message request = new Message(3);
        request.setString(0, ""); // TODO: 추후 DisaManager 와 DisaEngine 정리되면 reflector 추가 ex) com.diquest.infochatter.msg.ExtNetMsgCompileDicReal
        request.setString(1, target == null ? "" : target); // TODO: "COMPILE_ALL_DIC" 과 같이 각 사전에 대응되는 enum 필요
        request.setString(2, categoryName == null ? "" : categoryName);

        Message response = execute("", request);  // TODO: 추후 DisaManager 와 DisaEngine 정리되면 reflector 추가 ex) com.diquest.infochatter.msg.ExtNetMsgCompileDic

        if (response == null || response.get(0) == null || !"OK".equals(response.getString(0)))
            return null;

        long jobId = response.get(1) == null ? 0 : response.getLong(1);

        return new CommandJob(getIp(), getPort(), getTimeout()).getJobStatus(jobId);
    }

}

package com.diquest.disa4.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 일감 상태
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobStatus implements Serializable {

    /** 상태 */
    public enum Status {
        /** 생성 */
        CREATE,
        /** 시작 */
        START,
        /** 진행중 */
        RUNNING,
        /** 종료 */
        END,
        /** 오류 */
        ERROR
    }

    /** 작업 ID */
    private long jobId;

    /** 이름 (엔진부여) */
    @JsonProperty("processName")
    private String name;

    /** 진행률 (0~100) */
    @JsonProperty("progressRate")
    private int progress;

    /** 상태 */
    @JsonProperty("processState")
    private Status status;

}

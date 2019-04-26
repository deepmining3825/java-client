package com.diquest.disa4.client;

import com.diquest.disa4.client.command.CommandAnalyze;
import com.diquest.disa4.client.command.CommandCompile;
import lombok.Data;

/**
 * 디사 자바 클라이언트
 *
 * @author donghyunkim
 */
@Data
public class Disa4Client {

    /** 타임아웃 기본값 (5000ms) */
    private static final int DEFAULT_TIMEOUT = 5000;

    /**
     * Engine IP address
     *
     * @param ip IP address
     */
    private String ip;

    /**
     * Engine Port number
     *
     * @param port Port number
     */
    private int port;

    /**
     * 시간초과 설정
     *
     * @param timeout 시간초과 시간 (단위: ms)
     */
    private int timeout;

    public Disa4Client() {

    }

    public Disa4Client(String ip, int port) { this(ip, port, DEFAULT_TIMEOUT); }

    public Disa4Client(String ip, int port, int timeout) {
        this.ip = ip;
        this.port = port;
        this.timeout = timeout;
    }

    /**
     * 컴파일
     */
    public CommandCompile compile() {
        return new CommandCompile(getIp(), getPort(), getTimeout());
    }

    /**
     * 분석
     */
    public CommandAnalyze analyze() {
        return new CommandAnalyze(getIp(), getPort(), getTimeout());
    }

}

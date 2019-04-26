package com.diquest.disa4.client.command;

import com.diquest.disa4.client.exception.CommandException;
import com.diquest.frame.client.network.ExternalClientPool;
import com.diquest.frame.common.msg.ext.body.common.ErrorMessage;
import com.diquest.frame.util.msg.Message;
import com.diquest.frame.util.msg.Transmitable;
import com.diquest.frame.util.msg.external.ExtRequest;
import com.diquest.frame.util.msg.external.ExtResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@EqualsAndHashCode(exclude = "logger")
public abstract class BaseCommand {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** 디사 기본 Port */
    protected static final int DEFAULT_PORT = 2066;

    /** 타임아웃 기본값 (5000ms) */
    protected static final int DEFAULT_TIMEOUT = 5000;

    /** 디사 서버 IP */
    private String ip;

    /** 디사 서버 Port */
    private int port;

    /**  타임아웃 */
    private int timeout;

    public BaseCommand() {

    }

    public BaseCommand(String ip) {
        this(ip, DEFAULT_PORT, DEFAULT_TIMEOUT);
    }

    public BaseCommand(String ip, int port) {
        this(ip, port, DEFAULT_TIMEOUT);
    }

    public BaseCommand(String ip, int port, int timeout) {
        this.ip = ip;
        this.port = port;
        this.timeout = timeout;
    }

    protected Message execute(String reflector, Message request) {
        Transmitable response = executeInternal(reflector, request);
        if (response == null)
            return null;

        // 디사는 Message 로 통신하는데 객체가 다른경우 오류발생
        if (!(response instanceof Message)) {
            throw new CommandException(-70001);
        }

        return (Message) response;
    }

    private Transmitable executeInternal(String reflector, Transmitable request) {
        logger.debug("Connecting... ({}:{}, Timeout: {}ms)", getIp(), getPort(), getTimeout());
        logger.debug(">> CLASS: {}", reflector);
        logger.debug(">> REQ: {}", request);
        ExternalClientPool client = ExternalClientPool.getInstance(getIp(), getPort());

        ExtRequest internalRequest = new ExtRequest(reflector, request);
        ExtResponse internalResponse = 0 < getTimeout() ? client.invoke(internalRequest, getTimeout()) : client.invoke(internalRequest);
        // Worker 할당실패
        if (internalResponse == null) {
            throw new CommandException(-70000);
        }

        int errorCode = internalResponse.getErrorCode();
        Transmitable data = internalResponse.getData();
        logger.debug("<< RES: {}", data);
        logger.debug("Close... (Code: {})", errorCode);

        // 에러확인
        if (errorCode < 0) {
            String errorMessage = null;
            if (data instanceof ErrorMessage) {
                errorMessage = ((ErrorMessage) data).getErrorMessage();
            }

            throw new CommandException(errorCode, errorMessage);
        }

        return data;
    }
}

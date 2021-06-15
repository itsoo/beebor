package org.beeborframework.web.core;

import lombok.Getter;

/**
 * RequestMethod
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/12 19:22
 */
@Getter
public class RequestMethod {

    private final RequestType requestType;

    private final String[] paths;


    private RequestMethod(RequestType requestType, String[] paths) {
        this.requestType = requestType;
        this.paths = paths;
    }

    public static RequestMethod getInstance(RequestType requestType, String[] paths) {
        return new RequestMethod(requestType, paths);
    }


    /**
     * RequestType
     */
    public enum RequestType {

        /*** get request */
        GET,

        /*** post request */
        POST,

        /*** put request */
        PUT,

        /*** delete request */
        DELETE,

        /*** not support */
        NONE
    }
}

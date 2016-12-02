package com.andrestful.api;

import com.andrestful.annotation.HttpDelete;
import com.andrestful.annotation.HttpGet;
import com.andrestful.annotation.HttpPost;
import com.andrestful.annotation.HttpPut;
import com.andrestful.exception.RestfulException;
import com.andrestful.http.MultiRequestHandler;
import com.andrestful.util.StackTraceUtil;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP请求
 * Created by SCWANG on 2016/6/12.
 */
public class AbstractRequester {

    public static class HttpRequest {
        public HttpMethod method;
        public String path;

        public HttpRequest(HttpMethod method, String path) {
            this.path = path;
            this.method = method;
        }
    }


    /**
     * 打包参数到MAP
     */
    public static Map<String, Object> keyValueToMap(Object... keyvalue) {
        return keyValueToMap(new LinkedHashMap<String, Object>(), keyvalue);
    }

    /**
     * 打包参数到MAP
     */
    public static Map<String, Object> keyValueToMap(Map<String, Object> map, Object... keyvalue) {
        if (keyvalue != null && keyvalue.length > 0) {
            for (int i = 0; i < keyvalue.length / 2; i++) {
                if (keyvalue[2 * i] instanceof String) {
                    Object arg = keyvalue[2 * i + 1];
                    map.put((String) keyvalue[2 * i], arg);
                }
            }
        }
        return map;
    }


    /**
     * http 封装实现者
     */
    @SuppressWarnings("unused")
    protected static Implementer impl = new Implementer();

    /**
     * 实现者
     */
    @SuppressWarnings("unused")
    protected static class Implementer {
        /**
         * http 执行器
         */
        public RequestHandler handler = new MultiRequestHandler();

        public Response doRequest() throws Exception {
            HttpRequest request = getHttpRequest();
            return handler.doRequest(request.method, request.path, defaultHeader(), null, null);
        }

        public Response doRequestBody(Object body) throws Exception {
            HttpRequest request = getHttpRequest();
            return handler.doRequest(request.method, request.path, defaultHeader(), body, null);
        }

        public Response doRequestBody(Object... keyvalue) throws Exception {
            HttpRequest request = getHttpRequest();
            return handler.doRequest(request.method, request.path, defaultHeader(), keyValueToMap(keyvalue), null);
        }

        public Response doRequestParam(Object... keyvalue) throws Exception {
            HttpRequest request = getHttpRequest();
            return handler.doRequest(request.method, request.path, defaultHeader(), null, keyValueToMap(keyvalue));
        }

        public Response doRequestBodyParam(Object body, Object... keyvalue) throws Exception {
            HttpRequest request = getHttpRequest();
            return handler.doRequest(request.method, request.path, defaultHeader(), body, keyValueToMap(keyvalue));
        }

        public Response doUploadParam(String file, Object... keyvalue) throws Exception {
            HttpRequest request = getHttpRequest();
            return handler.doUpload(request.path, defaultHeader(), null, keyValueToMap(keyvalue), file);
        }

        public Response doUploadParam(String file1, String file2, Object... keyvalue) throws Exception {
            HttpRequest request = getHttpRequest();
            return handler.doUpload(request.path, defaultHeader(), null, keyValueToMap(keyvalue), file1, file2);
        }

        /**
         * 默认HTTP头
         * 包括登录用户ID和位置信息
         */
        public Map<String, String> defaultHeader() {
            return null;
        }

        /**
         * 统一获取API注解
         */
        public HttpRequest getHttpRequest() throws Exception {
            return getHttpRequest(2);
        }

        /**
         * 统一获取API注解
         */
        public HttpRequest getHttpRequest(int level) throws Exception {
            HttpGet get = StackTraceUtil.getCurrentMethodAnnotation(HttpGet.class, 1 + level);
            HttpPost post = StackTraceUtil.getCurrentMethodAnnotation(HttpPost.class, 1 + level);
            HttpDelete delete = StackTraceUtil.getCurrentMethodClassAnnotation(HttpDelete.class, 1 + level);
            HttpPut put = StackTraceUtil.getCurrentMethodAnnotation(HttpPut.class, 1 + level);

            if (get != null) {
                return new HttpRequest(get.method(), get.value());
            } else if (post != null) {
                return new HttpRequest(post.method(), post.value());
            } else if (delete != null) {
                return new HttpRequest(delete.method(), delete.value());
            } else if (put != null) {
                return new HttpRequest(put.method(), put.value());
            } else {
                Method method = StackTraceUtil.getCurrentMethod(1 + level);
                if (method == null) {
                    throw new RestfulException("访问API没有设置HttpRequest");
                }
                throw new RestfulException(method.getDeclaringClass().getSimpleName()+"."+method.getName()+" 没有设置HttpRequest");
            }
        }
    }
}
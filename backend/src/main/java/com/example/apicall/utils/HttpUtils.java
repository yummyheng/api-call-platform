package com.example.apicall.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpUtils {
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private HttpUtils() {
        // 私有构造函数，禁止实例化
    }

    /**
     * 执行HTTP请求
     * @param url 请求URL
     * @param method 请求方法
     * @param headers 请求头
     * @param requestBody 请求体
     * @return HttpResponse 响应对象
     * @throws URISyntaxException URI构建异常
     * @throws IOException IO异常
     */
    public static HttpResponse executeRequest(String url, String method, Map<String, String> headers, String requestBody) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(url);

        // GET请求参数处理
        if ("GET".equalsIgnoreCase(method) && requestBody != null && !requestBody.isEmpty()) {
            try {
                Map<String, Object> params = JSON.parseObject(requestBody, new TypeReference<Map<String, Object>>() {});
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() != null) {
                        uriBuilder.addParameter(entry.getKey(), entry.getValue().toString());
                    }
                }
            } catch (Exception e) {
                log.info("JSON参数解析失败，尝试解析为URL编码参数: {}", e.getMessage());
                try {
                    // 解析URL编码格式的参数
                    String[] paramPairs = requestBody.split("&");
                    for (String paramPair : paramPairs) {
                        String[] keyValue = paramPair.split("=", 2);
                        if (keyValue.length == 2) {
                            String key = keyValue[0];
                            String value = keyValue[1];
                            uriBuilder.addParameter(key, value);
                        }
                    }
                } catch (Exception ex) {
                    log.warn("URL编码参数解析也失败: {}", ex.getMessage());
                }
            }
        }

        String uri = uriBuilder.build().toString();
        HttpUriRequest httpRequest = createHttpRequest(method, uri, headers, requestBody);
        return httpClient.execute(httpRequest);
    }

    /**
     * 创建HTTP请求对象
     * @param method 请求方法
     * @param uri 请求URI
     * @param headers 请求头
     * @param requestBody 请求体
     * @return HttpUriRequest HTTP请求对象
     * @throws IOException IO异常
     */
    public static HttpUriRequest createHttpRequest(String method, String uri, Map<String, String> headers, String requestBody) throws IOException {
        HttpUriRequest httpRequest;

        switch (method.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(uri);
                if (requestBody != null && !requestBody.isEmpty()) {
                    httpPost.setEntity(new StringEntity(requestBody, "UTF-8"));
                }
                httpRequest = httpPost;
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uri);
                if (requestBody != null && !requestBody.isEmpty()) {
                    httpPut.setEntity(new StringEntity(requestBody, "UTF-8"));
                }
                httpRequest = httpPut;
                break;
            case "DELETE":
                HttpDelete httpDelete = new HttpDelete(uri);
                httpRequest = httpDelete;
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(uri);
                if (requestBody != null && !requestBody.isEmpty()) {
                    httpPatch.setEntity(new StringEntity(requestBody, "UTF-8"));
                }
                httpRequest = httpPatch;
                break;
            case "GET":
            default:
                HttpGet httpGet = new HttpGet(uri);
                httpRequest = httpGet;
                break;
        }

        // 设置请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpRequest::setHeader);
        }

        return httpRequest;
    }

    /**
     * 解析响应头为Map
     * @param httpResponse HTTP响应
     * @return Map<String, String> 响应头键值对
     */
    public static Map<String, String> parseResponseHeaders(HttpResponse httpResponse) {
        Map<String, String> responseHeaders = new HashMap<>();
        Arrays.stream(httpResponse.getAllHeaders()).forEach(header -> responseHeaders.put(header.getName(), header.getValue()));
        return responseHeaders;
    }

    /**
     * 读取响应体
     * @param httpResponse HTTP响应
     * @return String 响应体内容
     * @throws IOException IO异常
     */
    public static String readResponseBody(HttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        return entity != null ? EntityUtils.toString(entity, "UTF-8") : "";
    }

    /**
     * 关闭HttpClient资源
     * @throws IOException IO异常
     */
    @PreDestroy
    public static void closeHttpClient() throws IOException {
        httpClient.close();
    }
}
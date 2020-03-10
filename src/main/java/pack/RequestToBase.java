package pack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.facilities.TelegramHttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestToBase {
    private final String BASE_URL = "http://localhost:8080/api/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private volatile CloseableHttpClient httpclient;
    private DefaultBotOptions options;
    private volatile RequestConfig requestConfig;
    private String token;

    protected RequestToBase(DefaultBotOptions options) {
        this.options = options;
        this.httpclient = TelegramHttpClientBuilder.build(options);
        this.requestConfig = options.getRequestConfig();
        if (this.requestConfig == null) {
            this.requestConfig = RequestConfig.copy(RequestConfig.custom().build()).setSocketTimeout(75000).setConnectTimeout(75000).setConnectionRequestTimeout(75000).build();
        }
        this.token = this.getToken();
    }

    public String getToken() {
        AuthenticationDto dto = new AuthenticationDto("mailfrreg@yandex.ru", "password");
        return getObjectFromBase("POST", "authentication", dto, AuthenticationToken.class).getAuthenticationToken();
    }

    public <T> T getObjectFromBase(String method, String uri, Object body, Class<T> clazz) {
        try {
            return this.objectMapper.readValue(sendMethodRequest(method, uri, body), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> List<T> getListFromBase(String method, String uri, Object body, Class<T> clazz) {
        try {
            CollectionType javaType = this.objectMapper
                    .getTypeFactory()
                    .constructCollectionType(List.class, clazz);
            return this.objectMapper.readValue(sendMethodRequest(method, uri, body), javaType);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void sendExpenses(ExpensesDto userExpenses) {
        try {
            sendMethodRequest("POST", "expenses", userExpenses);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String sendMethodRequest(String method, String uri, Object body) throws IOException {
        String url = BASE_URL + uri;
        if (method.equalsIgnoreCase("POST")) {
            HttpPost httppost = this.configuredHttpPost(url);
            httppost.setEntity(new StringEntity(this.objectMapper.writeValueAsString(body), ContentType.APPLICATION_JSON));
            return this.sendHttpRequest(httppost);
        }
        HttpGet httpGet = this.configuredHttpGet(url);
        return this.sendHttpRequest(httpGet);
    }

    private HttpPost configuredHttpPost(String url) {
        HttpPost httppost = new HttpPost(url);
        httppost.setConfig(this.requestConfig);
        httppost.addHeader("charset", StandardCharsets.UTF_8.name());
        httppost.addHeader("Accept", "application/json");
        if (token != null) {
            httppost.addHeader("X-Chat-Token", token);
        }
        return httppost;
    }

    private HttpGet configuredHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.requestConfig);
        httpGet.addHeader("charset", StandardCharsets.UTF_8.name());
        httpGet.addHeader("Accept", "application/json");
        if (token != null) {
            httpGet.addHeader("X-Chat-Token", token);
        }
        return httpGet;
    }

    private String sendHttpRequest(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = this.httpclient.execute(request);
        Throwable var3 = null;

        String var6;
        try {
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            var6 = EntityUtils.toString(buf, StandardCharsets.UTF_8);
        } catch (Throwable var15) {
            var3 = var15;
            throw var15;
        } finally {
            if (response != null) {
                if (var3 != null) {
                    try {
                        response.close();
                    } catch (Throwable var14) {
                        var3.addSuppressed(var14);
                    }
                } else {
                    response.close();
                }
            }

        }

        return var6;
    }

}

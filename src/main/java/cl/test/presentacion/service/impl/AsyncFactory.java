package cl.test.presentacion.service.impl;


import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;


public class AsyncFactory {

    int maxTotalConnections = 50;
    int maxConnectionsPerRoute = 10;
    
    //milli
    int connectRequestTimeout = 3000;
    int connectTimeout = 2000;
    int readTimeout = 7000;
    

    
    public AsyncRestTemplate asyncRestTemplateFactory() {

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        
        connManager.setMaxTotal(maxTotalConnections);
        connManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);

        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();

        HttpComponentsAsyncClientHttpRequestFactory requestAsyncFactory =
                new HttpComponentsAsyncClientHttpRequestFactory();

        requestAsyncFactory.setConnectionRequestTimeout(connectRequestTimeout);
        requestAsyncFactory.setConnectTimeout(connectTimeout);
        requestAsyncFactory.setReadTimeout(readTimeout);

        requestAsyncFactory.setHttpClient(httpClient);
        
        AsyncRestTemplate art = new AsyncRestTemplate();
        art.setAsyncRequestFactory(requestAsyncFactory);
        
        
        //deberia ser inyectado desde DI container        
        return new AsyncRestTemplate(requestAsyncFactory);
    }

    
    
}

package cl.test.presentacion.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import cl.test.presentacion.service.IEjemploService;



@Service
public class EjemploServiceImpl implements IEjemploService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EjemploServiceImpl.class);
    
    public String getBienvenida(String language ) throws Exception {

            LOGGER.info("language="+language);
            
            //dummy sample:
            return language;
    }
    
   

    public String callback() throws Exception {

        Map<String, ListenableFuture<ResponseEntity<String>>> futures = new HashMap<>(); //podria ser un Guava Table
        List<String> respuestas = new ArrayList<>();
        

        AsyncFactory af = new AsyncFactory();
        AsyncRestTemplate artf = af.asyncRestTemplateFactory();

        //url a visitar
        String[] url = {"http://google.com", "http://bing.com", "https://stackoverflow.com/", "https://www.infoq.com/", "https://dzone.com/",
                        "https://jakarta.ee/", "https://www.eclipse.org/",  "http://www.noexisto.cl/", "https://spring.io/", "https://github.com/", 
                        "https://www.apache.org/"};

        
        //agrego llamadas asincronas
        for(int i=0; i<url.length; i++) {
            
            HttpMethod method = HttpMethod.GET;
            Class<String> responseType = String.class;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            HttpEntity<String> requestEntity = new HttpEntity<>("params", headers);
            //la llave es la misma url (modificable)
            
            // usar DeferredResult ?
            ListenableFuture<ResponseEntity<String>> future = artf.exchange(url[i], method, requestEntity, responseType);
                    

            // es "opcional"
            future.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
                  @Override
                  public void onSuccess(ResponseEntity<String> result) {
                    System.out.println("onSuccess " + result.getHeaders() );
                    //TODO: DeferredResult ?
                  }

                  @Override
                  public void onFailure(Throwable ex) {
                    System.out.println("onFailure!!! " + ex );
                  }
                });
                
             futures.put(url[i], future);
        }

        
        //TODO: borrar !
        // duermo los hilos solo por prueba:
        int segundos_sleep = 2;
        System.out.println("durmiendo por " + segundos_sleep + " segundos...");
        Thread.sleep(segundos_sleep * 1000);

        
        int i=0;
        //voy uno a uno... con Guava SuccessfulAsList seria lo mismo al final ya que quiero cada uno de los responses
        for (Map.Entry<String, ListenableFuture<ResponseEntity<String>>> llave : futures.entrySet()) {
            
            System.out.println((++i)+". -----------------------------------------------------\n");

            System.out.println("key = " + llave.getKey() + ",\n value = " + llave.getValue()); 
    
            ListenableFuture<ResponseEntity<String>> future = llave.getValue();
            ResponseEntity<String> entity = null;

            try {
            
            entity = future.get(); // blocking en cada uno, pero la respuesta ya puede existir
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                continue;
            } catch (ExecutionException ee) {
                ee.printStackTrace();
                continue;
            } 
                
            
            String headers = entity.getHeaders().toString();
            
            if(headers.indexOf("domain=")!=-1)
                System.out.println(headers.substring(headers.indexOf("; domain=")));
            else                 
                System.out.println(headers);

            System.out.println(entity.getStatusCode());
            //System.out.println(entity.getBody());
            respuestas.add(entity.getBody().toString());
            
            System.out.println("isDone? " + future.isDone() + "\n");
        }
           

        
        System.out.println("\nfin.");
    

        //return respuestas;
        return "done";
    }

    

}


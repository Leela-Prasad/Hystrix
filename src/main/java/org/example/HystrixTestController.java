package org.example;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HystrixTestController {

    @HystrixCommand(fallbackMethod = "fallbackTest",
                    commandProperties = {
                        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "80"),
                        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
                    })
    @GetMapping("/test")
    public String test() {
        System.out.println("Entered");
        /*if(true)
            throw new RuntimeException("Intentional");*/
        String result = new RestTemplate().getForObject("http://localhost:8080/test", String.class);
        System.out.println("Exit");
        return result;
    }

    public String fallbackTest() {
        System.out.println("Entered Fallback");
        return "fake success";
    }
}

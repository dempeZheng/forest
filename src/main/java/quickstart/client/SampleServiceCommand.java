package quickstart.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Component;
import quickstart.api.SampleService;

import javax.annotation.Resource;

/**
 * User: Dempe
 * Date: 2016/12/6
 * Time: 16:16
 */
@Component
public class SampleServiceCommand {

    @Resource(name = "sampleServiceProxy")
    SampleService remoteServiceRef;


    @HystrixCommand(groupKey = "ExampleGroup", commandKey = "HelloWorld", threadPoolKey = "HelloWorldPool", fallbackMethod = "sayFallback")
    public String say(String str) {
        String say = remoteServiceRef.say(str);
        System.out.println("say:" + say);
        str.toString();
        return say;
    }

    public String sayFallback(String str) {
        return "sayFallBack:" + str;
    }
}

package quickstart.server;

import com.dempe.forest.register.RegisterInfo;
import com.dempe.forest.register.redis.RedisRegistryService;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 5:27
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ComponentScan
public class SampleServer {

    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application.xml"});
        RedisRegistryService namingService = (RedisRegistryService) context.getBean("namingService");

        Set<String> serviceNameSet = Sets.newHashSet();
        serviceNameSet.add("sampleService");
        Map<String, List<RegisterInfo>> map = namingService.list(serviceNameSet);
        for (Map.Entry<String, List<RegisterInfo>> stringListEntry : map.entrySet()) {
            for (RegisterInfo registerInfo : stringListEntry.getValue()) {
                System.out.println(registerInfo);
            }
        }



    }




}

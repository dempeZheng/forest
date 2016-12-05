package quickstart;

import com.dempe.forest.register.RegisterInfo;
import com.dempe.forest.register.redis.RedisRegistryService;
import com.google.common.collect.Sets;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/5
 * Time: 18:16
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application.xml"});
        RedisRegistryService namingService = (RedisRegistryService) context.getBean("namingService");

        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setHost("localhost");
        registerInfo.setPort(9999);

        registerInfo.setService("sampleService");

        namingService.register(registerInfo);

        Set<String> serviceNameSet = Sets.newHashSet();
        serviceNameSet.add("sampleService");


        Map<String, List<RegisterInfo>> list = namingService.list(serviceNameSet);
        for (Map.Entry<String, List<RegisterInfo>> stringListEntry : list.entrySet()) {
            for (RegisterInfo info : stringListEntry.getValue()) {
                System.out.println(">>>>>>" + info);
            }

        }

    }

}

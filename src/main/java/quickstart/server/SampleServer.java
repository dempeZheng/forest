package quickstart.server;

import com.dempe.forest.AnnotationRouterMapping;
import com.dempe.forest.ForestExecutorGroup;
import com.dempe.forest.ServerConfig;
import com.dempe.forest.transport.NettyServer;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application.xml"});
        AnnotationRouterMapping mapping = new AnnotationRouterMapping(context);
        ServerConfig config = ConfigFactory.create(ServerConfig.class);
        ForestExecutorGroup executorGroup = new ForestExecutorGroup(config, mapping.listGroup(), context);
        new NettyServer(mapping, config, executorGroup).doBind();
    }


}

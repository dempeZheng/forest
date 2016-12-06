package quickstart.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/5
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
public class SpringSampleClient {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-client.xml"});
        SampleServiceCommand bean = context.getBean(SampleServiceCommand.class);
        String test = bean.say(null);
        System.out.println(test);
    }

}

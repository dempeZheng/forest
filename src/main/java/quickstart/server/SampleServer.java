package quickstart.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 5:27
 * To change this template use File | Settings | File Templates.
 */
public class SampleServer {

    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application.xml"});

    }


}

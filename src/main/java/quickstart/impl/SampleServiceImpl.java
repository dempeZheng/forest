package quickstart.impl;

import com.dempe.forest.core.annotation.Interceptor;
import com.dempe.forest.core.annotation.MethodExport;
import com.dempe.forest.core.annotation.Rate;
import com.dempe.forest.core.annotation.ServiceExport;
import quickstart.api.SampleService;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3
 * Time: 下午 5:31
 * To change this template use File | Settings | File Templates.
 */
@ServiceExport
public class SampleServiceImpl implements SampleService {

    @MethodExport
    @Rate(2)
    @Interceptor("metricInterceptor")
    @Override
    public String say(String str) {
        return "say " + str;
    }

    @Override
    public String say2(String str) {
        return "say2 " + str;
    }

    @Interceptor("metricInterceptor")
    @MethodExport
    @Override
    public String echo(String msg) {
        return "echo " + msg;
    }

    @MethodExport
    @Override
    public String hi(String msg) {
        return "hi " + msg;
    }

}

package quickstart.api;

import com.dempe.forest.core.annotation.HttpParam;
import com.dempe.forest.core.annotation.ServiceProvider;
import com.dempe.forest.core.annotation.MethodProvider;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 5:24
 * To change this template use File | Settings | File Templates.
 */
@ServiceProvider(serviceName = "sampleService", port = 8888)
public interface SampleService {

    @MethodProvider(methodName = "say")
    String say(@HttpParam String str);

}

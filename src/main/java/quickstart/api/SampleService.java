package quickstart.api;

import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.MethodProvider;
import com.dempe.forest.core.annotation.ServiceProvider;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 5:24
 * To change this template use File | Settings | File Templates.
 */
@ServiceProvider(serviceName = "sampleService", port = 8888)
public interface SampleService {

    @MethodProvider(methodName = "say", serializeType = SerializeType.fastjson, compressType = CompressType.gizp)
    String say(String str);

    @MethodProvider(methodName = "echo")
    String echo(String msg);

}

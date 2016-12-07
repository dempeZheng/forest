package quickstart.api;

import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.MethodProvider;
import com.dempe.forest.core.annotation.ServiceProvider;

/**
 * Created by Dempe on 2016/12/7.
 */
//@Component
@ServiceProvider(serviceName = "sampleService", port = 8888)
public interface SampleService {


    @MethodProvider(methodName = "say")
    String say(String str);

    @MethodProvider(methodName = "say2")
    String say2(String str);

    @MethodProvider(methodName = "echo")
    String echo(String msg);

    @MethodProvider(methodName = "hi", serializeType = SerializeType.fastjson, compressType = CompressType.gizp)
    String hi(String msg);


}

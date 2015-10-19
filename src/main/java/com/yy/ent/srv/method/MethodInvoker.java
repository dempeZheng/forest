package com.yy.ent.srv.method;

import com.yy.ent.srv.protocol.YYProto;
import com.yy.ent.srv.util.Validate;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/15
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class MethodInvoker {

    private static final Logger log = LoggerFactory.getLogger(MethodInvoker.class);

    private MethodMapping methodMapping;

    public MethodInvoker(MethodMapping methodMapping) {
        if (methodMapping == null) {
            throw new IllegalArgumentException("methodMapping can not be null");
        }
        Validate.notNull(methodMapping);
        this.methodMapping = methodMapping;
    }

    public Object invoke(YYProto yyProto) {
        if (yyProto == null) {
            log.error("MessageAction message can not be null");
            return null;
        }
        ActionMethod actionMethod = methodMapping.getAction(yyProto.getUri());
        if (actionMethod == null) {
            log.warn("No process method for Message: {} registered, message will be omitted", yyProto);
        } else {
            try {
                long beginTime = System.currentTimeMillis();
                Object result = actionMethod.call(yyProto);
                if (log.isDebugEnabled()) {
                    long endTime = System.currentTimeMillis();
                    log.debug("The actionMethod: {} call elapsed time is: {}", actionMethod, endTime - beginTime);
                }
                if (result instanceof YYProto) {
                    log.info("Retrun the result message: {}", result);
                    return result;
                } else {
                    log.debug("MessageAction method: {} return value: {} is not message instance", actionMethod, result);
                }
            } catch (Exception e) {
                log.error("Error occurs process message: {}", yyProto, e);
            }
        }
        return null;
    }

    public void invoke(Channel channel, YYProto yyProto) {
        if (channel == null || yyProto == null) {
            log.error("MessageAction channel: {} or message: {} can not be null", channel, yyProto);
            return;
        }
        ActionMethod actionMethod = methodMapping.getAction(yyProto.getUri());
        if (actionMethod == null) {
            log.warn("No process method for Message: {} registered, message will be omitted", yyProto);
            return;
        }
        try {
            long beginTime = System.currentTimeMillis();
            Object result = actionMethod.call(yyProto);
            if (log.isDebugEnabled()) {
                long endTime = System.currentTimeMillis();
                log.debug("The channel actionMethod: {} invoke elapsed time is: {}", actionMethod, endTime - beginTime);
            }
            // 消息使用原路返回
            if (result instanceof YYProto) {
                log.info("Send the message: {} to the channel: {}", result, channel);
                channel.write(result);
            } else if (result instanceof YYProto[]) {
                YYProto[] msgs = (YYProto[]) result;
                for (YYProto msg : msgs) {
                    if (msg == null) {
                        continue;
                    }
                    log.info("Send the message: {} to the channel: {}", msg, channel);
                    channel.write(msg);
                }
            } else {
                log.debug("MessageAction method: {} return value: {} is not message instance", actionMethod, result);
            }
        } catch (Exception e) {
            log.error("Error occurs process message: {}", yyProto, e);
        }
    }
}

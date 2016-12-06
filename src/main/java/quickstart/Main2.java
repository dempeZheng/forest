package quickstart;

import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/5
 * Time: 18:22
 * To change this template use File | Settings | File Templates.
 */
public class Main2 {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("116.31.122.26");
        jedis.set("hello", "1");
        String hello = jedis.get("hello");
        System.out.println(hello);
    }
}

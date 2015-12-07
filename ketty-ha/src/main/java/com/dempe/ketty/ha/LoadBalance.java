package com.dempe.ketty.ha;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/23
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */
public class LoadBalance {

    private int i = 0;

    private int cw = 0;

    private int[] weight;

    private int count;

    /**
     * 构造方法
     *
     * @param count 总数
     */
    public LoadBalance(int count) {
        this.count = count;
    }

    /**
     * 构造方法-加权轮询
     *
     * @param weight 权重数组
     * @throws Exception
     */
    public LoadBalance(int[] weight) {
        this.count = weight.length;
        this.weight = weight;
    }

    /**
     * 哈希index
     *
     * @param key
     * @return
     */
    public int hashIndex(String key) {
        long hash = 5381;
        for (int i = 0; i < key.length(); i++) {
            hash = ((hash << 5) + hash) + key.charAt(i);
            hash = hash & 0xFFFFFFFFl;
        }

        int index = (int) hash % this.count;
        index = Math.abs(index);

        return index;
    }

    /**
     * 加权轮询index，轮询算法不考虑多线程
     *
     * @return
     */
    public int roundIndexByWeight() {
        while (true) {
            this.i = (this.i + 1) % this.count;
            if (this.i == 0) {
                this.cw = this.cw - gcd();
                if (this.cw <= 0) {
                    this.cw = max();
                    if (this.cw == 0)
                        return 0;
                }
            }
            if (this.weight[this.i] >= this.cw)
                return this.i;
        }
    }

    /**
     * 轮询index，轮询算法不考虑多线程
     *
     * @return
     */
    public int roundIndex() {
        int j = this.i;
        j = (j + 1) % this.count;
        this.i = j;
        return this.i;
    }


    /**
     * 求最大公约数
     *
     * @return
     */
    private int gcd() {
        BigInteger value = null;
        if (this.weight.length > 0) {
            value = BigInteger.valueOf(this.weight[i]);
        }
        for (int i = 0; i < this.weight.length - 1; i++) {
            BigInteger tmp = BigInteger.valueOf(this.weight[i]);
            tmp = tmp.gcd(BigInteger.valueOf(this.weight[i + 1]));
//			System.out.println("tmp:"+tmp+ " value:"+value);
            if (value.compareTo(tmp) > 0) {
                value = tmp;
            }
        }
        if (null != value) {
            return value.intValue();
        }
        // TODO 为空返回多少
        return 0;
    }

    /**
     * 最大值
     *
     * @return
     */
    private int max() {
        int value = 0;
        if (this.weight.length > 0) {
            value = this.weight[0];
        }
        for (int i = 0; i < this.weight.length - 1; i++) {
            int tmp = this.weight[i];
            if (value < tmp) {
                value = tmp;
            }
        }
        return value;
    }

}


package com.yy.ent;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        int arr2[] = {1, -2, 3, 10, -4, 7, 2, -5};
        int arr[] = {-2, -1,-3};
        int max = arr[0];
        int preMax = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (max < 0) {
              max =0;
            }
            max = max + arr[i];
            if (max > preMax) {
                preMax = max;
            }
        }
        System.out.println(preMax);
    }


}

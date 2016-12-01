package com.dempe.forest.example;

import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/1
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SampleService {

    public String hello(String word) {
        return "hello>>>" + word;
    }
}

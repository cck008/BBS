package com.easybbs.controller;

import cn.hutool.core.util.RandomUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @RequestMapping("/test")
    public String test(){
        int random = getRandom(1, 100);
        return String.valueOf(random);
    }


    /**
     * 获取一个随机数
     * @param min 最小区间
     * @param max 最大区间
     * @return
     */
    public int getRandom(int min, int max){
        int i = RandomUtil.randomInt(min,max);
        return i;
    }
}

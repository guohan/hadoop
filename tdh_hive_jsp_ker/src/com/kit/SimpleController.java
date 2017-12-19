package com.kit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by guohan on 2016/12/5
 * 
 * http://localhost:8080/hello
 */
@Controller
@EnableAutoConfiguration
public class SimpleController {
 
    @RequestMapping(value ="/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(){
        return "hello world,this is a spring boot test project!";
    }
 
//    public static void main(String[] args) {
//       
//    }
    public static void main(String[] args)
	{
    	 SpringApplication.run(SimpleController.class, args);
	}
}
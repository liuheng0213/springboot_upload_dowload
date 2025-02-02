package com.stephen.controller;


import com.google.gson.Gson;
import com.stephen.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.*;

import java.net.URLEncoder;
import java.util.List;

@RequestMapping("/index")
@RestController
@ResponseBody
public class IndexController {
    private Gson gson = new Gson();
    @RequestMapping("/{age}")
    public void retrieveUser(@PathVariable("age") String age, @RequestParam(value = "id",required = false) String id, HttpServletResponse response) throws IOException {
        User user = new User();
        user.setAge(Integer.valueOf(age));
        user.setId(Long.valueOf(id));
        response.getWriter().append(gson.toJson(user));
        response.setStatus(200);
    }



    //input a list
    @RequestMapping("/{age}") //不指定method= xxx，则任何method都可以使用
    public List<User> retrieveUserList(@RequestBody List<User> users, HttpServletResponse response) throws IOException {
        return users;
    }
}

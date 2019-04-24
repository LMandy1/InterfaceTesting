package com.contact;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    Department department;
    String random=String.valueOf(System.currentTimeMillis());
    @BeforeEach
    void setUp() {
        if(department==null){
            department=new Department();
            department.deleteAll();
        }
    }

    @Test
    void list() {
        department.list("").then().statusCode(200);
        department.list("33").then().statusCode(200);
    }

    @Test
    void create() {
        department.create("department"+random, "1").then().body("errcode", equalTo(0));
    }

    @Test
    void createByMap(){
        HashMap<String, Object> map=new HashMap<String, Object>(){{
            put("name", String.format("testhome_d1_map%s", random));
            put("parentid", "1");
        }
        };
        department.create(map).then().body("errcode", equalTo(0));
    }

    @Test
    void createWithChinese() {
        department.create("department"+random, "1").then().body("errcode", equalTo(0));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data/createWithDup.csv")
    void createWithDup(String name, Integer expectCode){
        department.create(name+random, "1").then().body("errcode", equalTo(0));
        department.create(name+random, "1").then().body("errcode", equalTo(expectCode));

    }

    @Test
    void delete() {
        String nameOld="testhome_d1"+random;
        department.create(nameOld, "1");
        Integer idInt=department.list("").path("department.find{ it.name=='"+ nameOld +"' }.id");
        System.out.println(idInt);
        String id=String.valueOf(idInt);
        department.delete(id).then().body("errcode", equalTo(0));
    }


    @Test
    void update() {
        String nameOld="testhome_d1"+random;
        department.create(nameOld, "1");
        Integer idInt=department.list("").path("department.find{ it.name=='"+ nameOld +"' }.id");
        System.out.println(idInt);
        String id=String.valueOf(idInt);
        department.update("testhome_d2"+random,  id).then().body("errcode", equalTo(0));
    }

    @Test
    void updateAll(){
        //todo:
        HashMap<String, Object> map=new HashMap<>();
        department.api("api.json", map).then().statusCode(200);
    }
}
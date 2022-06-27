package com.triple.travelers.event;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyBatisTest {
    //SqlSessionFactory 객체를 자동으로 생성
    @Autowired
    private SqlSessionFactory sqlFactory;

    //SqlSessionFactory 객체가 제대로 만들어졌는지 Test
    @Test
    public void testFactory() {
        System.out.println(sqlFactory);
    }

    //MyBatis와 Mysql 서버가 제대로 연결되었는지 Test
    @Test
    public void testSession() throws Exception {
        try (SqlSession session = sqlFactory.openSession()) {
            System.out.println(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

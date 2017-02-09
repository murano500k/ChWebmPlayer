package com.stc.chviewer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by artem on 2/9/17.
 */
@RunWith(JUnit4.class)
public class UnitTest {


    @Test
    public void testData(){
        String webmUrl= "https://2ch.hk/b/src/146358725/14866409430490.jpg";
        String temp=webmUrl.substring(webmUrl.indexOf("src/")+4);
        System.out.println("testDataStructure: "+ temp.substring(0, temp.indexOf("/")));
    }


}

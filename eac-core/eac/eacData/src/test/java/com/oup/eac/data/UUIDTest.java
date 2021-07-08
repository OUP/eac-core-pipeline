package com.oup.eac.data;

import java.util.UUID;

import org.junit.Test;

public class UUIDTest {
    
    @Test
    public void generateId() {
       System.out.println(UUID.randomUUID().toString()); 
    }

}

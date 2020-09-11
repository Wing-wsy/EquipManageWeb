package com.equipmgr.annet.commen;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class test {
	public static void main(String[] args) {
        ArrayList<String> handoverList = new ArrayList<String>();
        handoverList.add("1Z");
        handoverList.add("1W");
        handoverList.add("2Z");
        handoverList.add("2W");
        System.out.println(handoverList);
        
        for(int i=0;i<handoverList.size();i++) {
        	JsonObject jsonObject = new JsonObject();
        	String ho = handoverList.get(i);
        	JsonArray QList = new JsonArray();
        	}
	}

}

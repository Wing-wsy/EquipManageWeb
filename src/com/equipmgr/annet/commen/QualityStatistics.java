package com.equipmgr.annet.commen;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class QualityStatistics {
	
	public static ArrayList<String> DateStatistics(String DateType,String startDate,String endDate) throws ParseException{
		ArrayList<String> result = new ArrayList<String>();
    	if("4".equals(DateType)) {
    		String start = startDate.substring(0,4);
    		int startInt = Integer.parseInt(start);
    		String end = endDate.substring(0,4);
    		int endInt = Integer.parseInt(end);
    		for(int i=startInt;i<=endInt;i++) {
    			int dateInt = i;
    			result.add(String.valueOf(dateInt));
    		}
    	}else if("7".equals(DateType)) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// ��ʽ��Ϊ����
    		Calendar min = Calendar.getInstance();
    		Calendar max = Calendar.getInstance();
     
    		min.setTime(sdf.parse(startDate));
    		min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
     
    		max.setTime(sdf.parse(endDate));
    		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
     
    		Calendar curr = min;
    		while (curr.before(max)) {
    			result.add(sdf.format(curr.getTime()));
    			curr.add(Calendar.MONTH, 1);
    		}
    	}else if("10".equals(DateType)) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// ��ʽ��Ϊ������
       		Calendar min = Calendar.getInstance();
    		Calendar max = Calendar.getInstance();
     
    		min.setTime(sdf.parse(startDate));
    		min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), min.get(Calendar.DATE));
     
    		max.setTime(sdf.parse(endDate));
    		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), max.get(Calendar.DATE) + 1);
     
    		Calendar curr = min;
    		while (curr.before(max)) {
    			result.add(sdf.format(curr.getTime()));
    			curr.add(Calendar.DATE, 1);
    		}
    		
    	}
		return result;
	}
	
	public static JsonArray resultStatistics(ArrayList<String> result,List<String> UserList,Map<String,String> map,Map<String,String> map1,String Grade){
		JsonArray returnList = new JsonArray();
        JsonObject jsonDate = new JsonObject();
        JsonArray dateList = new JsonArray();
        for(int i=0;i<result.size();i++) {
        	dateList.add(result.get(i));
        }
        jsonDate.add("time",dateList);
        returnList.add(jsonDate);
        
        for(int i=0;i<UserList.size();i++) {
        	JsonObject jsonObject = new JsonObject();
        	String User = UserList.get(i);
        	JsonArray QList = new JsonArray();
        	for(int j=0;j<result.size();j++) {
        		String key = User+result.get(j);
    			String mapi = map.get(key);
        		if(mapi!=null) {
        			String mapz = map1.get(key);
        			String percentage = "";
        			if(mapz!=null) {
        				int num1 = Integer.parseInt(mapi);
        	            int num2 = Integer.parseInt(mapz);
        	            NumberFormat numberFormat = NumberFormat.getInstance(); 
        	            numberFormat.setMaximumFractionDigits(2); 
        	            percentage = numberFormat.format((float) num1 / (float) num2 * 100);
        			}
        			QList.add(percentage);
        		}else {
        			if("A".equals(Grade)) {
        				QList.add("100");
        			}else {
        				QList.add("0");
        			}
        		}
        	}
        	jsonObject.add(User,QList);
        	returnList.add(jsonObject);
        }
		return returnList;
	}
	
	public static JsonArray resultReviewDoctor(List<String> UserList,Map<String,String> map){
		//[{time: ["放射科", "毛爱弟", "徐敏涛"]}, {数据: ["8", "8", "2"]}]
		JsonArray returnList = new JsonArray();
        
		JsonArray userList = new JsonArray();
		JsonArray numList = new JsonArray();
		 for(int i=0;i<UserList.size();i++) {
			 String User = UserList.get(i);
			 if(User!=null){
				 userList.add(User);
			 }
			 String number = map.get(User);
			 if(number!=null){
				 numList.add(number);
			 }else{
				 numList.add("0");
			 }
		 }
		 JsonObject jo1 = new JsonObject();
		 JsonObject jo2 = new JsonObject();
		 jo1.add("time", userList);
		 jo2.add("数据", numList);
		 returnList.add(jo1);
		 returnList.add(jo2);
		 
		return returnList;
	}
	
	public static JsonArray resultStatisticsNotName(Map<String,String> map,Map<String,String> map1,String Grade){
		JsonArray returnList = new JsonArray();
        JsonObject jsonDate = new JsonObject();
        JsonArray dateList = new JsonArray();
        returnList.add(jsonDate);
        
        JsonArray QList = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        Map<String ,Float> dataMap = new HashMap<>();
    	for(String key:map.keySet()) {
    		String mapi = map.get(key);
    		String mapz = map1.get(key);
    		String percentage = "";
			if(mapz!=null) {
				if(mapz!=null) {
    				int num1 = Integer.parseInt(mapi);
    	            int num2 = Integer.parseInt(mapz);
    	            NumberFormat numberFormat = NumberFormat.getInstance(); 
    	            numberFormat.setMaximumFractionDigits(2); 
    	            percentage = numberFormat.format((float) num1 / (float) num2 * 100.00);
    			}
				float percentage1 = Float.parseFloat(percentage);
				dataMap.put(key,percentage1);
			}
    	}
    	
    	List<Map.Entry<String,Float>> list = new ArrayList<Map.Entry<String,Float>>(dataMap.entrySet());
    	 Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
             @Override
             public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                 int compare = (o1.getValue()).compareTo(o2.getValue());
                 return -compare;
             }
         });
  
         Map<String, Float> returnMap = new LinkedHashMap<String, Float>();
         for (Map.Entry<String, Float> entry : list) {
             returnMap.put(entry.getKey(), entry.getValue());
         }
    	for(String key:returnMap.keySet()) {
    		dateList.add(key);
    		String val = returnMap.get(key).toString();
    		if(val.length()>4 && "100.".equals(val.substring(0,4))) {
    			QList.add("100");
    		}else {
    			QList.add(val);
    		}
    	}
        
    	jsonDate.add("time",dateList);
		jsonObject.add("数据",QList);
    	returnList.add(jsonObject);
		return returnList;
	}
	
	public static JsonArray resultShiftTechs(ArrayList<String> result,Map<String,String> map){
		JsonArray returnList = new JsonArray();
	    ArrayList<String> handoverList = new ArrayList<String>();
	    handoverList.add("handoverZ");
	    handoverList.add("takeoverZ");
	    handoverList.add("handoverW");
	    handoverList.add("takeoverW");
		for(int i=0;i<result.size();i++) {
			JsonObject jsonObject = new JsonObject();
	        JsonArray dateList = new JsonArray();
	        String date = result.get(i);
	        dateList.add(date);
	        jsonObject.add("time", dateList.get(0));
	        JsonArray QList = new JsonArray();
	        for(int j=0;j<handoverList.size();j++) {
	        	 
	        	 String key = handoverList.get(j)+date;
	    	     String mapi = map.get(key);
	    	     JsonArray contentList = new JsonArray();
	    	     JsonObject json = new JsonObject();
	    	    
        	    if(mapi!=null) {
        	    	contentList.add(mapi);
     	    	}else {
     	    		contentList.add(" ");
     		    }
        	    json.add(handoverList.get(j), contentList.get(0));
 			    QList.add(json);
        	    
	        }
	        jsonObject.add("data", QList);
	        returnList.add(jsonObject);
		}
		
		return returnList;
//		JsonArray returnList = new JsonArray();
//        JsonObject jsonDate = new JsonObject();
//        JsonArray dateList = new JsonArray();
//        for(int i=0;i<result.size();i++) {
//        	dateList.add(result.get(i));
//        }
//        jsonDate.add("time",dateList);
//        returnList.add(jsonDate);
//        
//        ArrayList<String> handoverList = new ArrayList<String>();
//        handoverList.add("1Z");
//        handoverList.add("1W");
//        handoverList.add("2Z");
//        handoverList.add("2W");
//        
//        for(int a=0;a<handoverList.size();a++) {
//        	JsonObject jsonObject = new JsonObject();
//        	String ho = handoverList.get(a);
//        	JsonArray QList = new JsonArray();
//        	
//        	for(int j=0;j<result.size();j++) {
//        		String key = ho+result.get(j);
//    			String mapi = map.get(key);
//           		if(mapi!=null) {
//        			QList.add(mapi);
//        		}else {
//        			QList.add(" ");
//        		}
//            }
//        	jsonObject.add(ho,QList);
//        	returnList.add(jsonObject);
//        }
//		
//		return returnList;

	}
	public static JsonArray resultShiftDoctors(ArrayList<String> result,Map<String,String> map){
		JsonArray returnList = new JsonArray();
	    ArrayList<String> handoverList = new ArrayList<String>();
	    handoverList.add("handoverZ");
	    handoverList.add("takeoverZ");
	    handoverList.add("handoverW");
	    handoverList.add("takeoverW");
		for(int i=0;i<result.size();i++) {
			JsonObject jsonObject = new JsonObject();
	        JsonArray dateList = new JsonArray();
	        String date = result.get(i);
	        dateList.add(date);
	        jsonObject.add("time", dateList.get(0));
	        JsonArray QList = new JsonArray();
	        for(int j=0;j<handoverList.size();j++) {
	        	 
	        	 String key = handoverList.get(j)+date;
	    	     String mapi = map.get(key);
	    	     JsonArray contentList = new JsonArray();
	    	     JsonObject json = new JsonObject();
	    	    
        	    if(mapi!=null) {
        	    	contentList.add(mapi);
     	    	}else {
     	    		contentList.add(" ");
     		    }
        	    json.add(handoverList.get(j), contentList.get(0));
 			    QList.add(json);
        	    
	        }
	        jsonObject.add("data", QList);
	        returnList.add(jsonObject);
		}
		
		return returnList;
	}
	
	public static JsonArray resultCanonical(ArrayList<String> result,Map<String,String> map,String name){
		
		JsonArray returnList = new JsonArray();
        JsonObject jsonDate = new JsonObject();
        JsonObject jsonObject = new JsonObject();
        
        JsonArray dateList = new JsonArray();
        for(int i=0;i<result.size();i++) {
        	dateList.add(result.get(i));
        }
        jsonDate.add("time",dateList);
        returnList.add(jsonDate);
        
        JsonArray QList = new JsonArray();
    	for(int i=0;i<result.size();i++) {
    		String key = result.get(i);
			String mapi = map.get(key);
    		if(mapi!=null){ 
    			float num = (Float.parseFloat(mapi));
    			QList.add(num);
    		}else {
    			QList.add("0");
    		}
    	}
    	jsonObject.add(name,QList);
    	returnList.add(jsonObject);
        
		return returnList;
	}
	
	
}

package com.gametranslator;
import java.util.HashMap;
import java.util.Locale;
public final class Dictionary {
 private static final HashMap<String,String> lines=new HashMap<>();
 static {
 lines.put("we need to get out of here!","لازم نطلع من هنا!");
 lines.put("follow me","اتبعني");
 lines.put("get in the car","اركب السيارة");
 lines.put("mission passed","اكتملت المهمة");
 lines.put("mission failed","فشلت المهمة");
 lines.put("wait here","انتظر هنا");
 }
 public static String translate(String source){
 String key=source.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+"," ");
 String found=lines.get(key);
 return found!=null?found:"[غير موجود بالقاموس] "+source;
 }
}
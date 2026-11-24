package com.gametranslator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.net.Uri;
import android.content.Context;
import android.media.projection.MediaProjectionManager;
import android.view.View;
import android.widget.*;
public class MainActivity extends Activity {
 private EditText english;
 private TextView status;
 private SeekBar sizeBar;
 private CheckBox yellow;
 @Override public void onCreate(Bundle state){super.onCreate(state);buildUi();}
 private TextView label(String text,int size){TextView v=new TextView(this);v.setText(text);v.setTextColor(Color.WHITE);v.setTextSize(size);v.setPadding(5,8,5,8);return v;}
 private Button button(String text,Runnable task){Button b=new Button(this);b.setText(text);b.setAllCaps(false);b.setOnClickListener(v->task.run());return b;}
 private void buildUi(){
 ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);scroll.setBackgroundColor(0xFF14212C);
 LinearLayout main=new LinearLayout(this);main.setPadding(24,15,24,24);main.setOrientation(LinearLayout.VERTICAL);
 main.addView(label("GAME TRANSLATOR  |  مترجم الألعاب",26));
 main.addView(label("فحص تطابق HDMI وعرض ترجمة عربية على الداتا شو — نسخة تجريبية V0.1",16));
 main.addView(label("مهم: ظهور إعدادات الجهاز فوق HDMI لا يعني السماح للتطبيقات بقراءة محتوى HDMI. هذا الإصدار لا يدّعي ترجمة اللعب تلقائياً.",15));
 status=label("الحالة: لم يبدأ اختبار العرض",16);main.addView(status);
 main.addView(button("1) السماح بالظهور فوق التطبيقات",()->{
 Intent i=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:"+getPackageName()));
 try{startActivity(i);}catch(Exception e){status.setText("تعذر فتح شاشة الصلاحيات: "+e.getMessage());}
 }));
 main.addView(button("2) تشغيل الترجمة التجريبية العائمة",()->sendOverlay("اختبار الترجمة العربية فوق اللعبة")));
 main.addView(button("3) إيقاف الترجمة العائمة",()->{Intent i=new Intent(this,OverlayService.class);i.setAction(OverlayService.ACTION_STOP);startService(i);status.setText("تم طلب إيقاف الترجمة");}));
 main.addView(label("جرّب تشغيل العبارة العائمة ثم بدّل إلى مدخل HDMI. إذا اختفت عند فتح HDMI، فقد يكون مدخل HDMI طبقة منفصلة لا يمكن لتطبيق عادي التراكب عليها.",14));
 english=new EditText(this);english.setSingleLine(false);english.setMinLines(2);english.setText("We need to get out of here!");english.setHint("اكتب جملة إنكليزية للاختبار");english.setTextColor(Color.WHITE);english.setHintTextColor(0xFFB7C5CE);main.addView(english);
 main.addView(button("عرض ترجمة تجريبية من القاموس",()->{
 String translated=Dictionary.translate(english.getText().toString());sendOverlay(translated);
 if(translated.startsWith("["))status.setText("الجملة غير موجودة بالقاموس المحلي، هذا الإصدار لا يحتوي على محرك ترجمة تلقائي.");
 }));
 yellow=new CheckBox(this);yellow.setText("لون أصفر للترجمة");yellow.setTextColor(Color.WHITE);main.addView(yellow);
 main.addView(label("حجم الخط",16));sizeBar=new SeekBar(this);sizeBar.setMax(26);sizeBar.setProgress(11);main.addView(sizeBar);
 main.addView(button("4) فحص قدرة التقاط الشاشة العامة",()->{
 MediaProjectionManager m=(MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
 try{startActivityForResult(m.createScreenCaptureIntent(),42);status.setText("تم طلب صلاحية Android MediaProjection. الموافقة لا تثبت التقاط HDMI؛ ربما يلتقط واجهة أندرويد فقط.");}
 catch(Exception e){status.setText("التقاط الشاشة غير متاح: "+e.getMessage());}
 }));
 main.addView(label("ملاحظة فنية: لا يوجد وصول عام مضمون إلى إطارات مدخل HDMI في Android. يحتاج التحقق من واجهات الشركة المصنّعة أو أدوات خارجية عند عدم توفر القراءة.",14));
 scroll.addView(main);setContentView(scroll);
 }
 private void sendOverlay(String text){
 if(!Settings.canDrawOverlays(this)){status.setText("فعّل صلاحية الظهور أولاً");return;}
 Intent i=new Intent(this,OverlayService.class);i.setAction(OverlayService.ACTION_TEXT);
 i.putExtra("text",text);i.putExtra("size",14f+sizeBar.getProgress());i.putExtra("yellow",yellow.isChecked());
 try{startService(i);status.setText("أُرسل النص إلى الطبقة العائمة. افحص ظهوره فوق مدخل HDMI.");}
 catch(Exception e){status.setText("فشل بدء العرض: "+e.getMessage());}
 }
 @Override protected void onActivityResult(int requestCode,int resultCode,Intent data){
 super.onActivityResult(requestCode,resultCode,data);
 if(requestCode==42)status.setText(resultCode==RESULT_OK?
 "وافق النظام على إذن التقاط شاشة Android، لكن صورة HDMI لم تُختبر أو تُقرأ بعد.":
 "رفض النظام أو لم يسمح بالتقاط الشاشة. هذا لا يثبت وجود وصول إلى HDMI.");
 }
}
package com.gametranslator;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;
public class OverlayService extends Service {
 public static final String ACTION_TEXT="com.gametranslator.TEXT";
 public static final String ACTION_STOP="com.gametranslator.STOP";
 private WindowManager manager;
 private TextView overlay;
 @Override public int onStartCommand(Intent intent,int flags,int startId){
  if(intent!=null&&ACTION_STOP.equals(intent.getAction())) {stopSelf();return START_NOT_STICKY;}
  if(overlay==null)showOverlay();
  if(intent!=null&&intent.hasExtra("text")&&overlay!=null){
    overlay.setText(intent.getStringExtra("text"));
    overlay.setTextSize(intent.getFloatExtra("size",25f));
    overlay.setTextColor(intent.getBooleanExtra("yellow",false)?Color.YELLOW:Color.WHITE);
  }
  return START_STICKY;
 }
 private void showOverlay(){
  manager=(WindowManager)getSystemService(WINDOW_SERVICE);
  overlay=new TextView(this);overlay.setText("اختبار عرض الترجمة فوق HDMI");
  overlay.setTextColor(Color.WHITE);overlay.setTextSize(25);overlay.setTypeface(null,Typeface.BOLD);
  overlay.setGravity(Gravity.CENTER);overlay.setPadding(18,8,18,8);
  GradientDrawable bg=new GradientDrawable();bg.setColor(0xC9000000);bg.setCornerRadius(15);overlay.setBackground(bg);
  int type=Build.VERSION.SDK_INT>=26?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_PHONE;
  WindowManager.LayoutParams p=new WindowManager.LayoutParams(-1,-2,type,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,PixelFormat.TRANSLUCENT);
  p.gravity=Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;p.y=72;
  try{manager.addView(overlay,p);}catch(Exception e){overlay=null;}
 }
 @Override public void onDestroy(){if(overlay!=null&&manager!=null){manager.removeView(overlay);overlay=null;}super.onDestroy();}
 @Override public IBinder onBind(Intent intent){return null;}
}
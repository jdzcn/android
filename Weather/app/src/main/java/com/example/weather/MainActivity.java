package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String cityid,city,update_time,wea,wea_img,tem,tem_day,tem_night,win,win_speed,win_meter,air;
    TextView tvcity,tvtime,tvwea,tvtem,tvtemday,tvtem_night,tvwin,tvwin_speed,tvwin_meter,tvair;
    ImageView imageView;
    String CHANNEL_ID="7831";
    int notificationId=7831;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        tvcity=(TextView) findViewById(R.id.tvcity);
        tvtime=(TextView) findViewById(R.id.tvtime);
        tvwea=(TextView) findViewById(R.id.tvwea);
        tvtem=(TextView) findViewById(R.id.tvtem);
        tvtemday=(TextView) findViewById(R.id.tvtemday);
        tvwin=(TextView) findViewById(R.id.tvwin);
        tvwin_meter=(TextView) findViewById(R.id.tvwin_meter);
        tvwin_speed=(TextView) findViewById(R.id.tvwin_speed);
        tvair=(TextView) findViewById(R.id.tvair);
        imageView=(ImageView)findViewById(R.id.imgwea) ;
        Button button=(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestWithHttpURLConnection();
            }
        });
        sendRequestWithHttpURLConnection();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void sendRequestWithHttpURLConnection() {
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.yiketianqi.com/free/day?appid=41633843&appsecret=If8zSUqK&unescape=1");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    parseJSONWithJSONObject(response.toString());
                    showResponse(response.toString());
                    //mynotify();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public void notifiy(View v) {
        mynotify();
    }

    public void mynotify() {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.qing)
                .setContentTitle(city+"天气")
                .setContentText(wea+" "+tem+"℃ "+tem_night+"-"+tem_day+"℃ "+win+win_speed+" "+" 空气质量:"+air)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                //responseText.setText(response);
                tvcity.setText(city);
                tvtime.setText(update_time);
                tvwea.setText(wea);
                tvtem.setText(tem+"℃");
                tvtemday.setText(tem_night+"-"+tem_day+"℃");

                tvwin.setText(win);
                tvwin_meter.setText(win_meter);
                tvwin_speed.setText(win_speed);
                tvair.setText(air);


                switch (wea_img) {
                    case "yu":
                        imageView.setImageResource(R.drawable.yu);
                        break;
                    case "qing":
                        imageView.setImageResource(R.drawable.qing);
                        break;
                    case "xue":
                        imageView.setImageResource(R.drawable.xue);
                        break;
                    case "lei":
                        imageView.setImageResource(R.drawable.lei);
                        break;
                    case "shachen":
                        imageView.setImageResource(R.drawable.shachen);
                        break;
                    case "wu":
                        imageView.setImageResource(R.drawable.wu);
                        break;
                    case "bingbao":
                        imageView.setImageResource(R.drawable.bingbao);
                        break;
                    case "yun":
                        imageView.setImageResource(R.drawable.yun);
                        break;
                    case "yin":
                        imageView.setImageResource(R.drawable.yin);
                        break;
                    default:
                        break;
                }
                
            }
        });
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            cityid = jsonObject.getString("cityid");
            city = jsonObject.getString("city");
            update_time = jsonObject.getString("update_time");
            wea = jsonObject.getString("wea");
            wea_img = jsonObject.getString("wea_img");
            tem = jsonObject.getString("tem");
            win = jsonObject.getString("win");
            air = jsonObject.getString("air");
            win_meter = jsonObject.getString("win_meter");
            win_speed = jsonObject.getString("win_speed");
            tem_night = jsonObject.getString("tem_night");
            tem_day = jsonObject.getString("tem_day");

            Log.d("MainActivity", "cityid is " + cityid);
            Log.d("MainActivity", "city is " + city);
            Log.d("MainActivity", "update_time is " + update_time);
            Log.d("MainActivity", "wea is " + wea);
            Log.d("MainActivity", "wea_img is " + wea_img);
            Log.d("MainActivity", "tem is " + tem);
            Log.d("MainActivity", "tem_day is " + tem_day);
            Log.d("MainActivity", "tem_night is " + tem_night);
            Log.d("MainActivity", "win is " + win);
            Log.d("MainActivity", "win_speed is " + win_speed);
            Log.d("MainActivity", "win_meter is " + win_meter);
            Log.d("MainActivity", "air is " + air);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
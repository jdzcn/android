package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

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
    TextView tvcity,tvtime,tvwea,tvtem,tvtem_day,tvtem_night,tvwin,tvwin_speed,tvwin_meter,tvair;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvcity=(TextView) findViewById(R.id.tvcity);
        tvtime=(TextView) findViewById(R.id.tvtime);
        tvwea=(TextView) findViewById(R.id.tvwea);
        tvtem=(TextView) findViewById(R.id.tvtem);
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

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                //responseText.setText(response);
                tvcity.setText(city);
                tvtime.setText(update_time);
                tvwea.setText(wea);
                tvtem.setText(tem);
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
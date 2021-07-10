package com.daesin.school;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;
    private Boolean main = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        /**메인 화면**/
        findViewById(R.id.navi_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
                drawerLayout.closeDrawers();
            }
        });
        /**공지 화면**/
        findViewById(R.id.navi_notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new NoticeFragment()).commit();
                drawerLayout.closeDrawers();
            }
        });
        /**가정통신문 화면**/
        findViewById(R.id.navi_letter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new LetterFragment()).commit();
                drawerLayout.closeDrawers();
            }
        });

        /**학사일정 화면**/
        findViewById(R.id.navi_schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new ScheduleFragment()).commit();
                drawerLayout.closeDrawers();
            }
        });

        /** 학교 앨범 화면 **/
        findViewById(R.id.navi_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new PhotoFragment()).commit();
                drawerLayout.closeDrawers();
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();

        TextView drawerDate = findViewById(R.id.drawer_dateText);
        Date currentTime = Calendar.getInstance().getTime();
        String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 ( EE )", Locale.getDefault()).format(currentTime);
        drawerDate.setText(date_text);
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    public void openUrl(String url) {
        if (url.equals("no")) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        System.out.println("MainActivity : openUrl(), url = " + url);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alarm:
                startActivity(new Intent(getApplicationContext(), NoticeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private long time= 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            if (!main) {
                time = 2000;
                main = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
            } else {
                Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (System.currentTimeMillis() - time < 2000) {
            finish();
        }
    }
}
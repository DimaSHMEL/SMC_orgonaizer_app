package com.example.smc_orgonaizer_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private FragmentContainerView currentPage;
    private RadioGroup menuBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Прячем бар
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //Получем радиогруппу и фрагмент
        currentPage = findViewById(R.id.fragmentContainerView);
        menuBar = findViewById(R.id.radioGroup);
        //Создаём интенты страниц
        Fragment schedulePage = Schedule.newInstance("none", "none");
        Fragment profilePage = Profile.newInstance("none", "none");
        Fragment mapPage = MapPage.newInstance("none", "none");
        Fragment newJobsPage = NewJobPage.newInstance("none", "none");
        Fragment infoPage = InfoPage.newInstance("none", "none");
        //Получем управление фрагментами
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        //Задаём первой страницей расписание
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView, schedulePage).commit();
        //Обработка группы чтобы менять фрагменты
        menuBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.bar_btn_profile:
                        //Смена на профиль заменой
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, profilePage)
                                .commit();
                        break;
                    case R.id.bar_btn_schedule:
                        //Смена на  расписание заменой
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, schedulePage)
                                .commit();
                        break;
                    case R.id.bar_btn_new_jobs:
                        //Смена на  расписание заменой
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, newJobsPage)
                                .commit();
                        break;
//                    case R.id.bar_btn_map:
//                        //Смена на  расписание заменой
//                        supportFragmentManager.beginTransaction()
//                                .replace(R.id.fragmentContainerView, mapPage)
//                                .commit();
//                        break;
//                    case R.id.bar_btn_info:
//                        //Смена на  расписание заменой
//                        supportFragmentManager.beginTransaction()
//                                .replace(R.id.fragmentContainerView, infoPage)
//                                .commit();
//                        break;
                    default:
                        break;
                }

            }
        });

    }


}
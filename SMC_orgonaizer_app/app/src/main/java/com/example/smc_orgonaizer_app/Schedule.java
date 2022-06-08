package com.example.smc_orgonaizer_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Schedule#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Schedule extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Schedule() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Schedule.
     */
    // TODO: Rename and change types and number of parameters
    public static Schedule newInstance(String param1, String param2) {
        Schedule fragment = new Schedule();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private LinearLayout scheduleScroll;
    private TextView mouth;
    private  Button peopleSelectorBtn;
    private Button typeSelectorBtn;
    private LinearLayout weeksRow;
    //Переменные для селектора
    private List<String> typeSelectors = new ArrayList<String>(Arrays.asList(new String[]{"видео", "фото", "дизайн", "текст", "все"}));
    private int typeSelectorState = 0;
    private String peopleSelector = "Шмелев";
    private boolean peopleSelectorState = false;
    //Переменные с базой данных
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase takenDb;
    private void changeDate(Calendar calendar)
    {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String date = df.format(calendar.getTime());
        mouth.setText(date);
    }
    public void onStart() {
        super.onStart();
        //Работа с базой данных
        databaseHelper = new DatabaseHelper(getContext());
        // создаем базу данных
        databaseHelper.create_db();
        //Заполнение страницы
        fillScroll(typeSelectors.get(0));

        //Добавление обработчика на кнопку селектора по типу
        typeSelectorBtn = getActivity().findViewById(R.id.schedule_typeSelectorChanger);
        typeSelectorBtn.setOnClickListener(new View.OnClickListener() {
            //Обработчик селектора
            @Override
            public void onClick(View v) {
                typeSelectorState += 1;
                typeSelectorState %= typeSelectors.size();
                typeSelectorBtn.setText(typeSelectors.get(typeSelectorState));
                switch (typeSelectorState)
                {
                    case 0:
                        typeSelectorBtn.setBackgroundColor(getResources().getColor(R.color.video_selector_color));
                        break;
                    case 1:
                        typeSelectorBtn.setBackgroundColor(getResources().getColor(R.color.photo_selector_color));
                        break;
                    case 2:
                        typeSelectorBtn.setBackgroundColor(getResources().getColor(R.color.text_selector_color));
                        break;
                    case 3:
                        typeSelectorBtn.setBackgroundColor(getResources().getColor(R.color.design_selector_color));
                        break;
                    case 4:
                        typeSelectorBtn.setBackgroundColor(getResources().getColor(R.color.all_selector_color));
                        break;
                    default:
                        break;
                }
                fillScroll(typeSelectors.get(typeSelectorState));

            }
        });
        //Добавление обработчика на кнопку по людям
        peopleSelectorBtn = getActivity().findViewById(R.id.schedule_people_selector_button);
        peopleSelectorBtn.setOnClickListener(new View.OnClickListener() {
            //Обработчик селектора
            @Override
            public void onClick(View v) {
                peopleSelectorState = !(peopleSelectorState);
                if(peopleSelectorState)
                {
                    peopleSelectorBtn.setText("все");
                }
                else
                {
                    peopleSelectorBtn.setText("личное");
                }
                fillScroll(typeSelectors.get(typeSelectorState));

            }
        });
        //Смена числа в месяце
        mouth = getActivity().findViewById(R.id.Schedule_Text_Month);
        changeDate(new GregorianCalendar());
        //Создание недели
        //fillWeeks(new GregorianCalendar());



    }
    private ArrayList<Button> createWeek(Calendar currentDate)
    {
        ArrayList<Button> week = new ArrayList<>();

        for(int i = 0; i < 7; i++)
        {
            Button button = new Button(getContext());
            button.setBackgroundColor(getResources().getColor(R.color.all_selector_color));
            button.setText(String.valueOf(currentDate.getFirstDayOfWeek()));
            button.setWidth(10);
            button.setGravity(1);
            week.add(button);
        }
        return week;
    }
    private void fillWeeks(Calendar currentDate)
    {
        //Заполнение недели
        weeksRow = getActivity().findViewById(R.id.Schedule_view_weeks);
        ArrayList<Button> week = createWeek(new GregorianCalendar());
        for(int i = 0; i < week.size(); i++)
        {
            weeksRow.addView(week.get(i));
        }
    }
    private ArrayList<ArrayList<String>> takeInfoFromTakenBD(String typeSelector)
    {
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        takenDb = databaseHelper.open();
        Cursor userCursor = takenDb.query(databaseHelper.TABLE_TAKEN_PROJECTS, null, null, null, null, null, null);
        if(userCursor.moveToFirst())
        {
            int indexID = userCursor.getColumnIndex(databaseHelper.COLUMN_ID);
            int indexDate = userCursor.getColumnIndex(databaseHelper.COLUMN_DATE);
            int indexTime = userCursor.getColumnIndex(databaseHelper.COLUMN_TIME);
            int indexName = userCursor.getColumnIndex(databaseHelper.COLUMN_NAME);
            int indexAdress = userCursor.getColumnIndex(databaseHelper.COLUMN_ADDRESS);
            int indexWorker = userCursor.getColumnIndex(databaseHelper.COLUMN_WORKER);
            int indexType= userCursor.getColumnIndex(databaseHelper.COLUMN_TYPE);
            do {
                userCursor.getString(indexDate);
            }
            while(userCursor.moveToNext());

        }


        userCursor.close();
        return dataList;

    }
    private List<ArrayList<String>> selectItems(String typeSelector)
    {
        ArrayList<ArrayList<String>> dataList = takeInfoFromTakenBD(typeSelector);
        ArrayList<ArrayList<String>> answer = new ArrayList<>();
        for(int i = 0; i < dataList.size(); i++)
        {
            if(peopleSelectorState)
            {
                if(typeSelector.equals("все"))
                {
                    answer.add(dataList.get(i));
                }
                else if(dataList.get(i).get(4).equals(typeSelector))
                {
                    answer.add(dataList.get(i));
                }
            }
            else
            {
                if(dataList.get(i).get(3).equals(peopleSelector)) {
                    if (typeSelector.equals("все")) {
                        answer.add(dataList.get(i));
                    } else if (dataList.get(i).get(4).equals(typeSelector)) {
                        answer.add(dataList.get(i));
                    }
                }
            }

        }
        return answer;

    }
    //Создать предмет
    private LinearLayout createAndSetItem()
    {
        LinearLayout newView = new LinearLayout(this.getContext());
        newView.setBackground(getResources().getDrawable(R.drawable.items_background));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.FILL | Gravity.FILL_HORIZONTAL;
        params.setMargins(20, 60, 20, 0);
        newView.setLayoutParams(params);
        newView.setPadding(20, 40, 20, 40);
        return newView;
    }
    //Создать текст в предмете
    private TextView createAndSetItemText(String text)
    {
        TextView newText = new TextView(this.getContext());
        newText.setText(text);
        newText.setTextColor(Color.parseColor("#FFFFFF"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 0, 0);
        params.gravity = Gravity.FILL;
        params.weight = 1;
        newText.setLayoutParams(params);
        newText.setTextSize(16);
        return newText;
    }

    //Создать список предметов
    private List<LinearLayout> createDatesList(String typeSelector)
    {
        List<ArrayList<String>> dataList = selectItems(typeSelector);
        List<LinearLayout> list = new ArrayList<>();
        for(int i = 0; i < dataList.size(); i++)
        {
            LinearLayout newView = createAndSetItem();
            for(int j = 0; j < dataList.get(i).size(); j++)
            {
                TextView newText = createAndSetItemText(dataList.get(i).get(j));
                newView.addView(newText);
            }
            list.add(newView);
        }
        return list;
    }
    //Заполнить предмеами список
    private void fillScroll(String typeSelector)
    {
        //Заполнение предметами
        List<LinearLayout> filling = createDatesList(typeSelector);
        scheduleScroll = getActivity().findViewById(R.id.schedule_scroll_linear_list);
        scheduleScroll.removeAllViews();
        for(int i = 0; i < filling.size(); i++)
        {
            scheduleScroll.addView(filling.get(i));
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }
}
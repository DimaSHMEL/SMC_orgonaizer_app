package com.example.smc_orgonaizer_app;

import static java.util.Collections.swap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private Calendar currentDate = new GregorianCalendar();

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
        changeDate(currentDate);
        //Создание недели
        fillWeeks();
        changeDateColor();


    }
    // Смена поли с нынешней датой
    private void changeDate(Calendar currentDate)
    {
        mouth.setText(getDate(currentDate));
    }
    private String getDate(Calendar currentDate)
    {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String date = df.format(currentDate.getTime());
        return date;
    }
    //создание массива кнопок
    private TextView makeDateButton(String date)
    {
        TextView button = new TextView(getContext());

        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(getResources().getColor(R.color.all_selector_color));
        gdDefault.setCornerRadius(100);
        button.setBackground(gdDefault);

        button.setText(date);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        params.weight = 1;

        button.setLayoutParams(params);
        button.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        button.setTextSize(25);
        button.setHeight(175);
        button.setWidth(150);
        button.setTextColor(getResources().getColor(R.color.white));
        return button;

    }
    private ArrayList<Integer> currentWeek;
    private ArrayList<TextView> createWeek(Calendar DATE)
    {
        ArrayList<TextView> week = new ArrayList<>();
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(DATE.getTime());
        currentWeek = new ArrayList<>();
        for(int i = Calendar.MONDAY; i <= Calendar.SATURDAY; i++)
        {
            currentDate.set(Calendar.DAY_OF_WEEK, i);
            String date = String.valueOf(Integer.parseInt(getDate(currentDate).split("\\.")[0]));
            week.add(makeDateButton(date));
            currentWeek.add(Integer.parseInt(getDate(currentDate).split("\\.")[0]));
        }
        currentDate.set(Calendar.WEEK_OF_YEAR, currentDate.getWeekYear() - 1);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String date = String.valueOf(Integer.parseInt(getDate(currentDate).split("\\.")[0]));
        week.add(makeDateButton(date));
        currentWeek.add(Integer.parseInt(getDate(currentDate).split("\\.")[0]));
        return week;
    }
    private void changeDateColor()
    {
        List<TextView> week = new ArrayList<>();
        for(int i = 0; i < weeksRow.getChildCount(); i++)
        {
            week.add((TextView) weeksRow.getChildAt(i));
        }
        for (int i = 0; i < week.size(); i++)
        {
            String TEMP = getDate(currentDate);
            Integer temp = Integer.parseInt(TEMP.split("\\.")[0]);
            if(week.get(i).getText().toString().equals(String.valueOf(temp)))
            {
                week.get(i).setBackgroundColor(getResources().getColor(R.color.purple_500));
            }
            else
            {
                week.get(i).setBackgroundColor(getResources().getColor(R.color.all_selector_color));

            }
        }
    }
    //Заполнение вида с неделями
    private void fillWeeks()
    {
        //Заполнение недели
        weeksRow = getActivity().findViewById(R.id.Schedule_view_weeks);
        ArrayList<TextView> week = createWeek(currentDate);
        for(int i = 0; i < week.size(); i++)
        {
            weeksRow.addView(week.get(i));
        }
    }
    //Функция доставания из бд данных
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
            int indexAddress = userCursor.getColumnIndex(databaseHelper.COLUMN_ADDRESS);
            int indexWorker = userCursor.getColumnIndex(databaseHelper.COLUMN_WORKER);
            int indexType= userCursor.getColumnIndex(databaseHelper.COLUMN_TYPE);
            do {
                ArrayList<String> line = new ArrayList<>();
                String dbDate = userCursor.getString(indexDate);
                if(dbDate.equals(getDate(currentDate)) || true)
                {
                    String dbWorker = userCursor.getString(indexWorker).split(" ")[0];
                    if(peopleSelectorState || peopleSelector.equals(dbWorker))
                    {
                        String dbType = userCursor.getString(indexType);
                        if(dbType.equals(typeSelector) || typeSelector.equals("все"))
                        {
                            String dbTime = userCursor.getString(indexTime);
                            String dbName = userCursor.getString(indexName);
                            String dbAddrees = userCursor.getString(indexAddress).split(" ")[0];
                            Integer dbID = userCursor.getInt(indexID);
                            line.add(dbTime); line.add(dbName); line.add(dbAddrees); line.add(dbWorker); line.add(dbType); line.add(String.valueOf(dbID));
                            dataList.add(line);
                        }
                    }
                }
            }
            while(userCursor.moveToNext());

        }
        userCursor.close();
        return dataList;

    }
    private List<ArrayList<String>> sortByTime(List<ArrayList<String>> data)
    {
        for(int i = 0; i < data.size() - 1; i++)
        {
            for (int j = 0; j < data.size(); j++)
            {
                String temp1 = data.get(i).get(0);
                String temp2 = data.get(i + 1).get(0);
                if(Integer.parseInt(temp1.split(":")[0]) > Integer.parseInt(temp2.split(":")[0])) {
                    swap(data, i, i + 1);
                }
                else if (Integer.parseInt(temp1.split(":")[0]) == Integer.parseInt(temp2.split(":")[0]))
                {
                    if(Integer.parseInt(temp1.split(":")[1]) > Integer.parseInt(temp2.split(":")[1]))
                    {
                        swap(data, i, i + 1);
                    }
                }
            }
        }
        return data;
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
        List<ArrayList<String>> dataList = sortByTime(takeInfoFromTakenBD(typeSelector));
        List<LinearLayout> list = new ArrayList<>();
        for(int i = 0; i < dataList.size(); i++)
        {
            LinearLayout newView = createAndSetItem();
            for(int j = 0; j < dataList.get(i).size() - 1; j++)
            {
                TextView newText = createAndSetItemText(dataList.get(i).get(j));
                newView.addView(newText);
            }
            newView.setId(Integer.parseInt(dataList.get(i).get(dataList.get(i).size() - 1)));
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
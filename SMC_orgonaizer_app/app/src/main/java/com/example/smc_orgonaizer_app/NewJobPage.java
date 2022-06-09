package com.example.smc_orgonaizer_app;

import static java.util.Collections.swap;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.CaseMap;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewJobPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewJobPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewJobPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewJobPage.
     */
    // TODO: Rename and change types and number of parameters
    public static NewJobPage newInstance(String param1, String param2) {
        NewJobPage fragment = new NewJobPage();
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
    //Селектор
    private List<String> typeSelectors = new ArrayList<String>(Arrays.asList(new String[]{"видео", "фото", "дизайн", "текст", "все"}));
    private int typeSelectorState = 0;
    private Button typeSelectorBtn;
    private float scale;
    @Override
    public void onStart() {
        super.onStart();
        scale = getContext().getResources().getDisplayMetrics().density;
        //Добавление обработчика на кнопку селектора по типу
        typeSelectorBtn = getActivity().findViewById(R.id.new_jobs_type_selector_btn);
        databaseHelper = new DatabaseHelper(getContext());
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
        fillScroll(typeSelectors.get(typeSelectorState));
    }
    DatabaseHelper databaseHelper;
    SQLiteDatabase database;
    //Функция доставания из бд данных
    private ArrayList<ArrayList<String>> takeInfoFromTakenBD(String typeSelector)
    {
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        database = databaseHelper.open();
        Cursor userCursor = database.query(databaseHelper.TABLE_NEW_PROJECTS, null, null, null, null, null, null);
        if(userCursor.moveToFirst())
        {
            int indexID = userCursor.getColumnIndex(databaseHelper.COLUMN_ID);
            int indexDate = userCursor.getColumnIndex(databaseHelper.COLUMN_DATE);
            int indexTime = userCursor.getColumnIndex(databaseHelper.COLUMN_TIME);
            int indexName = userCursor.getColumnIndex(databaseHelper.COLUMN_NAME);
            int indexAddress = userCursor.getColumnIndex(databaseHelper.COLUMN_ADDRESS);
            int indexDescription = userCursor.getColumnIndex(databaseHelper.COLUMN_DESCRIPTION);
            int indexType= userCursor.getColumnIndex(databaseHelper.COLUMN_TYPE);
            do {
                ArrayList<String> line = new ArrayList<>();
                String dbType = userCursor.getString(indexType);
                if (dbType.equals(typeSelector) || typeSelector.equals("все")) {
                    String dbDate = userCursor.getString(indexDate);
                    String dbTime = userCursor.getString(indexTime);
                    String dbName = userCursor.getString(indexName);
                    String dbDescription = userCursor.getString(indexDescription);
                    String dbAddress = userCursor.getString(indexAddress);
                    Integer dbID = userCursor.getInt(indexID);
                    line.add(dbName);
                    line.add(dbType);
                    line.add(dbDescription);
                    line.add(dbTime);
                    line.add(dbDate);
                    line.add(dbAddress);
                    line.add(String.valueOf(dbID));
                    dataList.add(line);
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
                String temp1 = data.get(i).get(3);
                String temp2 = data.get(i + 1).get(3);
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
        newView.setOrientation(LinearLayout.VERTICAL);
        newView.setBackground(getResources().getDrawable(R.drawable.items_background));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.FILL | Gravity.FILL_HORIZONTAL;
        params.setMargins((int) (20 * scale), (int) (20 * scale), (int) (20 * scale), 0);
        newView.setPadding((int) (20* scale), (int) (20* scale), (int) (20* scale), (int) (20* scale));
        newView.setLayoutParams(params);
        return newView;
    }
    private LinearLayout createAndSetHeader(String name, String type)
    {
        LinearLayout newView = new LinearLayout(this.getContext());
        newView.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newView.setLayoutParams(params);
        TextView Title = new TextView(this.getContext());
        Title.setText(name);
        Title.setTextColor(Color.parseColor("#FFFFFF"));
        Title.setTextSize(9 * scale);
        params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 2;
        Title.setLayoutParams(params);
        TextView Type = new TextView(this.getContext());
        Type.setText(type);
        params.weight = 1;
        Type.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        Type.setTextColor(Color.parseColor("#FFFFFF"));
        Type.setTextSize(5 * scale);
        Type.setPadding((int) (15* scale), (int) (10* scale), (int) (15* scale), (int) (10* scale));
        Type.setBackground(changeColor());
        Type.setLayoutParams(params);

        newView.addView(Title);
        newView.addView(Type);
        return newView;
    }
    private GradientDrawable changeColor()
    {
        GradientDrawable gdDefault = new GradientDrawable();
        switch (typeSelectorState)
        {
            case 0:
                gdDefault.setColor(getResources().getColor(R.color.video_selector_color));
                break;
            case 1:
                gdDefault.setColor(getResources().getColor(R.color.photo_selector_color));
                break;
            case 2:
                gdDefault.setColor(getResources().getColor(R.color.text_selector_color));
                break;
            case 3:
                gdDefault.setColor(getResources().getColor(R.color.design_selector_color));
                break;
            case 4:
                gdDefault.setColor(getResources().getColor(R.color.all_selector_color));
                break;
            default:
                break;
        }
        gdDefault.setCornerRadius(100);
        return  gdDefault;
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
        params.setMargins(0, (int) (20 * scale), 0, 0);
        params.gravity = Gravity.FILL;
        params.weight = 1;
        newText.setLayoutParams(params);
        newText.setTextSize(6 * scale);
        return newText;
    }
    private TextView makeButton(int id, int width)
    {
        TextView newText = new TextView(getContext());
        newText.setId(id);
        newText.setText("Записаться");
        newText.setTextColor(Color.parseColor("#FFFFFF"));
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getResources().getColor(R.color.accept));
        drawable.setCornerRadius(100);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, (int) (20* scale), 0, 0);
        newText.setLayoutParams(params);
        newText.setBackground(drawable);
        newText.setGravity(Gravity.CENTER);
        newText.setTextSize(6 * scale);
        newText.setPadding(0, (int) (10* scale), 0, (int) (10* scale));
        newText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                database = databaseHelper.open();
                Cursor userCursor = database.query(databaseHelper.TABLE_NEW_PROJECTS, null, null, null, null, null, null);
                if(userCursor.moveToFirst())
                {
                    int indexID = userCursor.getColumnIndex(databaseHelper.COLUMN_ID);
                    int indexDate = userCursor.getColumnIndex(databaseHelper.COLUMN_DATE);
                    int indexTime = userCursor.getColumnIndex(databaseHelper.COLUMN_TIME);
                    int indexName = userCursor.getColumnIndex(databaseHelper.COLUMN_NAME);
                    int indexAddress = userCursor.getColumnIndex(databaseHelper.COLUMN_ADDRESS);
                    int indexDescription = userCursor.getColumnIndex(databaseHelper.COLUMN_DESCRIPTION);
                    int indexType= userCursor.getColumnIndex(databaseHelper.COLUMN_TYPE);
                    do {
                        Integer dbID = userCursor.getInt(indexID);
                        TextView view = (TextView) v;
                        Integer viewID = view.getId();
                        if (viewID.equals(dbID)) {
                            String dbDate = userCursor.getString(indexDate);
                            String dbTime = userCursor.getString(indexTime);
                            String dbName = userCursor.getString(indexName);
                            String dbType = userCursor.getString(indexType);
                            String dbDescription = userCursor.getString(indexDescription);
                            String dbAddress = userCursor.getString(indexAddress);
                            contentValues.put(databaseHelper.COLUMN_DATE,dbDate);
                            contentValues.put(databaseHelper.COLUMN_TIME,dbTime);
                            contentValues.put(databaseHelper.COLUMN_TYPE,dbType);
                            contentValues.put(databaseHelper.COLUMN_NAME,dbName);
                            contentValues.put(databaseHelper.COLUMN_DESCRIPTION,dbDescription);
                            contentValues.put(databaseHelper.COLUMN_ADDRESS,dbAddress);
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE);
                            contentValues.put(databaseHelper.COLUMN_WORKER,sharedPreferences.getString("user_FIO", null));
                            userCursor.close();
                            database.insert(databaseHelper.TABLE_TAKEN_PROJECTS, null, contentValues);
                            database.delete(databaseHelper.TABLE_NEW_PROJECTS, databaseHelper.COLUMN_ID + "=" + v.getId(), null);
                            break;
                        }
                    }
                    while(userCursor.moveToNext());
                }
                fillScroll(typeSelectors.get(typeSelectorState));
            }
        });
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
            newView.addView(createAndSetHeader(dataList.get(i).get(0),dataList.get(i).get(1)));
            TextView newText = createAndSetItemText("Описание : " + dataList.get(i).get(2));
            newView.addView(newText);
            newText = createAndSetItemText("Время : " + dataList.get(i).get(3) + " " +  dataList.get(i).get(4));
            newView.addView(newText);
            newText = createAndSetItemText("Место : " + dataList.get(i).get(5));
            newView.addView(newText);
            newText = makeButton(Integer.parseInt(dataList.get(i).get(dataList.get(i).size() - 1)), newView.getWidth() / 3);
            newView.addView(newText);
            newView.setId(Integer.parseInt(dataList.get(i).get(dataList.get(i).size() - 1)));
            list.add(newView);
        }
        return list;
    }
    LinearLayout scheduleScroll;
    //Заполнить предмеами список
    private void fillScroll(String typeSelector)
    {
        //Заполнение предметами
        List<LinearLayout> filling = createDatesList(typeSelector);
        scheduleScroll = getActivity().findViewById(R.id.new_jobs_filling);
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
        return inflater.inflate(R.layout.fragment_new_jobs_page, container, false);
    }
}
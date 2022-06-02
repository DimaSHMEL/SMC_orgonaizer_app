package com.example.smc_orgonaizer_app;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
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
    private TextView mouthn;
    private Button typeSelectorBtn;
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        List<LinearLayout> filling = createDatesList();
        scheduleScroll = getActivity().findViewById(R.id.schedule_scroll_linear_list);
        for(int i = 0; i < filling.size(); i++)
        {
            scheduleScroll.addView(filling.get(i));
        }
        typeSelectorBtn = getActivity().findViewById(R.id.schedule_typeSelectorChanger);
        typeSelectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
    //Создать предмет
    public LinearLayout createAndSetItem()
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
    public TextView createAndSetItemText(String text)
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
    public List<LinearLayout> createDatesList()
    {
        dataList.add(new ArrayList<String>(Arrays.asList(new String[]{"10:40", "Подкаст ИИ", "a-135", "Шмелев", "видео"})));
        dataList.add(new ArrayList<String>(Arrays.asList(new String[]{"10:40", "Подкаст ИИ", "a-135", "Павлов", "видео"})));

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

    private String selector = "видео";
    private List<String> selectors = new ArrayList<String>(Arrays.asList(new String[]{"видео", "фото", "дизайн", "текст"}));
    public void changeSelectorButtonFunc(View view)
    {


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }
}
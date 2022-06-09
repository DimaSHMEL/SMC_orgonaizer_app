package com.example.smc_orgonaizer_app;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
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
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private Button exitButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private TextView profileText;
    private LinearLayout typesContainer;
    private LinearLayout descriptionContainer;
    private  float scale;
    @Override
    public void onStart() {
        super.onStart();
        scale = getContext().getResources().getDisplayMetrics().density;
        exitButton = getActivity().findViewById(R.id.profile_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignInActivity.class);
                saveAutih();
                startActivity(intent);
                getActivity().finish();
            }
        });
        profileText = getActivity().findViewById(R.id.profile_name);
        typesContainer = getActivity().findViewById(R.id.profile_types);
        descriptionContainer = getActivity().findViewById(R.id.profile_Info);
        setProfile();
    }
    private void setProfile()
    {
        SharedPreferences sPref = getActivity().getSharedPreferences("AUTH", MODE_PRIVATE);
        profileText.setText(sPref.getString("user_FIO", null));
        createAndSetTypes();
        createAndSetDescription();
    }
    private void createAndSetTypes()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AUTH", MODE_PRIVATE);
        String types = sharedPreferences.getString("user_type", null);
        List<String> Types = Arrays.asList(types.split(" "));
        for(int i = 0; i < Types.size(); i++)
        {
            TextView tag = new TextView(this.getContext());
            tag.setText(Types.get(i));
            tag.setTextColor(Color.parseColor("#FFFFFF"));
            tag.setTextSize(5 * scale);
            tag.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tag.setPadding((int) (15* scale), (int) (10* scale), (int) (15* scale), (int) (10* scale));
            tag.setBackground(changeColor(Types.get(i)));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            tag.setLayoutParams(params);
            typesContainer.addView(tag);
        }
    }
    private void createAndSetDescription()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AUTH", MODE_PRIVATE);
        String description = sharedPreferences.getString("user_description", null);
        String contacts = sharedPreferences.getString("user_contacts", null);
        TextView tex = new TextView(this.getContext());
        tex.setText(description);
        tex.setTextColor(Color.parseColor("#FFFFFF"));
        tex.setTextSize(9 * scale);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        tex.setLayoutParams(params);
        descriptionContainer.addView(tex);
        tex = new TextView(getContext());
        tex.setText(contacts);
        tex.setTextColor(Color.parseColor("#FFFFFF"));
        tex.setTextSize(9 * scale);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        tex.setLayoutParams(params);
        descriptionContainer.addView(tex);

    }
    private List<String> typeSelectors = new ArrayList<String>(Arrays.asList(new String[]{"видео", "фото", "дизайн", "текст", "активист", "комитетчик"}));
    private GradientDrawable changeColor(String type)
    {
        GradientDrawable gdDefault = new GradientDrawable();
        switch (typeSelectors.indexOf(type))
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
            case 5:
                gdDefault.setColor(getResources().getColor(R.color.all_selector_color));
                break;
            default:
                break;
        }
        gdDefault.setCornerRadius(100);
        return  gdDefault;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    private void saveAutih()
    {
        SharedPreferences sPref = getActivity().getSharedPreferences("AUTH", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean("LOGGED", false);
        ed.apply();
    }
}
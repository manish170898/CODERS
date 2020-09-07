package com.example.tinderdev.SwingFling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.example.tinderdev.R;

import java.util.List;

public class ArrayAdapter extends android.widget.ArrayAdapter<Cards> {
    Context context;

    public ArrayAdapter(Context context, int resourceId, List<Cards> items){
        super(context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Cards card_item =getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item,parent,false);
        }
        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);
        TextView interest = convertView.findViewById(R.id.Userinterest);
        TextView codingS = convertView.findViewById(R.id.UserSkills);
        TextView age = convertView.findViewById(R.id.age);


        name.setText(card_item.getName());
        interest.setText(card_item.getInterest());
        codingS.setText(card_item.getCoding());
        age.setText(card_item.getAge());
        switch (card_item.getProfileImageUrl()){
            case "default":
                Glide.with(getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                    Glide.with(getContext()).load(card_item.getProfileImageUrl()).into(image);
                    break;
        }

        return convertView;
    }
}

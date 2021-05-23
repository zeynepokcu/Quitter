package com.example.quitter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TimeFragment extends Fragment {
    DatabaseReference databaseReference;
    int arrayTime[] = new int [3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time, container, false);

        TextView showTime = (TextView) v.findViewById(R.id.txtShowTime);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                databaseReference.child("amount").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Integer value = snapshot.getValue(Integer.class);
                        int amount = value;
                        arrayTime[0]=amount;
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                databaseReference.child("time").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Integer value = snapshot.getValue(Integer.class);
                        int time = value;
                        arrayTime[1]=time;
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild("smokeFreeDays")) {
                            databaseReference.child("smokeFreeDays").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    int numberOfDays = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
                                    System.out.println(numberOfDays);
                                    arrayTime[2] = numberOfDays;
                                    int minValue=1;
                                    for (int i : arrayTime) {
                                        minValue *= i;
                                        showTime.setText(String.valueOf(minValue) + " minutes");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        } else if(!snapshot.hasChild("smokeFreeDays")){
                            int minValue=0;
                            for (int i : arrayTime) {
                                minValue *= i;
                                showTime.setText(String.valueOf(minValue) + " minutes");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return v;
    }


}
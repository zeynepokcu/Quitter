package com.example.quitter;

import android.annotation.SuppressLint;
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

public class SavingsFragment extends Fragment {
    DatabaseReference databaseReference;
    int arraySavings[] = new int[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_savings, container, false);

        TextView showSavings = (TextView) v.findViewById(R.id.txtShowSavings);

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
                        System.out.println(amount);
                        arraySavings[0] = amount;
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                databaseReference.child("price").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Integer value = snapshot.getValue(Integer.class);
                        int price = (value / 20);
                        System.out.println(price);
                        arraySavings[1] = price;
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
                                    arraySavings[2] = numberOfDays;
                                    int minValue = 1;
                                    for (int i : arraySavings) {
                                        minValue *= i;
                                        showSavings.setText(String.valueOf(minValue) + " TL");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        } else if(!snapshot.hasChild("smokeFreeDays")){
                            int minValue=0;
                            for (int i : arraySavings) {
                                minValue *= i;
                                showSavings.setText(String.valueOf(minValue) + " TL");
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
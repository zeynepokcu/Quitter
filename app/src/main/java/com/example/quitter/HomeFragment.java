package com.example.quitter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    CalendarView cal;
    TextView showDate;
    Button btnNo;
    Button btnYes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        cal = (CalendarView) v.findViewById(R.id.calendarView);
        showDate = (TextView) v.findViewById(R.id.showDate);
        btnNo = (Button) v.findViewById(R.id.buttonSubmit);
        btnYes = (Button) v.findViewById(R.id.buttonDelete);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDate();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDate();
            }
        });

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String selectedDates = sdf.format(new Date(year - 1900, month, dayOfMonth));
                showDate.setText(selectedDates);
            }
        });
        return v;
    }

    private void submitDate() {
        String dateValue = showDate.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("smokeFreeDays");

        String key = dateValue;

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.hasChild(dateValue)) {
                    Toast.makeText(getActivity(), "This date is already submitted!", Toast.LENGTH_SHORT).show();
                } else {
                    usersRef.child(key).setValue(dateValue, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(getActivity(), "Failed to submit!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getActivity(), "Successfully submitted!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.w("tag", "loadPost:onCancelled", error.toException());
            }
        });


    }


    private void deleteDate() {
        String dateValue = showDate.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("smokeFreeDays");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.hasChild(dateValue)) {
                    usersRef.child(dateValue).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "This date is not submitted!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }
}
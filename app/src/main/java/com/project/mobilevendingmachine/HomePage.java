package com.project.mobilevendingmachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    TextView diaryMilkText, fiveStarText, diaryNumber, fiveStarNumber;
    Button diaryPlus, diaryMinus, fiveStarPlus, fiveStarMinus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        diaryMilkText=(TextView)findViewById(R.id.diaryMilkText);
        fiveStarText=(TextView)findViewById(R.id.FiveStarText);
        diaryPlus=(Button)findViewById(R.id.diaryPlus);
        diaryMinus=(Button)findViewById(R.id.diaryMinus);
        fiveStarPlus=(Button)findViewById(R.id.fiveStarPlus);
        fiveStarMinus=(Button)findViewById(R.id.fiveStarMinus);
        diaryNumber=(TextView)findViewById(R.id.diaryNumber);
        fiveStarNumber=(TextView)findViewById(R.id.fiveStarNumber);

        diaryPlus.setOnClickListener(this);
        diaryMinus.setOnClickListener(this);
        fiveStarPlus.setOnClickListener(this);
        fiveStarMinus.setOnClickListener(this);

        final ArrayList<String> chocolates=new ArrayList<>();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chocolates");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    for(DataSnapshot snapshot1:snapshot.getChildren()) {
                        if (snapshot1.getKey().equals("name")) {
                            String name=snapshot1.getValue(String.class);
                            chocolates.add(name);
                        }
                    }
                }
                if(chocolates.size()!=0) {
                    fiveStarText.setText(chocolates.get(0));
                    diaryMilkText.setText(chocolates.get(1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int diary=0,fiveStar=0;
        int id=view.getId();
        switch (id){
            case R.id.diaryPlus:
                diary= Integer.parseInt(diaryNumber.getText().toString());
                ++diary;
                if(diary>10)
                    diary=10;
                diaryNumber.setText(diary+"");
                break;
            case R.id.diaryMinus:
                diary= Integer.parseInt(diaryNumber.getText().toString());
                --diary;
                if(diary<0)
                    diary=0;
                diaryNumber.setText(diary+"");
                break;
            case R.id.fiveStarPlus:
                fiveStar=Integer.parseInt(fiveStarNumber.getText().toString());
                ++fiveStar;
                if (fiveStar>10)
                    fiveStar=10;
                fiveStarNumber.setText(fiveStar+"");
                break;
            case R.id.fiveStarMinus:
                fiveStar=Integer.parseInt(fiveStarNumber.getText().toString());
                --fiveStar;
                if (fiveStar<0)
                    fiveStar=0;
                fiveStarNumber.setText(fiveStar+"");
                break;
        }
    }
}
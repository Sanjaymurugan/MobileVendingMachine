package com.project.mobilevendingmachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    TextView diaryMilkText, fiveStarText, diaryNumber, fiveStarNumber;
    Button diaryPlus, diaryMinus, fiveStarPlus, fiveStarMinus;

    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();

        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView=(RecyclerView)findViewById(R.id.chocolatesRV);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("chocolates");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                homePojo pojo=dataSnapshot.getValue(homePojo.class);
                Toast.makeText(HomePage.this,pojo.getName()+" hello",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        class viewHolder extends RecyclerView.ViewHolder{
            TextView chocolateName, chocolateNumber;
            Button plus, minus;

            public viewHolder(@NonNull View itemView) {
                super(itemView);
                plus=(Button)itemView.findViewById(R.id.plus);
                minus=(Button)itemView.findViewById(R.id.minus);
                chocolateName=(TextView)itemView.findViewById(R.id.chocolateName);
                chocolateNumber=(TextView)itemView.findViewById(R.id.chocolateNumber);
            }

            public void setChocolateName(String name){
                chocolateName.setText(name);
            }
        }

        Query query= FirebaseDatabase.getInstance().getReference().child("chocolates");

        FirebaseRecyclerOptions<homePojo> options=new FirebaseRecyclerOptions.Builder<homePojo>()
                .setQuery(query, new SnapshotParser<homePojo>() {
                    @NonNull
                    @Override
                    public homePojo parseSnapshot(@NonNull DataSnapshot snapshot) {
                        homePojo pojo=snapshot.getValue(homePojo.class);
//                        pojo.setName(snapshot.child("name").getValue().toString());
//                        pojo.setQuantity(Integer.parseInt(snapshot.child("quantity").getValue().toString()));
                        Toast.makeText(HomePage.this,pojo.getName()+"hello",Toast.LENGTH_SHORT).show();
                        return pojo;
                    }
                }).build();

        adapter= new FirebaseRecyclerAdapter<homePojo,viewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final viewHolder holder, int position, @NonNull homePojo model) {
                holder.setChocolateName(model.getName());
                final int no=model.getQuantity();

                holder.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectQuantity=Integer.parseInt(holder.chocolateNumber.getText().toString());
                        if(selectQuantity+1<=no) {
                            ++selectQuantity;
                            holder.chocolateNumber.setText(selectQuantity+"");
                        }
                        else
                            Toast.makeText(HomePage.this,"Only "+no+" Chocolates are available!",Toast.LENGTH_LONG).show();
                    }
                });

                holder.minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectQuantity=Integer.parseInt(holder.chocolateNumber.getText().toString());
                        if(selectQuantity-1>=0) {
                            selectQuantity--;
                            holder.chocolateNumber.setText(selectQuantity+"");
                        }
                        else
                            Toast.makeText(HomePage.this,"Please select the required number of chocolates...",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chocolate_child,parent,false);
                return new viewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

//        diaryMilkText=(TextView)findViewById(R.id.diaryMilkText);
//        fiveStarText=(TextView)findViewById(R.id.FiveStarText);
//        diaryPlus=(Button)findViewById(R.id.diaryPlus);
//        diaryMinus=(Button)findViewById(R.id.diaryMinus);
//        fiveStarPlus=(Button)findViewById(R.id.fiveStarPlus);
//        fiveStarMinus=(Button)findViewById(R.id.fiveStarMinus);
//        diaryNumber=(TextView)findViewById(R.id.diaryNumber);
//        fiveStarNumber=(TextView)findViewById(R.id.fiveStarNumber);
//
//        diaryPlus.setOnClickListener(this);
//        diaryMinus.setOnClickListener(this);
//        fiveStarPlus.setOnClickListener(this);
//        fiveStarMinus.setOnClickListener(this);

//        final ArrayList<String> chocolates=new ArrayList<>();
//
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chocolates");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    for(DataSnapshot snapshot1:snapshot.getChildren()) {
//                        if (snapshot1.getKey().equals("name")) {
//                            String name=snapshot1.getValue(String.class);
//                            chocolates.add(name);
//                        }
//                    }
//                }
//                if(chocolates.size()!=0) {
//                    fiveStarText.setText(chocolates.get(0));
//                    diaryMilkText.setText(chocolates.get(1));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        int diary=0,fiveStar=0;
        int id=view.getId();
//        switch (id){
//            case R.id.diaryPlus:
//                diary= Integer.parseInt(diaryNumber.getText().toString());
//                ++diary;
//                if(diary>10)
//                    diary=10;
//                diaryNumber.setText(diary+"");
//                break;
//            case R.id.diaryMinus:
//                diary= Integer.parseInt(diaryNumber.getText().toString());
//                --diary;
//                if(diary<0)
//                    diary=0;
//                diaryNumber.setText(diary+"");
//                break;
//            case R.id.fiveStarPlus:
//                fiveStar=Integer.parseInt(fiveStarNumber.getText().toString());
//                ++fiveStar;
//                if (fiveStar>10)
//                    fiveStar=10;
//                fiveStarNumber.setText(fiveStar+"");
//                break;
//            case R.id.fiveStarMinus:
//                fiveStar=Integer.parseInt(fiveStarNumber.getText().toString());
//                --fiveStar;
//                if (fiveStar<0)
//                    fiveStar=0;
//                fiveStarNumber.setText(fiveStar+"");
//                break;
//        }
    }
}

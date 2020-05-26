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

public class HomePage extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter adapter;
    TextView totalAmount;
    Button pay;
    ArrayList<homePojo> arrayList;

    @Override
    protected void onStart() {
        super.onStart();

        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView=(RecyclerView)findViewById(R.id.chocolatesRV);
        recyclerView.setLayoutManager(linearLayoutManager);
        totalAmount=(TextView)findViewById(R.id.totalAmount);
        arrayList=new ArrayList<>();

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
                        Toast.makeText(HomePage.this,pojo.getName()+"hello",Toast.LENGTH_SHORT).show();
                        return pojo;
                    }
                }).build();

        adapter= new FirebaseRecyclerAdapter<homePojo,viewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final viewHolder holder, final int position, @NonNull final homePojo model) {
                holder.setChocolateName(model.getName());
                final int no=model.getQuantity();
                final homePojo temp=new homePojo();
                temp.setName(model.getName());

                holder.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int amount=Integer.parseInt(totalAmount.getText().toString());
                        int selectQuantity=Integer.parseInt(holder.chocolateNumber.getText().toString());
                        if(selectQuantity+1<=no) {
                            ++selectQuantity;
                            holder.chocolateNumber.setText(selectQuantity+"");
                            amount+=5;
                            totalAmount.setText(amount+"");
                            temp.setQuantity(model.getQuantity()-selectQuantity);
                            arrayList.add(position,temp);
                        }
                        else
                            Toast.makeText(HomePage.this,"Only "+no+" Chocolates are available!",Toast.LENGTH_LONG).show();
                    }
                });

                holder.minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int amount=Integer.parseInt(totalAmount.getText().toString());
                        int selectQuantity=Integer.parseInt(holder.chocolateNumber.getText().toString());
                        if(selectQuantity-1>=0) {
                            selectQuantity--;
                            holder.chocolateNumber.setText(selectQuantity+"");
                            amount-=5;
                            totalAmount.setText(amount+"");
                            temp.setQuantity(model.getQuantity()-selectQuantity);
                            arrayList.add(position,temp);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        pay=(Button)findViewById(R.id.Pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("chocolates");
                for(homePojo pojo:arrayList){
                    reference.child(pojo.getName()).child("quantity").setValue(pojo.getQuantity());
                }
                totalAmount.setText("0");
            }
        });
    }
}

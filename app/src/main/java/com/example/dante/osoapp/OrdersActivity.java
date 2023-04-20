package com.example.dante.osoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    ListView listorder;
    List<OrderPojo> pojo;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    OrderAdapter adapter;
    ArrayList<String> listitems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        listorder= (ListView) findViewById(R.id.items);
        pojo=new ArrayList<>();
       adapter=new OrderAdapter(pojo,this);
       listorder.setAdapter(adapter);
        OrderList();

    }
    private class OrderAdapter extends BaseAdapter{

        List<OrderPojo> orderPojos;
        Context context;

        public OrderAdapter(List<OrderPojo> orderPojos, Context context) {
            this.orderPojos = orderPojos;
            this.context = context;
        }

        @Override
        public int getCount() {
            return orderPojos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = LayoutInflater.from(context).inflate(R.layout.orderslayout, parent, false);

            TextView id=view.findViewById(R.id.order_id);
            TextView total=view.findViewById(R.id.price);
            TextView quantity=view.findViewById(R.id.number);
            TextView address=view.findViewById(R.id.adress);
            TextView email=view.findViewById(R.id.email);
            RelativeLayout layout=view.findViewById(R.id.layout3);

            id.setText(orderPojos.get(position).getOrderid());
            address.setText(orderPojos.get(position).getDelivery_Address());
            email.setText(orderPojos.get(position).getCustoer_email());
            total.setText("Ksh. "+orderPojos.get(position).getTotal());
            quantity.setText(String.valueOf(orderPojos.get(position).getCount()));

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OrdersActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }
    }
    private void OrderList() {
       db.collection("order").//whereEqualTo("custoer_email",mAuth.getCurrentUser().getEmail());
        get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<CartPojo> list=new ArrayList<>();
                for (QueryDocumentSnapshot snap: queryDocumentSnapshots) {
                   listitems=new ArrayList<>();
                    listitems=(ArrayList<String>) snap.get("list");
                    String id = snap.getString("orderid");
                    String email=snap.getString("custoer_email");
                    String address=snap.getString("delivery_Address");
                    String total =snap.getString("total");
                    int m=listitems.size();
                    OrderPojo pojo1=new OrderPojo(id,email,address,total,m);
                    pojo.add(pojo1);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrdersActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
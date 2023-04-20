
package com.example.dante.osoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Favourite extends AppCompatActivity {
    NavigationBarView bottomNavigationView;
    ListView listfavourite;
    List<SnackPojo> pojo;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    SnackAdapter adapter;
    @Override
    protected void onStart() {
        super.onStart();

        bottomNavigationView.setSelectedItemId(R.id.nav_order);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        pojo=new ArrayList<>();
        listfavourite= (ListView) findViewById(R.id.items);
        FavouriteSnacksList();
       adapter=new SnackAdapter(pojo,this);
        listfavourite.setAdapter(adapter);

        bottomNavigationView= (NavigationBarView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.nav_order:

                        return true;



                    case R.id.nav_account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                }

                return false;
            }
        });

}

    private void FavouriteSnacksList() {

        Query ref = db.collection("Favourite").whereEqualTo("email", mAuth.getCurrentUser().getEmail());
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                    String name = snap.getString("iname");
                    String descr = snap.getString("idescription");
                    String image = snap.getString("image");
                    String price = snap.getString("price");
                    SnackPojo pojo1 = new SnackPojo(image, name, price, descr);
                    pojo.add(pojo1);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Favourite.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private class SnackAdapter extends BaseAdapter {


        Context context;
        List<SnackPojo> snackPojos;

        public SnackAdapter(List<SnackPojo> snackPojos,Context context ) {
            this.context = context;
            this.snackPojos = snackPojos;

        }
        public void filtered(List<SnackPojo> filterlist)
        {
            snackPojos=filterlist;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return snackPojos.size();
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

            view = LayoutInflater.from(context).inflate(R.layout.favourite, parent, false);

            ImageView image = view.findViewById(R.id.image);
            TextView title = view.findViewById(R.id.name);
            TextView price = view.findViewById(R.id.price);
            TextView descr=view.findViewById(R.id.descr);
            ImageButton add=view.findViewById(R.id.additem);
            //OnClickListeners
            //Adding to cart


            title.setText(snackPojos.get(position).getName());
            price.setText(""+snackPojos.get(position).getPrice());
            Glide.with(context).load(snackPojos.get(position).getImage()).into(image);
            descr.setText(snackPojos.get(position).getDesription());


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent=new Intent(getApplicationContext(),SnackDetailsActivity.class);
                    intent.putExtra("image",snackPojos.get(position).getImage());
                    intent.putExtra("name",snackPojos.get(position).getName());
                    intent.putExtra("price",snackPojos.get(position).getPrice());
                    intent.putExtra("description",snackPojos.get(position).getDesription());
                    startActivity(intent);

                }
            });
            return view;
        }
    }
}
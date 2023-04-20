package com.example.dante.osoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.arch.core.internal.FastSafeIterableMap;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class HomeActivity extends AppCompatActivity {

    ImageView prof;
    SearchView searchView;
    List<SnackPojo> listpojo=new ArrayList<>();
    ImageButton searchbtn,notification,sidebar;
    ListView snack_items;
    SearchView search;

    List<SnackPojo> snacks;
    int cartsize=0;
    TextView cart;
    List<CartPojo> CartItems,FilteredCart,FilteredCart2;

    String name,price1,quantity;
    String image1;
    SnackAdapter snackAdapter;
    List<SnackPojo> snack;
    androidx.appcompat.widget.Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

FirebaseFirestore db;
    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        if(mAuth.getCurrentUser()==null){
            Intent intent=new Intent(getApplicationContext(),AccountActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Create account or Login for better experience", Toast.LENGTH_SHORT).show();
        }

        CheckPernissions();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar=  findViewById(R.id.toolbar);
        searchView= (SearchView) findViewById(R.id.search);
        searchbtn= (ImageButton) findViewById(R.id.searchbtn);
        db=FirebaseFirestore.getInstance();
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottom_navigation);
        FilteredCart2=new ArrayList<>();
        CartItems=new ArrayList<>();
        FilteredCart=new ArrayList<>();
        cart= (TextView) findViewById(R.id.quantity);
        prof= (ImageView) findViewById(R.id.prof_image);
        searchbtn= (ImageButton) findViewById(R.id.searchbtn);
        notification= (ImageButton) findViewById(R.id.notify);
        sidebar= (ImageButton) findViewById(R.id.sidebar);
        search= (SearchView) findViewById(R.id.search);
        snack_items= (ListView) findViewById(R.id.items);
        snacks=new ArrayList<>();
        snack=new ArrayList<>();

        snackAdapter=new SnackAdapter(snacks,this);
        snack_items.setAdapter(snackAdapter);
        SnacksFirebaseQuery();
       // listpojo=snacks;
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 toolbar.setVisibility(View.INVISIBLE);
                 search.setVisibility(View.VISIBLE);
            }
        });
        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu=new PopupMenu(getApplicationContext(),sidebar);
                menu.inflate(R.menu.cart_menu);
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                      switch (item.getItemId()){
                          case R.id.cart_view:
                              Intent intent=new Intent(getApplicationContext(),CartActivity.class);
                              startActivity(intent);

                              return  true;
                          case R.id.cart_clear:
                              CartItems.clear();
                              cart.setText("0");
                              cartsize=0;
                              return true;
                          case R.id.cart_remove:
                              intent=new Intent(getApplicationContext(),CartActivity.class);
                              startActivity(intent);

                              Toast.makeText(HomeActivity.this, "You can remove item here", Toast.LENGTH_SHORT).show();
                      }
                        return false;
                    }
                });

            }
        });
       search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                toolbar.setVisibility(View.VISIBLE);
                search.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //FilteredList(query);
               snackAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                snackAdapter.getFilter().filter(newText);
                return false;
            }
        });
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:

                        return true;
                    case R.id.nav_order:
                        startActivity(new Intent(getApplicationContext(), Favourite.class));
                        overridePendingTransition(100, 0);

                        return true;



                    case R.id.nav_account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);

                        return true;

                }
                return false;
            }
        });


    }
    private void CheckPernissions() {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            }
            else{
                ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            }
            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            }
            else{
                ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

            }
            else{
                ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.INTERNET},1);

            }
            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            }
            else{
                ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

            }
        }
    }
    private void SnacksFirebaseQuery() {
        db.collection("snacks").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                           // int m = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getId();

                                String name=document.getString("name");
                                String desc=document.getString("description");
                                String image= document.getString("image");
                                String  price=document.getString("price");
                               // int p=Integer.parseInt(price);
                                SnackPojo pro=new SnackPojo(image,name,price,desc);
                                snacks.add(pro);
                            }
                          snackAdapter.notifyDataSetChanged();
                        }

                        else {
                            Toast.makeText(HomeActivity.this, "No Snacks Found", Toast.LENGTH_SHORT).show();
                            Toast.makeText(HomeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



       public class  CartAdapter extends BaseAdapter{

        List<CartPojo>  cartPojos;

           public CartAdapter(List<CartPojo> cartPojos) {
               this.cartPojos = cartPojos;
           }

           @Override
           public int getCount() {
               return cartPojos.size();

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
           public View getView(int position, View convertView, ViewGroup parent) {
               return null;
           }
       }
       private void SaveCart(){
           SharedPreferences sharedPreferences=getSharedPreferences("cart",MODE_PRIVATE);
           SharedPreferences.Editor editor=sharedPreferences.edit();
           Gson gson=new Gson();
           String json= gson.toJson(CartItems);
           editor.putString("cart",json);
           editor.apply();
          // Toast.makeText(this, "Cart Saved", Toast.LENGTH_SHORT).show();
       }
      public boolean SnackExist(List<CartPojo> cartlist,String key){
        boolean state=false;
        for (int i=0;i<cartlist.size();i++){
            if(cartlist.get(i).getName().equals(key)){
                state=true;
            }
            else {
                state=false;
            }
        }

          return state;
      }
    public class SnackAdapter extends BaseAdapter{


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

            view = LayoutInflater.from(context).inflate(R.layout.snacklayout, parent, false);

            ImageView image = view.findViewById(R.id.image);
            TextView title = view.findViewById(R.id.name);
            TextView price = view.findViewById(R.id.price);
            TextView descr=view.findViewById(R.id.descr);
            ImageButton add=view.findViewById(R.id.additem);
            //OnClickListeners
            //Adding to cart
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   name=snackPojos.get(position).getName();
                   price1=snackPojos.get(position).getPrice();
                   image1= snackPojos.get(position).getImage();
                   if(!SnackExist(CartItems,name)) {
                       cartsize++;
                       cart.setText(String.valueOf(cartsize));
                       CartItems.add(new CartPojo(1, name, price1, image1));
                       Toast.makeText(context, "Snack Added", Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(context, "Snack Already in the Cart", Toast.LENGTH_SHORT).show();
                   }
                }
            });

            notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SaveCart();
                    Intent intent=new Intent(getApplicationContext(),CartActivity.class);
                    startActivity(intent);

                }
            });

            title.setText(snackPojos.get(position).getName());
            price.setText("Ksh. "+snackPojos.get(position).getPrice());
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

        public Filter getFilter() {
            Filter filter = new Filter() {
                List<SnackPojo> resultdata = new ArrayList<>();
                List<SnackPojo> resultdata1= new ArrayList<>();




                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    String search1 = constraint.toString().toLowerCase();
                    if (constraint == null | constraint.length() == 0) {

                      filterResults.count =listpojo.size();// snackPojos.size();
                        filterResults.values = listpojo;//snackPojos;
                    }
                        else {
                        String searchstr = constraint.toString().toLowerCase();
                        for (SnackPojo pojo : snackPojos) {
                            if (pojo.getName().toLowerCase().contains(searchstr) || pojo.getPrice().toLowerCase().contains(searchstr)) {
                                resultdata.add(pojo);
                            }
                            filterResults.count = resultdata.size();
                            filterResults.values = resultdata;
                        }
                    }
                    return filterResults;

                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                   // snackPojos= (List<SnackPojo>) results.values;
                        snackPojos = (List<SnackPojo>) results.values;
                        notifyDataSetChanged();

                }
            };
            return filter;
        }
    }


}
package com.example.dante.osoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
   DrawerLayout drawerLayout;
   NavigationView navigationView;
   Toolbar toolbar;
    Calendar calendar;
    TextView date;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    GridView gridView, searchedresultlist;
    AdapterViewFlipper gridview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        calendar = Calendar.getInstance();
        date = findViewById(R.id.date);

        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
         CurrentDate();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.a_profile:
                Toast.makeText(AdminActivity.this, "Account coming soon ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_profile:
                Toast.makeText(AdminActivity.this, "Edit profile coming soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.clients:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.orders:
               Intent intent=new Intent(getApplicationContext(),OrdersActivity.class);
               startActivity(intent);
                break;
            case R.id.snack:

                Toast.makeText(this, "Snack list and management coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sales:
                Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show();

                break;
            case R.id.na_admin:
                Toast.makeText(AdminActivity.this, "Admin Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
              mAuth.signOut();
               intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    private void CurrentDate() {
        String d, days, month;

        d = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if (d.equals("1")) {
            d = "Su";
        } else if (d.equals("2")) {
            d = "Mo";
        } else if (d.equals("3")) {
            d = "Tu";
        } else if (d.equals("4")) {
            d = "We";
        } else if (d.equals("5")) {
            d = "Th";
        } else if (d.equals("6")) {
            d = "Fr";
        } else {
            d = "Sa";
        }

        days = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        if (month.equals("0")) {
            month = "Jan";
        } else if (month.equals("1")) {
            month = "Feb";
        } else if (month.equals("2")) {
            month = "Mar";
        } else if (month.equals("3")) {
            month = "Apr";
        } else if (month.equals("4")) {
            month = "May";
        } else if (month.equals("5")) {
            month = "Jun";
        } else if (month.equals("6")) {
            month = "Jul";
        } else if (month.equals("7")) {
            month = "Aug";
        } else if (month.equals("8")) {
            month = "Sep";
        } else if (month.equals("9")) {
            month = "Oct";
        } else if (month.equals("10")) {
            month = "Nov";
        } else {
            month = "Dec";
        }

        date.setText(d + ", " + days + "/" + month);
    }

  /*  private class ViewFlipper extends BaseAdapter {
        List<Pojo> listpojos;
        Context context;

        public ViewFlipper(List<Pojo> listpojos, Context context) {
            this.listpojos = listpojos;
            this.context = context;
        }

        @Override
        public int getCount() {
            return listpojos.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.deals_services, parent, false);

            ImageView image = view.findViewById(R.id.image);
            TextView perc = view.findViewById(R.id.perc);
            TextView price = view.findViewById(R.id.price);
            TextView name = view.findViewById(R.id.name);

            perc.setText(listpojos.get(position).getTitle());
            price.setText(listpojos.get(position).getPrice());
            image.setImageResource(listpojos.get(position).getImage());
            name.setText(listpojos.get(position).getName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toast.makeText(context, name.getText()+" selected", Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }
    }

    public class MyAdapter extends BaseAdapter {
        Context context;
        List<Pojo> listpojos;

        public MyAdapter(Context context, List<Pojo> listpojos) {
            this.context = context;
            this.listpojos = listpojos;
        }

        @Override
        public int getCount() {
            return listpojos.size();
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

            view = LayoutInflater.from(context).inflate(R.layout.recent_updates, parent, false);

            ImageView image = view.findViewById(R.id.image);
            TextView title = view.findViewById(R.id.title);
            TextView price = view.findViewById(R.id.price);

            title.setText(listpojos.get(position).getTitle());
            price.setText(listpojos.get(position).getPrice());
            image.setImageResource(listpojos.get(position).getImage());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context.getApplicationContext(), OrderActivity.class);
                    intent.putExtra("image",listpojos.get(position).getImage());
                    intent.putExtra("price",listpojos.get(position).getPrice());
                    intent.putExtra("title",listpojos.get(position).getTitle());
                    intent.putExtra("class",OrderActivity.class);
                    startActivity(intent);

                    // Toast.makeText(context, title.getText()+" selected", Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }
    }*/
}
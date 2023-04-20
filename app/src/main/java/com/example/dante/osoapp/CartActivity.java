package com.example.dante.osoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    int r;
    RelativeLayout addnewitem,checkout;
    TextView card;
    ListView cart;
    CardView check;
    List<CartPojo> cartlist=new ArrayList<>();
    List<CartPojo> category;
    Intent intent=getIntent();
    ArrayList<String > data=new ArrayList<>();
    TextView Total;
    int mq=0;
    ScrollView scrollView;
    ImageButton back;

    int sum=0;
    int c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        back= (ImageButton) findViewById(R.id.back);
        check= (CardView) findViewById(R.id.check);
        scrollView= (ScrollView) findViewById(R.id.scroll);
        cart= (ListView) findViewById(R.id.cart_list);
       card= (TextView) findViewById(R.id.empty_cart);
        addnewitem= (RelativeLayout) findViewById(R.id.layout3);
       checkout= (RelativeLayout) findViewById(R.id.checkout);
       Total= (TextView) findViewById(R.id.total);
       r=R.drawable.ic_baseline_horizontal_rule_24;
        cartlist=new ArrayList<>();
        CartAdapter1();
        if(cartlist.size()<=0){
       cart.setVisibility(View.GONE);
       card.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(this, "Cart="+intent.getStringArrayListExtra("name").toString(), Toast.LENGTH_SHORT).show();
        CartAdapter adapter=new CartAdapter(this,cartlist);
        cart.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void CartAdapter1() {

        SharedPreferences sharedPreferences=getSharedPreferences("cart",MODE_PRIVATE);

        Gson gson=new Gson();

        String json=sharedPreferences.getString("cart",null);
         Type type=new TypeToken<List<CartPojo>>() {}.getType();
          cartlist=gson.fromJson(json,type);
          if(cartlist == null){
              cartlist=new ArrayList<>();
          }

    }
    private void SaveCart1(){
        SharedPreferences sharedPreferences=getSharedPreferences("items",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json= gson.toJson(category);
        editor.putString("items",json);
        editor.apply();
        // Toast.makeText(this, "Cart Saved", Toast.LENGTH_SHORT).show();
    }
    public class CartAdapter extends BaseAdapter{

        Context context;
        List<CartPojo> cartPojoList;

        public CartAdapter(Context context, List<CartPojo> cartPojoList) {
            this.context = context;
            this.cartPojoList = cartPojoList;
        }

        @Override
        public int getCount() {
            return cartPojoList.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.cartlayout, parent, false);

            ImageView image = view.findViewById(R.id.image);
            TextView title = view.findViewById(R.id.name);
            TextView price = view.findViewById(R.id.price);
            TextView count=view.findViewById(R.id.count);
            ImageButton max=view.findViewById(R.id.max);
            ImageButton min=view.findViewById(R.id.min);
            CheckBox checked=view.findViewById(R.id.item_checked);
            c=cartPojoList.get(position).getNumber(); //+1;
            String p=cartPojoList.get(position).getPrice();
            int total1=Integer.parseInt(p);


            title.setText(cartPojoList.get(position).getName());
            price.setText("Ksh. "+cartPojoList.get(position).getPrice().replaceAll("[^0-9]",""));
           // image.setImageResource(cartPojoList.get(position).getImage1());
            Glide.with(context).load(cartPojoList.get(position).getImage()).into(image);
            count.setText(String.valueOf(cartPojoList.get(position).getNumber()));
            int total=Integer.parseInt(cartPojoList.get(position).getPrice());

            sum +=total;
           // Total.setText("Ksh. "+sum);

            checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if(checked.isChecked()){
                      // Total.setText(" ");
                       int tot=Integer.parseInt(Total.getText().toString());
                       int pr=Integer.parseInt(cartPojoList.get(position).getPrice());
                       int num=Integer.parseInt(count.getText().toString().trim());
                       tot+=(pr*num);
                       Total.setText(String.valueOf(tot));
                        mq++;

                    } else if (!checked.isChecked()) {
                       int tot=Integer.parseInt(Total.getText().toString());
                       int pr=Integer.parseInt(cartPojoList.get(position).getPrice());
                       int num=Integer.parseInt(count.getText().toString().trim());

                       tot -=(pr*num);
                       Total.setText(String.valueOf(tot));
                       mq--;
                   }
                   // Toast.makeText(CartActivity.this, "checked: "+mq, Toast.LENGTH_SHORT).show();
                }
            });


          check.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String p,n,num,im;
                  category=new ArrayList<>();

                  for (CartPojo pojo:cartPojoList) {
                      if(checked.isChecked()) {
                          p = pojo.getName();
                          n = pojo.getPrice();
                          im = pojo.getImage();
                          num=String.valueOf(pojo.getNumber());
                          //num = count.getText().toString();
                          category.add(new CartPojo(Integer.parseInt(num), p, n, im));
                      }
                  }
                  SaveCart1();
                  Intent intent1=new Intent(getApplicationContext(),Payment.class);
                  intent1.putExtra("subtotal",Total.getText().toString());
                  startActivity(intent1);
                  finish();
              }
          });
            for (CartPojo pojo:cartPojoList) {
                c=0;
                max.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(checked.isChecked()) {
                            int tot = Integer.parseInt(Total.getText().toString());
                            int pr = Integer.parseInt(pojo.getPrice());
                            int num = pojo.getNumber();
                            tot += (pr);
                            Total.setText(String.valueOf(tot));
                            pojo.setNumber(tot);
                        }
                        c++;
                        count.setText(String.valueOf(c));

                        // min.setImageDrawable(r);
                        min.setImageResource(r);
                    }
                });

            }
            for(CartPojo pojo:cartPojoList){
                min.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(checked.isChecked() && c>1) {
                            int tot = Integer.parseInt(Total.getText().toString());
                            int pr = Integer.parseInt(cartPojoList.get(position).getPrice());
                            // int num = cartPojoList.get(position).getNumber();
                            tot -= (pr);
                            c--;
                            Total.setText(String.valueOf(tot));
                        } else if (checked.isChecked() && c==1) {
                            min.setImageResource(R.drawable.ic_baseline_delete_outline_24);
                            int tot = Integer.parseInt(Total.getText().toString());
                            int pr = Integer.parseInt(cartPojoList.get(position-1).getPrice());
                            // int num = cartPojoList.get(position).getNumber();
                            tot -= (pr);
                            Total.setText(String.valueOf(tot));
                        } else if ((checked.isChecked() && c<1) || (!checked.isChecked() && c<1)) {
                            if(cartlist.size()>1) {
                                cartPojoList.remove(position-1);
                            }
                            else{
                                cartPojoList.remove(position);
                            }
                        }
                        else if(cartPojoList.size()==0){
                            Total.setText("0.0");
                        }
                        count.setText(String.valueOf(c));

                        // min.setImageDrawable(r);
                        min.setImageResource(r);
                    }
                });
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context, "position="+position, Toast.LENGTH_SHORT).show();


                }
            });
            return view;
        }
    }
}
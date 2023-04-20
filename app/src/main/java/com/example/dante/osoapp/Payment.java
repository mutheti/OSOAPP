package com.example.dante.osoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Payment extends AppCompatActivity implements OnMapReadyCallback {
    FirebaseFirestore db;
    GoogleMap googleMap;
    TextView Total,Subtotal,Del_Cost,Del_Address;
    ListView list;
    CardView order;
    List<CartPojo> receipt_items=new ArrayList<>();
    List<CartPojo> orderedsnackList;
    ImageButton back;
    String orderid,deliAddress,customer_email,total_price;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        back= (ImageButton) findViewById(R.id.back);
        Total= (TextView) findViewById(R.id.total);
        Subtotal= (TextView) findViewById(R.id.subtotal);
        Del_Cost= (TextView) findViewById(R.id.delivery_cost);
        Del_Address= (TextView) findViewById(R.id.delivery_address);
        list= (ListView) findViewById(R.id.list);
        order= (CardView) findViewById(R.id.order);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Submittig Your Order");

       // receipt_items=new ArrayList<>();
        RecieptItems();
       //ReceiptList();
      //  cartPojoList=new ArrayList<>();
        ReceiptAdapter adapter=new ReceiptAdapter(receipt_items,this);
        list.setAdapter(adapter);
        Intent intent=getIntent();
        Subtotal.setText(intent.getStringExtra("subtotal"));
        Del_Cost.setText("100");
        int total=100+Integer.parseInt(intent.getStringExtra("subtotal"));
        Total.setText(String.valueOf(total));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


       // Toast.makeText(this, "Trans Code: "+GenerateTransactionCode(5), Toast.LENGTH_SHORT).show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPurchasedSnacks();
                AddToOrder();
            }
        });

    }

    private void GetPurchasedSnacks() {
        orderedsnackList=new ArrayList<>();
        orderedsnackList=receipt_items;
        orderid=GenerateTransactionCode(6);
        deliAddress=Del_Address.getText().toString();
        customer_email=mAuth.getCurrentUser().getEmail();
        total_price=Total.getText().toString();
    }
    private void AddToOrder(){
        progressDialog.show();
       if(mAuth.getCurrentUser().getUid().isEmpty()){
           Intent intent=new Intent(getApplicationContext(),AccountActivity.class);
           startActivity(intent);
       }else{
           
           OrderPojo itemModel=new OrderPojo(orderid,customer_email,deliAddress,"","pending",total_price,orderedsnackList);
           firebaseFirestore.collection("order").add(itemModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
               @Override
               public void onSuccess(DocumentReference documentReference) {
                   itemModel.setId(documentReference.getId());
                   firebaseFirestore.collection("order").document(itemModel.getId()).set(
                           itemModel, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                           progressDialog.dismiss();
                           Intent intent=new Intent(getApplicationContext(),VerifiedOrder.class);
                           startActivity(intent);
                           finish();
                           Toast.makeText(Payment.this, "Order Submitted. ", Toast.LENGTH_SHORT).show();

                       }
                   });
               }

           });
       }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setTrafficEnabled(true);


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    private void RecieptItems() {
try {


    SharedPreferences sharedPreferences = getSharedPreferences("items", MODE_PRIVATE);

    Gson gson = new Gson();

    String json = sharedPreferences.getString("items", null);
    Type type = new TypeToken<List<CartPojo>>() {
    }.getType();

     receipt_items=gson.fromJson(json,type);

    if (receipt_items == null) {
        receipt_items = new ArrayList<>();
    }
} catch (Exception e){
    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
}
    }

    private String GenerateTransactionCode(int l){
      String AlphaNum="QWERTYUIOPASDFGHJKLMNBVCXZqwertyuioplkjhgfdsazxcvbnm1234567890";
      StringBuilder code=new StringBuilder(l);
        for (int i = 0; i <l ; i++) {
            int ch=(int)(AlphaNum.length()*Math.random());
            code.append(AlphaNum.charAt(ch));
        }
        return code.toString();
    }

    public class ReceiptAdapter extends BaseAdapter{

        List<CartPojo> receiptpojolist;
        Context context;

        public ReceiptAdapter(List<CartPojo> receiptpojolist, Context context) {
            this.receiptpojolist = receiptpojolist;
            this.context = context;
        }

        @Override
        public int getCount() {
            return receiptpojolist.size();
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

            view = LayoutInflater.from(context).inflate(R.layout.payment_receipt_items, parent, false);

            TextView name=view.findViewById(R.id.name);
            TextView price=view.findViewById(R.id.price);
            TextView quantity=view.findViewById(R.id.quantity);
            name.setText(receiptpojolist.get(position).getName());
            price.setText(receiptpojolist.get(position).getPrice());
            quantity.setText(String.valueOf(receiptpojolist.get(position).getNumber()));



            return view;
        }
    }

}
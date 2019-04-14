package com.example.project8.foodvoodapp;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        final TextView amountTv = findViewById(R.id.amount);
        final TextView nameTv = findViewById(R.id.item);
        final TextView itemTv = findViewById(R.id.item1);
        final TextView quantityTv = findViewById(R.id.quantity);

        final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mFirebaseRef = database.getReference(androidId);
        if (mFirebaseRef != null) {

            mFirebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = (String) dataSnapshot.child(getString(R.string.name)).getValue();
                    nameTv.setText(name);
                    String item = (String) dataSnapshot.child(getString(R.string.item)).getValue();
                    itemTv.setText(item);
                    String am = (String) dataSnapshot.child(getString(R.string.amount)).getValue();
                    amountTv.setText(am);
                    String q = (String) dataSnapshot.child(getString(R.string.quantity)).getValue();
                    quantityTv.setText(q);

                    Paper.init(OrderActivity.this);
                    Paper.book().write(getString(R.string.text), name + "\n" + item + "\n" + am + "\n" + q + "\n");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

    }
}
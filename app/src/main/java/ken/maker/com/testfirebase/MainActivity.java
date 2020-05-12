package ken.maker.com.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button xBDNhietDo, xBDDoAm;
    TextView tv_nhietDoC, tvNhietDoF, tv_doam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xBDNhietDo = findViewById(R.id.btn_bd_nhietdo);
        xBDDoAm = findViewById(R.id.btn_bd_doam);
        tv_nhietDoC = findViewById(R.id.tv_nhietdo_c);
        tvNhietDoF = findViewById(R.id.tv_nhietdo_f);
        tv_doam = findViewById(R.id.tv_doam);

        xBDNhietDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NhietDoActivity.class);
                startActivity(intent);
            }
        });

        xBDDoAm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DoAmActivity.class);
                startActivity(intent);
            }
        });

        readData();
        loopData();

    }

    private void loopData() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(2000L);

                        readData();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();

    }

    private void readData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Device/EsptoMobile/nhietdo");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key  = ds.getKey();
                    if (key.contains("C")) {
                        tv_nhietDoC.setText(  ds.getValue(Integer.class)+ " *C");
                    }
                    if (key.contains("F")) {
                        tvNhietDoF.setText(  ds.getValue(Integer.class)+ " * F");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(eventListener);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Device/EsptoMobile/doam");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                DataSnapshot doam = (DataSnapshot) dataSnapshot.getChildren();
                tv_doam.setText(dataSnapshot.getValue() + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(valueEventListener);

    }

    private void controlLed() {

    }
}

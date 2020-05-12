package ken.maker.com.testfirebase;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NhietDoActivity extends AppCompatActivity {

    List<DataEntry> seriesData;
    AnyChartView anyChartView;
    Set set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhietdo);
        set = Set.instantiate();
        CreateChart();
        getListData();

    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }

    private void getListData() {
        DatabaseReference mDatabase;

        seriesData = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("test/update/nhietdobd");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Data", "ker : " + dataSnapshot.getKey() + " Value : = " + dataSnapshot.getValue());
                seriesData.add(new CustomDataEntry(dataSnapshot.getKey(),  Double.valueOf(dataSnapshot.getValue(String.class))));
                set.data(seriesData);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };

        mDatabase.addChildEventListener(childEventListener);

    }

    private void CreateChart() {
        anyChartView = findViewById(R.id.any_chart_view);

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Biểu đồ nhiệt độ.");

        cartesian.yAxis(0).title("Đơn vị Độ C ");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Brandy");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }

}

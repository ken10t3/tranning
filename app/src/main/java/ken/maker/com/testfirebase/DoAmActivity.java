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
import com.anychart.charts.Cartesian3d;
import com.anychart.core.cartesian.series.Column3d;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.HoverMode;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.graphics.vector.SolidFill;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoAmActivity extends AppCompatActivity {
    AnyChartView anyChartView;

    List<DataEntry> seriesData;
    Set set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doam);
        anyChartView = findViewById(R.id.chartDoAm);
        set = Set.instantiate();
        createChart();
        getData();
    }

    private void getData() {
        seriesData = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("test/update/doambd");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                seriesData.add(new CustomDataEntry(dataSnapshot.getKey(), Double.valueOf(dataSnapshot.getValue(String.class))));
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
        databaseReference.addChildEventListener(childEventListener);
    }

    private void createChart() {

        Cartesian3d column3d = AnyChart.column3d();

        column3d.yScale().stackMode(ScaleStackMode.VALUE);

        column3d.animation(true);

        column3d.title("Types of Coffee");
        column3d.title().padding(0d, 0d, 15d, 0d);

        set.data(seriesData);
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

        Column3d series1 = column3d.column(series1Data);
        series1.name("Espresso");
        series1.fill(new SolidFill("#3e2723", 1d));
        series1.stroke("1 #f7f3f3");
        series1.hovered().stroke("3 #f7f3f3");

        column3d.legend().enabled(true);
        column3d.legend().fontSize(13d);
        column3d.legend().padding(0d, 0d, 20d, 0d);

        column3d.yScale().ticks("[0, 1, 2, 3, 4, 5]");
        column3d.xAxis(0).stroke("1 #a18b7e");
        column3d.xAxis(0).labels().fontSize("#a18b7e");
        column3d.yAxis(0).stroke("1 #a18b7e");
        column3d.yAxis(0).labels().fontColor("#a18b7e");
        column3d.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        column3d.yAxis(0).title().enabled(true);
        column3d.yAxis(0).title().text("Portions of Ingredients");
        column3d.yAxis(0).title().fontColor("#a18b7e");

        column3d.interactivity().hoverMode(HoverMode.BY_X);

        column3d.tooltip()
                .displayMode(TooltipDisplayMode.UNION)
                .format("{%Value} {%SeriesName}");

        column3d.yGrid(0).stroke("#a18b7e", 1d, null, null, null);

        anyChartView.setChart(column3d);
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }
}

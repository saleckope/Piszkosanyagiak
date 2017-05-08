package com.example.saleckope.piszkosanyagiak.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.saleckope.piszkosanyagiak.R;
import com.example.saleckope.piszkosanyagiak.model.MoneyItem;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class StatisticsGraphicsFragment extends Fragment {
    private GraphView graphViewAll;
    private GraphView graphViewCategory;
    private EditText yearEditText;
    private EditText monthEditText;
    private Button showButton;
    private List<MoneyItem> items = new ArrayList<MoneyItem>();
    private Calendar calendar = Calendar.getInstance();
    private int thisYear = calendar.get(Calendar.YEAR);
    private int thisMonth = calendar.get(Calendar.MONTH) + 1;
    private LineGraphSeries<DataPoint> seriesLine;
    private BarGraphSeries<DataPoint> seriesBar;
    private String[] label;
    private int sizeOfCategory = MoneyItem.Category.getSize();
    String[] horizontalLabels = new String[sizeOfCategory];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Reverse sorted list
        List<MoneyItem> reverseList = new ArrayList<>();

        //Get intent extra data
        reverseList = (ArrayList<MoneyItem>) getActivity().getIntent().getExtras().getSerializable("LIST");

        //Reversing list for line graphs
        for (int i = reverseList.size() - 1; i >= 0; i--) {
            items.add(reverseList.get(i));
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inlfate layout graphics
        View view = inflater.inflate(R.layout.fragment_statistics_graphics, container, false);
        //Find elements
        graphViewAll = (GraphView) view.findViewById(R.id.graphAll);
        graphViewCategory = (GraphView) view.findViewById(R.id.graphCategory);
        yearEditText = (EditText) view.findViewById(R.id.thisYearEditText);
        monthEditText = (EditText) view.findViewById(R.id.thisMonthEditText);
        showButton = (Button) view.findViewById(R.id.showButton);

        //Set elements
        yearEditText.setText(Integer.toString(thisYear));
        monthEditText.setText(Integer.toString(thisMonth));

        //User can change year and month with clicking button
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set new year and month
                thisYear = Integer.parseInt(yearEditText.getText().toString());
                thisMonth = Integer.parseInt(monthEditText.getText().toString());

                //Redraw graphs
                refreshLineGraph();
                refreshBarGraph();
            }
        });

        //create graphs
        createLineGraph();
        createBarGraph();
        return view;
    }

    //Method for creating line graph
    public void createLineGraph() {
        //Data os the line
        seriesLine = new LineGraphSeries<>(generateData());
        //Create the graph
        graphViewAll.addSeries(seriesLine);

        //There are no labels
        graphViewAll.getGridLabelRenderer().setHorizontalLabelsVisible(false);
    }

    //Refresh line graph with new data
    public void refreshLineGraph() {
        seriesLine.resetData(generateData());
    }

    //Generate data for line graph
    public DataPoint[] generateData() {
        List<DataPoint> dataPoint = new ArrayList<>();
        int sum;
        int xCoord = 0;

        //Calculate data from items list
        for (int i = 0; i < items.size(); i++) {
            sum = 0;
            for (int j = 0; j <= i; j++) {
                //Calculate sum money
                sum += items.get(j).amount;
            }
            //Add data only if month and year are the wanted month and year
            if (items.get(i).year == thisYear && items.get(i).month == thisMonth) {
                //The 0 element of data is the previous month's last data
                if (xCoord == 0) {
                    DataPoint d = new DataPoint(xCoord, sum - items.get(i).amount);
                    xCoord++;
                    dataPoint.add(d);
                }
                //Add data
                DataPoint d = new DataPoint(xCoord, sum);
                xCoord++;
                dataPoint.add(d);
            }
        }

        //Copy data from list to array
        DataPoint[] d = new DataPoint[dataPoint.size()];
        label = new String[dataPoint.size()];
        for (int i = 0; i < dataPoint.size(); i++) {
            d[i] = dataPoint.get(i);
        }

        //return with the data array
        return d;
    }

    //Method fro creating bar graph
    public void createBarGraph() {
        //Get data for bar graph
        seriesBar = new BarGraphSeries<>(generateBarData(horizontalLabels));
        //Add data to graph
        graphViewCategory.addSeries(seriesBar);

        //Set color different for each bar
        seriesBar.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });

        //Set spacing
        seriesBar.setSpacing(30);
        //Set value shown
        seriesBar.setDrawValuesOnTop(true);
        seriesBar.setValuesOnTopColor(Color.BLACK);
        seriesBar.setValuesOnTopSize(10);

        //Set horizontal labels to category names
        StaticLabelsFormatter staticLabelsFormatterBar = new StaticLabelsFormatter(graphViewCategory);
        staticLabelsFormatterBar.setHorizontalLabels(horizontalLabels);
        graphViewCategory.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatterBar);

        //Labels are in 90° angle
        graphViewCategory.getGridLabelRenderer().setHorizontalLabelsAngle(90);
    }

    //Method for refreshing bar graph data
    public void refreshBarGraph() {
        seriesBar.resetData(generateBarData(horizontalLabels));
    }

    //Method for generating data to bar graph
    public DataPoint[] generateBarData(String[] horizontalLabels) {
        //Data is with size os category size
        DataPoint[] dataPoint = new DataPoint[sizeOfCategory];
        int sum = 0;

        //Calculate data
        for (int i = 0; i < sizeOfCategory; i++) {
            for (int j = 0; j < items.size(); j++) {
                //Add data only if month and year are the wanted month and year
                //Add data to the right category
                if (items.get(j).category == MoneyItem.Category.getByOrdinal(i)
                        && items.get(j).year == thisYear && items.get(j).month == thisMonth) {
                    sum += Math.abs(items.get(j).amount);
                }
            }
            //Set horizontal label to category name
            horizontalLabels[i] = MoneyItem.Category.getByOrdinal(i).name();
            //Add data
            DataPoint d = new DataPoint(i, sum);
            dataPoint[i] = d;
            sum = 0;
        }

        //return data array
        return dataPoint;
    }
}
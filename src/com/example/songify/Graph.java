package com.example.songify;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

public class Graph extends ActionBarActivity{
	
	public GraphView graph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_layout);
		setTitle("New Activity");
		graph  = (GraphView) findViewById(R.id.graph);
		//graph.setTitle("Data points");
		addDataPoints(2,1.2);
	}
	 @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	 
	public void addDataPoints(int x, double y) {
		//GraphView graph = (GraphView) findViewById(R.id.graph);
		PointsGraphSeries<DataPoint> series1 = new PointsGraphSeries<DataPoint>(new DataPoint[] {
		          new DataPoint(0, 1),
		          new DataPoint(1, 5)
		});
		
		/*PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<DataPoint>(new DataPoint[] {
		          
		          new DataPoint(15, 30),
		          new DataPoint(16, 1),
		          new DataPoint(20, 18.6)
		});*/
		
		graph.addSeries(series1);
		//graph.addSeries(series2);
		series1.setSize(3);
		series1.setColor(Color.BLUE);
		//series2.setSize(3);
		//series2.setColor(Color.BLUE);
		/*graph.getGridLabelRenderer().setVerticalAxisTitle("something (dB)");
		graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.RED);
		graph.getGridLabelRenderer().setHorizontalAxisTitle("seconds (s)");
		graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.RED);*/
	}
	
}


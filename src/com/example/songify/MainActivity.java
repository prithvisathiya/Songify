package com.example.songify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;
//import com.google.common.collect.Multiset;
//import com.google.common.collect.HashMultiset;
//import java.util.Collection;

public class MainActivity extends ActionBarActivity {
	private static final int RECORDER_SAMPLERATE = 8000;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private boolean recording = false;
	private Thread thread;
	int l =0;
	AudioRecord recorder;
	Handler handler;
	
	List<DataPoint> datas = new ArrayList<DataPoint>();
	List<Short> amps = new ArrayList<Short>();
	List<DataPoint> ampsG = new ArrayList<DataPoint>();
	List<Double> majority = new ArrayList<Double>();
	Map<Double, Integer> map = new HashMap<Double, Integer>();
	int counter;
	
    int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, 
    		RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
    
    double[] currentFreq = {130.8, 146.83, 164.81, 174.61, 185.0, 196.0, 207.65, 220.0, 233.08, 246.9, 261.6, 277.2, 293.7, 311.1, 329.6, 349.2, 369.9, 392.0, 415.3, 440.0, 466.2, 493.88, 523.25, 554.37, 587.33, 622.25, 659.25, 698.46, 739.99, 783.99, 830.61, 880.00, 932.33, 987.77, 1046.50, 1108.73, 1174.66, 1244.51, 1318.51, 1396.91};
    String[] currentNote = {"C3", "D3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", "C4", "C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4","C5", "C#5", "D5", "D#5", "E5", "F5", "F#5", "G5", "G#5", "A5", "A#5", "B5","C6", "C#6", "D6", "D#6", "E6", "F6"};
    //int last = currentFreq.length;
    @Override
      
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);    
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private void Record() {
    	
    	System.out.println("bufferSize is " + bufferSize);
    	if(!recording){
	    	recording = true;
	    	
	    	recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
	                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
	                RECORDER_AUDIO_ENCODING, bufferSize);
	    	recorder.startRecording();
	    	thread = new Thread(new Runnable() {
	            public void run() {
	            	
	            	int size = bufferSize;
	            	
	            	
	            	
	            	while (recording){
	            		short[] audioSamples = new short[size];
	            		int read = recorder.read(audioSamples, 0, size);
	       
	            		double frequency = getFreq(RECORDER_SAMPLERATE, audioSamples);
	            		if (counter < 10){
	            			if (map.containsKey(frequency)) {
	            				map.put(frequency, map.get(frequency) + 1);
	            			}
	            			else
	            				map.put(frequency, 1);
	            			System.out.println(frequency);
	            			counter++;
	            		}
	            		
	            		else {	            			
	            			int max =0; 
	            			double majFreq = 0;
	            			for(double num : map.keySet()) {
	            				if(map.get(num) > max){
	            					max = map.get(num);
	            					majFreq = num;
	            				}
	            			}         			
	            			int k = l + 10;
	            			for (int i = l; i < k; i++) {
	            				datas.add(new DataPoint(l++,majFreq));
	            				//System.out.println(majFreq);
		            		}
	            			System.out.println("majFreq is " + majFreq);
	            			//datas.add(new DataPoint(l++, frequency));
	            			map.clear();
	            			counter = 0;
	            		}
	            		
	            		/*if(frequency != 0){
		            		int noteIndex = findNoteRec(currentFreq, frequency, 0, currentFreq.length - 1);
		            		
		            		if (noteIndex >= 0){
		            			System.out.println(frequency + " " + currentNote[noteIndex]);
		            			datas.add(new DataPoint(l++, frequency));
		            			
		            		}
		            			
		            		else{
		            			System.out.println(frequency + " " + noteIndex);
		            			datas.add(new DataPoint(l++, 0));
		            		}
	            		}
	            		else {
	            			System.out.println(frequency);
	            			datas.add(new DataPoint(l++, 0));
	            		}*/
	            		
	            	}
	            	
	            }
	        });
	    	thread.start();
    	}
    }
    
    private double getFreq(int sampRate, short[] audioData) {
    	int numCrossing = 0;
    	int size = audioData.length;
		for (int p=0; p<size/2; p+=4) {
			 /*if(audioData[p]<300 || audioData[p+1]<300 || audioData[p+2]<300 || audioData[p+3]<300)
				 return 0;*/
			 /*amps.add(audioData[p]);
			 amps.add(audioData[p+1]);
			 amps.add(audioData[p+2]);
			 amps.add(audioData[p+3]);*/
			
			 /*ampsG.add(new DataPoint(l++, audioData[p]));
			 ampsG.add(new DataPoint(l++, audioData[p+1]));
			 ampsG.add(new DataPoint(l++, audioData[p+2]));
			 ampsG.add(new DataPoint(l++, audioData[p+3]));*/
			 
			 if (audioData[p]>0 && audioData[p+1]<=0) 
				 numCrossing++;
			 else if (audioData[p]<0 && audioData[p+1]>=0) 
            	 numCrossing++;
             if (audioData[p+1]>0 && audioData[p+2]<=0) 
            	 numCrossing++;
             else if (audioData[p+1]<0 && audioData[p+2]>=0) 
            	 numCrossing++;
             if (audioData[p+2]>0 && audioData[p+3]<=0) 
            	 numCrossing++;
             else if (audioData[p+2]<0 && audioData[p+3]>=0) 
            	 numCrossing++;
             if (audioData[p+3]>0 && audioData[p+4]<=0) 
            	 numCrossing++;
             else if (audioData[p+3]<0 && audioData[p+4]>=0) 
            	 numCrossing++;
        }
    
		for (int p=(size/8)*4; p<size/2-1; p++) {
             if (audioData[p]>0 && audioData[p+1]<=0) numCrossing++;
             if (audioData[p]<0 && audioData[p+1]>=0) numCrossing++;
        }

		boolean validate = false;
		int i = 0;
		while(i < size && validate == false) {
			if (audioData[i] > 20000 || audioData[i] < -20000)
				validate = true;
			i++;
		}
		
		double freq;
		if (validate)
			freq = (2*RECORDER_SAMPLERATE/(double)size)*((double)numCrossing/2);
		else
			return 0;
		while(freq > 466.2)
			freq = freq/2;
		
		return freq;
    }
        
    
    private int findNoteRec(double[] arr, double x, int first, int last) {
         if (first > last) 
        	 return -1; 
         int mid = (first + last)/2;
         
         double leftBound;
         double upperBound;
         if(mid > 0)
        	 leftBound = arr[mid] - (arr[mid] - arr[mid-1]) / 2;
         else
        	 leftBound = arr[mid] - 6.25;
         if(mid < arr.length -1)
        	 upperBound = arr[mid] + (arr[mid + 1] - arr[mid]) / 2;
         else
        	 upperBound = arr[mid] + 6.25;
         
         if (x >= leftBound && x <= upperBound) 
        	 return mid;
         
         /*if (x >= arr[mid] - 6.5 && x <= arr[mid] + 6.5) 
        	 return mid;*/
         else if (arr[mid] < x)
            return findNoteRec(arr, x, mid+1, last);
         else // last possibility: a[mid] > x
            return findNoteRec(arr, x, first, mid-1);
    }
    private void StopRecord() {
    	recording = false;
    	recorder.stop();
    	recorder.release();
    	System.out.println("stopped");
    	recorder = null;
    	thread = null;
    }

    public void authenticate(View v) {
    	Button btn = (Button) findViewById(R.id.startRecord);
    	if(recording){
    		StopRecord();
    		
    		DataPoint[] insertableDatas = new DataPoint[datas.size()];
    		datas.toArray(insertableDatas);
    		GraphView graph  = (GraphView) findViewById(R.id.graph);
    		
    		graph.removeAllSeries();
    		PointsGraphSeries<DataPoint> series1 = new PointsGraphSeries<DataPoint>(insertableDatas);    		
    		series1.setSize(1);
    		series1.setColor(Color.RED);
    		graph.getGridLabelRenderer().setVerticalAxisTitle("Frequency");
    		graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(2);
    		graph.getViewport().setXAxisBoundsManual(true);
    		graph.getViewport().setMaxX(100);
    		graph.getViewport().setMinX(0);
    		
    		graph.getViewport().setYAxisBoundsManual(true);
    		graph.getViewport().setMaxY(500);
    		graph.getViewport().setMinY(0);
    		graph.getViewport().setScrollable(true);
    		graph.addSeries(series1);
    		System.out.println("number of samples is " + ampsG.size());
    		ampsG.clear();
    		
    		/*for(int i = 0; i < amps.size(); i ++)
    			System.out.println(amps.get(i));*/
    		
    		btn.setText("Record");
    		/*for(DataPoint newpoint : insertableDatas) {
    			System.out.println(newpoint.getX() + " " + newpoint.getY());
    		}*/
    		
    		datas.clear();
    		l = 0;
    	}
    	else {
    		
    		
    		Record();
    		btn.setText("Stop Record");
    		
    		/*GraphView graph  = (GraphView) findViewById(R.id.graph);
    		PointsGraphSeries<DataPoint> series1 = new PointsGraphSeries<DataPoint>(new DataPoint[] {
  		          new DataPoint(0, 1),
  		          new DataPoint(1, 5)
    		});
    		PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<DataPoint>(new DataPoint[] {
    		          new DataPoint(8, 1),
    		          new DataPoint(9, 100)
      		});
    		
    		series1.setSize(3);
    		series1.setColor(Color.BLUE);
    		series2.setSize(3);
    		series2.setColor(Color.BLUE);
    		graph.addSeries(series1);
    		graph.addSeries(series2);*/
    	}
    }
    
    public void graphIt(View v) {
    	Intent intent = new Intent(this, Graph.class);
    	startActivity(intent);
    }
}

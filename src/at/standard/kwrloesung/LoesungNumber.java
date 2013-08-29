package at.standard.kwrloesung;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoesungNumber extends SherlockActivity implements ActionBar.TabListener {
	int kwrNummer;
	Button myButton;
	EditText myEdit;
	TextView myText;
	TextView resultsView;
	String[] resultList;
	List<String> horizontalList = new ArrayList<String>();
	List<String> vertikalList = new ArrayList<String>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loesung_number);
        
        myButton = (Button)findViewById(R.id.button1);
        myEdit   = (EditText)findViewById(R.id.editText1);
        myText = (TextView)findViewById(R.id.textView1);
         
        myButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		Log.d("input", myEdit.getText().toString());
        		if (myEdit.getText().toString().length() != 4) {
        			Toast.makeText(getApplicationContext(), "Falsche Nummerneingabe!", Toast.LENGTH_LONG).show();
        		} else{
        			kwrNummer = Integer.parseInt(myEdit.getText().toString());
        			setContentView(R.layout.show_results);
        			getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        			newTab("Horizontal");
        			newTab("Vertikal");
        			
        			try {
        				Log.d("Reults", "Success");
						showResults();
					} catch (IOException e) {
						Log.d("Reults", "Exception");
						horizontalList.add("Error: Keine Internetverbindung");
						vertikalList.add("Error: Keine Internetverbindung");
						e.printStackTrace();
					} catch (InterruptedException e) {
						Log.d("Reults", "Exception");
						horizontalList.add("Error: Keine Internetverbindung");
						vertikalList.add("Error: Keine Internetverbindung");
						e.printStackTrace();
					} catch (ExecutionException e) {
						Log.d("Reults", "Exception");
						horizontalList.add("Error: Keine Internetverbindung");
						vertikalList.add("Error: Keine Internetverbindung");
						e.printStackTrace();
					}
        			
        		}
        			
        	}
        });
    }

    private void newTab(String name) {
    	ActionBar.Tab tab = getSupportActionBar().newTab();
        tab.setText(name);
        tab.setTabListener(this);
        getSupportActionBar().addTab(tab);
    }
    
    private void showResults() throws IOException, InterruptedException, ExecutionException {
    	ProgressDialog pD = new ProgressDialog(this);
    	pD.setTitle("Download");
    	pD.setMessage("Lösung von Rätsel " + kwrNummer + " wird verarbeitet.");
    	pD.setProgressStyle(0);
    	pD.setCancelable(true);
    	pD.show();
		String body = "ExternalId=" + kwrNummer;
		AsyncTask<String, Integer, String> res =  new GetPostData().execute(body);

		String results = res.get();
		int start = results.indexOf("[");
		int end = results.indexOf("]");
		if ((end - start) == 1) {
			results = "";
		} else {
			results = results.substring(start + 1, end - 1);
		}
		resultList = results.split("\\},\\{");
		for (int i = 0; i < resultList.length; i++) {
			if (resultList[i].endsWith("true")) {
				String[] line = resultList[i].split(",");
				String[] frage = line[0].split(":");
				String [] antwort = line[1].split(":");
				String f = "Frage " + frage[1] + ":";
				String a = antwort[1].substring(1, antwort[1].length() - 1);
				horizontalList.add(f + "\n\t" + a);
			} else if (resultList.length > 1) {
				String[] line = resultList[i].split(",");
				String[] frage = line[0].split(":");
				String [] antwort = line[1].split(":");
				String f = "Frage " + frage[1] + ":";
				String a = antwort[1].substring(1, antwort[1].length() - 1);
				vertikalList.add(f + "\n\t" + a);
			}
		}
		if (horizontalList.size() == 0 && vertikalList.size() == 0) {
			horizontalList.add("Keine Daten verfügbar!");
			vertikalList.add("Keine Daten verfügbar!");
		}
		
		pD.dismiss();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (tab.getText() == "Horizontal") {
			ListView lv = (ListView) findViewById(R.id.listView);
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, horizontalList);
			lv.setAdapter(arrayAdapter); 
		}
		if (tab.getText() == "Vertikal") {
			ListView lv = (ListView) findViewById(R.id.listView);
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, vertikalList);
			lv.setAdapter(arrayAdapter); 
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		//boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;
		menu.add("New")
        //.setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}
}

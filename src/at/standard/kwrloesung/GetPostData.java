package at.standard.kwrloesung;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

class GetPostData extends AsyncTask<String, Integer, String> {

	protected String doInBackground(String... body) {
    	try {
    	URL url = new URL("http://www.derstandard.at/RaetselApp/Home/GetCrosswordResult");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(body[0]);
		writer.flush();

		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String result = "";
		for (String line; (line = reader.readLine()) != null;)
		{
			result += line;
		}

		writer.close();
		reader.close();
		
		return result;
    	} catch (IOException e){
    		Log.e("POST", e.getMessage());
    		return e.getMessage();
    	}
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    }
    
    protected void onPreExecute () {
    }
}
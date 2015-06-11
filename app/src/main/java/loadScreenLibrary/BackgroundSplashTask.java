package loadScreenLibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by Ahmed on 11.06.2015.
 */
public class BackgroundSplashTask extends AsyncTask<Void, Integer, Void> {

    private ProgressDialog progressDialog;
    private Activity acty;

    public BackgroundSplashTask(Activity activity){
        acty = activity;
    }

    //First call from the AsyncTask
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Create a new progress dialog
        progressDialog = new ProgressDialog(acty);
        //Set the progress dialog to display a horizontal progress bar
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //Set the dialog title to 'Loading...'
        progressDialog.setTitle("Loading");
        //Set the dialog message to 'Loading application View, please wait...'
        progressDialog.setMessage("Loading the SunSystem View, please wait...");
        //This dialog can't be canceled by pressing the back key
        progressDialog.setCancelable(false);
        //This dialog isn't indeterminate
        progressDialog.setIndeterminate(false);
        //The maximum number of items is 100
        progressDialog.setMax(100);
        //Set the current progress to zero
        progressDialog.setProgress(0);
        //Display the progress dialog
        progressDialog.show();
    }


    //Second Call
    @Override
    protected Void doInBackground(Void... arg0) {

        try {
            synchronized (this)
            {
                //Initialize an integer (that will act as a counter) to zero
                int counter = 0;
                //While the counter is smaller than four
                while(counter <= 4)
                {
                    //Wait 850 milliseconds
                    this.wait(2000);
                    //Increment the counter
                    counter++;
                    //Set the current progress.
                    //This value is going to be passed to the onProgressUpdate() method.
                    publishProgress(counter*25);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Third call
    @Override
    protected void onProgressUpdate(Integer... arg0){
        progressDialog.setProgress(arg0[0]);
    }

    //Last call
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
    }
}

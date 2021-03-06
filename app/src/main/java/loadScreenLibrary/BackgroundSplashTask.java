package loadScreenLibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by Ahmed, Artjem, Arndt  on 11.06.2015.
 * Some codes are taken from the tutorial
 * http://www.41post.com/4588/programming/android-coding-a-loading-screen-part-1
 */
public class BackgroundSplashTask extends AsyncTask<Void, Integer, Void> {

    private ProgressDialog progressDialog;
    private Activity acty;
    private int counter = 0;

    public BackgroundSplashTask(Activity activity){
        acty = activity;
    }

    //First call from the AsyncTask and create the progressDialog with the attributes in seperate thread
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

    //The code to be executed in a background thread.
    //The counter will be update by the PlanetManager and onDrawFrame.
    @Override
    protected Void doInBackground(Void... arg0) {
        while(counter <= 10)
        {
            publishProgress(counter*10);
        }
        return null;
    }

    //sets the attribute for the Dialog and update the progress
    @Override
    protected void onProgressUpdate(Integer... arg0){
        progressDialog.setProgress(arg0[0]);
    }

    //Last call from the AsyncTask
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

    //Sets the counter for the ProgressDialog
    public void setCounter(int i){
        counter = i;
    }

    //Gets the counter for the ProgressDialog
    public int getCounter(){
        return counter;
    }

    //Sets hidden the ProgressDialog
    public void setProgressDialogHidden(){
        progressDialog.dismiss();
    }

}

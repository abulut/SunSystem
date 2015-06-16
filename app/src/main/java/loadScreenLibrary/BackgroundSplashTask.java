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
    private int counter = 0;

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

        synchronized (this)
        {
            //Initialize an integer (that will act as a counter) to zero

            //try{
            //While the counter is smaller than four
                while(counter <= 10)
                {
                    //Wait 850 milliseconds

                    //this.wait(850);

                    //Increment the counter
                    //counter++;
                    //Set the current progress.
                    //This value is going to be passed to the onProgressUpdate() method.
                    //if(counter == 9){counter++;}
                    publishProgress(counter*10);
                }
            //} catch (InterruptedException e) {
            //        e.printStackTrace();
            //}
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

    public void setCounter(int i){
        counter = i;
    }

    public int getCounter(){
        return counter;
    }

    public void setProgessDialogHidden(){
        progressDialog.dismiss();
    }

}

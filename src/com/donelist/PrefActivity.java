package com.donelist;

import android.app.Activity;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;



public class PrefActivity extends Activity {
    EditTextPreference targetAdder;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, new PrefFragment())
            .commit();

    }
   
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */
    
    public static class PrefFragment extends PreferenceFragment{
        private Activity activity;
        private Preference preference;
        
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
            
            preference = this.findPreference("enterNewTarget");
            preference.setOnPreferenceClickListener(new EditTargetListener());
            activity = this.getActivity();
        }
        
        public class EditTargetListener implements OnPreferenceClickListener{
            public boolean onPreferenceClick(Preference preference){
                DoneTarget doneTarget = new DoneTarget(activity);
                doneTarget.setDoneTarget(false);
                return true;
            }
        }
    }

   
}

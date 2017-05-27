package info.androidhive.loginandregistration.activity;

/**
 * Created by Krissy Angelova on 26.5.2017 г..
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;

/**
 * Created by Krissy Angelova on 21.5.2017 г..
 */

public class EventInfoActivity extends Activity {
    private static final String TAG = EventInfoActivity.class.getSimpleName();
    private Button btnBack;
    private TextView txtCode;
    private TextView txtName;
    private TextView txtAddress;
    private TextView txtDescription;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    Button presentButton;
    ScrollView scrollview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        btnBack = (Button) findViewById(R.id.btnBack);
        txtCode = (TextView) findViewById(R.id.txtCode);
        txtName = (TextView) findViewById(R.id.txtName);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtDescription = (TextView) findViewById(R.id.txtDescription);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        String response = bundle.getString("response");
        String code = null;
        try {
            JSONObject event = new JSONObject(response);
            Integer id = event.getInt("id");
            code = event.getString("unique_code");
            String name = event.getString("name");
            String address = event.getString("address");
            String description = event.getString("description");
            String userId = event.getString("user_id");

            txtCode.setText(code);
            txtName.setText(name);
            txtAddress.setText(address);
            txtDescription.setText(description);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadPresents(code, bundle);
        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void loadPresents(final String event_id, final Bundle bundle){
        scrollview = (ScrollView)findViewById(R.id.scrollPresentButtons);
        final LinearLayout linearlayout = new LinearLayout(this);
        linearlayout.setOrientation(LinearLayout.VERTICAL);
        scrollview.addView(linearlayout);

        String tag_string_req = "req_load_presents";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_PRESENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        //open event
                        JSONArray presents = jObj.getJSONArray("presents");
                        for(int i = 0; i < presents.length(); i++)
                        {
                            JSONObject event = presents.getJSONObject(i);
                            createButton(event, linearlayout, bundle);
                        }

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Save Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("event_id", event_id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void createButton(final JSONObject present, LinearLayout linearlayout, final Bundle bundle){
        HashMap<String, String> user = db.getUserDetails();

        LinearLayout linear1 = new LinearLayout(this);
        linear1.setOrientation(LinearLayout.HORIZONTAL);
        linearlayout.addView(linear1);
        presentButton = new Button(this);
        try {
            presentButton.setText(present.getString("name"));
            presentButton.setId(present.getInt("id"));
            String user_id = (present.getString("user_id"));
            if(user_id.equalsIgnoreCase("null") || user_id == null || user_id.isEmpty()){

            } else {
                if(user_id.equals(user.get("uid"))){
                    presentButton.setBackgroundColor(0x4F64FF61);
                } else {
                    presentButton.setBackgroundColor(0x50FF6861);
                }

            }
        }  catch (JSONException e) {
            e.printStackTrace();
        }
        presentButton.setTextSize(12);
        presentButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        linear1.addView(presentButton);


        presentButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(
                        EventInfoActivity.this,
                        PresentInfoActivity.class);
                bundle.putString("present", present.toString());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}


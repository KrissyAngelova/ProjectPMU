package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Krissy Angelova on 27.5.2017 г..
 */

public class PresentInfoActivity extends Activity {
    private static final String TAG = PresentInfoActivity.class.getSimpleName();
    private Button btnBack;
    private Button btnTake;
    private Button btnUntake;
    private TextView txtName;
    private TextView txtDescription;
    private TextView lblTakenPresent;
    private SQLiteHandler db;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_info);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnTake = (Button) findViewById(R.id.btnTake);
        btnUntake = (Button) findViewById(R.id.btnUntake);
        txtName = (TextView) findViewById(R.id.txtName);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        lblTakenPresent = (TextView) findViewById(R.id.lblTakenPresent);

        db = new SQLiteHandler(getApplicationContext());
        final HashMap<String, String> user = db.getUserDetails();

        final String unique_id = user.get("uid");

        final Bundle bundle = getIntent().getExtras();
        String response = bundle.getString("present");
        Integer id = null;
        String user_id = null;
        try {
            JSONObject present = new JSONObject(response);
            id = present.getInt("id");
            String name = present.getString("name");
            String description = present.getString("description");
            user_id = present.getString("user_id");
            txtName.setText(name);
            txtDescription.setText(description);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(user_id.equalsIgnoreCase("null")){
            lblTakenPresent.setVisibility(View.INVISIBLE);
            btnUntake.setVisibility(View.INVISIBLE);
        } else {
            if(user_id.equals(unique_id)){
                lblTakenPresent.setVisibility(View.INVISIBLE);
                btnTake.setVisibility(View.INVISIBLE);
            } else {
                btnTake.setVisibility(View.INVISIBLE);
                btnUntake.setVisibility(View.INVISIBLE);
            }
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        EventInfoActivity.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });
        final Integer present_id = id;
        btnTake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                takePresent(unique_id, present_id, bundle);
            }
        });

        btnUntake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                untakePresent(present_id, bundle);
            }
        });
    }

    private void takePresent(final String user_id, final Integer id,final Bundle bundle) {
        // Tag used to cancel the request
        String tag_string_req = "req_take_present";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_TAKE_PRESENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(
                                PresentInfoActivity.this,
                                EventInfoActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
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
                String response = bundle.getString("response");
                String code = null;
                try {
                    JSONObject event = new JSONObject(response);
                    code = event.getString("unique_code");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",id.toString());
                params.put("user_id", user_id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void untakePresent(final Integer id,final Bundle bundle) {
        // Tag used to cancel the request
        String tag_string_req = "req_untake_present";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UNTAKE_PRESENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(
                                PresentInfoActivity.this,
                                EventInfoActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
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
                String response = bundle.getString("response");
                String code = null;
                try {
                    JSONObject event = new JSONObject(response);
                    code = event.getString("unique_code");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",id.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

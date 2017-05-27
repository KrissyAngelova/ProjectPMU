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

public class MyPresentInfoActivity extends Activity {
    private static final String TAG = MyPresentInfoActivity.class.getSimpleName();
    private Button btnDelete;
    private Button btnBack;
    private TextView txtName;
    private TextView txtDescription;
    private TextView lblTakenPresent;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_present_info);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        txtName = (TextView) findViewById(R.id.txtName);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        lblTakenPresent = (TextView) findViewById(R.id.lblTakenPresent);

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
        if(user_id.equalsIgnoreCase("null") || user_id == null || user_id.isEmpty()){
            lblTakenPresent.setVisibility(View.INVISIBLE);

        } else {
            btnDelete.setVisibility(View.INVISIBLE);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MyEventInfoActivity.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

        final Integer present_id = id;

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                deletePresent(present_id, bundle);
            }
        });

    }

    private void deletePresent(final Integer id, final Bundle bundle) {
        // Tag used to cancel the request
        String tag_string_req = "req_delete_present";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_PRESENT, new Response.Listener<String>() {

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
                                MyPresentInfoActivity.this,
                                MyEventInfoActivity.class);
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
                Log.e(TAG, "Delete Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id.toString());
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

package com.example.butcheryapp_ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmPayment extends AppCompatActivity implements TransactionFinishedCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

        Button buttonbayar = findViewById(R.id.clickPay);

        buttonbayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the getSnapTokenFromServer method with a callback

            }
        });
    }

    private void clickPay(String snapToken) {
        // Check if snapToken is not null before proceeding
        if (snapToken != null) {
            // Set the snapToken in the TransactionRequest
            MidtransSDK.getInstance().setTransactionRequest(transactionRequest("101", 2000, 1, "John", snapToken));

            // Start the payment flow
            MidtransSDK.getInstance().startPaymentUiFlow(ConfirmPayment.this);
        } else {
            // Handle the case where snapToken is null
            Toast.makeText(this, "Error: SNAP token ID is null", Toast.LENGTH_LONG).show();
        }
    }

    // Example method to get snapToken from your server
    public interface SnapTokenCallback {
        void onSnapTokenReceived(String snapToken, String redirectUrl);
        void onError(VolleyError error);
    }

    private void getSnapTokenFromServer(final SnapTokenCallback callback) {
        // Make a network request to get the SnapToken JSON response
        // Use Volley, Retrofit, or any other networking library

        // Example using Volley
        String url = "https://a004-120-188-92-237.ngrok-free.app/api/getSnapToken/charge/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Handle the JSON response
                try {
                    String snapToken = response.getString("token");
                    String redirectUrl = response.getString("redirect_url");

                    // Invoke the callback with the obtained SnapToken and Redirect URL
                    callback.onSnapTokenReceived(snapToken, redirectUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                    callback.onError(new VolleyError("JSON parsing error"));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Invoke the callback with the error
                        callback.onError(error);
                    }
                });

        // Add the request to the RequestQueue to execute the network request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void makePayment(String linkSnap){
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl(BuildConfig.BASE_URL)
                .setClientKey(BuildConfig.CLIENT_KEY)
                .setTransactionFinishedCallback(this)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
                .buildSDK();
    }

    public static CustomerDetails customerDetails(){
        CustomerDetails cd = new CustomerDetails();
        cd.setFirstName("Angga Yunanda");
        cd.setEmail("anggaY@gmail.com");
        cd.setPhone("0856000123");
        return cd;
    }

    public static TransactionRequest transactionRequest(String id, double price, int qty, String name, String snapToken){
        TransactionRequest request =  new TransactionRequest(System.currentTimeMillis() + " " , 2000 );
        CustomerDetails customerDetails = customerDetails();

        request.setCustomerDetails(customerDetails);

        ItemDetails details = new ItemDetails(id, price, qty, name);

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(details);
        request.setItemDetails(itemDetails);
        CreditCard creditCard = new CreditCard();
        creditCard.setSaveCard(false);
        creditCard.setAuthentication(CreditCard.RBA);

        request.setCreditCard(creditCard);

        // Add the snapToken as a custom field
        request.setCustomField1(snapToken);

        return request;
    }

    // Override method onTransactionFinished untuk menangani hasil transaksi
    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    // Transaksi sukses
                    Toast.makeText(this, "Transaction Sukses " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getStatusMessage();
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Something Wrong: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}

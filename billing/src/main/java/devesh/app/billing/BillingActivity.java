package devesh.app.billing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

import devesh.app.billing.databinding.ActivityBillingBinding;
import devesh.ephrine.qr.common.CachePref;


public class BillingActivity extends AppCompatActivity {
    String TAG = "BillAct";
    ActivityBillingBinding binding;
    GPlayBilling gPlayBilling;

    ProductDetails productDetails;
    String price;
    String period;
    boolean isSubscribed;

    CachePref cachePref;

    private final PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult,
                                       List<Purchase> purchases) {
            // To be implemented in a later section.
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                Log.d(TAG, "onPurchasesUpdated: BillingClient.BillingResponseCode.OK");
                for (Purchase purchase : purchases) {
                    //handlePurchase(purchase);

                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                Log.d(TAG, "onPurchasesUpdated: BillingClient.BillingResponseCode.USER_CANCELED");
            } else {
                // Handle any other error codes.
                Log.d(TAG, "onPurchasesUpdated: ERROR");
            }
            fetchOwnedPlans();

        }
    };

    boolean isGooglePlayServiceAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSubscribed = false;
        price = null;

        binding = ActivityBillingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gPlayBilling = new GPlayBilling(this, purchasesUpdatedListener);
        cachePref = new CachePref(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        isSubscribed = cachePref.getBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_isSubscribed));

        gPlayBilling.init(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.d(TAG, "onBillingSetupFinished: ");
                    getProducts();
                    fetchOwnedPlans();
                }
            }
        });

        try {
            getProducts();
        } catch (Exception e) {

        }

        try {
            fetchOwnedPlans();

        } catch (Exception e) {

        }
        setUIDetails();

        // Check Google Play Services
        if (gPlayBilling.isGooglePlayServicesAvailable()) {
            // Google Play Services Available
            Log.d(TAG, "onStart: Google Play Services Available");
            binding.SubNowButton.setVisibility(View.VISIBLE);
            binding.ErrorLL.setVisibility(View.GONE);


        } else {
            // Google Play Services not Available
            Log.d(TAG, "onStart: Google Play Services not Available");
            binding.ErrorLL.setVisibility(View.VISIBLE);
            binding.ErrorTextview.setText("Error:Google Play Services is Not Available !!");

        }

    }

    void getProducts() {
        gPlayBilling.getProducts(
                (billingResult, queryProductDetailsResult) -> {
                    try {

                        List<ProductDetails> list = queryProductDetailsResult.getProductDetailsList();

                        Log.d(TAG, "onProductDetailsResponse: " + list.get(0).toString());
                        productDetails = list.get(0);
                        gPlayBilling.productDetails = productDetails;

                        ProductDetails.SubscriptionOfferDetails s = productDetails.getSubscriptionOfferDetails().get(0);
                        price = s.getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice();
                        period = s.getPricingPhases().getPricingPhaseList().get(0).getBillingPeriod();
                        Log.d(TAG, "onProductDetailsResponse: SubscriptionOfferDetails " + s);
                        Log.d(TAG, "onProductDetailsResponse: getOfferTags " + s.getPricingPhases().getPricingPhaseList().get(0));

                        setUIDetails();

                    } catch (Exception e) {


                        throw new RuntimeException(e);
                    }

                });


    }

    public void PayNow(View v) {

        if (!isSubscribed) {
            BillingResult result = gPlayBilling.StatPay(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken());
            Log.d(TAG, "PayNow: " + result.getResponseCode());
        } else {
            Toast.makeText(this, "Thank You for Subscribing to Premium Plan", Toast.LENGTH_LONG).show();
        }


    }

    void setUIDetails() {
        //String price=productDetails.getSubscriptionOfferDetails();
        runOnUiThread(() -> {
            if (price != null) {
                binding.PriceTextView.setText(price);
            }


            if (isSubscribed) {
                binding.SubNowButton.setText("Subscribed");
                binding.ThankYouTextview.setVisibility(View.VISIBLE);
            } else {
                binding.SubNowButton.setText("Subscribe Now");
                binding.ThankYouTextview.setVisibility(View.GONE);
            }


        });

    }

    void fetchOwnedPlans() {
        gPlayBilling.fetchPayments((billingResult, list) -> {
            if (list.isEmpty()) {
                isSubscribed = false;
            }
            for (Purchase p : list) {
                int state = p.getPurchaseState();
                Log.d(TAG, "fetchOwnedPlans: " + p.getOrderId() + "\nState: " + p.getPurchaseState());
                if (state == Purchase.PurchaseState.PURCHASED) {
                    Log.d(TAG, "fetchOwnedPlans: PURCHASED");
                    isSubscribed = true;
                } else if (state == Purchase.PurchaseState.PENDING) {
                    Log.d(TAG, "fetchOwnedPlans: PENDING");
                    isSubscribed = true;

                } else if (state == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                    Log.d(TAG, "fetchOwnedPlans: UNSPECIFIED_STATE");
                    isSubscribed = false;
                } else {
                    isSubscribed = false;
                    Log.d(TAG, "fetchOwnedPlans: UNSPECIFIED_STATE unknown");
                }
            }
            cachePref.setBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_isSubscribed), isSubscribed);
            setUIDetails();
        });
    }


}
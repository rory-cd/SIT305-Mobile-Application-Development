package com.rorycd.learningassistant

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import com.rorycd.learningassistant.data.UserRepository
import com.rorycd.learningassistant.navigation.LearningAssistantNavHost
import com.rorycd.learningassistant.ui.learningApplication
import com.rorycd.learningassistant.ui.theme.LearningAssistantTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as LearningApplication).container
        userRepository = appContainer.userRepo

        val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                lifecycleScope.launch {
                    // Grant premium
                    userRepository.upgradeUser()
                    Toast.makeText(this@MainActivity, R.string.account_upgraded, Toast.LENGTH_SHORT).show()
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                Toast.makeText(this, R.string.payment_cancelled, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.billing_error, Toast.LENGTH_SHORT).show()
            }
        }

        val billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enableAutoServiceReconnection()
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            // Configure other settings.
            .build()

        // Connect to Google Play
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {}
            override fun onBillingServiceDisconnected() {}
        })

        enableEdgeToEdge()
        setContent {
            LearningAssistantTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LearningAssistantNavHost(
                        context = this,
                        billingClient = billingClient,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

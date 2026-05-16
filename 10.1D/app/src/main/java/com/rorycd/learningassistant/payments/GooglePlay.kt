package com.rorycd.learningassistant.payments

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Launches a Google Play billing flow for the provided product ID.
 */
suspend fun processPurchases(activity: Activity, billingClient: BillingClient, productId: String) {
    // Create a list of 1 in-app product
    val productList = listOf(
        QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
    )

    // Add product to product details params
    val params = QueryProductDetailsParams.newBuilder()
    params.setProductList(productList)

    // Check Google Play product details
    val productDetailsResult = withContext(Dispatchers.IO) {
        billingClient.queryProductDetails(params.build())
    }
    // Process the result
    val productDetailsList = productDetailsResult.productDetailsList

    if (productDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
        !productDetailsList.isNullOrEmpty()) {

        // Get first product
        val productDetails = productDetailsList[0]
        Log.e("PRODUCT", productDetails.toString())

        // Set params for billing flow
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        // Launch the billing flow
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }
}

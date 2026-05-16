package com.rorycd.learningassistant.payments

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun processPurchases(activity: Activity, billingClient: BillingClient, productId: String) {
    val productList = listOf(
        QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
    )
    val params = QueryProductDetailsParams.newBuilder()
    params.setProductList(productList)

    val productDetailsResult = withContext(Dispatchers.IO) {
        billingClient.queryProductDetails(params.build())
    }

    // Process the result
    val productDetailsList = productDetailsResult.productDetailsList

    if (productDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
        !productDetailsList.isNullOrEmpty()) {

        val productDetails = productDetailsList[0]

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

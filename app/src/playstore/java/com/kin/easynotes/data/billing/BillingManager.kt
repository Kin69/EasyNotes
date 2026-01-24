package com.kin.easynotes.data.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BillingManager(private val context: Context) : PurchasesUpdatedListener {

    companion object {
        const val PRODUCT_ID_SUPPORT = "support_development"
    }

    private val _purchaseState = MutableStateFlow<PurchaseState>(PurchaseState.Idle)
    val purchaseState: StateFlow<PurchaseState> = _purchaseState.asStateFlow()

    private var productDetails: ProductDetails? = null
    private var pendingActivity: Activity? = null
    private var isConnected = false
    private var isConnecting = false

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    init {
        startConnection()
    }

    private fun startConnection() {
        if (billingClient.isReady) {
            isConnected = true
            isConnecting = false
            return
        }

        if (isConnecting) {
            return
        }

        isConnecting = true
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                isConnecting = false
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    isConnected = true
                    queryProductDetails()
                } else {
                    isConnected = false
                    _purchaseState.value = PurchaseState.Error("Billing setup failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnected = false
                isConnecting = false
            }
        })
    }

    private fun queryProductDetails() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PRODUCT_ID_SUPPORT)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                productDetailsResult.productDetailsList.isNotEmpty()) {
                productDetails = productDetailsResult.productDetailsList.first()
                _purchaseState.value = PurchaseState.ProductAvailable(productDetails!!)
                pendingActivity?.let { activity ->
                    launchBillingFlow(activity)
                    pendingActivity = null
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _purchaseState.value = PurchaseState.Error("Product not found in Play Console")
            } else {
                _purchaseState.value = PurchaseState.Error("Failed to query products: ${billingResult.debugMessage}")
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity) {
        pendingActivity = activity

        if (!isConnected && !isConnecting) {
            _purchaseState.value = PurchaseState.Connecting
            startConnection()
            return
        }

        if (isConnecting) {
            _purchaseState.value = PurchaseState.Connecting
            return
        }

        val details = productDetails
        if (details == null) {
            _purchaseState.value = PurchaseState.Connecting
            queryProductDetails()
            return
        }

        pendingActivity = null
        launchBillingFlow(activity)
    }

    private fun launchBillingFlow(activity: Activity) {
        val details = productDetails ?: return

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val result = billingClient.launchBillingFlow(activity, billingFlowParams)
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            _purchaseState.value = PurchaseState.Error("Failed to launch billing: ${result.debugMessage}")
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    handlePurchase(purchase)
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _purchaseState.value = PurchaseState.Cancelled
            }
            else -> {
                _purchaseState.value = PurchaseState.Error("Purchase failed: ${billingResult.debugMessage}")
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient.consumeAsync(consumeParams) { billingResult, _ ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _purchaseState.value = PurchaseState.Purchased
                }
            }
        }
    }

    fun resetState() {
        productDetails?.let {
            _purchaseState.value = PurchaseState.ProductAvailable(it)
        } ?: run {
            _purchaseState.value = PurchaseState.Idle
        }
    }

    fun endConnection() {
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
        isConnected = false
        isConnecting = false
        pendingActivity = null
    }
}

sealed class PurchaseState {
    data object Idle : PurchaseState()
    data object Connecting : PurchaseState()
    data class ProductAvailable(val productDetails: ProductDetails) : PurchaseState()
    data object Purchased : PurchaseState()
    data object Cancelled : PurchaseState()
    data class Error(val message: String) : PurchaseState()
}

package com.nlhd.checkout

import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.google.gson.Gson
import com.google.pay.button.PayButton
import com.nlhd.checkout.components.CardCustomerInfoItem
import com.nlhd.checkout.components.CardOrderSummary
import com.nlhd.checkout.components.CardPaymentMethod
import com.nlhd.checkout.components.CheckoutItem
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.colorDivide
import com.nlhd.core.utils.contentBlueTopBar
import com.nlhd.core.utils.contentPrice
import com.nlhd.domain.entity.checkout.CheckoutOrderRequest
import com.nlhd.domain.entity.checkout.CheckoutResponse
import com.nlhd.domain.entity.checkout.Payment
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.time.LocalDate

val allowedPaymentMethodsJson = """
[
  {
    "type": "CARD",
    "parameters": {
      "allowedAuthMethods": ["PAN_ONLY", "CRYPTOGRAM_3DS"],
      "allowedCardNetworks": ["VISA", "MASTERCARD"]
    },
    "tokenizationSpecification": {
      "type": "PAYMENT_GATEWAY",
      "parameters": {
        "gateway": "example",
        "gatewayMerchantId": "exampleGatewayMerchantId"
      }
    }
  }
]
""".trimIndent()

private fun createPaymentsClient(activity: Activity): PaymentsClient {
    val walletOptions = Wallet.WalletOptions.Builder()
        .setEnvironment(WalletConstants.ENVIRONMENT_TEST) // đổi thành PRODUCTION khi live
        .build()
    return Wallet.getPaymentsClient(activity, walletOptions)
}

fun getPaymentDataRequest(price: String, product: String): String {
    return """
    {
      "apiVersion": 2,
      "apiVersionMinor": 0,
      "allowedPaymentMethods": $allowedPaymentMethodsJson,
      "transactionInfo": {
        "totalPriceStatus": "FINAL",
        "totalPrice": "$price",
        "currencyCode": "VND"
      },
      "merchantInfo": {
        "merchantName": "$product"
      }
    }
    """.trimIndent()
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    activity: Activity,
    checkoutResponse: CheckoutResponse,
    viewModel: CheckoutViewModel = koinViewModel(),
    onClickBack: () -> Unit,
    onSuccess: (Payment) -> Unit
) {
    val products = checkoutResponse.selectedProducts
    val context = LocalContext.current
    val customer = checkoutResponse.customerInformation
    val state = viewModel.state.collectAsStateWithLifecycle()
    val payments = viewModel.payments.collectAsStateWithLifecycle()
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val gson = Gson()

    val paymentsClient = remember { createPaymentsClient(activity) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                result.data?.let { intent ->
                    val paymentData = PaymentData.getFromIntent(intent)
                    //Log.d("AAA", "Payment Success: ${paymentData?.toJson()}")
                    if (keyStore.value != "") {
                        val jsonList = gson.toJson(checkoutResponse.selectedProducts)
                        val checkout = CheckoutOrderRequest(
                            customerInfoId = customer.id,
                            paymentMethod = "momo",
                            selectedProducts = jsonList,
                            totalAmount = checkoutResponse.totalAmount.toString()
                        )

                        viewModel.checkoutOrder(
                            token = keyStore.value,
                            checkoutOrderRequest = checkout
                        )
                    }
                }
            }
            Activity.RESULT_CANCELED -> {
                Log.d("AAA", "Payment Cancelled")
            }
            else -> {
                Log.d("AAA", "Error result: ${result.resultCode}")
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Order summary",
                            style = AppTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Easy cancellation",
                            style = AppTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = contentBlueTopBar
                            ),
                            textAlign = TextAlign.Center
                        )

                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onClickBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(
                                AppTheme.dimens.medium2)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth().background(color = Color.White)
            ) {
                /*Text(
                    "Lưu ý: Vui lòng kiểm tra kỹ thông tin trước khi mua hàng",
                    style = AppTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Font.fontFamily,
                        color = contentPrice
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFFFEF2F4))
                        .padding(AppTheme.dimens.small3)
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.small))*/
                Row(
                    modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small2),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Tổng cộng (${checkoutResponse.selectedProducts.size} sản phẩm)",
                        style = AppTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        textAlign = TextAlign.Center
                    )
                    val priceFormat = NumberFormat.getNumberInstance().format(checkoutResponse.totalAmount)
                    Text(
                        "${priceFormat}đ",
                        style = AppTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        textAlign = TextAlign.Center
                    )
                }


                Button(
                    onClick = {
                        if (keyStore.value != "") {
                            val jsonList = gson.toJson(checkoutResponse.selectedProducts)
                            payments.value.forEach { triple->
                                if (triple.second && triple.first == 0) {
                                    val checkout = CheckoutOrderRequest(
                                        customerInfoId = customer.id,
                                        paymentMethod = "cod",
                                        selectedProducts = jsonList,
                                        totalAmount = checkoutResponse.totalAmount.toString()
                                    )

                                    viewModel.checkoutOrder(
                                        token = keyStore.value,
                                        checkoutOrderRequest = checkout
                                    )
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = contentPrice
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 0.dp,
                            start = AppTheme.dimens.small2,
                            end = AppTheme.dimens.small2,
                            bottom = AppTheme.dimens.small3
                        )
                ) {
                    Text(
                        "Đặt hàng",
                        style = AppTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
        containerColor = colorDivide
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Spacer(modifier = Modifier.height(AppTheme.dimens.extraSmall))
                Row(
                    modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(
                        AppTheme.dimens.small3),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Store VN",
                        style = AppTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Add note",
                            style = AppTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Light,
                                color = Color.Black
                            ),
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_next),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(AppTheme.dimens.medium)
                        )
                    }

                }
            }
            items(products.size) { index ->
                val product = products[index]
                CheckoutItem(product = product)
            }
            item {
                val today = LocalDate.now()

                val day = today.dayOfMonth
                val month = today.monthValue
                val year = today.year
                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                CardOrderSummary(
                    title = "Order summary",
                    quantity = products.size.toString(),
                    date = "$day-$month-$year",
                    totalAmount = checkoutResponse.totalAmount
                )
            }
            item {
                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                CardCustomerInfoItem(
                    title = "Customer Infomation",
                    nameCustomer = customer.fullName,
                    phone = customer.phoneNumber,
                    address = customer.address,
                    description = customer.description ?: "Chưa có mô tả"
                )
            }
            item {
                Spacer(modifier = Modifier.height(AppTheme.dimens.small))
                CardPaymentMethod(
                    title = "Payment methods",
                    payments = payments.value,
                    onChange = {
                        viewModel.changePayment(it)
                    }
                )

                payments.value.forEach { triple->
                    if (triple.second && triple.first == 1) {
                        Spacer(modifier = Modifier.height(AppTheme.dimens.small3))
                        PayButton(
                            onClick = {
                                val price = checkoutResponse.totalAmount.toString()
                                val product = checkoutResponse.selectedProducts.joinToString(", ") { it.name }
                                val request = PaymentDataRequest.fromJson(getPaymentDataRequest(price = price, product = product))
                                val task = paymentsClient.loadPaymentData(request)
                                task.addOnCompleteListener { completedTask ->
                                    try {
                                        val paymentData = completedTask.getResult(ApiException::class.java)
                                        Log.d("AAA", "PaymentData: ${paymentData?.toJson()}")
                                    } catch (exception: ApiException) {
                                        if (exception.statusCode == CommonStatusCodes.RESOLUTION_REQUIRED) {
                                            (exception as? ResolvableApiException)?.let { resolvable ->
                                                launcher.launch(
                                                    IntentSenderRequest.Builder(resolvable.resolution).build()
                                                )
                                            }
                                        } else {
                                            Log.e("AAA", "ApiException: ${exception.statusCode}")
                                        }
                                    }
                                }
                            },
                            allowedPaymentMethods = allowedPaymentMethodsJson
                        )
                    }
                }

            }
        }
        when (state.value) {
            is CheckoutState.Error -> {
                Toast.makeText(context, (state.value as CheckoutState.Error).message.toString(), Toast.LENGTH_SHORT).show()
            }
            CheckoutState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = contentPrice
                    )
                }
            }
            CheckoutState.Pending -> {

            }
            is CheckoutState.Success -> {
                LaunchedEffect(
                    key1 = Unit
                ) {
                    viewModel.setState(CheckoutState.Pending)
                    Toast.makeText(context, (state.value as CheckoutState.Success).checkoutOrderResponse.message.toString(), Toast.LENGTH_SHORT).show()
                    onSuccess((state.value as CheckoutState.Success).checkoutOrderResponse.payment)
                }

            }
        }
    }
}

@Preview
@Composable
private fun TestComp() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "Lưu ý: Vui lòng kiểm tra kỹ thông tin trước khi mua hàng",
            style = AppTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = contentPrice
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFFEF2F4))
                .padding(AppTheme.dimens.small3)
        )
        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
        Row(
            modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.small2),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Tổng cộng (1 sản phẩm)",
                style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center
            )
            Text(
                "284.000đ",
                style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = contentPrice
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = AppTheme.dimens.small2)
        ) {
            Text(
                "Đặt hàng",
                style = AppTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}
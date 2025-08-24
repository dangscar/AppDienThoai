package com.nlhd.data.mapper

import com.nlhd.data.model.checkout.CheckoutOrderRequestDto
import com.nlhd.data.model.checkout.CheckoutOrderResponseDto
import com.nlhd.data.model.checkout.CheckoutRequestDto
import com.nlhd.data.model.checkout.CheckoutResponseDto
import com.nlhd.data.model.checkout.CustomerInformation
import com.nlhd.data.model.checkout.Payment
import com.nlhd.data.model.checkout.ProductCheckout
import com.nlhd.data.model.checkout.SelectedProduct
import com.nlhd.domain.entity.checkout.CheckoutOrderRequest

fun CheckoutRequestDto.toDomain(checkoutRequestDto: CheckoutRequestDto): com.nlhd.domain.entity.checkout.CheckoutRequest {
    return com.nlhd.domain.entity.checkout.CheckoutRequest(
        customer_info = checkoutRequestDto.customer_info,
        selected_products = checkoutRequestDto.selected_products
    )
}

fun ProductCheckout.toDomain(productCheckout: ProductCheckout): com.nlhd.domain.entity.checkout.ProductCheckout {
    return com.nlhd.domain.entity.checkout.ProductCheckout(
        color_product_id = productCheckout.color_product_id,
        name = productCheckout.name,
        image = productCheckout.image,
        price = productCheckout.price,
        quantity = productCheckout.quantity,
        color = productCheckout.color,
        ram = productCheckout.ram,
        storage = productCheckout.storage
    );
}

fun CheckoutResponseDto.toDomain(checkoutResponseDto: CheckoutResponseDto): com.nlhd.domain.entity.checkout.CheckoutResponse {
    return com.nlhd.domain.entity.checkout.CheckoutResponse(
        customerInformation = checkoutResponseDto.customerInformation!!.toDomain(checkoutResponseDto.customerInformation),
        message = checkoutResponseDto.message,
        selectedProducts = checkoutResponseDto.selectedProducts!!.map { it!!.toDomain(it) },
        totalAmount = checkoutResponseDto.total_amount
    )
}

fun CustomerInformation.toDomain(customerInformation: CustomerInformation): com.nlhd.domain.entity.checkout.CustomerInformation {
    return com.nlhd.domain.entity.checkout.CustomerInformation(
        address = customerInformation.address,
        description = customerInformation.description,
        fullName = customerInformation.fullName,
        id = customerInformation.id,
        isSelected = customerInformation.isSelected,
        phoneNumber = customerInformation.phoneNumber,
        userId = customerInformation.user_id
    )
}

fun SelectedProduct.toDomain(selectedProduct: SelectedProduct): com.nlhd.domain.entity.checkout.SelectedProduct {
    return com.nlhd.domain.entity.checkout.SelectedProduct(
        color = selectedProduct.color,
        color_product_id = selectedProduct.color_product_id,
        image = selectedProduct.image,
        name = selectedProduct.name,
        price = selectedProduct.price,
        quantity = selectedProduct.quantity,
        ram = selectedProduct.ram,
        storage = selectedProduct.storage
    )
}

//Order
fun CheckoutOrderRequestDto.toDomain(checkoutOrderRequestDto: CheckoutOrderRequestDto): CheckoutOrderRequest {
    return CheckoutOrderRequest(
        customerInfoId = checkoutOrderRequestDto.customer_information_id,
        paymentMethod = checkoutOrderRequestDto.payment_method,
        selectedProducts = checkoutOrderRequestDto.selected_products,
        totalAmount = checkoutOrderRequestDto.total_amount
    )
}

fun CheckoutOrderResponseDto.toDomain(checkoutOrderResponseDto: CheckoutOrderResponseDto): com.nlhd.domain.entity.checkout.CheckoutOrderResponse {
    return com.nlhd.domain.entity.checkout.CheckoutOrderResponse(
        message = checkoutOrderResponseDto.message,
        payment = checkoutOrderResponseDto.payment!!.toDomain(checkoutOrderResponseDto.payment)
    )
}

fun Payment.toDomain(payment: Payment): com.nlhd.domain.entity.checkout.Payment {
    return com.nlhd.domain.entity.checkout.Payment(
        amountPaid = payment.amount_paid,
        createdAt = payment.created_at,
        id = payment.id,
        orderId = payment.order_id,
        paymentMethod = payment.payment_method,
        status = payment.status,
        updatedAt = payment.updated_at
    )
}
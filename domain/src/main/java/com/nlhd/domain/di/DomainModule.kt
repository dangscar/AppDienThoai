package com.nlhd.domain.di

import com.nlhd.domain.usecase.address.AddAddress
import com.nlhd.domain.usecase.address.AddressUseCase
import com.nlhd.domain.usecase.address.DeleteAddress
import com.nlhd.domain.usecase.address.EditAddress
import com.nlhd.domain.usecase.address.GetAddress
import com.nlhd.domain.usecase.address.SelectedAddress
import com.nlhd.domain.usecase.address.UpdateAddress
import com.nlhd.domain.usecase.authentication.AuthenticationUseCase
import com.nlhd.domain.usecase.authentication.Login
import com.nlhd.domain.usecase.authentication.Logout
import com.nlhd.domain.usecase.authentication.Profile
import com.nlhd.domain.usecase.authentication.ProfileAdmin
import com.nlhd.domain.usecase.cart.AddCart
import com.nlhd.domain.usecase.cart.CartUseCase
import com.nlhd.domain.usecase.cart.CheckoutPreview
import com.nlhd.domain.usecase.cart.GetCart
import com.nlhd.domain.usecase.checkout.CheckoutOrder
import com.nlhd.domain.usecase.checkout.CheckoutUseCase
import com.nlhd.domain.usecase.dashboard.DashboardUseCase
import com.nlhd.domain.usecase.dashboard.GetDashboard
import com.nlhd.domain.usecase.manageCategory.GetCategory
import com.nlhd.domain.usecase.manageCategory.ManageCategoryUseCase
import com.nlhd.domain.usecase.manageProduct.AddProduct
import com.nlhd.domain.usecase.manageProduct.ManageProductUseCase
import com.nlhd.domain.usecase.product.GetProductDetail
import com.nlhd.domain.usecase.product.GetProducts
import com.nlhd.domain.usecase.product.ProductUseCase
import com.nlhd.domain.usecase.product.SearchProducts
import org.koin.dsl.module

val domainModule = module {
    single {
        ProductUseCase(
            getProducts = GetProducts(get()),
            getProductDetail = GetProductDetail(get()),
            addCart = AddCart(get()),
            searchProducts = SearchProducts(get())
        )
    }

    single {
        AuthenticationUseCase(
            login = Login(get()),
            profile = Profile(get()),
            logout = Logout(get()),
            profileAdmin = ProfileAdmin(get())
        )
    }

    single {
        CartUseCase(
            getCart = GetCart(get()),
            addCart = AddCart(get()),
            checkoutPreview = CheckoutPreview(get())
        )
    }

    single {
        CheckoutUseCase(
            checkoutOrder = CheckoutOrder(get())
        )
    }

    single {
        ManageProductUseCase(
            addProduct = AddProduct(get())
        )
    }

    single {
        ManageCategoryUseCase(
            getCategory = GetCategory(get())
        )
    }

    single {
        DashboardUseCase(
            getDashboard = GetDashboard(get())
        )
    }

    single {
        AddressUseCase(
            getAddress = GetAddress(get()),
            addAddress = AddAddress(get()),
            selectedAddress = SelectedAddress(get()),
            editAddress = EditAddress(get()),
            updateAddress = UpdateAddress(get()),
            deleteAddress = DeleteAddress(get())
        )
    }

}
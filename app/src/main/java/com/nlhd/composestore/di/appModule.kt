package com.nlhd.composestore.di

import com.nlhd.address.AddressViewModel
import com.nlhd.admin.AdminProfileViewModel
import com.nlhd.cart.CartViewModel
import com.nlhd.checkout.CheckoutViewModel
import com.nlhd.composestore.NavigationViewModel
import com.nlhd.dashboard.DashboardViewModel
import com.nlhd.detail.DetailViewModel
import com.nlhd.home.HomeViewModel
import com.nlhd.manage_product.EditProductScreen.EditProductViewModel
import com.nlhd.manage_product.LoadProductScreen.LoadProductViewModel
import com.nlhd.manage_product.AddProductScreen.ManageProductViewModel
import com.nlhd.manage_product.AddVersionProductScreen.AddVersionProductViewModel
import com.nlhd.manage_product.LoadVersionProductScreen.LoadVersionProductViewModel
import com.nlhd.order.OrderViewModel
import com.nlhd.search.SearchViewModel
import com.nlhd.user.EditProfileViewModel
import com.nlhd.user.LoginViewModel
import com.nlhd.user.ProfileViewModel
import com.nlhd.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { CartViewModel(
        cartUseCase = get(),
        addressUseCase = get()
    ) }
    viewModel { CheckoutViewModel(get()) }
    viewModel { AdminProfileViewModel(get()) }
    viewModel { NavigationViewModel(get()) }
    viewModel { ManageProductViewModel(get(), get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { AddressViewModel(get()) }

    viewModel { SearchViewModel(
        productUseCase = get()
    ) }

    viewModel {
        OrderViewModel(get())
    }

    viewModel {
        EditProfileViewModel(get())
    }

    viewModel {
        LoadProductViewModel(get())
    }

    viewModel { EditProductViewModel(get(), get()) }
    viewModel { LoadVersionProductViewModel(get()) }
    viewModel { AddVersionProductViewModel(get()) }

}
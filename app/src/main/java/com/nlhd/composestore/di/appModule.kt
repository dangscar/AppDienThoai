package com.nlhd.composestore.di

import com.nlhd.address.AddressViewModel
import com.nlhd.admin.AdminProfileViewModel
import com.nlhd.cart.CartViewModel
import com.nlhd.checkout.CheckoutViewModel
import com.nlhd.composestore.NavigationViewModel
import com.nlhd.dashboard.DashboardViewModel
import com.nlhd.detail.DetailViewModel
import com.nlhd.home.HomeViewModel
import com.nlhd.manage_product.ManageProductViewModel
import com.nlhd.search.SearchViewModel
import com.nlhd.user.LoginViewModel
import com.nlhd.user.ProfileViewModel
import com.nlhd.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
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


}
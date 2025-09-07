package com.nlhd.composestore.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
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
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.io.File

@SuppressLint("UnsafeOptInUsageError")
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


    /*single {
        val context: Context = androidContext()
        val cacheSize: Long = 1000 * 1024 * 1024
        SimpleCache(
            File(context.cacheDir, "media"),
            LeastRecentlyUsedCacheEvictor(cacheSize),
            StandaloneDatabaseProvider(context)
        )
        CacheDataSource.Factory().setCache(get()).setUpstreamDataSourceFactory(DefaultDataSource.Factory(context)).setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }*/



}
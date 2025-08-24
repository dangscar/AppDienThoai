package com.nlhd.data.di

import com.nlhd.data.repository.AddressRepositoryImp
import com.nlhd.data.repository.AuthenticationRepositoryImp
import com.nlhd.data.repository.CartRepositoryImp
import com.nlhd.data.repository.CheckoutRepositoryImp
import com.nlhd.data.repository.DashboardRepositoryImp
import com.nlhd.data.repository.ManageCategoryRepositoryImp
import com.nlhd.data.repository.ManageProductRepositoryImp
import com.nlhd.data.repository.ProductRepositoryImp
import com.nlhd.domain.repository.AddressRepository
import com.nlhd.domain.repository.AuthenticationRepository
import com.nlhd.domain.repository.CartRepository
import com.nlhd.domain.repository.CheckoutRepository
import com.nlhd.domain.repository.DashboardRepository
import com.nlhd.domain.repository.ManageCategoryRepository
import com.nlhd.domain.repository.ManageProductRepository
import com.nlhd.domain.repository.ProductRepository
import org.koin.dsl.module

val dataModule = module {
    single<ProductRepository> {
        ProductRepositoryImp(get())
    }
    single<AuthenticationRepository> {
        AuthenticationRepositoryImp(get())
    }
    single<CartRepository> {
        CartRepositoryImp(get())
    }
    single<CheckoutRepository> {
        CheckoutRepositoryImp(get())
    }
    single<ManageProductRepository> {
        ManageProductRepositoryImp(get())
    }
    single<ManageCategoryRepository> {
        ManageCategoryRepositoryImp(get())
    }
    single<DashboardRepository> {
        DashboardRepositoryImp(get())
    }
    single<AddressRepository> {
        AddressRepositoryImp(get())
    }
}
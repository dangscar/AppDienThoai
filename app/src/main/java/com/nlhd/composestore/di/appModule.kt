package com.nlhd.composestore.di

import android.annotation.SuppressLint
import androidx.media3.database.ExoDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
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
import com.nlhd.manage_product.VersionProductScreen.LoadVersionProductViewModel
import com.nlhd.order.OrderViewModel
import com.nlhd.search.SearchViewModel
import com.nlhd.shortvideo.ContentCommonViewModel
import com.nlhd.shortvideo.Profile.ProfileShortVideoViewModel
import com.nlhd.shortvideo.Search.SearchShortVideoSuccessViewModel
import com.nlhd.shortvideo.ShortVideoViewModel
import com.nlhd.shortvideo.UploadVideo.UploadVideoViewModel
import com.nlhd.shortvideo.VideoViewModel
import com.nlhd.user.EditProfileViewModel
import com.nlhd.shortvideo.LikedVideo.LikedVideoViewModel
import com.nlhd.shortvideo.MyVideos.MyVideoViewModel
import com.nlhd.user.LoginViewModel
import com.nlhd.user.ProfileViewModel
import com.nlhd.user.UploadAvatar.UploadAvatarViewModel
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



    //ExoPlayer

    single {
        val ctx = androidContext()
        val cacheSize = 1_000L * 1024 * 1024
        SimpleCache(
            File(ctx.cacheDir, "media"),
            LeastRecentlyUsedCacheEvictor(cacheSize),
            ExoDatabaseProvider(ctx) // hoặc StandaloneDatabaseProvider(ctx)
        )
    }

    single<HttpDataSource.Factory> { DefaultHttpDataSource.Factory() }

    single<DataSource.Factory> {
        CacheDataSource.Factory()
            .setCache(get<SimpleCache>())
            .setUpstreamDataSourceFactory(get<HttpDataSource.Factory>())
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    single { DefaultMediaSourceFactory(get<DataSource.Factory>()) }

    single {
        ExoPlayer.Builder(androidContext())
            .setMediaSourceFactory(get<DefaultMediaSourceFactory>())
            .build()
    }
    viewModel {
        ContentCommonViewModel(get())
    }

    viewModel {
        ShortVideoViewModel(get())
    }

    viewModel {
        VideoViewModel(get())
    }

    viewModel {
        SearchShortVideoSuccessViewModel(get())
    }

    viewModel {
        ProfileShortVideoViewModel(get())
    }

    single {
        val ctx = androidContext()
        ExoPlayer.Builder(ctx).build()
    }

    viewModel {
        UploadVideoViewModel(get(), get())
    }

    viewModel {
        UploadAvatarViewModel(get())
    }

    viewModel {
        LikedVideoViewModel(get())
    }

    viewModel {
        MyVideoViewModel(get())
    }
}
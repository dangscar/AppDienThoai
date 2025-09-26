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
import com.nlhd.domain.usecase.authentication.UpdateProfile
import com.nlhd.domain.usecase.authentication.UploadAvatar
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
import com.nlhd.domain.usecase.manageProduct.AddVersionProduct
import com.nlhd.domain.usecase.manageProduct.GetProduct
import com.nlhd.domain.usecase.manageProduct.GetVersionProducts
import com.nlhd.domain.usecase.manageProduct.LoadProducts
import com.nlhd.domain.usecase.manageProduct.ManageProductUseCase
import com.nlhd.domain.usecase.manageProduct.UpdateProduct
import com.nlhd.domain.usecase.order.GetOrders
import com.nlhd.domain.usecase.order.OrderUseCase
import com.nlhd.domain.usecase.product.GetProductDetail
import com.nlhd.domain.usecase.product.GetProducts
import com.nlhd.domain.usecase.product.ProductUseCase
import com.nlhd.domain.usecase.product.SearchProducts
import com.nlhd.domain.usecase.shortvideo.AddComment
import com.nlhd.domain.usecase.shortvideo.AddVideo
import com.nlhd.domain.usecase.shortvideo.DeleteVideo
import com.nlhd.domain.usecase.shortvideo.Favorites
import com.nlhd.domain.usecase.shortvideo.Follows
import com.nlhd.domain.usecase.shortvideo.GetComments
import com.nlhd.domain.usecase.shortvideo.GetFavoriteVideos
import com.nlhd.domain.usecase.shortvideo.GetInfoProfile
import com.nlhd.domain.usecase.shortvideo.GetLikedVideos
import com.nlhd.domain.usecase.shortvideo.GetMyVideos
import com.nlhd.domain.usecase.shortvideo.GetVideoByUser
import com.nlhd.domain.usecase.shortvideo.GetVideos
import com.nlhd.domain.usecase.shortvideo.GetVideosSearch
import com.nlhd.domain.usecase.shortvideo.IncreaseViews
import com.nlhd.domain.usecase.shortvideo.Likes
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import com.nlhd.domain.usecase.shortvideo.UpdateCaption
import org.koin.dsl.module

val domainModule = module {
    single {
        ProductUseCase(
            getProducts = GetProducts(get()),
            getProductDetail = GetProductDetail(get()),
            addCart = AddCart(get()),
            searchProducts = SearchProducts(get()),
        )
    }

    single {
        AuthenticationUseCase(
            login = Login(get()),
            profile = Profile(get()),
            logout = Logout(get()),
            profileAdmin = ProfileAdmin(get()),
            updateProfile = UpdateProfile(get()),
            uploadAvatar = UploadAvatar(get())
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
            addProduct = AddProduct(get()),
            loadProducts = LoadProducts(get()),
            getProduct = GetProduct(get()),
            updateProduct = UpdateProduct(get()),
            getVersionProducts = GetVersionProducts(get()),
            addVersionProduct = AddVersionProduct(get())
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

    single {
        OrderUseCase(
            getOrders = GetOrders(get())
        )
    }




    single {
        ShortVideoUseCase(
            getVideos = GetVideos(get()),
            follows = Follows(get()),
            likes = Likes(get()),
            favorites = Favorites(get()),
            getComments = GetComments(get()),
            addComment = AddComment(get()),
            getVideosSearch = GetVideosSearch(get()),
            getInfoProfile = GetInfoProfile(get()),
            getVideosByUser = GetVideoByUser(get()),
            increaseViews = IncreaseViews(get()),
            addVideo = AddVideo(get()),
            getVideosLiked = GetLikedVideos(get()),
            getVideosFavorite = GetFavoriteVideos(get()),
            getMyVideos = GetMyVideos(get()),
            deleteVideo = DeleteVideo(get()),
            updateCaption = UpdateCaption(get())
        )
    }

}
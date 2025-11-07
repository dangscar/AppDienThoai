package com.nlhd.composestore

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.gson.Gson
import com.nlhd.address.AddressScreen
import com.nlhd.address.EditAddressScreen
import com.nlhd.admin.AdminProfileScreen
import com.nlhd.cart.CartScreen
import com.nlhd.checkout.CheckoutScreen
import com.nlhd.checkout.CheckoutSuccessScreen
import com.nlhd.composestore.navigate.AddColorProduct
import com.nlhd.composestore.navigate.AddProduct
import com.nlhd.composestore.navigate.AddVersionProduct
import com.nlhd.composestore.navigate.Address
import com.nlhd.composestore.navigate.EditAddress
import com.nlhd.composestore.navigate.EditColorProduct
import com.nlhd.composestore.navigate.EditProduct
import com.nlhd.composestore.navigate.EditProfile
import com.nlhd.composestore.navigate.LikedVideo
import com.nlhd.composestore.navigate.LoadColorProduct
import com.nlhd.composestore.navigate.LoadProduct
import com.nlhd.composestore.navigate.LoadVersionProduct
import com.nlhd.composestore.navigate.MyVideos
import com.nlhd.composestore.navigate.ProfileShortVideo
import com.nlhd.composestore.navigate.Search
import com.nlhd.composestore.navigate.SearchShortSuccess
import com.nlhd.composestore.navigate.SearchShortVideo
import com.nlhd.composestore.navigate.SearchSuccess
import com.nlhd.composestore.navigate.UploadAvatar
import com.nlhd.composestore.navigate.UploadVideo
import com.nlhd.dashboard.Navigate
import com.nlhd.home.HomeScreen
import kotlinx.serialization.Serializable
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.Font
import com.nlhd.core.utils.containerSearch
import com.nlhd.core.utils.containerTextFieldLogin
import com.nlhd.core.utils.containerTopBar
import com.nlhd.core.utils.contentPrice
import com.nlhd.dashboard.Dashboard
import com.nlhd.detail.DetailScreen
import com.nlhd.domain.entity.checkout.CheckoutResponse
import com.nlhd.domain.entity.checkout.Payment
import com.nlhd.domain.entity.product.Product
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.manage_product.EditProductScreen.EditProductScreen
import com.nlhd.manage_product.AddProductScreen.ManageProductScreen
import com.nlhd.manage_product.AddVersionProductScreen.AddVersionProductScreen
import com.nlhd.manage_product.ColorProductScreen.AddColorProductScreen
import com.nlhd.manage_product.ColorProductScreen.EditColorProductScreen
import com.nlhd.manage_product.ColorProductScreen.LoadColorProductScreen
import com.nlhd.manage_product.LoadProductScreen.ProductScreen
import com.nlhd.manage_product.VersionProductScreen.LoadVersionProductScreen
import com.nlhd.order.OrderScreen
import com.nlhd.search.SearchScreen
import com.nlhd.search.SearchSuccessScreen
import com.nlhd.shortvideo.ContentCommonViewModel
import com.nlhd.shortvideo.Profile.ProfileShortVideoScreen
import com.nlhd.shortvideo.Search.SearchShortSuccessScreen
import com.nlhd.shortvideo.ShortVideoScreen
import com.nlhd.shortvideo.UploadVideo.UploadVideoScreen
import com.nlhd.user.EditProfileScreen
import com.nlhd.shortvideo.LikedVideo.LikedVideoScreen
import com.nlhd.shortvideo.MyVideos.MyVideoScreen
import com.nlhd.user.UploadAvatar.UploadAvatarScreen
import com.nlhd.user.UserScreen
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import com.nlhd.category.ManageCategoryScreen
import com.nlhd.composestore.navigate.ManageCategory
import com.nlhd.shortvideo.UploadVideo.UploadVideoMainScreen
import kotlinx.coroutines.delay

@Serializable
object AdminScreen

@Serializable
object CustomerScreen

@Serializable
object LoadingScreen

@Serializable
sealed class Navigation(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Home : Navigation("home", "Trang chủ", R.drawable.ic_home)
    object Shop: Navigation("shop", "Shop", R.drawable.ic_shop)
    object AddVideo: Navigation("addVideo", "Thêm video", R.drawable.ic_addvideo)
    object Order: Navigation("order", "Đơn hàng", R.drawable.ic_artical)
    object User : Navigation("user", "Người dùng", R.drawable.ic_profile)
}

@Serializable
object General

@Serializable
data class Detail(
    val productId: Int,
    val version: Int,
    val color: Int
)

@Serializable
object Cart


@Serializable
data class Checkout(
    val checkoutResponse: String
)

@Serializable
object Dashboard

@Serializable
object Admin



@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BottomBar(
    currentDestination: NavDestination?,
    navController: NavHostController
) {

    Column {
        Divider(
            thickness = AppTheme.dimens.extraSmall,
            //color = Color(0xED484646)
            color = Color(0x4ACECBCB)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = if (currentDestination?.hierarchy?.any { it.route == Navigation.Home.route } == true) Color.Black else Color.Transparent)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val route = listOf(Navigation.Home, Navigation.Shop, Navigation.AddVideo ,Navigation.Order, Navigation.User)
            route.forEach { navigation ->
                BottomBarItem(
                    navigation = navigation,
                    isSelected = currentDestination?.hierarchy?.any { it.route == navigation.route } == true,
                    onClick = {
                        navController.navigate(navigation.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    isShortVideo = currentDestination?.hierarchy?.any { it.route == Navigation.Home.route } == true
                )
            }

        }
    }

}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BottomBarItem(
    navigation: Navigation,
    isSelected: Boolean,
    isShortVideo: Boolean,
    onClick: () -> Unit
) {

    val iconColor = if (isSelected) {
        if (isShortVideo) {
            Color.White
        } else {
            Color.Black
        }

    } else {
        Color.Gray
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = AppTheme.dimens.small)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onClick()
                    }
                )
            }
    ) {
        Icon(painter = painterResource(navigation.icon), contentDescription = null, modifier = Modifier.size(
            AppTheme.dimens.iconBottomBar),tint = iconColor)
        Text(navigation.title, style = AppTheme.typography.labelSmall.copy(
            color = iconColor,
            fontWeight = FontWeight.SemiBold,
        ))
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navigation(
    activity: Activity
) {
    val navController = rememberNavController()
    val viewModel: NavigationViewModel = koinViewModel()
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")
    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(
        key1 = keyStore.value
    ) {
        if (keyStore.value.isNotEmpty()) {
            viewModel.getAdminProfile(keyStore.value)
        }
    }
    val isAdmin = (state.value) is NavigationState.Success
    val navigate = if (isAdmin) {
        AdminScreen
    }
    else CustomerScreen
    NavHost(
        navController = navController,
        startDestination = navigate
    ) {
        composable<CustomerScreen> {
            CustomerScreen(
                onNavigateAdmin = {
                    navController.navigate(AdminScreen)
                },
                activity = activity
            )
        }
        composable<AdminScreen> {
           AdminScreen(
               onClickBack = {
                   if (navController.previousBackStackEntry != null) {
                       navController.popBackStack()
                   }
               }
           )
        }

    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AdminScreen(
    onClickBack: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Dashboard
    ) {
        composable<Dashboard> {
            Dashboard(
                onClickNavigate = {
                    when (it) {
                        Navigate.Product -> {
                            navController.navigate(LoadProduct)
                        }
                        Navigate.Order -> {

                        }
                        Navigate.Customer -> {

                        }
                        Navigate.Balance -> {

                        }
                        Navigate.Profile -> {
                            navController.navigate(Admin)
                        }

                        Navigate.Category -> {
                            navController.navigate(ManageCategory)
                        }
                    }
                }
            )
        }
        composable<Admin> {
            AdminProfileScreen(
                onClickBack = onClickBack,
                onClickBackAdmin = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable<AddProduct> {
            ManageProductScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable<LoadProduct> {
            ProductScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onClickAddProduct = {
                    navController.navigate(AddProduct)
                },
                onClickEditProduct = {
                    navController.navigate(EditProduct(it))
                },
                onClick = { id, name ->
                    navController.navigate(LoadVersionProduct(id, name))
                }
            )
        }
        composable<EditProduct> {
            val id = it.toRoute<EditProduct>().id
            EditProductScreen(
                id = id,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable<LoadVersionProduct> {
            val productId = it.toRoute<LoadVersionProduct>().productId
            val name = it.toRoute<LoadVersionProduct>().productName
            LoadVersionProductScreen(
                productId = productId,
                productName = name,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onClick = {
                    navController.navigate(LoadColorProduct(it))
                },
                onClickAddVersionProduct = {
                    navController.navigate(AddVersionProduct(productId))
                }
            )
        }
        composable<AddVersionProduct> {
            val productId = it.toRoute<AddVersionProduct>().productId
            AddVersionProductScreen(
                productId = productId,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable<ManageCategory> {
            ManageCategoryScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable<LoadColorProduct> {
            val id = it.toRoute<LoadColorProduct>().id
            LoadColorProductScreen(
                versionProductId = id,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onClickAddColor = {
                    navController.navigate(AddColorProduct(it))
                },
                onClickEditColor = {
                    navController.navigate(EditColorProduct(it))
                }
            )
        }
        composable<AddColorProduct> {
            val versionProductId = it.toRoute<AddColorProduct>().versionProduct
            AddColorProductScreen(
                versionProductId = versionProductId,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable<EditColorProduct> {
            val colorId = it.toRoute<EditColorProduct>().id
            EditColorProductScreen(
                colorId = colorId,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}

@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CustomerScreen(
    onNavigateAdmin: () -> Unit,
    activity: Activity
) {
    val navController = rememberNavController()
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    val activity = LocalContext.current as ComponentActivity
    val contentCommonViewModel: ContentCommonViewModel = koinViewModel(viewModelStoreOwner = activity)
    NavHost(
        startDestination = General,
        navController = navController,
    ) {
        composable<General>(
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            GeneralScreen(onClick = { product->
                navController.navigate(Detail(productId = product.id, version = product.versions[0].id, color = product.colors[0].id))
            },
                onClickCart = {
                    navController.navigate(Cart)
                },
                onClickSearch = {
                    navController.navigate(Search)
                },
                onNavigateAdmin = onNavigateAdmin,
                onClickEditProfile = {
                    navController.navigate(EditProfile)
                },
                onClickSeeProduct = { productId, versionId, colorId ->
                    navController.navigate(Detail(productId, versionId, colorId))
                },
                onClickSearchShortVideo = {
                    navController.navigate(SearchShortVideo)
                },
                onClickProfile = { userId ->
                    navController.navigate(ProfileShortVideo(userId))
                },
                onClickAddVideo = {
                    navController.navigate(UploadVideo)
                },
                onClickAvatar = { navController.navigate(UploadAvatar)},
                onClickLikedVideo = {
                    navController.navigate(LikedVideo)
                },
                onClickMyVideo = {
                    navController.navigate(MyVideos)
                }
            )
        }
        composable<Detail>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            val productId = it.toRoute<Detail>().productId
            val version = it.toRoute<Detail>().version
            val color = it.toRoute<Detail>().color
            DetailScreen(
                productId = productId,
                version = version,
                color = color,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onClickCart = {
                    navController.navigate(Cart)
                },
                onNavigateCheckout = {
                    val selectedProductsJson = Json.encodeToString(it)
                    navController.navigate(Checkout(selectedProductsJson))
                },
                onClickSearch = {
                    navController.navigate(Search)
                }
            )
        }

        composable<Search>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            SearchScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        softwareKeyboardController?.hide()
                        navController.popBackStack()
                    }
                },
                onClickSearchSuccess = {
                    navController.navigate(SearchSuccess(it))
                }
            )
        }

        composable<SearchSuccess>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            SearchSuccessScreen(
                search = it.toRoute<SearchSuccess>().search,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onClickProduct = { product ->
                    navController.navigate(Detail(productId = product.id, version = product.versions[0].id, color = product.colors[0].id))
                }
            )
        }

        composable<Cart>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            CartScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onNavigateAddress = {
                    navController.navigate(Address)
                },
                onNavigateEditAddress = {
                    navController.navigate(EditAddress(it))
                },
                onNavigateCheckout = {
                    val selectedProductsJson = Json.encodeToString(it)
                    navController.navigate(Checkout(selectedProductsJson))
                }
            )
        }

        composable<Address>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            AddressScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable<EditAddress>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            val id = it.toRoute<EditAddress>().id
            EditAddressScreen(
                id = id,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable<Checkout>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            val checkoutResponseString = it.toRoute<Checkout>().checkoutResponse
            val gson = Gson()
            val checkoutResponse = gson.fromJson(checkoutResponseString, CheckoutResponse::class.java)

            CheckoutScreen(
                checkoutResponse = checkoutResponse,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onSuccess = {
                    navController.navigate(Payment(
                        amountPaid = it.amountPaid,
                        createdAt = it.createdAt,
                        id = it.id,
                        orderId = it.orderId,
                        paymentMethod = it.paymentMethod,
                        status = it.status,
                        updatedAt = it.updatedAt
                    ))
                },
                activity = activity
            )
        }

        composable<Payment>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            val amountPaid = it.toRoute<Payment>().amountPaid
            CheckoutSuccessScreen(
                onClickBack = {
                    navController.navigate(General) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<EditProfile>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },

        ) {
            EditProfileScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable<SearchShortVideo>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            SearchScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        contentCommonViewModel.releaseAll()
                        softwareKeyboardController?.hide()
                        navController.popBackStack()
                    }
                },
                onClickSearchSuccess = {
                    if (contentCommonViewModel.releaseAll()) {
                        navController.navigate(SearchShortSuccess(it))
                    }

                }
            )
        }

        composable<SearchShortSuccess>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            SearchShortSuccessScreen(
                search = it.toRoute<SearchShortSuccess>().search,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        contentCommonViewModel.releaseAll()
                        navController.popBackStack()
                    }
                },
                onClickSeeProduct = { productId, versionId, colorId ->
                    navController.navigate(Detail(productId, versionId, colorId))
                },
                onClickProfile = {userId->
                    navController.navigate(ProfileShortVideo(userId))
                },
                onSearch = {
                    navController.navigate(SearchShortVideo)
                }
            )
        }

        composable<ProfileShortVideo>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            ProfileShortVideoScreen(
                userId = it.toRoute<ProfileShortVideo>().userId,
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                onClickSeeProduct = {
                    productId, versionId, colorId ->
                    navController.navigate(Detail(productId, versionId, colorId))
                },
                onSearch = {
                    navController.navigate(SearchShortVideo)
                }
            )
        }

        composable<UploadVideo>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            UploadVideoScreen(
                onClickBack = {
                    navController.navigate(General) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onClickBackStack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable<UploadAvatar>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            UploadAvatarScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable<LikedVideo>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            LikedVideoScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }

        composable<MyVideos>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            MyVideoScreen(
                onClickBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}

@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralScreen(
    onClick: (Product) -> Unit,
    onClickCart: () -> Unit,
    onClickSearch: () -> Unit,
    onClickEditProfile: () -> Unit,
    onNavigateAdmin: () -> Unit,
    onClickSeeProduct: (Int, Int, Int) -> Unit,
    onClickSearchShortVideo: () -> Unit,
    onClickProfile: (Int) -> Unit,
    onClickAddVideo: () -> Unit,
    onClickAvatar: () -> Unit,
    onClickLikedVideo: () -> Unit,
    onClickMyVideo: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    val activity = LocalContext.current as ComponentActivity
    val contentCommonViewModel: ContentCommonViewModel = koinViewModel(viewModelStoreOwner = activity)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        contentColor = Color.Black,
        bottomBar = {
            BottomBar(
                currentDestination = currentDestination,
                navController = navController
            )
        },

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Navigation.Home.route
        ) {
            composable(Navigation.Home.route) {
                ShortVideoScreen(
                    innerPadding = innerPadding,
                    onClickBack = {
                        contentCommonViewModel.releaseAll()
                        navController.navigate(Navigation.Home.route) {
                            popUpTo(Navigation.Home.route) { inclusive = true } // 👈 xoá cả entry Video
                            launchSingleTop = true
                        }
                    },
                    onClickSeeProduct = onClickSeeProduct,
                    onClickSearch = onClickSearchShortVideo,
                    onClickProfile = onClickProfile,
                )
            }

            composable(Navigation.Shop.route) {
                HomeScreen(
                    innerPadding = innerPadding,
                    onClick = onClick,
                    onClickCart = onClickCart,
                    onClickSearch = onClickSearch
                )

            }

            composable(Navigation.AddVideo.route) {
                UploadVideoMainScreen(
                    paddingValues = innerPadding,
                    onClickBack = {
                        navController.navigate(Navigation.Home.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Navigation.Order.route) {
                OrderScreen(
                    paddingValues = innerPadding
                )
            }

            composable(Navigation.User.route) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    UserScreen(
                        onNavigateAdmin = onNavigateAdmin,
                        onClickEditProfile = onClickEditProfile,
                        onClickAddVideo = onClickAddVideo,
                        onClickBack = {
                            contentCommonViewModel.releaseAll()
                            navController.navigate(Navigation.Home.route) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onClickAvatar = onClickAvatar,
                        onClickLikedVideo = onClickLikedVideo,
                        onClickMyVideo = onClickMyVideo
                    )
                }
            }
        }
    }

}

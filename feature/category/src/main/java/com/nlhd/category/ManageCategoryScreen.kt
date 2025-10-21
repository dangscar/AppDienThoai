package com.nlhd.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
enum class LoadingState {
    LOADING, SUCCESS, ERROR
}
data class Category(
    val id: Int,
    val name: String,
    val productCount: Int,
    val color: Color
)
class CategoryViewModel {
    private val mockCategories = listOf(
        Category(1, "Apple iPhone", 152, Color(0xFFE8F5E9)),
        Category(2, "Samsung Galaxy", 98, Color(0xFFE1F5FE)),
        Category(3, "Xiaomi/Redmi", 210, Color(0xFFFFFDE7)),
        Category(4, "Oppo", 65, Color(0xFFFBE9E7)),
        Category(5, "Vivo", 40, Color(0xFFF3E5F5)),
        Category(6, "Laptop & Tablet", 85, Color(0xFFECEFF1))
    )

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _loadingState = MutableStateFlow(LoadingState.LOADING)
    val loadingState: StateFlow<LoadingState> = _loadingState
    suspend fun loadCategories() {
        _loadingState.update { LoadingState.LOADING }
        delay(1500)
        if (Math.random() > 0.1) {
            _categories.update { mockCategories }
            _loadingState.update { LoadingState.SUCCESS }
        } else {
            _loadingState.update { LoadingState.ERROR }
        }
    }
}
val mockViewModel = CategoryViewModel()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoryScreen(viewModel: CategoryViewModel = mockViewModel, modifier: Modifier = Modifier) {

    val categories by viewModel.categories.collectAsState()
    val state by viewModel.loadingState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            // Category management header bar
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Quản Lý Danh Mục",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onClick = { println("Thêm danh mục mới") }) {
                        Icon(Icons.Filled.Add, contentDescription = "Thêm danh mục")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5)),
        ) {
            Text(
                "Danh sách các danh mục sản phẩm",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            when (state) {
                LoadingState.LOADING -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                LoadingState.ERROR -> {
                    ErrorView(
                        message = "Lỗi tải dữ liệu. Hãy kiểm tra kết nối mạng của bạn và IP server.",
                        onRetry = {
                            viewModel.loadCategories()
                        }
                    )
                }
                LoadingState.SUCCESS -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(categories) { category ->
                            CategoryManagementItem(category = category) {
                                println("User wants to edit category: ${category.name}")
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ErrorView(message: String, onRetry: suspend () -> Unit) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = {

                scope.launch {
                    onRetry()
                }
            }
        ) {
            Text("Thử lại")
        }
    }
}

@Composable
fun CategoryManagementItem(category: Category, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(category.color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.name.take(1),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))


            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "SL Sản phẩm: ${category.productCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }


            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Edit arrow",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewManageCategoryScreen() {
    MaterialTheme {
        ManageCategoryScreen(viewModel = mockViewModel)
    }
}

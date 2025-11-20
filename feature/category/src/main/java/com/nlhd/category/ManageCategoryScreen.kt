package com.nlhd.category

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nlhd.core.R
import com.nlhd.core.theme.AppTheme
import com.nlhd.keystore.KeyStoreManager
import org.koin.androidx.compose.koinViewModel

data class Category(
    val id: Int,
    val name: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ManageCategoryScreen(
    viewModel: ManageCategoryViewModel = koinViewModel(),
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    val keyStore = KeyStoreManager.getKeyStore(context).collectAsStateWithLifecycle("")

    var showDialogAdd by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val manageCategoryState by viewModel.manageCategoryState.collectAsStateWithLifecycle()
    val addCategoryState by viewModel.addCategoryState.collectAsStateWithLifecycle()
    val name by viewModel.name.collectAsStateWithLifecycle()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quản lý danh mục",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialogAdd = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Thêm danh mục", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Tìm kiếm danh mục...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, "Tìm kiếm")
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            when (manageCategoryState) {
                is ManageCategoryState.Error -> {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(padding), contentAlignment = Alignment.Center) {
                        Text((manageCategoryState as ManageCategoryState.Error).message, style = AppTheme.typography.titleMedium)
                    }
                }
                ManageCategoryState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LottieAnimation(
                            composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(AppTheme.dimens.large)
                        )
                    }
                }
                is ManageCategoryState.Success -> {
                    val categories = (manageCategoryState as ManageCategoryState.Success).data.categories
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(categories.size) {
                            val category = categories[it]
                            CategoryItem(category.name) {

                            }
                        }
                    }
                }
            }

            when (addCategoryState) {
                is AddCategoryState.Error -> {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(padding), contentAlignment = Alignment.Center) {
                        Text((addCategoryState as AddCategoryState.Error).message, style = AppTheme.typography.titleMedium)
                    }
                }
                AddCategoryState.Idle -> {}
                AddCategoryState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LottieAnimation(
                            composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(AppTheme.dimens.large)
                        )
                    }
                }
                is AddCategoryState.Success -> {
                    viewModel.getCategory()
                    viewModel.setName("")
                    viewModel.updateAddCategoryState()
                    showDialogAdd = false
                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    // Add Dialog
    if (showDialogAdd) {
        CategoryDialog(
            name = name,
            onDismiss = { showDialogAdd = false },
            onValueChange = viewModel::setName,
            onSave = {
                viewModel.addCategory(keyStore.value)

            }
        )
    }

}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun CategoryItem(
    name: String,
    onEdit: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = onEdit
            ) {
                Icon(Icons.Default.Edit, null)
            }
        }
    }
}

@Composable
fun CategoryDialog(
    name: String,
    onDismiss: () -> Unit,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Thêm danh mục mới")
        },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = onValueChange,
                label = { Text("Tên danh mục") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) onSave()
                },
                enabled = name.isNotBlank()
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
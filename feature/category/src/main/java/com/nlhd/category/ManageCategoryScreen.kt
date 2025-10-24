package com.nlhd.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

data class Category(
    val id: Int,
    val name: String,
    val productCount: Int,
    val isActive: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoryScreen() {
    var categories by remember {
        mutableStateOf(
            listOf(
                Category(1, "Samsung", 25, true),
                Category(2, "iPhone", 30, true),
                Category(3, "Xiaomi", 20, true),
                Category(4, "OPPO", 15, true),
                Category(5, "Vivo", 10, false)
            )
        )
    }
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var searchQuery by remember { mutableStateOf("") }

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
                    IconButton(onClick = { /* Navigate back */ }) {
                        Icon(Icons.Default.ArrowBack, "Quay lại")
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
                    selectedCategory = null
                    showDialog = true
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

            // Statistics Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatItem("Tổng danh mục", categories.size.toString(), Color(0xFF2196F3))
                    StatItem("Đang hoạt động", categories.count { it.isActive }.toString(), Color(0xFF4CAF50))
                    StatItem("Tổng sản phẩm", categories.sumOf { it.productCount }.toString(), Color(0xFFFF9800))
                }
            }

            // Category List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val filteredCategories = categories.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }

                items(filteredCategories) { category ->
                    CategoryItem(
                        category = category,
                        onEdit = {
                            selectedCategory = category
                            showDialog = true
                        },
                        onDelete = {
                            categories = categories.filter { it.id != category.id }
                        },
                        onToggleStatus = {
                            categories = categories.map {
                                if (it.id == category.id) it.copy(isActive = !it.isActive)
                                else it
                            }
                        }
                    )
                }
            }
        }
    }

    // Add/Edit Dialog
    if (showDialog) {
        CategoryDialog(
            category = selectedCategory,
            onDismiss = { showDialog = false },
            onSave = { name ->
                if (selectedCategory != null) {
                    // Edit
                    categories = categories.map {
                        if (it.id == selectedCategory!!.id) it.copy(name = name)
                        else it
                    }
                } else {
                    // Add new
                    val newId = (categories.maxOfOrNull { it.id } ?: 0) + 1
                    categories = categories + Category(newId, name, 0, true)
                }
                showDialog = false
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
    category: Category,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleStatus: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

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
                    text = category.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${category.productCount} sản phẩm",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Status badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (category.isActive) Color(0xFF4CAF50).copy(alpha = 0.1f)
                    else Color(0xFFFF5252).copy(alpha = 0.1f)
                ) {
                    Text(
                        text = if (category.isActive) "Hoạt động" else "Tắt",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = if (category.isActive) Color(0xFF4CAF50) else Color(0xFFFF5252),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Menu button
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, "Menu")
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Chỉnh sửa") },
                            onClick = {
                                showMenu = false
                                onEdit()
                            },
                            leadingIcon = { Icon(Icons.Default.Edit, null) }
                        )
                        DropdownMenuItem(
                            text = { Text(if (category.isActive) "Tắt" else "Bật") },
                            onClick = {
                                showMenu = false
                                onToggleStatus()
                            },
                            leadingIcon = {
                                Icon(
                                    if (category.isActive) Icons.Default.Close
                                    else Icons.Default.Check,
                                    null
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Xóa", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Delete, null, tint = Color.Red)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDialog(
    category: Category?,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (category == null) "Thêm danh mục mới" else "Chỉnh sửa danh mục")
        },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tên danh mục") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) onSave(name)
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
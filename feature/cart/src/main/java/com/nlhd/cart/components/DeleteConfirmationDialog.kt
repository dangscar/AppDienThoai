package com.nlhd.cart.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.nlhd.core.theme.AppTheme
import com.nlhd.core.utils.containerConfirm

@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Xác nhận xóa", style = AppTheme.typography.titleMedium)
            },
            text = {
                Text("Bạn có chắc chắn muốn xóa mục này?", style = AppTheme.typography.headlineLarge)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    Text("Xóa", color = containerConfirm)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Hủy")
                }
            }
        )
    }
}

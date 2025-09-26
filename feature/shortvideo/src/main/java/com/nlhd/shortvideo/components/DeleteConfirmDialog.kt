package com.nlhd.shortvideo.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nlhd.core.theme.AppTheme

@Composable
fun DeleteConfirmDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Xác nhận xóa", style = AppTheme.typography.titleMedium.copy(
                    color = Color.Black
                ))
            },
            text = {
                Text("Bạn có chắc chắn muốn xóa mục này không?", style = AppTheme.typography.bodyMedium.copy(
                    color = Color.Black
                ))
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                    Text("Xóa", style = AppTheme.typography.bodyMedium.copy(
                        color = Color.Red
                    ))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Hủy", style = AppTheme.typography.bodyMedium.copy(
                        color = Color.Black
                    ))
                }
            }
        )
    }
}
package com.nlhd.shortvideo.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    bottomAction: @Composable () ->Unit,
) {

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = {},
        shape = BottomSheetDefaults.ExpandedShape,
        scrimColor = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        bottomAction()
    }
}
package com.jackappsdev.password_manager.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.PdfBitmapConverter

/**
 * A composable function that displays a full-screen preview of a PDF document.
 * If the PDF cannot be rendered, it shows an error message instead.
 */
@Composable
internal fun PdfFullScreenPreview(
    pdfBytes: ByteArray,
    pdfIndex: Int,
    onDismiss: () -> Unit,
    onExport: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            PreviewHeader(
                title = stringResource(R.string.label_pdf_index, pdfIndex),
                onDismiss = onDismiss,
                onExport = onExport
            )
            PdfViewerScreen(
                pdfBytes = pdfBytes,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Composable
private fun PdfViewerScreen(
    pdfBytes: ByteArray,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pdfBitmapConverter = remember { PdfBitmapConverter(context) }
    var renderedPages by remember { mutableStateOf<List<Bitmap>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(pdfBytes) {
        isLoading = true
        renderedPages = pdfBitmapConverter.pdfToBitmaps(pdfBytes)
        isLoading = false
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            LoadingStateView()
        } else if (renderedPages.isEmpty()) {
            Text(
                text = stringResource(R.string.toast_attachment_read_failed),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(renderedPages) { _, page ->
                    PdfPage(page = page)
                }
            }
        }
    }
}

@Composable
private fun PdfPage(
    page: Bitmap,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = page,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(page.width.toFloat() / page.height.toFloat())
    )
}

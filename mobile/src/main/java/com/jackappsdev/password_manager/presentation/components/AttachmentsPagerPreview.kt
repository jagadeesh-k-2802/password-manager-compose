package com.jackappsdev.password_manager.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jackappsdev.password_manager.core.IMAGES_JPEG_MIME
import com.jackappsdev.password_manager.core.PDF_MIME
import com.jackappsdev.password_manager.core.isPdf

@Composable
internal fun AttachmentsPagerPreview(
    attachments: List<ByteArray>,
    initialIndex: Int,
    onDismiss: () -> Unit,
    onExport: (ByteArray, String, String) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { attachments.size }
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            key = { it }
        ) { pageIndex ->
            val bytes = attachments[pageIndex]
            val isPdf = remember(bytes) { bytes.isPdf() }

            if (isPdf) {
                PdfPreviewContent(
                    pdfBytes = bytes,
                    pdfIndex = pageIndex + 1,
                    onDismiss = onDismiss,
                    onExport = {
                        onExport(bytes, "pdf-${pageIndex + 1}.pdf", PDF_MIME)
                    }
                )
            } else {
                ImagePreviewContent(
                    image = bytes,
                    imageIndex = pageIndex + 1,
                    onDismiss = onDismiss,
                    onExport = {
                        onExport(bytes, "image-${pageIndex + 1}.jpg", IMAGES_JPEG_MIME)
                    }
                )
            }
        }
    }
}

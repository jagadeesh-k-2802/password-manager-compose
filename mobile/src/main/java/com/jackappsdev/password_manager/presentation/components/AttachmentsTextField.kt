package com.jackappsdev.password_manager.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.ALL_SUPPORTED_MIME_TYPES
import com.jackappsdev.password_manager.core.AttachmentPickResult
import com.jackappsdev.password_manager.core.IMAGES_JPEG_MIME
import com.jackappsdev.password_manager.core.MAX_ATTACHMENTS_PER_PASSWORD
import com.jackappsdev.password_manager.core.MAX_IMAGE_SIZE_BYTES
import com.jackappsdev.password_manager.core.MAX_IMAGE_SIZE_MB
import com.jackappsdev.password_manager.core.MAX_PDF_SIZE_BYTES
import com.jackappsdev.password_manager.core.MAX_PDF_SIZE_MB
import com.jackappsdev.password_manager.core.PDF_MIME
import com.jackappsdev.password_manager.core.isPdf
import com.jackappsdev.password_manager.core.readAttachment
import com.jackappsdev.password_manager.shared.core.showToast

/**
 * A text-field styled container that shows the attached Images and PDFs as chips. Supports
 * picking new attachments (with size/type validation) and previewing them in
 * a dialog. When [readOnly] is true the add and delete affordances are hidden.
 */
@Composable
fun AttachmentsTextField(
    images: List<ByteArray>,
    onAddAttachment: (ByteArray) -> Unit,
    onRemoveAttachment: (Int) -> Unit,
    onExport: (ByteArray, String, String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    maxAttachments: Int = MAX_ATTACHMENTS_PER_PASSWORD
) {
    val context = LocalContext.current
    var previewIndex by remember { mutableStateOf<Int?>(null) }
    val scrollState = rememberScrollState()

    // UI Theming
    val outlineColor = OutlinedTextFieldDefaults.colors().unfocusedTextColor
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val iconTint = MaterialTheme.colorScheme.onSurfaceVariant
    val surfaceColor = MaterialTheme.colorScheme.surface
    val shape = OutlinedTextFieldDefaults.shape

    @SuppressLint("LocalContextGetResourceValueCall")
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        when (val result = readAttachment(context, uri, MAX_IMAGE_SIZE_BYTES, MAX_PDF_SIZE_BYTES)) {
            is AttachmentPickResult.Success -> onAddAttachment(result.bytes)
            is AttachmentPickResult.TooLarge -> onAttachmentTooLarge(context, uri)
            is AttachmentPickResult.InvalidType -> context.showToast(context.getString(R.string.toast_attachment_invalid_type))
            is AttachmentPickResult.ReadFailed -> context.showToast(context.getString(R.string.toast_attachment_read_failed))
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 64.dp)
                .border(width = 1.dp, color = outlineColor, shape = shape)
                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 0.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.AttachFile,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .padding(top = 11.dp)
                    .size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                images.forEachIndexed { index, bytes ->
                    val isPdf = remember(bytes) { bytes.isPdf() }
                    InputChip(
                        selected = false,
                        onClick = { previewIndex = index },
                        label = {
                            Text(
                                if (isPdf) stringResource(R.string.label_pdf_index, index + 1)
                                else stringResource(R.string.label_image_index, index + 1)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (isPdf) Icons.Outlined.PictureAsPdf else Icons.Outlined.Image,
                                contentDescription = null,
                                modifier = Modifier.size(InputChipDefaults.IconSize)
                            )
                        },
                        trailingIcon = {
                            if (!readOnly) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = stringResource(R.string.accessibility_remove_attachment),
                                    modifier = Modifier
                                        .size(InputChipDefaults.IconSize)
                                        .clickable { onRemoveAttachment(index) }
                                )
                            }
                        }
                    )
                }

                if (!readOnly && images.size < maxAttachments) {
                    AssistChip(
                        onClick = { launcher.launch(ALL_SUPPORTED_MIME_TYPES) },
                        label = { Text(stringResource(R.string.btn_add_attachment)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.AddPhotoAlternate,
                                contentDescription = stringResource(R.string.accessibility_add_attachment),
                                modifier = Modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    )
                }
            }
        }

        Text(
            text = stringResource(R.string.label_attachments),
            style = MaterialTheme.typography.bodySmall,
            color = labelColor,
            modifier = Modifier
                .padding(start = 12.dp)
                .background(color = surfaceColor, shape = RoundedCornerShape(1.dp))
                .padding(horizontal = 4.dp)
                .offset(y = (-8).dp)
        )
    }

    @Suppress("AssignedValueIsNeverRead")
    previewIndex?.let {
        if (previewIndex in images.indices) {
            val bytes = images[it]
            val isPdf = remember(bytes) { bytes.isPdf() }
            when {
                isPdf -> {
                    PdfFullScreenPreview(
                        pdfBytes = bytes,
                        pdfIndex = it + 1,
                        onDismiss = { previewIndex = null },
                        onExport = { onExport(bytes, "pdf-${it + 1}.pdf", PDF_MIME) }
                    )
                }
                else -> {
                    ImageFullScreenPreview(
                        image = bytes,
                        imageIndex = it + 1,
                        onDismiss = { previewIndex = null },
                        onExport = { onExport(bytes, "image-${it + 1}.jpg", IMAGES_JPEG_MIME) }
                    )
                }
            }
        } else {
            previewIndex = null
        }
    }
}

private fun onAttachmentTooLarge(context: Context, uri: Uri) {
    val mimeType = context.contentResolver.getType(uri)
    val isPdf = mimeType == PDF_MIME
    if (isPdf) {
        context.showToast(context.getString(R.string.toast_pdf_too_large, MAX_PDF_SIZE_MB))
    } else {
        context.showToast(context.getString(R.string.toast_image_too_large, MAX_IMAGE_SIZE_MB))
    }
}

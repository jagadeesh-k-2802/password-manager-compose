package com.jackappsdev.password_manager.presentation.components

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.core.ImagePickResult
import com.jackappsdev.password_manager.core.readImageForAttachment
import com.jackappsdev.password_manager.shared.core.showToast

private const val MAX_IMAGES_PER_PASSWORD = 4
private const val MAX_IMAGE_SIZE_MB = 2
private const val MAX_IMAGE_SIZE_BYTES = MAX_IMAGE_SIZE_MB * 1024 * 1024

/**
 * A text-field styled container that shows the attached images as chips. Supports
 * picking new images (with size/type validation) and previewing attached images in
 * a dialog. When [readOnly] is true the add and delete affordances are hidden which
 * matches the password detail screen.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImagesTextField(
    images: List<ByteArray>,
    onAddImage: (ByteArray) -> Unit,
    onRemoveImage: (Int) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    maxImages: Int = MAX_IMAGES_PER_PASSWORD
) {
    val context = LocalContext.current
    var previewIndex by remember { mutableStateOf<Int?>(null) }

    @SuppressLint("LocalContextGetResourceValueCall")
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        when (val result = readImageForAttachment(context, uri, MAX_IMAGE_SIZE_BYTES)) {
            is ImagePickResult.Success -> onAddImage(result.bytes)
            is ImagePickResult.TooLarge -> context.showToast(context.getString(R.string.toast_image_too_large, MAX_IMAGE_SIZE_MB))
            is ImagePickResult.InvalidType -> context.showToast(context.getString(R.string.toast_image_invalid_type))
            is ImagePickResult.ReadFailed -> context.showToast(context.getString(R.string.toast_image_read_failed))
        }
    }

    val outlineColor = OutlinedTextFieldDefaults.colors().unfocusedTextColor
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val iconTint = MaterialTheme.colorScheme.onSurfaceVariant
    val surfaceColor = MaterialTheme.colorScheme.surface
    val shape = OutlinedTextFieldDefaults.shape

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

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(5f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                images.forEachIndexed { index, _ ->
                    InputChip(
                        selected = false,
                        onClick = { previewIndex = index },
                        label = { Text(stringResource(R.string.label_image_index, index + 1)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Image,
                                contentDescription = null,
                                modifier = Modifier.size(InputChipDefaults.IconSize)
                            )
                        },
                        trailingIcon = {
                            if (!readOnly) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = stringResource(R.string.accessibility_remove_image),
                                    modifier = Modifier
                                        .size(InputChipDefaults.IconSize)
                                        .clickable { onRemoveImage(index) }
                                )
                            }
                        }
                    )
                }

                if (!readOnly && images.size < maxImages) {
                    AssistChip(
                        onClick = { launcher.launch("image/*") },
                        label = { Text(stringResource(R.string.btn_add_image)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.AddPhotoAlternate,
                                contentDescription = stringResource(
                                    R.string.accessibility_add_image
                                ),
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

    previewIndex?.let { index ->
        if (index in images.indices) {
            ImageFullScreenPreview(
                image = images[index],
                imageIndex = index + 1,
                onDismiss = { previewIndex = null }
            )
        } else {
            previewIndex = null
        }
    }
}

@Composable
private fun ImageFullScreenPreview(
    image: ByteArray,
    imageIndex: Int,
    onDismiss: () -> Unit
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
        val bitmap = remember(image) {
            runCatching { BitmapFactory.decodeByteArray(image, 0, image.size) }.getOrNull()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = stringResource(R.string.accessibility_image_preview),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.toast_image_read_failed),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .statusBarsPadding()
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .align(Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.accessibility_close_preview),
                        tint = Color.White
                    )
                }
                Text(
                    text = stringResource(R.string.label_image_index, imageIndex),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
        }
    }
}

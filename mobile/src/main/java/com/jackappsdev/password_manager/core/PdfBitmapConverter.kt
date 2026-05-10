package com.jackappsdev.password_manager.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PdfBitmapConverter(
    private val context: Context
) {
    var renderer: PdfRenderer? = null

    suspend fun pdfToBitmaps(pdfBytes: ByteArray): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            renderer?.close()

            val tempFile = File(context.cacheDir, "temp_pdf_preview.pdf")
            FileOutputStream(tempFile).use { fos ->
                fos.write(pdfBytes)
            }

            val descriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val bitmaps = renderPages(descriptor)
            descriptor.close()
            tempFile.delete()
            bitmaps
        }
    }

    private suspend fun renderPages(descriptor: ParcelFileDescriptor): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            with(PdfRenderer(descriptor)) {
                renderer = this

                return@withContext (0 until pageCount).map { index ->
                    async {
                        openPage(index).use { page ->
                            val bitmap = createBitmap(page.width, page.height)

                            Canvas(bitmap).apply {
                                drawColor(Color.WHITE)
                                drawBitmap(bitmap, 0f, 0f, null)
                            }

                            page.render(
                                bitmap,
                                null,
                                null,
                                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                            )

                            bitmap
                        }
                    }
                }.awaitAll()
            }
        }
    }
}

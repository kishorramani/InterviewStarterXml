package com.example.interviewstarter.data.remote.upload

import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.utils.io.core.writeFully
import java.io.File

class FileUploadRepository(
    private val client: HttpClient
) {

    suspend fun upload(
        url: String,
        file: File,
        onProgress: (sent: Long, total: Long) -> Unit = { _, _ -> }
    ) {
        client.post(url) {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("description", file.name)

                        append(
                            key = "file",
                            filename = file.name,
                            contentType = ContentType.Application.OctetStream,
                            size = file.length()
                        ) {
                            writeFully(file.readBytes())
                        }
                    }
                )
            )

            onUpload { sent, total ->
                onProgress(sent, total ?: 0L)
            }
        }
    }
}
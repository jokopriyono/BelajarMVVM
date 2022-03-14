package com.jokopriyono.belajarmvvm.ui

import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jokopriyono.belajarmvvm.data.MainRepository
import com.jokopriyono.belajarmvvm.data.model.UploadResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val uploadResponse = MutableLiveData<UploadResponse>()
    val loading = MutableLiveData(false)
    val message = MutableLiveData<String>()

    fun uploadImageToServer(file: File) {
        val url = "https://store1.gofile.io/uploadFile"
        val token = "MycC2HEmsOPrnbNSrCbCZ2Q7MRUnRhBN"
        val fileRequestBody = file.asRequestBody(
            getMimeType(file.path)!!.toMediaType()
        )
        // contoh untuk melampirkan file di form-data
        val fileMultiPart = MultipartBody.Part.createFormData(
            "file",
            file.name, fileRequestBody
        )
        // contoh untuk melampirkan text di form-data
        val tokenRequestBody = token.toRequestBody(
            "text/plain".toMediaType()
        )

        viewModelScope.launch(ioDispatcher) {
            mainRepository.uploadFileAndGetResult(
                onStart = { loading.postValue(true) },
                onComplete = { loading.postValue(false) },
                onError = { message.postValue(it) },
                url,
                fileMultiPart,
                tokenRequestBody
            ).collect {
                uploadResponse.postValue(it)
            }

        }
    }

    private fun getMimeType(path: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}
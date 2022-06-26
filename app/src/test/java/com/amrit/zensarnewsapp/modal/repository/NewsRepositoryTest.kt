package com.amrit.zensarnewsapp.modal.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amrit.zensarnewsapp.network.APIClient
import com.amrit.zensarnewsapp.network.ApiService
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.Assert.assertNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepositoryTest {

    private lateinit var newsRepository: NewsRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val server = MockWebServer()

    @Before
    fun setUp() {
        server.start(8000)
        val BASE_URL = server.url("/").toString()
        val okHttpClient = OkHttpClient
            .Builder()
            .build()

        val apiService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build().create(ApiService::class.java)
        mockkObject(APIClient)
        every { APIClient.getApiService() } returns apiService
        newsRepository = NewsRepository()
    }

    @Test
    fun `given invalid country code, verify null response is returned`() {
        val fetchNewsData = runBlocking {
            newsRepository.fetchNewsData(null)
        }
        assertNull(fetchNewsData)
    }

    @Test
    fun `given dummy json, verify valid response is returned`() {
        val mockedResponse =
            MockResponseFileReader("api-response/headlines_success_response.json").content
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedResponse)
        )

        val response = runBlocking { newsRepository.fetchNewsData() }
        Assert.assertEquals("The Canadian Press", response!![0].author ?: "")
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}
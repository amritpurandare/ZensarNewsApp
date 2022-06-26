package com.amrit.zensarnewsapp.viewmodal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.amrit.zensarnewsapp.modal.Articles
import com.amrit.zensarnewsapp.modal.repository.NewsRepository
import com.amrit.zensarnewsapp.network.Response
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsViewModalTest {

    private lateinit var newsViewModal: NewsViewModal

    @MockK
    private lateinit var newsRepository: NewsRepository
    @MockK
    private lateinit var observer: Observer<Response<List<Articles>>>

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        newsViewModal = NewsViewModal()
    }

    @Test
    fun `if getNewsHeadline is called, verify livedata is not null`() {
        newsViewModal.newsHeadLines.observeForever(observer)
        every { observer.onChanged(any()) } returns Unit
        coEvery { newsRepository.fetchNewsData() } returns ArrayList<Articles>()
        newsViewModal.getNewsHeadline()
        assertNotNull(newsViewModal.newsHeadLines.value)
    }
}
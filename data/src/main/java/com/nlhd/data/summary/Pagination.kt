package com.nlhd.data.summary

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nlhd.core.utils.Utils

fun urlMethod(page: Int, method: String) = "${Utils.BASE_URL}/api$method/?page=$page"

fun<T : Any> returnPage(resources: List<T>, page: Int): PagingSource.LoadResult.Page<Int, T> = PagingSource.LoadResult.Page(
    data = resources,
    prevKey = if (page == 1) null else page - 1,
    nextKey = page + 1
)

fun<T : Any> emptyPage(): PagingSource.LoadResult.Page<Int, T> = PagingSource.LoadResult.Page(
    data = emptyList(),
    prevKey = null,
    nextKey = null
)

fun<T : Any> returnState(state: PagingState<Int, T>): Int? = state.anchorPosition?.let {
    val anchorPage = state.closestPageToPosition(it)
    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
}
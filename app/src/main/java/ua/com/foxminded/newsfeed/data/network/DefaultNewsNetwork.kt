package ua.com.foxminded.newsfeed.data.network

import retrofit2.Response
import ua.com.foxminded.newsfeed.data.dto.NewsResponse
import ua.com.foxminded.newsfeed.util.Result

class DefaultNewsNetwork(private val newsFeedApi: NewsFeedApi) : NewsNetwork {

    private val list = ArrayList<NewsResponse>()

    override suspend fun getAllNews(): Result<List<NewsResponse>> {
        list.clear()
        handle(newsFeedApi.getNytNews())?.let { return Result.Error(Throwable(it)) }
        handle(newsFeedApi.getWiredNews())?.let { return Result.Error(Throwable(it)) }
        handle(newsFeedApi.getCnnNews())?.let { return Result.Error(Throwable(it)) }
        return Result.Success(list)
    }

    override suspend fun getNytNews(): Result<NewsResponse> {
        return handleRetrofitResponse(newsFeedApi.getNytNews())
    }

    override suspend fun getCnnNews(): Result<NewsResponse> {
        return handleRetrofitResponse(newsFeedApi.getCnnNews())
    }

    override suspend fun getWiredNews(): Result<NewsResponse> {
        return handleRetrofitResponse(newsFeedApi.getWiredNews())
    }

    private fun handleRetrofitResponse(response: Response<NewsResponse>): Result<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(
            Throwable("Status code: ${response.code()}. ${response.message()}")
        )
    }

    private fun handle(response: Response<NewsResponse>): String? {
        return if (response.isSuccessful) {
            response.body()?.let {
                list.add(it)
                return null
            }
        } else {
            ("Status code: ${response.code()}. ${response.message()}")
        }
    }
}
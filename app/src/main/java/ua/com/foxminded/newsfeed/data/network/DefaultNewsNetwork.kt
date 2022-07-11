package ua.com.foxminded.newsfeed.data.network

import retrofit2.Response
import ua.com.foxminded.newsfeed.data.model.NewsResponse
import ua.com.foxminded.newsfeed.util.Result

class DefaultNewsNetwork(private val newsFeedApi: NewsFeedApi) : NewsNetwork {

    override suspend fun getNytNews(): Result<NewsResponse> {
        return handleRetrofitResponse(newsFeedApi.getNytNews())
    }

    override suspend fun getCnnNews(): Result<NewsResponse> {
        return handleRetrofitResponse(newsFeedApi.getCnnNews())
    }

    override suspend fun getFinancialTimesNews(): Result<NewsResponse> {
        return handleRetrofitResponse(newsFeedApi.getFinancialTimesNews())
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
}
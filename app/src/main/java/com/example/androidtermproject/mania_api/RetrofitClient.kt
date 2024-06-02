import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class RetrofitClient {
    private val BASE_URL = "https://www.maniadb.com/"
    private var instance: Retrofit? = null

    open fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create()) // SimpleXmlConverterFactory 추가
                .build()
        }
        return instance!!
    }
}

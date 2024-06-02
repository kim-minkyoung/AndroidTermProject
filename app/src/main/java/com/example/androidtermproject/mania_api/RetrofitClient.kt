import com.example.androidtermproject.mania_api.ManiaDBService
import com.example.androidtermproject.mania_api.Song
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class RetrofitClient {
    private val BASE_URL = "https://www.maniadb.com/"
    private var instance: Retrofit? = null

    open fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
        }
        return instance!!
    }
}

fun main() {
    val retrofitClient = RetrofitClient()
    val retrofit = retrofitClient.getInstance()

    // 노래 이름
    val songName = "BlackSheepWall"

    val apiService = retrofit.create(ManiaDBService::class.java)
    val call = apiService.searchSongs()

    call.enqueue(object : Callback<List<Song>> {
        override fun onResponse(call: Call<List<Song>>, response: Response<List<Song>>) {
            if (response.isSuccessful) {
                val songs = response.body()
                // 응답이 비어있지 않은지 확인
                if (songs != null && songs.isNotEmpty()) {
                    // 첫 번째 노래 정보만 사용
                    val song = songs[0]
                    // 사용할 데이터 추출
                    val title = song.title
                    val artist = song.artist

                    // 사용할 데이터 출력 또는 처리
                    println("노래 제목: $title")
                    println("아티스트: $artist")

                    // 추가 작업 수행
                } else {
                    println("검색 결과가 없습니다.")
                }
            } else {
                println("응답이 성공적이지 않음: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<List<Song>>, t: Throwable) {
            // 실패한 경우 처리
            println("API 호출 실패: ${t.message}")
        }
    })

}

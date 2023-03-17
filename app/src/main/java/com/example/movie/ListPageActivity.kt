package com.example.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ListPageActivity : AppCompatActivity(), Callback<MovieResponse> {
    private lateinit var edtSearch: AppCompatEditText
    private lateinit var rvMovies: RecyclerView

    private lateinit var movieService: MovieService
    private val listMovies by lazy { mutableListOf<Movie>() }
    private val adapter by lazy { MovieAdapter(listMovies) }

    private var isLoading = false
    private var page = 1
    private var totalResult: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtSearch = findViewById(R.id.edt_search)
        rvMovies = findViewById(R.id.rv_movies)

        initRecyclerView()
        initRetrofit()
        //set listener
        edtSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                page = 1
                searchMovie(textView.text.toString())
            }
            true
        }
    }

    private fun initRecyclerView() {
        val margin = resources.getDimensionPixelSize(R.dimen.padding_content)
        rvMovies.addItemDecoration(GridSpacingItemDecoration(2, margin, margin, false, false))
        rvMovies.adapter = adapter

        val gridLayoutManager = rvMovies.layoutManager as GridLayoutManager
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.isPositionFooter(position)) gridLayoutManager.spanCount else 1
            }
        }

        rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    val canLoadMore = page * 10 < totalResult
                    if (canLoadMore && gridLayoutManager.findLastCompletelyVisibleItemPosition() == listMovies.size - 1) {
                        adapter.isLoadMore = true
                        isLoading = true
                        loadMore()
                    }
                }
            }
        })
    }

    private fun loadMore() {
        listMovies.add(Movie())
        adapter.notifyItemInserted(listMovies.size - 1)
        rvMovies.scrollToPosition(listMovies.size - 1)
        page++
        searchMovie(edtSearch.text.toString())
    }

    private fun initRetrofit() {
        val okhttpBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        okhttpBuilder.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .client(okhttpBuilder.build())
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        movieService = retrofit.create(MovieService::class.java)
    }

    private fun searchMovie(title: String) {
        val call = movieService.getListMovies(title = title, page = page)
        call.enqueue(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
        val result = response.body()?.results ?: mutableListOf()
        totalResult = response.body()?.totalResults?.toIntOrNull() ?: 0
        if (isLoading) {
            adapter.isLoadMore = false
            listMovies.removeLast()
            adapter.notifyItemRemoved(listMovies.size - 1)
            listMovies.addAll(result)
            adapter.notifyItemRangeInserted(listMovies.size - result.size, listMovies.size)
            isLoading = false
        } else {
            listMovies.clear()
            listMovies.addAll(result)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
        Toast.makeText(this, t.message, Toast.LENGTH_LONG).show()
    }
}
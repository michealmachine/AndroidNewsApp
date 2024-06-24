package com.example.mynewsapp.data.model

// 定义一个数据类，用于解析后端 API 发送的 JSON 数据
data class NewsResponse(
    // 新闻获取的状态
    val status: String,
    // 获取的新闻总数
    val totalResults: Int,
    // 新闻列表，包含多篇新闻
    val articles: List<Article>
) {
    // 定义一个数据类，用于解析新闻的详细信息
    data class Article(
        // 新闻来源
        val source: Source,
        // 新闻作者
        val author: String?,
        // 新闻标题
        val title: String,
        // 新闻描述
        val description: String?,
        // 新闻的 URL
        val url: String,
        // 新闻图片的 URL
        val urlToImage: String?,
        // 新闻发布的时间
        val publishedAt: String?,
        // 新闻的内容
        val content: String?
    )

    // 定义一个数据类，用于解析新闻来源的信息
    data class Source(
        // 新闻来源的 ID
        val id: String?,
        // 新闻来源的名称
        val name: String
    )
}
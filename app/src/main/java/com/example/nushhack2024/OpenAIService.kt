package com.example.nushhack2024

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

class OpenAIService {
    fun gpt()=runBlocking {
        val token = System.getenv("OPENAI_API_KEY") ?: error("OpenAI API key is missing")
        val openAI = OpenAI(token = token)
        val modelID = ModelId("gpt-3.5-turbo-1106")
        val userMessage = "Hello, ChatGPT! How can you assist me?"
        val chatRequest = ChatCompletionRequest(
            model = modelID, // or "gpt-4" for GPT-4
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = userMessage
                )
            )
        )
        val chatResponse = openAI.chatCompletion(chatRequest)

        // Print out the response from ChatGPT
        println(chatResponse.choices.first().message.content)
    }
}
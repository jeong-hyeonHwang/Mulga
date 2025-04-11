package com.ilm.mulga.data.service

import android.util.Log
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RabbitMqPublisher(
    private val host: String,
    private val port: Int,
    private val username: String,
    private val password: String,
    private val virtualHost: String = "/",
) {
    private var connection: Connection? = null
    private var channel: Channel? = null

    suspend fun connect(): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val factory = ConnectionFactory().apply {
                this.host = this@RabbitMqPublisher.host
                this.port = this@RabbitMqPublisher.port
                this.username = this@RabbitMqPublisher.username
                this.password = this@RabbitMqPublisher.password
                this.virtualHost = this@RabbitMqPublisher.virtualHost
            }
            connection = factory.newConnection()
            channel = connection?.createChannel()
            Log.d("📡RabbitMQ", "Connected to RabbitMQ")
            true
        } catch (e: Exception) {
            Log.e("📡RabbitMQ", "Connection error: ${e.message}")
            false
        }
    }

    suspend fun `publish`(queueName: String, message: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            channel?.queueDeclare(queueName, true, false, false, null)
            channel?.basicPublish("", queueName, null, message.toByteArray())
            Log.d("📡RabbitMQ", "Published: $message")
            true
        } catch (e: Exception) {
            Log.e("📡RabbitMQ", "Publish error: ${e.message}")
            false
        }
    }

    fun close() {
        channel?.close()
        connection?.close()
        Log.d("📡RabbitMQ", "Connection closed")
    }
}
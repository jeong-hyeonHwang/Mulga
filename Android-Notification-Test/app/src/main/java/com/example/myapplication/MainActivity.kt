package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.notification.adapter.NotificationAdapter
import com.example.myapplication.notification.repository.NotificationRepository
import com.example.myapplication.viewmodel.NotificationViewModel
import com.example.myapplication.viewmodel.NotificationViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotificationAdapter
    private lateinit var viewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 알림 접근 권한 설정 버튼
        binding.btnNotificationAccess.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        // RecyclerView 설정: LayoutManager 추가
        adapter = NotificationAdapter()
        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotifications.adapter = adapter

        // Repository에 ApplicationContext 전달하고, ViewModelFactory를 통해 ViewModel 생성
        val repository = NotificationRepository(applicationContext)
        val factory = NotificationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(NotificationViewModel::class.java)

        // Flow를 수집하여 데이터 변경 시 어댑터 업데이트 및 로그 출력
        lifecycleScope.launch {
            viewModel.notifications.collectLatest { notificationList ->
                Log.d("MainActivity", "Flow emit: ${notificationList.size} items")
                adapter.submitList(notificationList)
            }
        }
    }
}

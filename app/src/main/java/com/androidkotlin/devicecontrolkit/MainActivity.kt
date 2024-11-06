package com.androidkotlin.devicecontrolkit


import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidkotlin.devicecontrolkit.databinding.ActivityMainBinding
import java.lang.String

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!  // 널 안전성을 위한 위임 프로퍼티

    val viewModel: DeviceViewModel by viewModels()  // 위임으로 ViewModel 초기화
    private lateinit var logAdapter: LogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupLogRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        with(binding) {  // 스코프 함수로 binding 참조 간소화
            connectButton.setOnClickListener {
                it.isEnabled = false
                viewModel.connect()
            }

            // KV 컨트롤
            btnKvUp.setOnClickListener {
                kvControl.progress.let { currentValue ->
                    if (currentValue < 100) {
                        viewModel.setKvValue(currentValue + 1)
                    }
                }
            }

            btnKvDown.setOnClickListener {
                kvControl.progress.let { currentValue ->
                    if (currentValue > 0) {
                        viewModel.setKvValue(currentValue - 1)
                    }
                }
            }

            kvControl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) viewModel.setKvValue(progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })

            // mA 컨트롤
            btnMaUp.setOnClickListener {
                maControl.progress.let { currentValue ->
                    if (currentValue < 100) {
                        viewModel.setMaValue(currentValue + 1)
                    }
                }
            }

            btnMaDown.setOnClickListener {
                maControl.progress.let { currentValue ->
                    if (currentValue > 0) {
                        viewModel.setMaValue(currentValue - 1)
                    }
                }
            }

            maControl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) viewModel.setMaValue(progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }
    }

    private fun observeViewModel() {
        // 상태 관찰
        viewModel.status.observe(this) { status ->
            binding.statusText.text = status
            binding.connectButton.isEnabled = status.startsWith("Disconnected")
        }

        // KV 값 관찰
        viewModel.kvValue.observe(this) { value ->
            binding.kvValue.text = getString(R.string.kv_format, value)
            binding.kvControl.progress = value
            binding.btnKvUp.isEnabled = value < 100
            binding.btnKvDown.isEnabled = value > 0
        }

        // mA 값 관찰
        viewModel.maValue.observe(this) { value ->
            binding
            binding.maValue.text = getString(R.string.ma_format, value)
            binding.maControl.progress = value
            binding.btnMaUp.isEnabled = value < 100
            binding.btnMaDown.isEnabled = value > 0
        }

        // 에러 관찰
        viewModel.error.observe(this) { error ->
            showErrorDialog(error)
        }

        // 로그 관찰
        viewModel.logs.observe(this) { logs ->
            logAdapter.setLogs(logs)
        }
    }

    private fun showErrorDialog(error: DeviceError) {
        AlertDialog.Builder(this)
            .setTitle(error.type.description)
            .setMessage(buildString {
                append(error.message)
                append("\n\n해결방법: ")
                append(error.solution)
            })
            .setPositiveButton("확인", null)
            .show()
    }

    private fun setupLogRecyclerView() {
        logAdapter = LogAdapter()
        binding.logRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = logAdapter
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package moe.htk030.myipwtf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import moe.htk030.myipwtf.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = (requireActivity() as MainActivity)
        activity.updateTriggered.observe(activity) {
            if (it) {
                refreshData()
                activity.updateTriggered.postValue(false)
            }
        }
        refreshData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun refreshData() {
        APIRequestThread(requireActivity()) {
            requireActivity().runOnUiThread {
                for (k in it.keys) {
                    when (k) {
                        "YourFuckingIPAddress" -> binding.dataIp.text = it[k]
                        "YourFuckingLocation" -> binding.dataLocation.text = it[k]
                        "YourFuckingHostname" -> binding.dataHostname.text = it[k]
                        "YourFuckingISP" -> binding.dataIsp.text = it[k]
                        "YourFuckingTorExit" -> binding.dataTor.text = it[k]
                        "YourFuckingCity" -> binding.dataCity.text = it[k]
                        "YourFuckingCountry" -> binding.dataCountry.text = it[k]
                        "YourFuckingCountryCode" -> binding.dataCountrycode.text = it[k]
                        else -> Log.d("030-data", "known data $k ${it[k]}")
                    }
                }
                binding.dataCity.visibility = if (binding.dataCity.text != binding.dataLocation.text) View.VISIBLE else View.GONE
            }
        }.start()
    }
}
package com.example.cosmify

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cosmify.databinding.FragmentFirstBinding
import org.json.JSONObject
import java.net.URL


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val thread = Thread {
            try {
                val factjson =
                    JSONObject(URL("https://cosmify-367717.uk.r.appspot.com/fact-of-day").readText())
                val fact = factjson.getString("fact")
//                val autoCompView = this.getRootView().findViewById(R.id.autocomplete_city)
                val textv = getView()?.findViewById(R.id.textview_first) as TextView
                textv.text = fact

            } catch (exc: Exception) {
                Log.e("Exception", exc.toString())
            }
        }
        thread.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.khai_dipplom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.khai_dipplom.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {

    val profileViewModel: MainScreenViewModel by activityViewModels()

    lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setProfile()

        return binding.root
    }

    private fun setProfile(){

        val cuus = profileViewModel.getUserPr()

        binding.serName.text = cuus.username
        binding.pname1.text = cuus.name1
        binding.pname2.text = cuus.name2
        binding.pname3.text = cuus.name3
    }

}


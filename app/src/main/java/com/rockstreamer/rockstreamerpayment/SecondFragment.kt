package com.rockstreamer.rockstreamerpayment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.aemerse.iap.DataWrappers
import com.aemerse.iap.IapConnector
import com.aemerse.iap.SubscriptionServiceListener
import com.rockstreamer.rockstreamerpayment.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var iapConnector: IapConnector
    val isBillingClientConnected: MutableLiveData<Boolean> = MutableLiveData()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        isBillingClientConnected.value = false

        val nonConsumablesList = listOf("lifetime")
        val consumablesList = listOf("base", "moderate", "quite", "plenty", "yearly")
        val subsList = listOf("test_pavel")

        iapConnector = IapConnector(
            context = requireActivity(),
            nonConsumableKeys = nonConsumablesList,
            consumableKeys = consumablesList,
            subscriptionKeys = subsList,
            key = requireActivity().resources.getString(R.string.payment_token),
            enableLogging = true
        )


        iapConnector.removeSubscriptionListener(object :SubscriptionServiceListener{
            override fun onSubscriptionRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                Log.d("SUBSCRIPTION"  , "cancel on Restored response is ${purchaseInfo.originalJson.toString()}")
            }

            override fun onSubscriptionPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                Log.d("SUBSCRIPTION"  , "cancel on Purchased response is ${purchaseInfo.originalJson.toString()}")

            }

            override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.SkuDetails>) {
                Log.d("SUBSCRIPTION"  , "Update on Restored response is")

            }

        })

        iapConnector.addSubscriptionListener(object : SubscriptionServiceListener {
            override fun onSubscriptionRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                // will be triggered upon fetching owned subscription upon initialization
            }

            override fun onSubscriptionPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                // will be triggered whenever subscription succeeded
                Log.d("SUBSCRIPTION"  , "response is ${purchaseInfo.originalJson.toString()}")
                Log.d("SUBSCRIPTION"  , "response is ${purchaseInfo.sku}")

                when (purchaseInfo.sku) {
                    "subscription" -> {

                    }
                }
            }

            override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.SkuDetails>) {
                // list of available products will be received here, so you can update UI with prices if needed
            }
        })


        binding.buttonSecond.setOnClickListener {
            iapConnector.subscribe(requireActivity() , "test_pavel")
        }

        binding.buttonCancel.setOnClickListener {
            iapConnector.unsubscribe(requireActivity() , "test_pavel")
        }


        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
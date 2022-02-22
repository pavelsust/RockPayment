package com.rockstreamer.rockstreamerpayment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.anjlab.android.iab.v3.SkuDetails
import com.rockstreamer.rockstreamerpayment.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */



class FirstFragment : Fragment() , BillingProcessor.IBillingHandler{

    private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!


    private lateinit var billingProcessor: BillingProcessor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        billingProcessor = BillingProcessor(requireActivity(), requireActivity().resources.getString(R.string.payment_token), this)


        binding.buttonFirst.setOnClickListener {
            billingProcessor.subscribe(requireActivity(), "test_pavel")

//            paymentDatabase.setValue("Pavel")
//                .addOnSuccessListener(OnSuccessListener<Void?> {
//                    Toast.makeText(requireActivity() , "Success" , Toast.LENGTH_SHORT).show()
//                })
//                .addOnFailureListener(OnFailureListener {
//                    Toast.makeText(requireActivity() , "${it.printStackTrace()}" , Toast.LENGTH_SHORT).show()
//                })
        }

        binding.buttonInfo.setOnClickListener {
//           var info: PurchaseInfo? =  billingProcessor.getSubscriptionPurchaseInfo("test_pavel")
//            Log.d("SUBSCRIPTION"  , "${info!!.purchaseData.autoRenewing}")
//

            billingProcessor.getSubscriptionListingDetailsAsync("test_pavel", object :
                BillingProcessor.ISkuDetailsResponseListener {
                override fun onSkuDetailsResponse(products: MutableList<SkuDetails>?) {
                    var item = products?.get(0)!!
                    Log.d("SUBSCRIPTION"  , "${item.isSubscription}")

                }

                override fun onSkuDetailsError(error: String?) {

                }
            })

        }

        binding.buttonRestore.setOnClickListener {
            billingProcessor.loadOwnedPurchasesFromGoogleAsync(object :
                BillingProcessor.IPurchasesResponseListener {
                override fun onPurchasesSuccess() {
                    Log.d("SUBSCRIPTION"  , "On Purchases Success")
                }

                override fun onPurchasesError() {
                    Log.d("SUBSCRIPTION"  , "On Purchases Error")
                }
            })
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {
        Log.d("PURCHASE" , "response data ${details!!.responseData},/n" +
                "" +
                " ${details.purchaseData.autoRenewing}\n" +
                "" +
                "order id: ${details.purchaseData.orderId}\n" +
                "package name: ${details.purchaseData.packageName}" +
                "purchase state name: ${details.purchaseData.purchaseState.name}" +
                "product id: ${details.purchaseData.productId}\n" +
                "token: ${details.purchaseData.purchaseToken}")

    }

    override fun onPurchaseHistoryRestored() {

    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {

    }

    override fun onBillingInitialized() {

    }
}
package com.antidote.mchanjo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.datacapture.QuestionnaireFragment
import com.google.android.fhir.datacapture.mapping.ResourceMapper
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Questionnaire

class RegisterInfant : Fragment() {
    private var questionnaireJsonString: String? = null
    private val submissionSuccess: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register_infant, container, false)

        // Enable the options menu in this fragment
        setHasOptionsMenu(true)
        // Reading the JSON questionnaire
            questionnaireJsonString = getStringFromAssets("registration.json")
        // Display the questionnaire
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add(
                    R.id.fragment_container_view,
                    QuestionnaireFragment.builder().setQuestionnaire(questionnaireJsonString!!).build()
                )
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePatientSaveAction()
        childFragmentManager.setFragmentResultListener(
            QuestionnaireFragment.SUBMIT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, _ -> submitQuestionnaire() }
    }

    private fun observePatientSaveAction() {
        submissionSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Handle page navigation here
                findNavController().navigate(R.id.action_registerInfant_to_dashBoard)
                // Show success toast message
                Toast.makeText(requireContext(), "\uD83E\uDD31The infant is successfully registered\uD83E\uDEC2", Toast.LENGTH_SHORT).show()
            } else {
                // Show error toast message
                Toast.makeText(requireContext(), "Submission failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getStringFromAssets(fileName: String): String {
        val assetManager = requireContext().assets
        return assetManager.open(fileName).bufferedReader().use { it.readText() }
    }

    // Creating a callback function to handle submission from the questionnaire
    private fun submitQuestionnaire() {
        // Getting the responses
        val fragment = childFragmentManager.findFragmentById(R.id.fragment_container_view) as QuestionnaireFragment
        val questionnaireResponse = fragment.getQuestionnaireResponse()
        // Printing the response to the logcat
        val jsonParser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
        val questionnaireResponseString = jsonParser.encodeResourceToString(questionnaireResponse)
        Log.d("response", questionnaireResponseString)

        //Extracting the FHIR resource from QuestionnaireResponse
        lifecycleScope.launch {
            val questionnaire = jsonParser.parseResource(questionnaireJsonString) as Questionnaire
            val bundle = ResourceMapper.extract(questionnaire,questionnaireResponse)
            Log.d("extraction result", jsonParser.encodeResourceToString(bundle))
        }

        // Simulating submission success or failure
        val submissionSuccessful = true // Replace with your actual submission logic
        submissionSuccess.value = submissionSuccessful
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.submit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.submit -> {
                submitQuestionnaire()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

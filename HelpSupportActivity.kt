package com.example.transportproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HelpSupportActivity : AppCompatActivity() {

    private lateinit var faqListView: ExpandableListView
    private lateinit var chatbotInput: EditText
    private lateinit var chatbotSendBtn: Button
    private lateinit var chatbotResponse: TextView
    private lateinit var issueDescription: EditText
    private lateinit var uploadImageBtn: Button
    private lateinit var imagePreview: ImageView
    private lateinit var reportIssueBtn: Button
    private lateinit var ticketListView: ListView
    private lateinit var watchTutorialsBtn: Button
    private val supportTickets = mutableListOf<String>()
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_support)
        faqListView = findViewById(R.id.faqListView)
        chatbotInput = findViewById(R.id.chatbotInput)
        chatbotSendBtn = findViewById(R.id.chatbotSendBtn)
        chatbotResponse = findViewById(R.id.chatbotResponse)
        issueDescription = findViewById(R.id.issueDescription)
        uploadImageBtn = findViewById(R.id.uploadImageBtn)
        imagePreview = findViewById(R.id.imagePreview)
        reportIssueBtn = findViewById(R.id.reportIssueBtn)
        ticketListView = findViewById(R.id.ticketListView)
        watchTutorialsBtn = findViewById(R.id.watchTutorialsBtn)

        setupFAQ()

        chatbotSendBtn.setOnClickListener {
            val userQuery = chatbotInput.text.toString().trim()
            chatbotResponse.text = ChatbotResponse.getResponse(userQuery)
        }

        uploadImageBtn.setOnClickListener { pickImage() }

        reportIssueBtn.setOnClickListener {
            val description = issueDescription.text.toString().trim()
            if (description.isNotEmpty()) {
                supportTickets.add(description)
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, supportTickets)
                ticketListView.adapter = adapter
                Toast.makeText(this, "Issue Reported!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter issue description!", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.emailSupportBtn).setOnClickListener {
            sendEmail("support@logistics.com", "Help Needed")
        }
        findViewById<Button>(R.id.callSupportBtn).setOnClickListener {
            makeCall("+1234567890")
        }
        findViewById<Button>(R.id.whatsappSupportBtn).setOnClickListener {
            openWhatsApp("+1234567890")
        }
        watchTutorialsBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
            startActivity(intent)
        }
    }

    private fun setupFAQ() {
        val faqList = mapOf(
            "How to track my shipment?" to listOf("Go to 'Track Shipment' and enter your tracking number."),
            "How to contact support?" to listOf("Call or email support from this page."),
            "What to do if my shipment is delayed?" to listOf("Check the tracking status or contact support.")
        )

        val faqAdapter = FAQAdapter(this, faqList)
        faqListView.setAdapter(faqAdapter)

        faqListView.setOnGroupExpandListener { _ -> setExpandableListViewHeight(faqListView) }
        faqListView.setOnGroupCollapseListener { _ -> setExpandableListViewHeight(faqListView) }
    }

    private fun setExpandableListViewHeight(expandableListView: ExpandableListView) {
        val listAdapter = expandableListView.expandableListAdapter ?: return
        var totalHeight = 0
        for (i in 0 until listAdapter.groupCount) {
            val groupItem = listAdapter.getGroupView(i, false, null, expandableListView)
            groupItem.measure(0, 0)
            totalHeight += groupItem.measuredHeight

            if (expandableListView.isGroupExpanded(i)) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem = listAdapter.getChildView(i, j, false, null, expandableListView)
                    listItem.measure(0, 0)
                    totalHeight += listItem.measuredHeight
                }
            }
        }

        val params = expandableListView.layoutParams
        params.height = totalHeight + (expandableListView.dividerHeight * (listAdapter.groupCount - 1))
        expandableListView.layoutParams = params
        expandableListView.requestLayout()
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            imagePreview.setImageURI(imageUri)
            imagePreview.visibility = ImageView.VISIBLE
        }
    }

    private fun sendEmail(email: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        startActivity(intent)
    }

    private fun makeCall(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        startActivity(intent)
    }

    private fun openWhatsApp(phone: String) {
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phone")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}

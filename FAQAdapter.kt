package com.example.transportproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class FAQAdapter(private val context: Context, private val faqList: Map<String, List<String>>) : BaseExpandableListAdapter() {

    private val questionList = ArrayList(faqList.keys)

    override fun getGroupCount(): Int {
        return questionList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return faqList[questionList[groupPosition]]?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Any {
        return questionList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return faqList[questionList[groupPosition]]?.get(childPosition) ?: ""
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val question = getGroup(groupPosition) as String
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = question

        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val answer = getChild(groupPosition, childPosition) as String
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(android.R.layout.simple_list_item_1, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = answer

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}

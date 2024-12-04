package com.example.personnelselection

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MyDialog: DialogFragment() {

    private var removable: Removable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        removable = context as Removable?

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val person = arguments?.getSerializable("person") as? Person
        val builder = AlertDialog.Builder(
            requireActivity()
        )

        return builder.setTitle("Внимание!")
            .setMessage("Хотите удалить сотрудника?")
            .setIcon(R.drawable.ic_delete)
            .setCancelable(true)
            .setPositiveButton("Да") { _, _ ->
                removable?.remove(person)
                Toast.makeText(context, "Cотрудник, ${person?.firstName} ${person?.lastName} удалён", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Нет") { _, _ -> }
            .create()

    }
}
package com.example.personnelselection

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), Removable {

    private val personData: MutableList<Person> = mutableListOf()

    private var adapter: ListAdapter? = null

    private lateinit var personLiveData: PersonViewModel
    private lateinit var filteredPersonLiveData: PersonViewModel

    private var roleAdapter: ArrayAdapter<String>? = null
    private var role: String? = null
    private var filterRole: String? = null

    private lateinit var toolbarMain: Toolbar
    private lateinit var listViewLV: ListView
    private lateinit var firstNameET: EditText
    private lateinit var lastNameET: EditText
    private lateinit var ageET: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var toolbarRoleSpinner: Spinner
    private lateinit var saveBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        personLiveData = ViewModelProvider(this)[PersonViewModel::class.java]
        filteredPersonLiveData = ViewModelProvider(this)[PersonViewModel::class.java]
        adapter = ListAdapter(this@MainActivity, personData)
        listViewLV.adapter = adapter

        roleAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            DataBase().getRole()
        )
        roleAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        roleSpinner.adapter = roleAdapter
        val itemSelectedListener: OnItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    role = if (position != 0) parent?.getItemAtPosition(position) as String else ""
                    if (position == 0) (view as TextView).text = "Выберите должность"
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        roleSpinner.onItemSelectedListener = itemSelectedListener

        toolbarRoleSpinner.adapter = roleAdapter
        val filterItemSelectedListener: OnItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    filterRole = parent?.getItemAtPosition(position) as String

                    if (position != 0){
                        filterPersonsByRole(filterRole)
                    } else {
                        (view as TextView).text = "Фильтр"
                        personLifeData()
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        toolbarRoleSpinner.onItemSelectedListener = filterItemSelectedListener

        personLifeData()
        filteredPersonLifeData()

        saveBTN.setOnClickListener{
            val person = Person (
                firstNameET.text.toString(),
                lastNameET.text.toString(),
                ageET.text.toString().toIntOrNull(),
                role
            )

            if (!InputPersonValidation(this, person).isValidate()) return@setOnClickListener

            val currentList = personLiveData.personLiveData.value ?: mutableListOf()
            currentList.add(person)
            personLiveData.personLiveData.value = currentList

            clearEditText()

            Toast.makeText(this, "${person.firstName} ${person.lastName} добавлен", Toast.LENGTH_SHORT).show()
        }

        listViewLV.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val personPosition = adapter!!.getItem(position)
                val dialog = MyDialog()
                val args = Bundle()
                args.putSerializable("person", personPosition)
                dialog.arguments = args
                dialog.show(supportFragmentManager, "custom")
            }
    }

    private fun init() {
        toolbarMain = findViewById(R.id.toolbarMain)
        title = ""
        setSupportActionBar(toolbarMain)
        listViewLV = findViewById(R.id.listViewLV)
        firstNameET = findViewById(R.id.firstNameET)
        lastNameET = findViewById(R.id.lastNameET)
        ageET = findViewById(R.id.ageET)
        roleSpinner = findViewById(R.id.roleSP)
        toolbarRoleSpinner = findViewById(R.id.toolbarRoleSP)
        saveBTN = findViewById(R.id.saveBTN)
    }

    private fun personLifeData() {
        personLiveData.personLiveData.observe(this, Observer { persons ->
            adapter?.clear()
            adapter?.addAll(persons)
            adapter?.notifyDataSetChanged()
        })
    }

    private fun filteredPersonLifeData() {
        filteredPersonLiveData.filteredPersonLiveData.observe(this, Observer { persons ->
            adapter?.clear()
            adapter?.addAll(persons)
            adapter?.notifyDataSetChanged()
        })
    }

    private fun clearEditText() {
        firstNameET.text.clear()
        lastNameET.text.clear()
        ageET.text.clear()
        roleSpinner.setSelection(0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain -> {
                Toast.makeText(this, "Вы вышли из приложения", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    fun filterPersonsByRole(role: String?) {
        val personList = personLiveData.personLiveData.value ?: mutableListOf()
        val filteredPersonList = personList.filter { it.role == role }.toMutableList()
        filteredPersonLiveData.filteredPersonLiveData.value = filteredPersonList
    }

    override fun remove(person: Person?) {
        val currentList = personLiveData.personLiveData.value ?: mutableListOf()
        currentList.remove(person)
        personLiveData.personLiveData.value = currentList
    }
}
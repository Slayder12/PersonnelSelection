package com.example.personnelselection

class DataBase {
   private val role = mutableListOf (
       "Должность",
       "Разработчик",
       "Тестировщик",
       "UI/UX дизайнер",
       "Аналитик данных",
       "Менеджер проектов",
       "DevOps-инженер",
       "Маркетолог"
   )
    fun getRole() = role
}

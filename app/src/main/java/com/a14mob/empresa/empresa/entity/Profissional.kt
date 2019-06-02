package com.a14mob.empresa.empresa.entity

data class Profissional(var id:Int, var nome: String, var email: String,
                        var equipes: List<Equipe>, var atividades: List<Atividade>,
                        var meta: String, var foto: String)
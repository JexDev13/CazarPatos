package com.jeremy.arias.cazarpatos

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jeremy.arias.cazarpatos.database.RankingPlayerDBHelper

class RankingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        consultarPuntajeJugadores()
        //OperacionesSqLite()
        //GrabarRankingSQLite()
        //LeerRankingsSQLite()
    }

    fun OperacionesSqLite(){
        RankingPlayerDBHelper(this).deleteAllRanking()
        RankingPlayerDBHelper(this).insertRankingByQuery(Player("Jugador 9",10))
        val patosCazados = RankingPlayerDBHelper(this).readDucksHuntedByPlayer("Jugador 9")
        RankingPlayerDBHelper(this).updateRanking(Player("Jugador 9",5))
        RankingPlayerDBHelper(this).deleteRanking("Jugador 9")
        RankingPlayerDBHelper(this).insertRanking(Player("Jugador 9",7))
        val players = RankingPlayerDBHelper(this).readAllRankingByQuery()
    }
    fun GrabarRankingSQLite(){
        val jugadores = arrayListOf(
            Player("Jeremy.Arias", 18),
            Player("Jugador 2", 6),
            Player("Jugador 3", 3),
            Player("Jugador 4", 9)
        )
        jugadores.sortByDescending { it.huntedDucks }
        for(jugador in jugadores){
            RankingPlayerDBHelper(this).insertRanking(jugador)
        }
    }
    fun LeerRankingsSQLite(){
        val jugadoresSQLite = RankingPlayerDBHelper(this).readAllRanking()
        val recyclerViewRanking: RecyclerView = findViewById(R.id.recyclerViewRanking)
        recyclerViewRanking.layoutManager = LinearLayoutManager(this)
        recyclerViewRanking.adapter = RankingAdapter(jugadoresSQLite)
        recyclerViewRanking.setHasFixedSize(true)
    }

    fun consultarPuntajeJugadores() {
        val db = Firebase.firestore
        db.collection("ranking")
            .orderBy("huntedDucks", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                Log.d(EXTRA_LOGIN, "Success getting documents")
                var jugadores = ArrayList<Player>()
                for (document in result) {
                    val jugador = document.toObject(Player::class.java)
                    //val jugador = document.toObject<Player>()
                    jugadores.add(jugador)
                }
                //Poblar en RecyclerView información usando mi adaptador
                val recyclerViewRanking: RecyclerView = findViewById(R.id.recyclerViewRanking);
                recyclerViewRanking.layoutManager = LinearLayoutManager(this);
                recyclerViewRanking.adapter = RankingAdapter(jugadores);
                recyclerViewRanking.setHasFixedSize(true);
            }
            .addOnFailureListener { exception ->
                Log.w(EXTRA_LOGIN, "Error getting documents.", exception)
                Toast.makeText(this, "Error al obtener datos de jugadores", Toast.LENGTH_LONG)
                    .show()
            }
    }


}

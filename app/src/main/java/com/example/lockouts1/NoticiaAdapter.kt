package com.example.lockouts1

import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater


class NoticiaAdapter(
    private val lista: List<Noticia>,
    private val onClick: (Noticia) -> Unit
) : RecyclerView.Adapter<NoticiaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tituloNoticia)
        val resumen: TextView = view.findViewById(R.id.resumenNoticia)

        init {
            view.setOnClickListener {
                onClick(lista[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_noticia, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticia = lista[position]
        holder.titulo.text = noticia.titulo
        holder.resumen.text = noticia.resumen
    }

    override fun getItemCount() = lista.size
}

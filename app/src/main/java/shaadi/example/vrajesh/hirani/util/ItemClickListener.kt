package shaadi.example.vrajesh.hirani.util

import shaadi.example.vrajesh.hirani.room.entity.ProfileEntity

interface ItemClickListener {
    fun onItemClick(position: Int, item: ProfileEntity)
}
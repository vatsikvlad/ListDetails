package com.example.listasczegoly.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.listasczegoly.R.drawable

@Entity(tableName = "trasy")
data class Trasa(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val length: Int,
    val image: Int,
    val description: String,
    val isRower: Boolean,
)
val allTrasy = listOf(
    Trasa(
        title = "Gorc - Lubań",
        length = 60,
        image = drawable.gorc_luban,
        description = "Turbacz z pewnością należy do jednych z najbardziej atrakcyjnych turystycznie miejsc na południu Polski, które warto chociaż raz odwiedzić: piękne stamtąd widoki, a ścieżki umiarkowanie trudne.",
        isRower = true
    ),
    Trasa(
        title = "Bieszczady - Cisna",
        length = 34,
        image = drawable.bieszczady_cisna,
        description = "Bieszczady są ciekawą miejscówką na rowerowe wypady enduro. Oznaczone szlaki w dużej mierze są wąskimi, naturalnymi singlami. Przedstawiona trasa do Wysokiego Berda prowadzi lasami, następnie do przełęczy Orłowicza i Smerek.",
        isRower = true
    ),
    Trasa(
        title = "Drewniane Mazowsze",
        length = 76,
        image = drawable.drewniane_mazowsze,
        description = "Trasa rowerowa wiodąca przez południowo-zachodni skraj województwa mazowieckiego. Na trasie znajdziemy liczne obiekty drewnianej architektury, kościoły i tereny zalewowe.",
        isRower = true
    ),
    Trasa(
        title = "Janosikowe Diery",
        length = 18,
        image = drawable.janosikove_diery,
        description = "Wycieczka na Mały i Wielki Rozsutec przez Janosikowe Diery. Szlaki z drabinkami, klamrami i łańcuchami. Niezapomniane widoki z obu szczytów.",
        isRower = false
    ),
    Trasa(
        title = "Kasprowy Wierch",
        length = 21,
        image = drawable.kasprowy,
        description = "Klasyczna trasa tatrzańska z Kuźnic przez Dolinę Bystrej. Podejście długie, ale równomierne. Szczyt oferuje pełną panoramę Tatr.",
        isRower = false
    ),
    Trasa(
        title = "Kozi Wierch",
        length = 20,
        image = drawable.kozi_wierch,
        description = "Wymagająca trasa przez Żleb Kulczyńskiego. Panorama na Dolinę Gąsienicową i Orlą Perć. Najwyższy szczyt leżący w całości w Polsce.",
        isRower = false
    )
)

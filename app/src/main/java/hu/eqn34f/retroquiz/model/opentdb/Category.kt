package hu.eqn34f.retroquiz.model.opentdb

import com.google.gson.annotations.SerializedName

enum class Category(val id: Int){
    @SerializedName("General%20Knowledge")
    GeneralKnowledge(9),

    @SerializedName("Entertainment%3A%20Books")
    Books(10),

    @SerializedName("Entertainment%3A%20Film")
    Film(11),

    @SerializedName("Entertainment%3A%20Music")
    Music(12),

    @SerializedName("Entertainment%3A%20Musicals%20%26%20Theatres")
    Theater(13),

    @SerializedName("Entertainment%3A%20Television")
    Television(14),

    @SerializedName("Entertainment%3A%20Video%20Games")
    VideoGames(15),

    @SerializedName("Entertainment%3A%20Board%20Games")
    BoardGames(16),

    @SerializedName("Science%20%26%20Nature")
    Nature(17),

    @SerializedName("Science%3A%20Computers")
    ComputerScience(18),

    @SerializedName("Science%3A%20Mathematics")
    Mathematics(19),

    @SerializedName("Mythology")
    Mythology(20),

    @SerializedName("Sports")
    Sports(21),

    @SerializedName("Geography")
    Geography(22),

    @SerializedName("History")
    History(23),

    @SerializedName("Politics")
    Politics(24),

    @SerializedName("Art")
    Art(25),

    @SerializedName("Celebrities")
    Celebrities(26),

    @SerializedName("Animals")
    Animals(27),

    @SerializedName("Vehicles")
    Vehicles(28),

    @SerializedName("Entertainment%3A%20Comics")
    Comics(29),

    @SerializedName("Science%3A%20Gadgets")
    Gadgets(30),

    @SerializedName("Entertainment%3A%20Japanese%20Anime%20%26%20Manga")
    Anime(31),

    @SerializedName("Entertainment%3A%20Cartoon%20%26%20Animations")
    Cartoons(32),


}
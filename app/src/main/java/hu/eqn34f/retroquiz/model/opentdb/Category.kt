package hu.eqn34f.retroquiz.model.opentdb

import com.google.gson.annotations.SerializedName

enum class Category(val id: Int){
    @SerializedName("General Knowledge")
    GeneralKnowledge(9),

    @SerializedName("Entertainment: Books")
    Books(10),

    @SerializedName("Entertainment: Film")
    Film(11),

    @SerializedName("Entertainment: Music")
    Music(12),

    @SerializedName("Entertainment: Musicals & Theatres")
    Theater(13),

    @SerializedName("Entertainment: Television")
    Television(14),

    @SerializedName("Entertainment: Video Games")
    VideoGames(15),

    @SerializedName("Entertainment: Board Games")
    BoardGames(16),

    @SerializedName("Science & Nature")
    Nature(17),

    @SerializedName("Science: Computers")
    ComputerScience(18),

    @SerializedName("Science: Mathematics")
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

    @SerializedName("Entertainment: Comics")
    Comics(29),

    @SerializedName("Science: Gadgets")
    Gadgets(30),

    @SerializedName("Entertainment: Japanese Anime & Manga")
    Anime(31),

    @SerializedName("Entertainment: Cartoon & Animations")
    Cartoons(32),


}
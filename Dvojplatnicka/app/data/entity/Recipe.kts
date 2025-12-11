@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val content: String
) {
    fun getId() = id
    fun getName() = name
    fun getContent() = content
}
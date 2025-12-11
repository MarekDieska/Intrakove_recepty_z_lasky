@Database(
    entities = [Recipe::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Callback to populate database on creation
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Populate database in background thread
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.recipeDao())
                }
            }
        }

        suspend fun populateDatabase(recipeDao: RecipeDao) {
            // Clear existing data
            recipeDao.deleteAllRecipes()

            // Insert initial recipes from your strings.xml
            val recipes = listOf(
                Recipe(
                    name = "Placky",
                    content = """Zemiaky očistíme, nastrúhame. Zmiešame s múkou, celým vajcom a roztlačeným cesnakom. Vzniknuté cesto osolíme a okoreníme.

Placky robíme ako palacinky (t.j. na panvici a oleji robíme tvary aké chceme v dostatočnej hrúbke), buď dávame viac kúskov na jeden krát - bude ich viac menších, alebo po jednej, čiže budú väčšie.

Môžeme ich jesť s kyslou smotanou alebo zakysankou, prípadne niečím podobným."""
                ),
                Recipe(
                    name = "Thai polievka",
                    content = """1 PL olej
2 PL zázvor
1 stonka citrónová tráva
2 KL zelená karí pasta
2 KL čili pasta
4 hrnčeky kurací vývar
1 PL trstinový cukor
3 konzervy kokosové mlieko
200 g huby šitake
200 g uvarené kuracie mäso
2 PL limetová šťava
4 ks sušená čili paprička

V hrnci rozpálime olej. Pridáme nastrúhaný zázvor, nasekanú citrónovú trávu, kari a čili pastu. Za stáleho miešania prilejeme horúci kurací vývar, trstinový cukor a polievku varíme približne 15 minút.

Do menšieho hrnca nalejeme kokosové mlieko. Doň vložíme huby šitake nakrájané na plátky, za občasného miešania varíme domäkka približne 5 minút.

Predvarené huby s mliekom vlejeme do hrnca s vývarom. Pridáme na kocky nakrájané kuracie prsia a varíme ďalších 7 minút. Dochutíme soľou, korením a limetovou šťavou. Pred podávaním polievku ozdobíme čili papričkou.

TIP: Ak máme radi štipľavé môžeme čili papričku nakrájať a zjesť."""
                ),
                Recipe(
                    name = "Lievance",
                    content = """200 g	múka hladká
50 g	cukor kryštálový
2 ks	vajce
250 ml	mlieko
2 PL	olej
1 bal.	prášok kypriaci do pečiva
1 bal.	cukor vanilkový
štipka	soli

Hladkú múku preosejeme. Pridáme polovicu kryštáloveho cukru, vanilkový cukor, prášok do pečiva, štipku soli a premiešame túto suchú zmes.

Následne do nej pridáme 2 lyžice oleja, mlieko a žĺtky z 2 vajec. Mixérom vymiešame.

Z bielkov a zvyšnej polovice cukru vyšľaháme sneh. Opatrne ho metličkou alebo varechou vmiešame do cesta, cesto bude nadýchané. Necháme odpočívať 10 minút.

Lievance opekáme na panvici, ktorú si na začiatok potrieme olejom. Pečieme na miernom ohni, aby sa nepripiekli. Keď sa na vrchu začnú tvoriť bublinky, je čas ich otočiť.

Napokon lievance môžeme servírovať. Je milión spôsobov, ako ich môžeme ozdobiť. Či už si vyberiete džem, Nutellu, orieškový krém alebo tvaroh je na vás."""
                )
            )

            recipeDao.insertAllRecipes(recipes)
        }
    }
}
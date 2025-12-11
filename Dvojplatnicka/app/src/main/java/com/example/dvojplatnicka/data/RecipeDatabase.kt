package com.example.dvojplatnicka.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dvojplatnicka.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    @Database(entities = [Recipe::class], version = 1)
    @TypeConverters(Converters::class)
    abstract class RecipeDatabase : RoomDatabase() {
        abstract fun recipeDao(): RecipeDao
    }

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getInstance(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                context.deleteDatabase("recipe_database")
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

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.recipeDao())
                }
            }
        }

        suspend fun populateDatabase(recipeDao: RecipeDao) {
            recipeDao.deleteAllRecipes()

            val recipes = listOf(
                Recipe(
                    name = "Placky",
                    content = "Zemiaky očistíme, nastrúhame. Zmiešame s múkou, celým vajcom a roztlačeným cesnakom. Vzniknuté cesto osolíme a okoreníme.\n\nPlacky robíme ako palacinky (t.j. na panvici a oleji robíme tvary aké chceme v dostatočnej hrúbke), buď dávame viac kúskov na jeden krát - bude ich viac menších, alebo po jednej, čiže budú väčšie.\n\nMôžeme ich jesť s kyslou smotanou alebo zakysankou, prípadne niečím podobným.",
                    image = R.drawable.placky,
                    type = RecipeType.STARTER
                ),
                Recipe(
                    name = "Thai polievka",
                    content = "1 PL olej\n2 PL zázvor\n1 stonka citrónová tráva\n2 KL zelená karí pasta\n2 KL čili pasta\n4 hrnčeky kurací vývar\n1 PL trstinový cukor\n3 konzervy kokosové mlieko\n200 g huby šitake\n200 g uvarené kuracie mäso\n2 PL limetová šťava\n4 ks sušená čili paprička\n\nV hrnci rozpálime olej. Pridáme nastrúhaný zázvor, nasekanú citrónovú trávu, kari a čili pastu. Za stáleho miešania prilejeme horúci kurací vývar, trstinový cukor a polievku varíme približne 15 minút.\n\nDo menšieho hrnca nalejeme kokosové mlieko. Doň vložíme huby šitake nakrájané na plátky, za občasného miešania varíme domäkka približne 5 minút.\n\nPredvarené huby s mliekom vlejeme do hrnca s vývarom. Pridáme na kocky nakrájané kuracie prsia a varíme ďalších 7 minút. Dochutíme soľou, korením a limetovou šťavou. Pred podávaním polievku ozdobíme čili papričkou.\n\nTIP: Ak máme radi štipľavé môžeme čili papričku nakrájať a zjesť.",
                    image = R.drawable.th_polievka,
                    type = RecipeType.STARTER
                ),
                Recipe(
                    name = "Lievance",
                    content = "200g múka hladká\n50g cukor kryštálový\n2 ks vajce\n250 ml mlieko\n2 PL olej\n1 bal. prášok kypriaci do pečiva\n1 bal. cukor vanilkový\nštipka soli\n\nHladkú múku preosejeme. Pridáme polovicu kryštáloveho cukru, vanilkový cukor, prášok do pečiva, štipku soli a premiešame túto suchú zmes.\n\nNásledne do nej pridáme 2 lyžice oleja, mlieko a žĺtky z 2 vajec. Mixérom vymiešame.\n\nZ bielkov a zvyšnej polovice cukru vyšľaháme sneh. Opatrne ho metličkou alebo varechou vmiešame do cesta, cesto bude nadýchané. Necháme odpočívať 10 minút.\n\nLievance opekáme na panvici, ktorú si na začiatok potrieme olejom. Pečieme na miernom ohni, aby sa nepripiekli. Keď sa na vrchu začnú tvoriť bublinky, je čas ich otočiť.\n\nNapokon lievance môžeme servírovať. Je milión spôsobov, ako ich môžeme ozdobiť. Či už si vyberiete džem, Nutellu, orieškový krém alebo tvaroh je na vás.",
                    image = R.drawable.lievance,
                    type = RecipeType.DESSERT
                ),
                Recipe(
                    name = "Chilli con carne",
                    content = "2 ks väčšia cibuľa\n600 g mleté mäso hovädzie\n1 bal. lúpané paradajky\n1 bal. kukurica\n2 bal. pretlak\noregano\nsoľ\nkorenie čierne mleté chilli\n1 bal. fazuľa\nolej\n\nNakrájanú cibuľu dáme na olej orestovať do väčšieho hrnca, keď chytí zlatistú farbu pridáme k nej mäso.\n\nKeď máme spravené aj mäso, pridáme oba pretlaky a lúpane rajčiny.\n\nNakoniec pridáme kukuricu a fazuľu. Spolu to poriadne premiešame a necháme pod pokrievkou 15 minúť bublať.\n\nAž úplne nakoniec koreníme ( kvôli veľkému množstvu sladkých igrediencii si to nakoniec dochutite podľa seba)\n\nToto jedlo sa väčšinou podáva s chlebovou plackou alebo tortilou. Dobrú chuť",
                    image = R.drawable.chilli_con_carne,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Muffiny",
                    content = "4 banány\n3 celé vajcia\n¾ hrnčeka cukru\n1 vanilkový cukor\n½ čl soli\n½ hrnčeka hery roztopiť\n2 hrnčeky hladkej múky\n½ čl sódy bikarbóny\n½ čl kypriaceho prášku\n1 hrnček mletých orechov\n1 tabuľka čokolády",
                    image = R.drawable.muffiny,
                    type = RecipeType.DESSERT
                ),
                Recipe(
                    name = "Baklava",
                    content =  "400 g lístkové cesto\n100 g maslo\n100 g orechy (vlašské mleté)\n50 g pistácie (mleté)\nškorica (mletá)\ncukor (kryštálový)\nSIRUP:\n8 lyžíc med\n200 g cukor (kryštálový)\n1 lyžica šťava (z citróna)\n100 ml voda\n\nV miske si zmiešame mleté vlašské orechy, pistácie, cukor a škoricu. Lístkové cesto si rozdelíme na tri diely a z každého vyvaľkáme tenký plát. \n\nPlech alebo zapekaciu misu vymastíme maslom a vložíme prvý plát. Na cesto nasypeme polovicu orechovej zmesi. Potom vložíme druhý plát cesta a posypeme zvyšnou polovicou orechovej zmesi. \n\nNakoniec dáme opäť plát cesta, ktorý potrieme zmäknutým maslom a nožom narežeme na štvorčeky. \n\nBaklavu pečieme v predhriatej rúre na 180 stupňov 30 minút.\n\nNa sladký sirup dáme do hrnca všetky suroviny, premiešame a za stáleho miešania a zahrievania vytvoríme konzistentnú hmotu. \n\nPo upečení vyberieme baklavu z rúry a polejeme pripraveným sladkým sirupom.",
                    image = R.drawable.baklava,
                    type = RecipeType.DESSERT
                ),
                Recipe(
                    name = "Hamburger",
                    content = "žemľa na hamburger\nmäso\nplátkový syr\nkyslé uhorky\nparadajky\nľadový šalát\nsoľ, mleté čierne korenie, čili korenie\nworčestrová omáčka\n\nOmáčka od lukiho:\n3 pl majonézy\n1 pl horčice\n1 pl kečupu\n2 pl worčestrovej omáčky\nČierne korenie, cesnak\nUhorková voda",
                    image = R.drawable.hamburger,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Carbonara",
                    content = "500 g špagiet\n200 g údenej slaniny\n1 cibuľa\n1 strúčik cesnaku\n100 ml bieleho suchého vína\n250 ml smotany na šľahanie\n3 žĺtka\n2 lyžice strúhaného syra\nmorská soľ\nčierne korenie\nlístky čerstvej bazalky\n\nŠpagety uvaríme podľa návodu v osolenej vode a scedíme.\n\nSlaninu nakrájame na kocky a na rozohriatej panvici a za stáleho miešania opečieme do zlatista. Potom ju preložíme z panvice do misky a na výpeku zo slaniny orestujeme nadrobno nakrájanú cibuľu a cesnak. Krátko opražíme a zalejeme bielym vínom. Povaríme, kým sa alkohol z vína odparí. Potom pridáme polovicu smotany a krátko povaríme. Na panvicu pridáme späť opraženú slaninku.\n\nV miske zmiešame žĺtky, druhú polovicu smotany a syr grana padano. Zmes pridáme na panvicu, stíšime plameň, osolíme a okoreníme. Pridáme horúce uvarené špagety, dôkladne premiešame a stiahneme zo sporáka.Pred servírovaním ozdobíme nasekanou bazalkou.",
                    image = R.drawable.carbonara,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Shakshuka",
                    content = "2 lyžice olivového oleja\n120 g cibule\n150 g čerstvej papriky\n2 malé strúčiky cesnaku\n1 zarovnaná lyžička rímskeho kmínu\n1 plná lyžička sladkej alebo údenej papriky\nchilli podľa chuti\n60 g paradajkového pretlaku\n1 konzerva lúpaných paradajok (v sezóne 400 g čerstvých olúpaných paradajok)\n4 vajcia\nčerstvá petržlenová vňať alebo koriander\nsoľ\nčierne korenie\n\nV panvici na olivovom oleji nechajte zosklovatieť cibuľu nakrájanú na malé kocky.Pridajte drobne nakrájané papriky a pár minút za občasného premiešania orestujte.Pridajte drvený cesnak a nechajte ho rozvoňať.Pridajte korenie a pár sekúnd za stáleho miešania opečte.\n\nPridajte paradajkový pretlak a za veľmi častého premiešania ho minútku osmažte (tým sa trochu zredukuje jeho kyslá chuť a naopak mierne osladne).Všetko zalejte paradajkami. Osoľte a okoreňte.Duste pod pokrievkou na miernom plameni za občasného premiešania asi 20 minút.Nakoniec v zmesi vytvorte jamky a rozklepnite do nich vajcia.Za miernej teploty nechajte bez miešania zmes prebublávať, kým bielok nestuhne, ale žĺtok zostane krásne tekutý.V prípade, že máte radšej vajcia kompletne prevarené, panvicu zakryte pokrievkou a zmes nechajte dusiť, kým vajcia nestuhnú.Na stôl podávajte v panvici, aby si každý mohol nabrať, posypané nasekanou čerstvou petržlenovou vňaťou alebo koriandrom.\n\nTIP\nKto sa nevyhýba mliečnym výrobkom, môže do zmesi spolu s vajcami pridať aj trochu nahrubo rozdrobené fety.",
                    image = R.drawable.shakshuka,
                    type = RecipeType.STARTER
                ),
                Recipe(
                    name = "Butter chicken",
                    content = "Omáčka\n3–4 veľké cibule nakrájané na drobno\n2–3 PL korenia garam masala\n1/2 čl chilly\n1 čl kajenského korenia\n1 PL kurkumy\n2 PL nastrúhaného zázvoru\n5 strúčikov cesnaku\nSoľ podľa potreby\n1 čl čierne mleté korenie\n1 PL rímska rasca\n1PL mletý kardamon\n1 čl škorica\n1 čl mletý koriander\n2čl sladká paprika\n7 lyžíc masla\n800 g nakrájaných konzervovaných rajčín – dve konzervy\n500 ml smotany na šľahanie\n\nMäso s marinádou\n1 kg kuracie prsia, alebo vykostené kuracie stehná\n4 lyžice citrónovej šťavy\n4 strúčiky cesnaku\n4 cm nastrúhaného zázvoru\n2 lyžice garam masala\n250 ml plnotučného bílého jogurtu1.\n\nZmiešajte si suroviny na marinádu a zmiešajte s nakrájaným kuracím mäsom. Nechajte marinovať do druhého dňa v chladničke.\n\nNamarinované mäso zprudka zo všetkých strán opečte na dvoch lyžiciach masla. Po opečení vyberte a dajte nabok.\n\nDo tej istej panvice dajte ďalšie 3 lyžice masla a na ňom orestujte nasekaný cesnak, čerstvý zázvor a cibuľu. Postupne pridajte všetky koreniny a veľmi krátko zarestujte. Dávajte pozor, aby sa Vám koreniny nepripálili.\n\nPridajte nadrobno nakrájané konzervované rajčiny, a spolu s koreninami povarte tak dlho než sa cibuľa a paradajky celkom rozvaria a vznikne jemná omáčka. Do panvice nakoniec pridajte smotanu, povarte, aby sa suroviny spojili a do hotovej omáčky vložte orestované mäso a varte asi 15 minút. Vypnite sporák a primiešajte ešte dve lyžice masla.\n\nPodávajte s čerstvým koriandrom ako prílohu môžete zvoliť ryžu alebo placky Naan.",
                    image = R.drawable.butter_chicken,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Placky naan",
                    content = "500 g chlebová múka (ja som použila prastarú múku Aurora od Bionákupy),\n7 g sušeného droždia alebo 50 g kvásku (ja som použila droždie Antico Molino Rosso od Bionákupy)\n150 g vody\n20 g cukru\n150 g mlieka\n1 vajíčko 10 g soli\n\nAk robíte verziu z droždia, vo veľkej mise v mlieku rozpustite droždie s 1 čl cukru. Nechajte asi 15 min. postáť, kým trochu nenakysne.\n\nPrimiešajte ostatné suroviny a vypracujte hladké cesto, ktoré nechajte nakysnúť na dvojnásobok pri verzii z droždia to môže trvať asi 2 hodiny a pri verzii z kvásku 6–10 hodín podľa okolitej teploty a sily kvásku.\n\nPo nakysnutí rozdeľte toľko dielov, ké chcete maž placky veľké. Ja som delila asi na 60 g časti.\n\nPlech na pečenie potrite olejom, vytvarujte placky a nechajte ešte 15 min. postáť pri verzii z droždia a pri verzii z kvásku asi 2–4 hodiny. Znovu závisí od okolitej teploty.\n\nPečte vo vopred vyhriatej rúre pri teplote 200 St.C asi 5–7 minút z každej strany.",
                    image = R.drawable.placky_naan,
                    type = RecipeType.STARTER
                ),
                Recipe(
                    name = "Palacinky",
                    content = "1 vajce\n1 pl cukru\nŠtipka soli\n500 ml mlieka\n200 g hladkej múky\nTuk na praženie\nNutella, ovocie, džem na potretie",
                    image = R.drawable.palacinky,
                    type = RecipeType.DESSERT
                ),
                Recipe(
                    name = "Pizza",
                    content = "hrnček hladká múka\n1 KL kypriaci prášok\n0.5 KL soľ\n0.5 hrnčeka voda\n1 PL olivový olej\n\nNasypeme múku.Pridáme soľ, kypriaci prášok, vodu a olej. Zapneme robota a počkáme kým sa cesto nezačne odlepovať od stien – cca po 1 minúte mixovania.\n\nCesto vyklopíme na pomúčenú pracovnú dosku a rukami vypracujeme elastický bochník.Z tohto množstva by nám mali vyjsť dve stredne veľké pizze.Cesto rozvaľkáme do kruhu/kruhov o hrúbke asi 0,5 cm, pridáme naše obľúbené suroviny na pizzu.\n\nMôžeme ho pripraviť dvoma spôsobmi: buď v rúre vyhriatej na 200°C (7–10 minút) alebo ho opečieme na suchej panvici (pridáme pizza suroviny) a nakoniec pizzu dokončíme pod grilom v rúre.\n\nTak či onak – pizzu môžete servírovať do 15 minút od začiatku jej prípravy.",
                    image  = R.drawable.pizza,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Zapekanky",
                    content = "Nakrojíme bagetu na polovicu, po dĺžke prerežeme a naplníme naozaj hocičím. Syrom, šunkou, salámou, najlepšie pestom na vrchu. \n\nNakoniec dáme zapiecť ",
                    image = R.drawable.zapekanky,
                    type = RecipeType.STARTER
                ),
                Recipe(
                    name = "Crispy chicken",
                    content = "500 g	kuracie prsia\n6 PL	múka hladká\n3 PL	škrob zemiakový\n1 PL	olej rastlinný\n1	vajce\n1,5 KL	prášok do pečiva\nštipka	soli\npodľa potreby	voda\n\nOmáčka:\n250 ml	kečup\npodľa chuti	čili omáčka\n250 g	med\n3–4 lyžice	sezamové semená\n\nPrsia nakrájame na menšie kocky. Do vačšej misy pridáme prísady na cesto (múku, škrob, soľ, vajce, olej, kypriaci prášok a toľko vody aby nám vzniklo pomerne husté cesto). (ja som to robila z 1 kg. prs).\n\nMäso dáme do cesta a dobre ho zamiešame.\n\nSpravíme si omáčku: všetky prísady spolu zmiešame.\n\nMäso vyprážame vo veľkom množste a v dobre rozohriatom oleji. Vyprážame dozlatista.\n\nKuracie kúsky vyberieme ,ihneď ich dáme do omáčky , poriadne zamiešame.\n\nVyberieme, poukladáme do misky. Vhodnou prílohou je ryža alebo čínske rezance.",
                    image = R.drawable.crispy_chicken,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Teriyaki",
                    content = "2 ks\nkuracie stehná (bez kostí a kože)\ntrochu kukuričného škrobu\ntrochu hladkej múky\ntrochu oleja\nsezamové semienka\n\nTERIYAKI OMÁČKA:\n2 PL sójovej omáčky\n2 PL medu\n2 PL kryštálového cukru\n1 PL ryžového vína alebo octu\n2 PL vody\n3 plátky čerstvého zázvoru\n1 strúčik cesnaku\n\nPOSTUP\nV miske vymiešame omáčku zo sójovej omáčky, medu, cukru, vína a vody.\n\nPridáme  tenké plátky zázvoru, cesnaku a omáčku necháme postáť, kým nepripravíme kura.\n\nKuracie stehná poprášime škrobom z oboch strán a potom tiež hladkou múkou.\n\nNa panvici rozpálime olej a obalené stehná na ňom opražíme do chrumkava z oboch strán (asi 5 minút na stranu).\n\nPotom mäsko podlejeme omáčkou a z času na čas ho otočíme. Dusíme kým sa omáčka nezredukuje na polovicu.\n\nPotom plátky mäsa vytiahneme a pokrájame ich na tenké rezančeky.\n\nK omáčke prilejeme asi pol deci vody a necháme ju opäť zovrieť. Pridáme kuracie rezančeky a spolu krátko prevaríme (asi minútku).\n\nKurča teriyaki servírujeme k dusenej ryži a pred podávaním ho posypeme sezamovými semienkami.",
                    image = R.drawable.teriyaki,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Tortilla placky",
                    content = "250 g pšeničnej múky (hladkej, prípadne hladkej celozrnnej múky)\n½ lyžičky prášku do pečiva\n½ lyžičky soli\n50 ml olivového oleja\n100 ml teplej vody",
                    image = R.drawable.tortilla_placky,
                    type = RecipeType.STARTER
                ),
                Recipe(
                    name = "Gyros",
                    content = "Potrebujeme:\n500 g kuracieho alebo bravčového mäsa\n5-7 lyžičiek Gyros korenia\n3 lyžice olivového oleja\n1 lyžičku citrónovej šťavy\n\nNa omáčku:\n2 tégliky jogurtu (2×150 ml)\n2 uhorky\n3-4 strúčiky cesnaku\nSoľ a korenie\nCitrónovú šťavu\nOlivový olej – 1-2 lyžičky\n\nNa tortily:\nPokojne môžete použiť aj tie kupované, ja ich robím domáce, podľa tohto receptu:\n450 g hladkej múky\n2 lyžičky soli\n1 lyžičku cukru\n1 bal. sušeného droždia\n4 lyžice olivového oleja\n300 ml vody            \n\nPostup:\nMäso nakrájame na prúžky a niekoľko hodín posypte zmesou gyros korenie. Potom jednoducho opražíme na rozpálenej panvici na troche oleja – na kuracie mäso stačí len pár minút a je hotové, na bravčové potrebujete trošku viac času.\n\nNa tzatziki:\nUhorku nastrúhame, zmiešame s jogurtom, pretlačeným cesnakom, olejom a citrónovou šťavou, dochutíme soľou a korením. Necháme vychladiť.\n\nNa tortilly (8-10 ks):\nVšetky prísady spracujeme a cesto rozdelíme na 8-10 kúskov. Počkáme, kým cesto podrastie a potom roztiahneme, alebo rozvaľkáme na placky. Necháme ešte 15 minút odležať. Na nepriľnavej panvici pečieme z oboch strán dozlatista. Prípadne môžete piecť na panvici pokrytej papierom na pečenie. Počas pečenia z oboch strán zľahka popicháme vidličkou.\n\nNakoniec stačí len všetko poskladať dohromady. Mäsko, domáce hranolky – ak ich chcete použiť – zabalíme do tortilly a pridáme aj omáčku. Podávame so zeleninovým šalátom.\n",
                    image  = R.drawable.gyros,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Tiramisu",
                    content = "5 ks vajcia\n 4 lyžice kryš.cukor\n 500 g mascarpone\n kakao rozpustené vo vode\n piškóty dlhé \n\nŽĺtky a cukor vyšľaháme do peny.Ešte lepší efekt dosiahneme ak ich vyšľaháme nad parou .Do vyšľahanej peny pridáme mascarpone a nakoniec zľahka primiešame tuhý sneh. \n\n Uvaríme silnú kávu do ktorej pridáme trochu cukru a likér amareto.Piškóty opatrne a hlavne rýchlo namáčame v káve a ukladáme do formy alebo na plech.Na piškóty natrieme polovicu pripraveného krému .Poukaladáme dalšie namočené piškóty a opäť natrieme krémom. \n\n Nakoniec posypeme kakaom alebo postrúhame čokoládou. Dáme do chladničky najlepšie na 24 hodín.",
                    image = R.drawable.tiramisu,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Maďarský guláš",
                    content = "hovädzie mäso 1,2kg\ncibuľa 500g\npaprika červená sladká 2ČL\npaprika 3ks\nparadajky 3-4 kusy\nparadajkový pretlak 2PL\nbravčová masť 2PL\nčierne korenie\nsoľ\npapriková pasta pálivá\nčili paprička\n\n1\nMäso nakrájame na 3– až 4–centimetrové kocky, cibuľu nakrájame nadrobno, paradajky oblanšírujeme v horúcej vode, stiahneme šupku a nakrájame na malé kúsky. Papriku očistíme a nakrájame na tenké pásiky.\n\n2\nDo hrnca dáme bravčovú masť, pridáme cibuľu a na strednom ohni smažíme, až začne cibuľa žltnúť a karamelizovať. Pridáme na kocky nakrájané mäso, zatiahneme a pridáme mletú papriku a hneď pridáme nakrájané papriky a paradajky a paradajkový pretlak. Dáme na slabý oheň a občas premiešame, kým táto zmes nepustí šťavu. Pridáme soľ, korenie a podlejeme vodou.\n\n3\nDusíme na miernom ohni a za občasného miešania a podlievania vodou podľa potreby asi 2 hodiny. Guláš necháme vyredukovať na požadovanú hustotu a podľa potreby dochutíme. Podávame s haluškami. V našich končinách sa tiež podáva so žemľovou alebo s parenou knedľou.",
                    image = R.drawable.madarsky_gulas,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Moravské koláče",
                    content = "Mlieko 300ml\ncukor vanilkový 1bal.\ndroždie 3/4 kocky\nhladká múka 500g\nkryštálový cukor 2,5PL\nžĺtky 2ks\nhrozienka 2PL\n\nposýpka\ncitrónová kôra 1ČL\nkryštálový cukor 50g\npolohrubá múka 80g\n\n1\nV 200 ml teplého mlieka rozmiešame vanilínový cukor, droždie a môžeme pridať aj 1 PL hladkej múky. Prikryjeme a necháme vzísť kvások (trvá to približne 10 minút).\n\n2\nDo misy nasypeme múku, cukor, žĺtky, soľ, prilejeme kvások a zvyšné mlieko a spracujeme. Približne v polovici miesenia pridáme do cesta zmäknuté maslo a vymiesime hladké cesto. Prikryjeme utierkou a na teplom mieste necháme cca 30-45 minút kysnúť.\n\n3\nNa tvarohovú náplň zmiešame všetky ingrediencie. Preferujem hrudkovitý tvaroh v alobale, ten vo vaničke obsahuje veľa vody a náplň by sa roztekala. Hrozienka pridávame už vopred namočené do vody, ktorú následne zlejeme.\n\n4\nNa posýpku zmiešame všetky suroviny.\n\n5\nVykysnuté cesto preložíme na dosku, ktorú podľa potreby môžeme posypať múkou, aby sa cesto nelepilo. Cesto rozvaľkáme na hrúbku cca 0,5 cm a vykrajujeme z neho kolieska s priemerom 8-10 cm (pohárom to ide fajn). Na kolieska nanesieme tvarohovú náplň a zabalíme. Koláčiky prenesieme na plechy vystlané papierom na pečenie. Do každého koláčika urobíme jamku, do ktorej dáme slivkový lekvár. Koláče potrieme rozšľahaným vajíčkom, posypeme posýpkou.\n\n6\nKoláčiky necháme na plechu ešte podkysnúť a potom vložíme do rúry vyhriatej na 180 °C a pečieme cca 20-25 minút alebo podľa skúsenosti s Vašou rúrou :)",
                    image = R.drawable.moravske_kolace,
                    type = RecipeType.DESSERT
                ),
                Recipe(
                    name = "Feta cestoviny",
                    content = "cestovina (podľa potreby)\njedna kocka feta syra\ncherry paradajky (podľa vlastného uváženia)\ncesnak\nčerstvá bazalka\nkoreniny- oregano, mleté čierne korenie, soľ\nolivový olej\n\n1\nPripravte si pekáč alebo inú formu (my sme použili jánsku misu). Umyte cherry paradajky a spolu s neolúpaným cesnakom ich dajte do pripravenej formy. Následne ich polejte olivovým olejom a pridajte čierne korenie, soľ a oregano. Dobre premiešajte. V strede formy si vytvorte miesto na kocku feta syra, ktorú taktiež okoreňte a pokvapkajte olivovým olejom.\n\n2\nPredhrejte si rúru a dajte piecť na 180 stupňov, približne na 30 minút. Zatiaľ si môžete uvariť cestoviny a pripraviť čerstvú bazalku. My sme použili mušličkové cestoviny Conchiglie, ale pokojne použite aj iný druh, napríklad penne.\n\n3\nPribližne po 30 minútach vyberte formu z rúry a uistite sa, že sú paradajky aj cesnak dostatočne mäkké. Lyžičkou povyberajte strúčiky cesnaku, a olúpte ich.\n\n4\nPotom ich vráťte naspäť a všetko roztlačte vidličkou a dobre premiešajte. Vznikne vám tak výborná „omáčka“. Pridajte čerstvú bazalku a uvarené cestoviny. Opäť všetko spolu premiešajte.",
                    image = R.drawable.feta_cestoviny,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Špagety s bazalkou",
                    content = "1 ks Pomodoro - paradajková omáčka s bylinkami\nTrochu kukurice\nKs lístky bazalky\n10 ks cherry paradajky\n1/4 ks špagety\nŠtipka soli\nTrochu oleja\n\n1\n Dáme variť špagety. Do vody pridáme štipku soli a trocha oleja, aby sa neprilepili. Počas varenia špagiet, si pripravíme omáčku.\n\n2\nDo menšieho hrnca pridáme omáčku, kukuricu, nakrájanú čerstvú bazalku, nakrajáne cherry paradajky na štvrťky. Varíme približne 5 minút. Po dovarení špagiet precedíme cez sitko a podávame s omáčkou. Môžeme dodekorovať s lístkami čerstvej bazalky. Dobrú chuť.",
                    image = R.drawable.spagety_s_bazalkou,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Kari jablko",
                    content = "kuracie prsia\n3 ks jablko\n1 ks cibuľa\n1 PL sójová omáčka\n250 ml smotana na varenie\npodľa chuti soľ\npodľa chuti mleté čierne korenie\npodľa chuti bazalka\npodľa chuti kari korenie\nOlej\njarná cibuľka\n\n1\nKuracie prsia nakrájame na väčšie kocky. Pridáme trochu oleja a podľa chuti soľ, mleté čierne korenie a bazalku. Jablká umyjeme, vyrežeme jadrovníky a nakrájame na kocky. \n\n2\nCibuľu ošúpeme a nakrájame nadrobno. Na panvici rozhorúčime trochu oleja, na ktorom následne opražíme cibuľku. Potom pridáme kuracie prsia a opražíme. Pridáme jablká a krátko opečieme. \n\n3\nKu opečenému kuraciemu mäsu s jablkami pridáme sójovú omáčku, smotanu na varenie a karí korenie. Varíme kým nie je mäso hotové a jablká mäkké. Nakoniec ešte môžeme podľa chuti osoliť a okoreniť. \n\n4\nPodávame posypané nasekanou jarnou cibuľkou a s ryžou.",
                    image = R.drawable.kari_jablko,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Segedínsky guláš",
                    content = "1 ks bravčové pliecko (800g - 1,5kg)\n750 g kyslá kapusta\n2 PL bravčová masť\n1 ks cibuľa\n2 PL mletá červená paprika\n500 ml smotana na šľahanie\n1 ČL mletá rasca\n1/2 KL mleté čierne korenie\npodľa chuti soľ\n\n1\nBravčové mäso dobre umyjeme a nakrájame na kocky. Cibuľu očistíme a nakrájame na drobno.\n\n 2\nDo hlbokého hrnca dáme bravčovú masť a cibuľku orestujeme do sklovita.\n\n3\nPridáme mäso a restujeme, kým sa zatiahne.\n\n4\nPridáme mletú rascu, mleté čierne korenie a podľa chuti soľ. Podlejeme vodou a dusíme zhruba 45 minút.\n\n5\nMedzitým si kapustu nakrájame.\n\n6\nPridáme ju k mäsu.\n\n7\nĎalej pridáme mletú červenú papriku, dobre premiešame a dusíme do mäkka. Podľa potreby podlejem vodou.\n\n8\nNa záver pridáme smotanu. Ak chcete mať guláš hustejší, v smotane rozmiešame lyžicu hladkej múky. Necháme povariť na miernom ohni ešte 10 minút, podľa potreby dochutíme a môžeme podávať.\n\n9\nNajlepšie chutí s domácou parenou knedľou.",
                    image = R.drawable.segedinsky_gulas,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Pečienka s rýžou",
                    content = "500 g ghovädzia pečeň\n1 ks biela cibuľa\nTrochu hladkej múky\n1,5 hrnčeku ryže\npodľa chuti soľ\npodľa chuti mleté čierne korenie\npodľa chuti korenie cigánska pečienka\n3 PL kečup\n\n1\nHovädziu pečeň umyjeme a nakrájame na plátky. Cibuľu nakrájame nadrobno. Na oleji speníme nadrobno nakrájanú cibuľu, pridáme plátky pečene a sprudka opečieme. Okoreníme, podlejeme vodou a dusíme do mákka. Osolíme až ked je už pečeň mäkká. \n\n2\nOmáčku môžme zahustiť múkou. Ryžu si v osolenej vode uvaríme do mäkka a podávame ako prílohu.",
                    image = R.drawable.pecienka_ryza,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Paradajková polievka",
                    content = "2 lyžice olivového oleja\n2× cibuľa šalotka (očistené a nadrobno nakrájané)\n1 strúčik cesnaku (utrieť)\n400 g olúpaných nakrájaných paradajok z konzervy\n500 ml zeleninového vývaru\nniekoľko čerstvých lístkov bazalky\nsoľ\nčierne korenie\n\nV hrnci na strednom stupni rozohrejte 2 lyžice olivového oleja.\n\nDo hrnca pridajte najemno nakrájanú šalotku a opekajte 5 – 7 minút na strednom stupni. K šalotke pridajte rozotretý strúčik cesnaku a opekajte ešte ďalšiu 1 minútu.\n\nPotom stlmte na mierny stupeň, pridajte pokrájané paradajky, 500 ml zeleninového vývaru, soľ a korenie a varte 30 minút.\n\nNakoniec zmes rozmixujte. Hotovú polievku rozdeľte do 4 tanierov, ozdobte čerstvou bazalkou a podávajte.",
                    image = R.drawable.tomato_soup,
                    type = RecipeType.STARTER
                ),
                Recipe(
                    name = "Mexický guláš",
                    content = "1 ks - cibuľa\n1 PL - hladká múka\n1 malá plechovka hustého paradajkového pretlaku\n2 plné hrste - nastrúhaný obľúbený syr\n1 kávová lyžička - červená mletá paprika\n1000 g - hovädzie mäso\nPodľa chuti - čierne mleté korenie\nPodľa chuti - soľ\n4 PL - olej\n\nHovädzie mäso vhodné na guláš (napr. hovädzie zadné) narežeme na kocky. V miske ho zmiešame so soľou, mletým čiernym korením, mletou červenou paprikou a 2 PL oleja. Takto napacované mäso dáme do chladničky, najlepšie na celú noc. Mäso nasiakne koreninami, rýchlejšie sa uvarí a bude ešte lahodnejšie.\n\nOčistenú cibuľu nakrájame na kocky a na oleji ju speníme dosklovita. Pridáme mäso, sprudka ho orestujeme, aby sa celé zatiahlo. Keď máme mäso opečené, pridáme pretlak, hrniec zakryjeme pokrievkou a mäso necháme dusiť. Guláš občas skontrolujeme, premiešame varechou a keď sa nám zdá, že je málo šťavy, dolievame postupne vodou podľa potreby.\n\nAsi po hodine varenia pridáme k mäsu mrazený hrášok. Pridávame ho až neskôr, aby sa nám celý nerozvaril. Guláš varíme ešte cca 30-45 minút, kým nebude mäso mäkké a omáčka sa úplne zredukuje.\n\nKeď sa všetka voda z omáčky odparí, je dostatočne hustá. Ak však patríte medzi jedákov, ktorí majú radi viac omáčky, zahustite ju hladkou múkou. Múku rozmiešajte v cca 1 šálke vody, pomaly vlejte do gulášu a zamiešajte. Varte ešte aspoň 7-10 minút. Hotový mexický guláš podávame s ryžou a posypeme strúhaným syrom, napr. cheddarom, goudou či eidamom.",
                    image = R.drawable.mexicky_gulas,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Bryndzové halušky",
                    content = "150 g slaniny z mangalice\n500 g zemiakov\n 250 g hladkej múky\n1 vajíčko\nsoľ\n200 g bryndze\n200 g kyslej smotany (18 %)\n1 zväzok lahôdkovej cibuľky\n\nSlaninu nakrájame na menšie kocky a opečieme v kastróle dochrumkava.\n\n Zemiaky očistíme, umyjeme a nastrúhame najemno. Pridáme múku a vajíčko. Dôkladne premiešame.\n\n Vo väčšom hrnci necháme zovrieť vodu, ktorú osolíme. Pomocou sitka a stierky na halušky do nej pretláčame pripravené cesto. Dôkladne premiešame, aby sa nezlepili.\n\n Halušky varíme, kým vyplávajú na hladinu (približne 3 minúty). Vo väčšej miske zmiešame bryndzu s kyslou smotanou. Pridáme halušky a zľahka premiešame.",
                    image = R.drawable.halusky,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Bryndzové pirohy",
                    content = "500 g zemiakov (ideálne typ C)\n 300 g polohrubej múky\n 2 vajcia\n 375 g plnotučnej bryndze\n čierne korenie\n morská soľ\n 1 zväzok lahôdkovej cibuľky\n 100 g slaniny z mangalice\n 300 g kyslej smotany\n\nPrípravu cesta začneme ideálne deň vopred. Zemiaky uvaríme v šupke a necháme vychladnúť najprv pri izbovej teplote a potom v chladničke. Na druhý deň zemiaky ošúpeme a nastrúhame najemno. Z polovice nastrúhaných zemiakov primiešavaním asi 200 g múky a 2 vajec vypracujeme cesto.\n\n Tip: Množstvo múky závisí od typu zemiakov. S cestom pracujeme rýchlo, keď príliš dlho stojí, zemiaky začnú púšťať vodu.\n\n Cesto vyformujeme do tvaru valca a prikryjeme kuchynskou utierkou. Z druhej polovice zemiakov, zmiešaním s 250 g bryndze, korením, so soľou a s 1 najemno nasekanou bielou časťou lahôdkovej cibuľky, pripravíme plnku. Zvyšnou múkou posypeme pracovnú dosku a postupne ju zapracujeme do cesta.\n\n Tip: Cesto je lepšie, keď je v ňom menej múky.\n\n Cesto rozvaľkáme na hrúbku asi 2 mm. Vykrajujeme z neho pomocou formy alebo pohára kolieska. Každý naplníme 1 lyžicou plnky. Okraje cesta navlhčíme a formu uzavrieme.\n\n Tip: Ak nemáme formu na pirohy, kolieska s navlhčenými okrajmi preložíme na polovicu a dôkladne uzavrieme najprv prstami a potom vidličkou.\n\n Pirohy varíme vo vriacej osolenej vode asi 10 minút, kým vyplávajú na hladinu. Hneď po vložení do vody ich opatrne premiešame, aby sa neprilepili. Slaninu nakrájame na menšie kocky a opečieme v kastróle do chrumkava. Podľa potreby môžeme pridať bravčovú masť. V miske zmiešame zvyšnú bryndzu a kyslú smotanu.",
                    image = R.drawable.bryndzove_pirohy,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Langoš",
                    content = "250 g hladká múka\n 1/2 KL soľ\n 1/4 KL kypriaci prášok do pečiva\n 1/2 PL olej\n 10 g droždie\n 40 g biely jogurt\n 125 ml mlieko\n\nDo misy nasypeme múku, pridáme soľ a kypriaci prášok do pečiva. Do stredu urobíme jamku, do ktorej nalejeme olej a rozmrvíme droždie, pridáme jogurt a vlažné mlieko.\n\nVareškou vymiešame hladké cesto, ktoré ihneď rozdelíme na 4 časti. Pomúčenou rukou z cesta tvarujeme placky, ktoré vyťahujeme do všetkých strán. Opečieme ich v panvici na rozpálenom oleji z oboch strán.",
                    image = R.drawable.langos,
                    type = RecipeType.STARTER
                ),
                Recipe(
                    name = "Kotlíkový guláš",
                    content = "3 kg hovädzieho gulášu\n2 kg cibule\n250 g bravčovej masti\n soľ\n celé čierne korenie\n bobkový list\n 12  strúčikov cesnaku\n mletá sladká paprika\n 1,5 kg zemiakov\n teplá voda (ideálne vriaca)\n 750 g paprík\n 750 g paradajok\n majoránka\n 1 chlieb\n\nCibule nasekáme na menšie kúsky a restujeme v kotlíku za občasného miešania na roztopenej bravčovej masti. Ochutíme soľou, celým čiernym korením a bobkovými listami. Strúčiky cesnaku rozpučíme čepeľou noža, nakrájame nadrobno, pridáme k cibuli a prikryjeme pokrievkou.\n\nKeď je cibuľa do sklovita opečená, pridáme mäso a restujeme ho, pokým sa nezatiahne. Potom ho ochutíme mletou paprikou, premiešame a ešte chvíľu restujeme. \n\nPridáme zemiaky nakrájané na kocky, dochutíme soľou, podlejeme vodou a prikryjeme pokrievkou.\n\nPo približne 20 minútach pridáme na menšie kúsky nakrájané umyté papriky a paradajky. Guláš dusíme pod pokrievkou domäkka. Na  záver dochutíme majoránkou.",
                    image = R.drawable.kotlikovy_gulas,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Wafle",
                    content = "130 g hladká múka\n 1 PL kypriaci prášok\n 0.25 KL soľ\n 1 PL kryštálový cukor\n 1 PL vanilkový cukor\n 2 ks vajce (M)\n 180 ml plnotučné mlieko\n 50 g maslo\nV miske premiešame naváženú múku s kypriacim práškom, soľou, kryštálovým cukrom a vanilkovým cukrom.\n\nV inej miske vymiešame mlieko, celé vajíčka a roztopené maslo.\n\n V strede sypkých surovín urobíme jamku, do ktorej nalejeme vajíčkovú zmes a vymiešame redšie cesto.\n\n Vafľovač potrieme troškou oleja a dáme zahriať.\n\n Cesto nalievame do rozpáleného vafľovača žufankou. Ideálne, nie úplne ku krajom, aby nevytekalo. Zavrieme a pečieme podľa potreby.\n\n Vafle podávame s našimi obľúbenými prílohami: roztopeným maslom, medom,  džemom, javorovým sirupom, ovocím, čokoládou, šľahačkou...",
                    image = R.drawable.wafle,
                    type = RecipeType.DESSERT
                ),
                Recipe(
                    name = "Sviečková",
                    content = "1,2 kg mladého býčka\n 6 ks mrkva vačšia\n 3 ks petržlen\n 1 ks zeler vačší\n 3 ks cibule\n 4 PL olej\n 250 ml smotana na varenie 10%\n 200 ml smotana kyslá 16%\n 2 PL paprika červená mletá\n soľ mleté čierne korenie\n 5 ks borievky sušené celé\n 2 ks bobkový list\n kryštálový cukor podľa chuti\n\n Hovädzie je náročné /časovo/ na varenie, preto ak natrafíte na mladeho býčka, neváhajte .o)) Mäso v celku, umyjeme, odblaníme, osušíme.\n\n Mrkvu, petržlen, zeler, očistíme, umyjeme, nakrajame na kolieska. Cibulu osupeme, umyjeme, nakrajame na kocky.\n\n Do hrnca dame olej, na nom oprazime cibulku. Zo vsetkych stran poopekame masko v celku. Ked je zatiahnute, vyberieme ho. Na cibulke oprazime par sekund mletu cervenu papriku, pridame nakrajanu zeleninku, tiez chvilku poopekame, pridame borievky, bobkovy list, osolime, okorenime, pridame masko, zalejeme vriacou vodou a nechame dusit. Nemam kuchtu, tak som masko dusila asi tri hodiny.\n\n Ked je masko makke, vyberieme ho z hrnca. Vylovime, bobkove listy a borievky. Ponorným mixerom pomixuje uz aj tak viacmenej rozvarenu zeleninku. Pridala som asi lyzicu krystaloveho cukru, smotanu aj slahacku zamiesala a cakala na prevretie. Zatiam som masko nakrajala na platky a ked omacka zovrela, vypla som ohen a masko dala naspat do omacky aby neoschlo.",
                    image = R.drawable.svieckova,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "San sebastian",
                    content = "900g Philadelphia\n 360g kryštálový cukor al. práškový cukor\n 1 vanilkový struk al. 1-2 čaj. lyž. vanilkovej pasty\n 360ml šľahačky 33-35%\n 300g vajec\n 38g hladkej múky alebo kukuričného škrobu\n forma veľkosť 24- 25\n\n1. Všetky ingrediencie musia byť izbovej teploty.\n\n 2. Šľahačku, vanilku a múku spojíme tyčovým mixérom a odložíme.\n\n 3. Do misky mixéra dáme syr, cukor a miešame na nízkych otáčkach, aby syr zmäkol a bol krémový, NEŠĽAHAŤ!!\n\n 4. Po 1ks pridávame vajcia, môžeme používať aj pasterizované. Občas zastavíme, hrúdky zmiešame , aby sa vajicia spojili.\n\n 5. Pridáme zmes šľahačky a múky. Celkom miešame 5-6 minút.\n\n 6. Pečieme 30 -35 minút pri 230°C, v režime konvekcia alebo vrch spodok . Nezabúdame vrch musí byť spálený. Pre viac spálený vrch zapneme na 2-3 minúty funkciu grill.\n\n 7. Stred sa musí triasť. Počas pečenia stúpne a je dosť vysoký, ale ked vychladne spadne a tak to má byť nemusíte sa obávať.",
                    image = R.drawable.san_sebastian,
                    type = RecipeType.DESSERT
                ),
                Recipe(
                    name = "Manti",
                    content = "Cesto:\n\n 2 ks vajce\n 1 KL soľ\n 450 g hladká múka\n 120 ml voda\n\n Plnka:\n\n 300 g mleté hovädzie mäso\n 1 ks cibuľa\n podľa chuti\n soľ, mleté čierne korenie\n\n Omáčka:\n\n 400 g biely jogurt (100 g na porciu)\n 3 strúčiky cesnak\n 4 PL maslo\n podľa chuti čili vločky\n\n1\n Cesto: v miske spolu vyšľaháme celé vajíčka spolu so soľou, pridáme trochu vody a postupne rukou zapracujeme múku. Nakoniec pridáme ešte zvyšok vody a hnetením v miske vypracujeme hladké cesto.\n\n 2\n Misku prekryjeme fóliou a cesto necháme postáť zhruba 30 minút.\n\n 3\n Neskôr cesto rozdelíme na 4 časti a z každej za poctivého pomúčenia dosky vyvaľkáme naozaj tenučký kruhový plát (cca 2 mm hrubý). Plát nerežeme na pásiky hrubé cca 2 centimetre a potom priečne na  malé štvorce.\n\n 4\n Plnka: mleté mäso premiešame s nadrobno nasekanou cibuľou, soľou a korením podľa chuti. Hotovo.\n\n5\n Na malé štvorčeky cesta ukladáme len štipky plnky, aby sa dali uzavrieť. Cestoviny uzatvárame tak, že štvorček preložíme na polovicu cez plnku, spojíme a potom ešte pritlačíme rožteky spoja a vytvoríme akýsi mini batôžtek. Batôžteky ukladáme na pomúčenú dosku. Takto postupujeme kým sa nám neminie cesto i plnka.\n\n 6\n Manti po častiach (4 časti cesta) varíme v už vriacej osolenej vode 12 a 15 minút.\n\n 7\n Omáčka: v jogurte rozmiešame pretlačený cesnak. Na masle opražíme čili vločky.\n\n 8\n Horúce manti polejeme cesnakovým jogurtom a navrch pridáme pražene čili.",
                    image = R.drawable.manti,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Surimi cestoviny",
                    content = "1 balík krabích tyčiniek\n 1 konzerva miešanej zeleniny (najmä kukurica a hrášok)\n 250 ml majonézy\n 500 g sušených cestovín (ideálne vrtuľky)\n soľ, podľa potreby\n biele korenie\n šťava z polky citróna\n\n1\nKrabie tyčinky odbalíme a nakrájame na rovnomerné kúsky.\n\n 2\nPridáme zeleninu z konzervy.\n\n 3\nNasleduje majonéza, soľ, biele korenie a citrónová šťava.\n\n 4\nPridáme uvarené cestoviny; len pozor, až po vychladnutí. Dôsledne všetko spolu premiešame.",
                    image = R.drawable.surimi_cestoviny,
                    type = RecipeType.MAIN_COURSE
                ),
                Recipe(
                    name = "Roštenka",
                    content = "600 g bravčová roštenka\n 1 ks biela cibuľa\n trochuv hladká múka\n trochu olej\n podľa chuti soľ\n podľa chuti mleté čierne korenie\n podľa chuti worchesterská omáčka\n podľa chuti horčica\n trochu maslo čerstvé\n\n1\nRoštenku umyjeme, narežeme na plátky. Osolíme, okoreníme mletým čiernym korením. Každý plátok pokvapkáme worčestrovou omáčkou, potrieme horčicou a olejom a necháme v chlade chvíľu odležať.\n\n2\nPripravené rezne zaprášime múkou a na cibuľke opražíme. Podlejeme vodou,alebo bujónom, pridáme trochu masla a dusíme do mäkka.Podávame s ryžou.",
                    image = R.drawable.rostenka,
                    type = RecipeType.MAIN_COURSE
                )
            )
            recipeDao.insertAllRecipes(recipes)
        }
    }
}

package com.example.ams.golovolomki.Assistants;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;
import com.example.ams.golovolomki.R;
import java.util.ArrayList;

/* table PuzzleTable structure
+--------+------------+-----------------+----------+------------+------------+----------------+----------+
|- 0 id -|- 1 header -|- 2 description -|- 3 text -|- 4 advice -|- 5 answer -|- 6 complexity -|- 7 wiki -|
+--------+------------+-----------------+----------+------------+------------+----------------+----------+

1 - title puzzle
2 - description for main list view
3 - main text
4 - advice puzzle
5 - answer puzzle
6 - complexity (* - easy, ** - normal, *** - hard)
7 - path to wiki
*/

/* table SettingsTable structure
+--------+---------------+--------------+-------------+----------+-----------------+----------------+------------+----------------+---------------+
|- 0 id -|- 1 lastIndex -|- 2 appStart -|- 3 dbindex -|- 4 toUp -|- 5 doubleClick -|- 6 moveToLast -|- 7 isRate -|- 8 toNextRate -|- 9 textAlign -|
+--------+---------------+--------------+-------------+----------+-----------------+----------------+------------+----------------+---------------+

1 - index of the last shown puzzle
2 - count starting app
3 - current version db
4 - flag sorting main list view
5 - duplicate action !!! - NOT USED - !!!
6 - show last puzzle on start app
7 - rate flag
8 - count to next show rating dialog
9 - text align left-center
*/

/* table Favorite structure
+--------+--------------+-----------+
|- 0 id -|- 1 favorite -|- 2 state -|
+--------+---------------+----------+

1 - index favorite puzzle
2 - state closing puzzle
*/



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String  DATABASE_NAME       = "Puzzle.db";

    @SuppressWarnings("!!! D.B. VERSION !!!")
    public static final int      SCHEMA              = 3;


    public static final String   TABLE_NAME_PUZZLE   = "PuzzleTable",
                                 TABLE_NAME_SETTINGS = "SettingsTable",
                                 TABLE_NAME_FAVORITE = "FavoriteTable";

    private ContentValues        contentValues;

    // column names
    public static final String
            COLUMN_ID                   = "id",
            COLUMN_HEADER               = "header",
            COLUMN_DESCRIPTION          = "description",
            COLUMN_TEXT                 = "text",
            COLUMN_ADVICE               = "advice",
            COLUMN_ANSWER               = "answer",
            COLUMN_COMPLEXITY           = "complexity",
            COLUMN_WIKI                 = "wiki",

            COLUMN_SETTINGS_ID          = "id",
            COLUMN_SETTINGS_LASTINDEX   = "lastIndex",
            COLUMN_SETTINGS_APPSTART    = "appStart",
            COLUMN_SETTINGS_DBINDEX     = "dbindex",
            COLUMN_SETTINGS_TOUP        = "toup",
            COLUMN_SETTINGS_DOUBLECLICK = "doubleClick",
            COLUMN_SETTINGS_MOVETOLAST  = "moveToLast",
            COLUMN_SETTINGS_ISRATE      = "isRate",
            COLUMN_SETTINGS_TONEXTRATE  = "toNextRate",
            COLUMN_SETTINGS_TEXTALIGN   = "textAlign",

            COLUMN_FAVORITE_ID          = "id",
            COLUMN_FAVORITE_FAVORITE    = "favorite",
            COLUMN_FAVORITE_STATE       = "state";



    public static Cursor valueCursor, settingsCursor, favoriteCursor;

    public static ArrayList<Integer> listImages = new ArrayList<Integer>();




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);

        InitializeImages();
    }

    public void InitializeImages() {
        listImages.add(R.drawable.img0); listImages.add(R.drawable.img1); listImages.add(R.drawable.img2); listImages.add(R.drawable.img3); listImages.add(R.drawable.img4);
        listImages.add(R.drawable.img5); listImages.add(R.drawable.img6); listImages.add(R.drawable.img7); listImages.add(R.drawable.img8); listImages.add(R.drawable.img9);
        listImages.add(R.drawable.img10); listImages.add(R.drawable.img11); listImages.add(R.drawable.img12); listImages.add(R.drawable.img13); listImages.add(R.drawable.img14);
        listImages.add(R.drawable.img15); listImages.add(R.drawable.img16); listImages.add(R.drawable.img17);
    }

    public void InitializationSettingsTable(SQLiteDatabase db) {
        db.execSQL("INSERT INTO "
                + TABLE_NAME_SETTINGS           + " ("
                + COLUMN_SETTINGS_LASTINDEX     + ", "
                + COLUMN_SETTINGS_APPSTART      + ", "
                + COLUMN_SETTINGS_DBINDEX       + ", "
                + COLUMN_SETTINGS_TOUP          + ", "
                + COLUMN_SETTINGS_DOUBLECLICK   + ", "
                + COLUMN_SETTINGS_MOVETOLAST    + ", "
                + COLUMN_SETTINGS_ISRATE        + ", "
                + COLUMN_SETTINGS_TONEXTRATE    + ", "
                + COLUMN_SETTINGS_TEXTALIGN     + ") "
                + "VALUES (0, 0, 1, 'true', 'true', 'false', 'false', 2, 'true');"); // WARNING! UPDATE DB INDEX
    }

    public void InitializationFavoriteTable(SQLiteDatabase db) {
        db.execSQL("INSERT INTO "
                + TABLE_NAME_FAVORITE       + " ("
                + COLUMN_FAVORITE_FAVORITE  + ", "
                + COLUMN_FAVORITE_STATE     + ") "
                + "VALUES ('', '');");
    }

    private void InsertDateIntoPuzzleTable(SQLiteDatabase db, String header, String description,
                                           String text, String advice, String answer,
                                           String complexity, String wiki) {
        db.execSQL("INSERT INTO "
                + TABLE_NAME_PUZZLE     + " ("
                + COLUMN_HEADER         + ", "
                + COLUMN_DESCRIPTION    + ", "
                + COLUMN_TEXT           + ", "
                + COLUMN_ADVICE         + ", "
                + COLUMN_ANSWER         + ", "
                + COLUMN_COMPLEXITY     + ", "
                + COLUMN_WIKI           + ") "
                + "VALUES ('"           + header
                + "', '"                + description
                + "', '"                + text
                + "', '"                + advice
                + "', '"                + answer
                + "', '"                + complexity
                + "', '"                + wiki
                + "');");

        db.execSQL("INSERT INTO "
                + TABLE_NAME_FAVORITE       + " ("
                + COLUMN_FAVORITE_FAVORITE  + ", "
                + COLUMN_FAVORITE_STATE     + ") "
                + "VALUES ('', '');");
    }


    public void InitializationPuzzleTable(SQLiteDatabase db) {

        // 1 - 5
        this.InsertDateIntoPuzzleTable(db, "НАЧАЛО", "\nCпасибо, что выбрали данное приложение!\nНе забудьте обязательно прочитать\nданный раздел...", "Добро пожаловать в приложение МОЗГОЛОМКИ - лучший в своем роде офлайн сборник загадок и головоломок со всего мира.\nВ данном сборнике Вы найдете сотни лучших головоломок и загадок, а также подсказки и ответы на них.\nДанное приложение не является игрой, здесь нет задачи непременно отгадать все загадки и узнать ответ на каждую головоломку.\nЗадачей данного приложения является заставить Вас размышлять, думать и анализировать при решении очередной загадки.\nНе спешите смотреть ответ, попробуйте подумать, тщательно прочитайте вопрос еще раз или воспользуйтесь подсказкой.\nПомните это!\n\nP.S. Надеюсь мое приложение будет полезным для Вас.\nЕсли Вам понравится данное приложение не забудьте оценить его и оставить отзыв.\nОценки и отзывы очень важны для меня, они помогают мне сделать приложении более качественным.\n\nОценить приложение можно на странице ПОМОЩЬ.\n\nХорошего настроения и удачи!", "\n\nВы можете проверить себя в любой момент, сверившись с ответом!\nНе спешите открывать данную страницу, попробуйте подумать еще раз, тщательно прочитайте загадаку или воспользуйтесь подсказкой.", "\n\nСложная загадка?\nВам поможет подсказка!\nНа данной странице Вы найдете подсказку для каждой головоломки.", "ВВОДНАЯ", "");
        this.InsertDateIntoPuzzleTable(db, "КАРТЫ", "\nГоловоломка про игральные карты,\nв которой нужно угадать масть\n и достоинство карт...", "Представьте, что перед вами лежат три карты «РУБАШКОЙ ВВЕРХ».\nУгадайте масть и достоинство у каждой из трех карт.\nЗная, что:\n1. Есть минимум одна\n «ТРОЙКА» справа от «ДВОЙКИ».\n2. Есть минимум одна\n «ТРЕФА» справа от «ТРЕФЫ».\n3. Есть минимум одна\n «ТРОЙКА» слева от «ТРОЙКИ».\n4. Есть минимум одна\n «ТРЕФА» слева от «БУБНЫ».", "\n\n1 - двойка треф.\n2 - тройка треф.\n3 - тройка бубен.", "\n\nПервой будет двойка треф.", "ЛЕГКАЯ", "Игральные_карты");
        this.InsertDateIntoPuzzleTable(db, "ПРОГУЛКА", "\nУтренняя прогулка может быть\nзахватывающей, если гулять по\nнеобычным местам...", "Как-то утром в понедельник один человек шел по Санкт-Петербургу пару километров и его никто не увидел.\nИ он не заметил ни единой живой души.\nБыло светло, мужчина все хорошо видел и вокруг и куда идет.\nВесь свой путь он прошел пешком.\nВ это время по городу продвигалось много людей недалеко от него.\nОднако, ни он никого, ни его никто не увидел.\nКак же так?", "\n\nОн шел по канализации.", "\n\nЭто есть в каждом крупном городе.", "ТЯЖЕЛАЯ", "Канализация");
        this.InsertDateIntoPuzzleTable(db, "ГЛОБУС", "\nВ философии — это необратимое течение,\nпротекающее лишь в одном направлении — из прошлого, через настоящее в будущее...", "Большую часть марта\nв Киеве плюс 2,\nв Лондоне — ноль,\nа в Нью-Йорке минус 5.\nА сколько в марте в Хабаровске,\nесли в июле там +11?", "\n\n+11.\nРечь идет о часовых поясах", "\n\nСмотри на глобус.\nВ России их всего 11.", "СРЕДНЯЯ", "Часовой_пояс");
        this.InsertDateIntoPuzzleTable(db, "ЭПИЗОД", "\nОдна из загадок самого длинный\nмультсериала в истории\nамериканского телевидения...", "В одном из эпизодов мультсериала «Симпсоны» Барт в присутствии китайских шпионов задается вопросом, может ли он предать свою страну, ведь он каждый день произносит клятву верности флагу США.\nВ ответ следует возражение, что клятва приносится не стране, а флагу, а флаг…\nЗакончите мысль шпионов тремя словами.",  "\n\nА флаг сделан в Китае.", "\n\nА флаг СДЕЛАН…", "ЛЕГКАЯ", "Симпсоны");
        // 6 - 10
        this.InsertDateIntoPuzzleTable(db, "ФРАЗА", "\nСовременная загадка по знаменитой фразе\nфранцузского летчика и писателя.\nЭту фразу слышал каждый...", "Одна из компаний, занимающихся выделенными линиями сети «ИНТЕРНЕТ», решила в своем рекламном слогане показать, что она заботится о своих ранее подключенных клиентах.\nДля этого компания изменила последнее слово в известной фразе известного летчика.\nВоспроизведите получившуюся фразу.", "\n\nМы в ответе за тех, кого подключили.\nКомпания — МТС.", "\n\nАвтор этой (изначальной) фразы французский писатель и опытный лётчик Антуан де Сент-Экзюпери\n(1900-1944)", "ТЯЖЕЛАЯ", "");
        this.InsertDateIntoPuzzleTable(db, "ИГРЫ", "\nИгра́ - вид осмысленной и непродуктивной\nдеятельности, где мотив лежит не в результате её,\n а в самом процессе...", "Название одной из этих игр является уменьшительной формой названия другой, хотя игры эти совершенно разные.\nПервую из них, в которую, наверное, играли многие из вас, можно отнести к жанру интеллектуальных.\nВо вторую некоторые из вас тоже вполне могли играть, хотя в последние годы она заметно утратила былую популярность.\nНазовите обе игры.", "\n\n«ГОРОДА» и «ГОРОДКИ».", "\n\nТелевизионная юмористическая программа,\nвыходившая на ТРК «Петербург» с 17 апреля 1993 года.", "ТЯЖЕЛАЯ", "");
        this.InsertDateIntoPuzzleTable(db, "ТАЙГА", "\nВпервые подробный анализ понятия «ТАЙГА»\n(слово монгольского происхождения)\nдал российский ботаник П. Н. Крылов...", "В одну из экспедиций известного путешественника Арсеньева вместе с ним пошел геолог Гусев.\nОн оказался неприспособленным к жизни в тайге: часто терял ориентировку, отставал от отряда и не имел навыков походной жизни.\nОднажды он, неся алюминиевый котелок, привязал его к котомке так, что крышка болталась и звенела.\nАрсеньев попросил одного из стрелков помочь Гусеву перевязать котелок.\nНо стрелок сказал, что этого делать не следует.\nКак он аргументировал свое мнение?", "\n\nЕсли Гусев снова потеряется, то по звону его будет легко найти.", "\n\nЗАГЛУШКА.", "СРЕДНЯЯ", "");
        this.InsertDateIntoPuzzleTable(db, "НАУКА", "\nНаучные основы этой науки были заложены\nиспытателем ампелографом\nСимоном де Рохас Клементе...", "Однажды Дионис решил сделать подарок своему любимому другу Ампелу и подвесил его\n(не Ампела, конечно, а подарок)\nна высокий вяз.\nАмпел полез на дерево, не удержался, упал и разбился.\nДионис огорчился и назвал несостоявшийся подарок в честь Ампела.\nТеперь скажите, видами и сортами чего занимается наука ампелография?", "\n\nВинограда\n(виноградная лоза называется «АМПЕЛОС»).", "\n\nЗАГЛУШКА", "СРЕДНЯЯ", "");
        this.InsertDateIntoPuzzleTable(db, "ЛЕГЕНДА", "\nЗапасы воды в Байкале гигантские - 23 тыс. км³\n(около 19% от 123 тыс. км³ всех\nмировых запасов озёрной пресной воды)...", "Бурятская легенда рассказывает о старике Байкале и его 336-ти сыновьях.\nА почему их ровно 336?", "\n\nИменно столько рек впадает в Байкал.", "\n\nЗАГЛУШКА", "ЛЕГКАЯ", "");
        // 11 - 15
        this.InsertDateIntoPuzzleTable(db, "ЯД", "\nЯд — вещество, приводящее в дозах,\nдаже небольших относительно массы тела,\nк нарушению жизнедеятельности организма...", "Помимо яда, они содержат большое количество белков, углеводов, витаминов и аминокислот.\nПри должной обработке они даже съедобны.\nСчитается также, что особым образом приготовленная настойка из них помогает при суставном ревматизме.\nЕще они эффективны в борьбе с насекомыми, на что намекает и их название.\nНазовите их.", "\n\n«МУХОМОРЫ».", "\n\nЗАГЛУШКА", "ЛЕГКАЯ", "Яд");
        this.InsertDateIntoPuzzleTable(db, "ДИЕТА", "\nРазработкой и рекомендациями\nдиеты для больного занимается\nдиетология — наука о лечебном питании...", "С недавних пор в Италии стала модной новая диета.\nСуть ее сводится к употреблению продуктов в строго определенной последовательности.\nНачинать есть, например, следует с малины, помидоров или лососины.\nЗатем, после небольшой передышки, можно приналечь на бананы, жареную картошку или булку с маслом.\nА на десерт – зелень, огурцы или плоды киви.\nПо определению из этой диеты выпадают обе «белые смерти» – сахар и соль, а также черная икра и баклажаны.\nКак называют такую диету?", "\n\n«СВЕТОФОР».", "\n\nЗАГЛУШКА", "СРЕДНЯЯ", "Диета");
        this.InsertDateIntoPuzzleTable(db, "ШКОЛЬНИКИ", "\nПервоначально греческое слово\nσχολή  (школа) означало\n«досуг, свободное времяпровождение»...", "Французские школьники используют удобную рифмованную фразу, которая в русском переводе звучит так: «Как однажды Жак-звонарь городской сломал фонарь».\nА что в подобных случаях используют русские школьники?", "\n\n«Каждый охотник желает знать, где сидит фазан»\n– мнемонический ряд цветов.", "\n\nЗАГЛУШКА", "ЛЕГКАЯ", "Школа");
        this.InsertDateIntoPuzzleTable(db, "ИССКУСТВО", "\nВ масштабах всего общества,\nискусство — особый способ познания\nи отражения действительности...", "Этим «ИСКУССТВОМ» может любоваться каждый, независимо от достатка и места жительства.\nНо большинство предпочитает не иметь этого у себя в квартире.\nПо преданию богиня Афина обрекла на вечное занятие этим мастера, чье искусство сравнялось с искусством богини.\nКак мы называем произведения этого «ИСКУССТВА»?", "\n\n«ПАУТИНА».", "\n\nЗАГЛУШКА", "ТЯЖЕЛАЯ", "Искусство");
        this.InsertDateIntoPuzzleTable(db, "ФОРУМ", "\nАрхеоло́гия - историческая дисциплина,\n зучающая прошлое человечества\nпо вещественным источникам...", "Посетитель одного из сайтов по черной археологии заметил на форуме, что профессиональный копатель перед тем, как отправиться на промысел, должен перелопатить горы информации, учесть и просчитать всё, вплоть до погодных условий и природных особенностей местности – иными словами, решить для себя 3 задачи.\nВсе эти задачи оканчивались одним словом – «ИСКАТЬ».\nВоспроизведите начальные слова этих задач.", "\n\nЧто? Где? Когда?\nКомментарий:\nискателю надо определить, что конкретно он ищет, где будет производить поиски и в какое время эти поиски удобнее будет производить", "\n\nПередача такая есть на телевиденье.\nЗнатоки там играют с совой.", "ТЯЖЕЛАЯ", "Археология");
        // 16 - 20
        this.InsertDateIntoPuzzleTable(db, "ЗАГАДКА", "\nА. Эйнштейн придумал эту задачу в\nпрошлом веке и полагал, что 98% жителей\nЗемли не в состоянии ее решить...", "А. Эйнштейн придумал эту задачу в прошлом веке и полагал, что 98% жителей Земли не в состоянии ее решить.\nПринадлежите ли вы к 2% самых умных людей планеты?\nЗдесь нет никакого фокуса, только чистая логика.\n\n1. Есть 5 домов каждый разного цвета.\n2. В каждом доме живет по одному человеку отличной друг от друга национальности.\n3. Каждый жилец пьет только один определенный напиток, курит определенную марку сигарет и держит определенное животное.\n4. Никто из 5 человек не пьет одинаковые с другими напитки, не курит одинаковые сигареты и не держит одинаковое животное.\n\nВопрос: кому принадлежит рыба?", "\n\nХозяин рыбы - немец.", "1. Англичанин живет в красном доме.\n2. Швед держит собаку.\n3. Датчанин пьет чай.\n4. Зеленый дом стоит слева от белого (считайте, что эти дома стоят рядом - иначе в задаче получаются два решения).\n5. Жилец зеленого дома пьет кофе.\n6. Человек, который курит Pall Mall, держит птицу.\n7. Жилец из среднего дома пьет молоко.\n8. Жилец из желтого дома курит Dunhill.\n9. Норвежец живет в первом доме.\n10. Курильщик Marlboro живет около того, кто держит кошку.\n11. Человек, который содержит лошадь, живет около того, кто курит Dunhill.\n12. Курильщик сигарет Winfield пьет пиво.\n13. Норвежец живет около голубого дома.\n14. Немец курит Rothmans.\n15. Курильщик Marlboro живет по соседству с человеком, который пьет воду.", "ТРУЖНАЯ", "Загадка_Эйнштейна");
        this.InsertDateIntoPuzzleTable(db, "УЧИТЕЛЬ", "\nГоловоломка\nхитрого учитель и\nкласса одаренных детей...", "Как-то в одной школе решили устроить проверку, насколько школьники хорошо учатся.\nПроверка состояла в том, что на уроке присутствует комиссия и наблюдает за тем, как школьники отвечают на поставленные учителем вопросы.\nУчитель обязан спрашивать всех, не только отличников.\nВ одном из классов комиссия была поражена тем, что на каждый вопрос учителя абсолютно весь класс тянул руку чтобы ответить. Учитель спрашивал абсолютно разных по успеваемости учиников и все время получал от них верный ответ.\nКак это могло получиться, если учитывать, что класс не был переполнен одаренными учениками?", "\n\nУчитель заранее договорился с учениками о том, что те кто наверняка знают правильный ответ должны тянуть левую руку, те кто не знают или не уверены - правую.\nВ результате, у комиссии возникала иллюзия, что все знают ответ, но учитель знал кого спрашивать и кто наверняка сможет ответить верно", "\n\nВсе кроется в хитрости учителя.", "СРЕДНЯЯ", "Учитель");



        this.InsertDateIntoPuzzleTable(db, "ЭСКИМОСЫ", "ЗАГЛУШКА", "Эскимосы считают, что он бывает жидким, легким, тяжелым, хрупким, блестящим, и еще насчитывают более двухсот видов его.\nО чем идет речь?", "\n\nСнег.", "\n\nЗАГЛУШКА", "*", "Снег");
        this.InsertDateIntoPuzzleTable(db, "ЛЕГЕНДА", "Согласно местной легенде, жители этой маленькой горной европейской страны являются потомками черного орла...", "Согласно местной легенде, жители этой маленькой горной европейской страны, с преимущественно мусульманским населением, являются потомками большого черного орла.\nЧто это за страна, если в переводе ее название означает «Страна орлов»?", "\n\nАлбания.", "\n\nЗАГЛУШКА", "***", "Албания");
        this.InsertDateIntoPuzzleTable(db, "ТОЛПА", "ЗАГЛУШКА", "По-древнегречески толпа — «ОХЛОС».\nА как, согласно одной из версий, древние греки называли отдельно взятого представителя этой толпы?", "\n\nОхломон.", "\n\nЗАГЛУШКА", "*", "Толпа");
        // 21 - 25
        this.InsertDateIntoPuzzleTable(db, "ФОНАРИК", "ЗАГЛУШКА", "Одна из английских фирм выпускает «вечный» электрический фонарик.\nРеклама сообщает, что ему не вредит погружение на глубину до 150 метров, фонарик не поддается коррозии, его невозможно сломать или разбить.\nНа это изделие дается гарантия сроком на всю жизнь владельца.\nОднако в гарантийном талоне есть примечание: «Фирма не отвечает за последствия нападения на фонарик акулы, медведя и…»\nКого?", "\n\nРебенка.", "\n\nЗАГЛУШКА", "*", "");
        this.InsertDateIntoPuzzleTable(db, "ЧИСЛО", "ЗАГЛУШКА", "Редактор журнала «Юность», писатель Борис Полевой, редактируя журнал юность, иногда ставил на полях число 22.\nЧто этим хотел сказать писатель?", "\n\nПеребор.", "\n\nЗАГЛУШКА", "*", "");
        this.InsertDateIntoPuzzleTable(db, "АЛМАЗ", "ЗАГЛУШКА", "Какое правило неукоснительно соблюдается в отношении алмазов, масса которых больше 50 карат?", "\n\nИм дают имена.", "\n\nЗАГЛУШКА", "**", "");
        this.InsertDateIntoPuzzleTable(db, "ПАРАДОКС", "ЗАГЛУШКА", "Есть 3 ящика: «A», «B» и «С», в одном из них приз в других пусто.\nВы выбираете «A». Ведущий точно знает где приз и сперва открывает заведомо неверный вариант «B», показывая, что он пустой.\nПосле чего спрашивает не хотите ли вы поменять свой выбор?\nТеперь  у вас есть возможность остаться при своем варианте «А», либо сменить его на «С».\nСтоит ли менять свой выбор и почему?", "\n\nДа, всегда стоит менять выбор.\nИзначально у Вас 1/3 шансов угадать приз или 33.3%.\nВыбор неправильного ящика составляет 2/3 шансов или 66.7%.\nКогда Вы меняете вариант у Вас становится в два раза больше шансов получить приз.", "\n\nЗАГЛУШКА", "**", "Парадокс_Монти_Холла");
        this.InsertDateIntoPuzzleTable(db, "СЧЕТ", "ЗАГЛУШКА", "Решая эту задачку, постарайтесь все вычисления делать быстро и в уме, ничего не пишите и не используйте калькулятор, и результат вас удивит.\nВозьмите 1000.\nПрибавьте 40.\nПрибавьте еще тысячу.\nПрибавьте 30.\nЕще 1000.\nПлюс 20.\nПлюс 1000.\nИ плюс 10.\nЧто получилось?", "\n\nОтвет 5000?\nНеверно.\nПравильный ответ 4100.\nЕсли не верите, пересчитайте на калькуляторе.", "\n\nЗАГЛУШКА", "**", "");
        // 26 - 30
        this.InsertDateIntoPuzzleTable(db, "ВОЖДЬ", "ЗАГЛУШКА", "Один путешественник был захвачен племенем, вождь которого решил, что тот должен умереть.\nВождь был очень мудрым человеком и дал путешественнику право выбора.\nПутешественник должен был сказать одну фразу.\nЕсли фраза оказывалась правдивой, то его сбрасывали с высокой скалы.\nЕсли она была лживой, то путешественника должны были растерзать львы».\nНо путешественник сказал такую фразу, после которой его отпустили.\nКакую?", "\n\nОн сказал:\n«Меня растерзают львы.\nТеперь, если бы вождь отдал его на растерзание львам, то эта фраза оказалась бы правдивой, и путешественника должны были бы сбросить со скалы.\nНо если его сбросят со скалы, то фраза окажется лживой.\nВождь признал, что единственно правильным решением будет отпустить путешественника.", "\n\nЗАГЛУШКА", "***", "");
        this.InsertDateIntoPuzzleTable(db, "МОНЕТЫ", "ЗАГЛУШКА", "Имеется набор из 1999 монет.\nИзвестно, что 1410 из них - фальшивые.\nФальшивая монета по весу отличается на 1 г от подлинной, причем одни фальшивые монеты могут быть легче, а другие тяжелее подлинных.\nУ нас есть чашечные весы, которые умеют показывать разницу в весе.\nКак за одно взвешивание определить подлинность любой монеты из набора?", "\n\nВзвешиваем все монеты кроме этой и смотрим на разность в весе.\nОбозначим вес нормальной монеты как N, тогда все монеты будут весить либо\n1998 * N + 2x (где 0 =< 705) - в данном случае наша монета настоящая, либо\n1998 * N + (2x - 1) (где 0=<705) - в этом случае наша монета фальшивая.", "\n\nЗАГЛУШКА", "**", "");
        this.InsertDateIntoPuzzleTable(db, "ГОРОДА", "ЗАГЛУШКА", "Есть два близ лежащих города, в одном все лжецы, а в другом правдолюбы.\nИ те и другие приезжают друг к другу в гости.\nКакой нужно поставить единственный вопрос прохожему, что бы узнать в каком вы находитесь городе?", "\n\nНадо спросить:\n«Вы здесь в гостях?»\nЕсли ответ «ДА», то вы в городе лжецов.\nА если ответ «НЕТ», то в городе правдолюбов.", "\n\nЗАГЛУШКА", "**", "");
        this.InsertDateIntoPuzzleTable(db, "НАКАЗАНИЕ", "ЗАГЛУШКА", "В 1926 и 1948 годах Германия была наказана за развязывание войн так же, как когда-то была наказана Спарта.\n\nЧто это за наказание?", "\n\nНемецким спортсменам было запрещено участвовать в Олимпийских играх.", "\n\nЗАГЛУШКА", "*", "");
        this.InsertDateIntoPuzzleTable(db, "КОРОЛЬ", "ЗАГЛУШКА", "Известно, что великий Пеле неоднократно выступал в качестве певца.\nИнтересно, что первая грампластинка с записью песен в его исполнении была выпущена в Советском Союзе.\nБразильские газеты сочли это национальным позором, ударом по престижу национальной журналистики.\nОдна из газет писала:\n«Первую пластинку Короля издали не мы, а русские.\nДа еще и умудрились…».\n\nКакое обстоятельство особо возмутило бразильскую прессу?", "\n\n«Да еще умудрились не заплатить ему ни сентаво».", "\n\nЗАГЛУШКА", "***", "");
        // 31 - 35
        this.InsertDateIntoPuzzleTable(db, "ЧИСЛА", "ОПИСАНИЕ", "Два по 11, четыре по 7 и еще 5, а всего в итоге 6.\n\nО чем речь?", "\n\nПонедельник 11 букв.\nВторник 7 букв.\nСреда 5 букв.\nЧетверг 7 букв.\nПятница 7 букв.\nСуббота 7 букв.\nВоскресенье 11 букв.\nПолучаем неделя - 6 букв.", "\n\nПОДСКАЗКА", "**", "");
        this.InsertDateIntoPuzzleTable(db, "СЛОВО", "ОПИСАНИЕ", "Английский ученый-психолог Дэвид Льюис утверждает, что это безопасно лишь для женщин, тогда как для мужчин может стать источником опасных болезней.\nПроведенные исследования показали, что лишь у четверти женщин наблюдались какие-либо незначительные отклонения, например, сердцебиение.\nМужчины же, наоборот, крайне отрицательно отреагировали на это: у них участился пульс, стала проявляться аритмия, резко подскочило кровяное давление.\n\nНазовите это английским словом, которое относительно недавно проникло и в русский язык.", "\n\n«ШОПИНГ».", "\n\nПОДСКАЗКА", "*", "");





        //this.InsertDateIntoPuzzleTable(db, "НАЗВАНИЕ", "ОПИСАНИЕ", "ВОПРОС", "\n\nОТВЕТ", "\n\nПОДСКАЗКА", "*", "");

    }









    // update state value
    public void UpdateSettingsState(SQLiteDatabase db, long id, String state) {
        contentValues = new ContentValues();
        contentValues.put(COLUMN_SETTINGS_LASTINDEX, state);

        db.update(TABLE_NAME_SETTINGS, contentValues, "id=" + id, null);
    }

    public void UpdateSettingsRate(SQLiteDatabase db, long id, String state) {
        contentValues = new ContentValues();
        contentValues.put(COLUMN_SETTINGS_ISRATE, state);

        db.update(TABLE_NAME_SETTINGS, contentValues, "id=" + id, null);
    }

    public void UpdateSettingsToNextRate(SQLiteDatabase db, long id, String state) {
        contentValues = new ContentValues();
        contentValues.put(COLUMN_SETTINGS_TONEXTRATE, state);

        db.update(TABLE_NAME_SETTINGS, contentValues, "id=" + id, null);
    }

    public void UpdateSettingsAppStart(SQLiteDatabase db, long id, String state) {
        contentValues = new ContentValues();
        contentValues.put(COLUMN_SETTINGS_APPSTART, state);

        db.update(TABLE_NAME_SETTINGS, contentValues, "id=" + id, null);
    }

    public void UpdateSettingsToUp(SQLiteDatabase db, long id, boolean toUp) {
        contentValues = new ContentValues();

        if (toUp) contentValues.put(COLUMN_SETTINGS_TOUP, "true");
        else contentValues.put(COLUMN_SETTINGS_TOUP, "false");

        db.update(TABLE_NAME_SETTINGS, contentValues, "id=" + id, null);
    }

    public void UpdateSettingsDoubleClick(SQLiteDatabase db, long id, boolean doubleClick) {
        contentValues = new ContentValues();

        if (doubleClick) contentValues.put(COLUMN_SETTINGS_DOUBLECLICK, "true");
        else contentValues.put(COLUMN_SETTINGS_DOUBLECLICK, "false");

        db.update(TABLE_NAME_SETTINGS, contentValues, "id=" + id, null);
    }

    public void UpdateSettingsMoveToLast(SQLiteDatabase db, long id, boolean moveToLast) {
        contentValues = new ContentValues();

        if (moveToLast) contentValues.put(COLUMN_SETTINGS_MOVETOLAST, "true");
        else contentValues.put(COLUMN_SETTINGS_MOVETOLAST, "false");

        db.update(TABLE_NAME_SETTINGS, contentValues, "id=" + id, null);
    }

    public void UpdateSettingsTextAlign(SQLiteDatabase db, long id, boolean textAlign) {
        contentValues = new ContentValues();

        if (textAlign) contentValues.put(COLUMN_SETTINGS_TEXTALIGN, "true");
        else contentValues.put(COLUMN_SETTINGS_TEXTALIGN, "false");

        db.update(TABLE_NAME_SETTINGS, contentValues, "id=" + id, null);
    }






    public void UpdatePuzzleFavorite(SQLiteDatabase db, long id, String state) {
        contentValues = new ContentValues();
        contentValues.put(COLUMN_FAVORITE_FAVORITE, state);
        db.update(TABLE_NAME_FAVORITE, contentValues, "id=" + id, null);
    }


    public void UpdatePuzzleState(SQLiteDatabase db, long id, String state) {
        contentValues = new ContentValues();
        contentValues.put(COLUMN_FAVORITE_STATE, state);
        db.update(TABLE_NAME_FAVORITE, contentValues, "id=" + id, null);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PUZZLE);

        db.execSQL("CREATE TABLE "
                + TABLE_NAME_PUZZLE     + " ("
                + COLUMN_ID             + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_HEADER         + " TEXT, "
                + COLUMN_DESCRIPTION    + " TEXT, "
                + COLUMN_TEXT           + " TEXT, "
                + COLUMN_ADVICE         + " TEXT, "
                + COLUMN_ANSWER         + " TEXT, "
                + COLUMN_COMPLEXITY     + " TEXT, "
                + COLUMN_WIKI           + " TEXT"
                + ");");
    }

    //--------------------------------------------------------------------------------------------//
    public void DropSettingsTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SETTINGS);

        db.execSQL("CREATE TABLE "
                + TABLE_NAME_SETTINGS           + " ("
                + COLUMN_SETTINGS_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SETTINGS_LASTINDEX     + " INTEGER, "
                + COLUMN_SETTINGS_APPSTART      + " INTEGER, "
                + COLUMN_SETTINGS_DBINDEX       + " INTEGER, "
                + COLUMN_SETTINGS_TOUP          + " TEXT, "
                + COLUMN_SETTINGS_DOUBLECLICK   + " TEXT, "
                + COLUMN_SETTINGS_MOVETOLAST    + " TEXT, "
                + COLUMN_SETTINGS_ISRATE        + " TEXT, "
                + COLUMN_SETTINGS_TONEXTRATE    + " INTEGER, "
                + COLUMN_SETTINGS_TEXTALIGN     + " TEXT"
                + ");");

        InitializationSettingsTable(db);
    }
    //--------------------------------------------------------------------------------------------//
    public void DropFavoriteTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAVORITE);

        db.execSQL("CREATE TABLE "
                + TABLE_NAME_FAVORITE       + " ("
                + COLUMN_FAVORITE_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FAVORITE_FAVORITE  + " TEXT, "
                + COLUMN_FAVORITE_STATE     + " TEXT"
                + ");");

        InitializationFavoriteTable(db);
    }
    //--------------------------------------------------------------------------------------------//
    public void DropPuzzleTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PUZZLE);

        db.execSQL("CREATE TABLE "
                + TABLE_NAME_PUZZLE     + " ("
                + COLUMN_ID             + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_HEADER         + " TEXT, "
                + COLUMN_DESCRIPTION    + " TEXT, "
                + COLUMN_TEXT           + " TEXT, "
                + COLUMN_ADVICE         + " TEXT, "
                + COLUMN_ANSWER         + " TEXT, "
                + COLUMN_COMPLEXITY     + " TEXT, "
                + COLUMN_WIKI           + " TEXT"
                + ");");

        InitializationPuzzleTable(db);
    }
    //--------------------------------------------------------------------------------------------//




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + TABLE_NAME_PUZZLE     + " ("
                + COLUMN_ID             + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_HEADER         + " TEXT, "
                + COLUMN_DESCRIPTION    + " TEXT, "
                + COLUMN_TEXT           + " TEXT, "
                + COLUMN_ADVICE         + " TEXT, "
                + COLUMN_ANSWER         + " TEXT, "
                + COLUMN_COMPLEXITY     + " TEXT, "
                + COLUMN_WIKI           + " TEXT"
                + ");");

        db.execSQL("CREATE TABLE "
                + TABLE_NAME_SETTINGS           + " ("
                + COLUMN_SETTINGS_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SETTINGS_LASTINDEX     + " INTEGER, "
                + COLUMN_SETTINGS_APPSTART      + " INTEGER, "
                + COLUMN_SETTINGS_DBINDEX       + " INTEGER, "
                + COLUMN_SETTINGS_TOUP          + " TEXT, "
                + COLUMN_SETTINGS_DOUBLECLICK   + " TEXT, "
                + COLUMN_SETTINGS_MOVETOLAST    + " TEXT, "
                + COLUMN_SETTINGS_ISRATE        + " TEXT, "
                + COLUMN_SETTINGS_TONEXTRATE    + " INTEGER, "
                + COLUMN_SETTINGS_TEXTALIGN     + " TEXT"
                + ");");

        db.execSQL("CREATE TABLE "
                + TABLE_NAME_FAVORITE       + " ("
                + COLUMN_FAVORITE_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FAVORITE_FAVORITE  + " TEXT, "
                + COLUMN_FAVORITE_STATE     + " TEXT"
                + ");");

        this.InitializationPuzzleTable(db);
        this.InitializationSettingsTable(db);
        this.InitializationFavoriteTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PUZZLE);

            db.execSQL("UPDATE " + TABLE_NAME_SETTINGS + " SET " + COLUMN_SETTINGS_DBINDEX + " = " + newVersion + ";");

            db.execSQL("CREATE TABLE "
                    + TABLE_NAME_PUZZLE     + " ("
                    + COLUMN_ID             + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_HEADER         + " TEXT, "
                    + COLUMN_DESCRIPTION    + " TEXT, "
                    + COLUMN_TEXT           + " TEXT, "
                    + COLUMN_ADVICE         + " TEXT, "
                    + COLUMN_ANSWER         + " TEXT, "
                    + COLUMN_COMPLEXITY     + " TEXT, "
                    + COLUMN_WIKI           + " TEXT"
                    + ");");
            this.InitializationPuzzleTable(db);
        }
    }

}
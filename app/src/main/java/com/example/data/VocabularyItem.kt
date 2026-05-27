package com.example.data

data class VocabularyItem(
    val id: String,
    val dutch: String,
    val macedonian: String,
    val macedonianCyrillic: String,
    val pronunciation: String,
    val category: String,
    val emoji: String,
    val themeColorHex: Long,
    val imagePath: String = "",
    val audioPath: String = ""
)

object Vocabulary {
    val items = listOf(
        // FAMILY CATEGORY (Gezin)
        VocabularyItem(
            id = "mama",
            dutch = "Mama",
            macedonian = "Majka",
            macedonianCyrillic = "Мајка",
            pronunciation = "MAYE-kah",
            category = "Gezin",
            emoji = "👩",
            themeColorHex = 0xFFFFE5EC
        ),
        VocabularyItem(
            id = "papa",
            dutch = "Papa",
            macedonian = "Tatko",
            macedonianCyrillic = "Татко",
            pronunciation = "TAT-ko",
            category = "Gezin",
            emoji = "👨",
            themeColorHex = 0xFFE3F2FD
        ),
        VocabularyItem(
            id = "baby",
            dutch = "Baby",
            macedonian = "Bebe",
            macedonianCyrillic = "Бебе",
            pronunciation = "BEH-beh",
            category = "Gezin",
            emoji = "👶",
            themeColorHex = 0xFFF3E5F5
        ),
        VocabularyItem(
            id = "oma",
            dutch = "Oma",
            macedonian = "Baba",
            macedonianCyrillic = "Баба",
            pronunciation = "BAH-bah",
            category = "Gezin",
            emoji = "👵",
            themeColorHex = 0xFFE8F5E9
        ),

        // KITCHEN CATEGORY (Keuken)
        VocabularyItem(
            id = "fork",
            dutch = "Vork",
            macedonian = "Vilushka",
            macedonianCyrillic = "Вилушка",
            pronunciation = "vee-LOOSH-kah",
            category = "Keuken",
            emoji = "🍴",
            themeColorHex = 0xFFECEFF1
        ),
        VocabularyItem(
            id = "spoon",
            dutch = "Lepel",
            macedonian = "Lazhica",
            macedonianCyrillic = "Лажица",
            pronunciation = "LAH-zheet-sah",
            category = "Keuken",
            emoji = "🥄",
            themeColorHex = 0xFFFFFDE3
        ),
        VocabularyItem(
            id = "glass",
            dutch = "Glas",
            macedonian = "Chasha",
            macedonianCyrillic = "Чаша",
            pronunciation = "CHAH-shah",
            category = "Keuken",
            emoji = "🥛",
            themeColorHex = 0xFFE0F7FA
        ),
        VocabularyItem(
            id = "knife",
            dutch = "Mes",
            macedonian = "Nozh",
            macedonianCyrillic = "Нож",
            pronunciation = "NOZH",
            category = "Keuken",
            emoji = "🔪",
            themeColorHex = 0xFFFFEBEE
        ),

        // VACATION CATEGORY (Vakantie)
        VocabularyItem(
            id = "beach",
            dutch = "Strand",
            macedonian = "Plazha",
            macedonianCyrillic = "Плажа",
            pronunciation = "PLAH-zhah",
            category = "Vakantie",
            emoji = "🏖️",
            themeColorHex = 0xFFFFF8E1
        ),
        VocabularyItem(
            id = "sea",
            dutch = "Zee",
            macedonian = "More",
            macedonianCyrillic = "Море",
            pronunciation = "MO-reh",
            category = "Vakantie",
            emoji = "🌊",
            themeColorHex = 0xFFE0F3F8
        ),
        VocabularyItem(
            id = "icecream",
            dutch = "IJsje",
            macedonian = "Sladoled",
            macedonianCyrillic = "Сладолед",
            pronunciation = "SLAH-do-led",
            category = "Vakantie",
            emoji = "🍦",
            themeColorHex = 0xFFFFF3E0
        ),
        VocabularyItem(
            id = "ball",
            dutch = "Bal",
            macedonian = "Topka",
            macedonianCyrillic = "Топка",
            pronunciation = "TOP-kah",
            category = "Vakantie",
            emoji = "⚽",
            themeColorHex = 0xFFE8EAF6
        ),
        // NUMBERS CATEGORY (Getallen)
        VocabularyItem(
            id = "num_1",
            dutch = "Een",
            macedonian = "Eden",
            macedonianCyrillic = "Еден",
            pronunciation = "EY-den",
            category = "Getallen",
            emoji = "1️⃣",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_2",
            dutch = "Twee",
            macedonian = "Dva",
            macedonianCyrillic = "Два",
            pronunciation = "DVAH",
            category = "Getallen",
            emoji = "2️⃣",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_3",
            dutch = "Drie",
            macedonian = "Tri",
            macedonianCyrillic = "Три",
            pronunciation = "TREE",
            category = "Getallen",
            emoji = "3️⃣",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_4",
            dutch = "Vier",
            macedonian = "Chetiri",
            macedonianCyrillic = "Четири",
            pronunciation = "CHEH-tee-ree",
            category = "Getallen",
            emoji = "4️⃣",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_5",
            dutch = "Vijf",
            macedonian = "Pet",
            macedonianCyrillic = "Пет",
            pronunciation = "PET",
            category = "Getallen",
            emoji = "5️⃣",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_6",
            dutch = "Zes",
            macedonian = "Shest",
            macedonianCyrillic = "Шест",
            pronunciation = "SHEST",
            category = "Getallen",
            emoji = "6️⃣",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_7",
            dutch = "Zeven",
            macedonian = "Sedum",
            macedonianCyrillic = "Седум",
            pronunciation = "SEH-doom",
            category = "Getallen",
            emoji = "7️⃣",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_8",
            dutch = "Acht",
            macedonian = "Osum",
            macedonianCyrillic = "Осум",
            pronunciation = "OH-soom",
            category = "Getallen",
            emoji = "8️⃣",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_9",
            dutch = "Negen",
            macedonian = "Devet",
            macedonianCyrillic = "Девет",
            pronunciation = "DEH-vet",
            category = "Getallen",
            emoji = "9️⃣",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_10",
            dutch = "Tien",
            macedonian = "Deset",
            macedonianCyrillic = "Десет",
            pronunciation = "DEH-set",
            category = "Getallen",
            emoji = "🔟",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_11",
            dutch = "Elf",
            macedonian = "Edinaeset",
            macedonianCyrillic = "Единаесет",
            pronunciation = "eh-dee-NAY-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_12",
            dutch = "Twaalf",
            macedonian = "Dvanaeset",
            macedonianCyrillic = "Дванаесет",
            pronunciation = "dvah-NAY-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_13",
            dutch = "Dertien",
            macedonian = "Trinaeset",
            macedonianCyrillic = "Тринаесет",
            pronunciation = "tree-NAY-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_14",
            dutch = "Veertien",
            macedonian = "Chetirinaeset",
            macedonianCyrillic = "Четиринаесет",
            pronunciation = "cheh-tee-ree-NAY-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_15",
            dutch = "Vijftien",
            macedonian = "Petnaeset",
            macedonianCyrillic = "Петнаесет",
            pronunciation = "pet-NAY-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_16",
            dutch = "Zestien",
            macedonian = "Shesnaeset",
            macedonianCyrillic = "Шеснаесет",
            pronunciation = "shes-NAY-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_17",
            dutch = "Zeventien",
            macedonian = "Sedumnaeset",
            macedonianCyrillic = "Седумнаесет",
            pronunciation = "seh-doom-NAY-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_18",
            dutch = "Achttien",
            macedonian = "Osumnaeset",
            macedonianCyrillic = "Осумнаесет",
            pronunciation = "oh-soom-NAY-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_19",
            dutch = "Negentien",
            macedonian = "Devetnaeset",
            macedonianCyrillic = "Деветнаесет",
            pronunciation = "deh-vet-NAY-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_20",
            dutch = "Twintig",
            macedonian = "Dvaeset",
            macedonianCyrillic = "Дваесет",
            pronunciation = "DVAH-eset",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_30",
            dutch = "Dertig",
            macedonian = "Trieset",
            macedonianCyrillic = "Триесет",
            pronunciation = "TREE-eset",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_40",
            dutch = "Veertig",
            macedonian = "Chetirieset",
            macedonianCyrillic = "Четириесет",
            pronunciation = "cheh-TEE-ree-eset",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_50",
            dutch = "Vijftig",
            macedonian = "Pedeset",
            macedonianCyrillic = "Педесет",
            pronunciation = "PEH-deh-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_60",
            dutch = "Zestig",
            macedonian = "Sheeset",
            macedonianCyrillic = "Шеесет",
            pronunciation = "SHEH-eset",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_70",
            dutch = "Zeventig",
            macedonian = "Sedumdeset",
            macedonianCyrillic = "Седумдесет",
            pronunciation = "seh-DOOM-deh-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_80",
            dutch = "Achttig",
            macedonian = "Osumdeset",
            macedonianCyrillic = "Осумдесет",
            pronunciation = "oh-SOOM-deh-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_90",
            dutch = "Negentig",
            macedonian = "Devedeset",
            macedonianCyrillic = "Деведесет",
            pronunciation = "deh-VEH-deh-set",
            category = "Getallen",
            emoji = "🔢",
            themeColorHex = 0xFFE1BEE7
        ),
        VocabularyItem(
            id = "num_100",
            dutch = "Honderd",
            macedonian = "Sto",
            macedonianCyrillic = "Сто",
            pronunciation = "STOH",
            category = "Getallen",
            emoji = "💯",
            themeColorHex = 0xFFE1BEE7
        )
    )
}

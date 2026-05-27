package com.example.data

data class VocabularyItem(
    val id: String,
    val dutch: String,
    val macedonian: String,
    val macedonianCyrillic: String,
    val pronunciation: String,
    val category: String,
    val emoji: String,
    val themeColorHex: Long
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
        )
    )
}

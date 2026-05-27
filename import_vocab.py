#!/usr/bin/env python3
import os
import sys
import json
import sqlite3

# The Room Identity Hash verified by version 2 of AppDatabase in your Kotlin code
ROOM_IDENTITY_HASH = "5229a05d1509ecfbe49993e863926a12"

def parse_color(color_val):
    """
    Safely converts hex string (e.g. '0xFFFFD180', '#FFE5EC', or 'ECEFF1') or int to SQLite integer.
    """
    if color_val is None:
        return 0xFFCCCCCC # Default neutral grey
    if isinstance(color_val, int):
        return color_val
    try:
        color_str = str(color_val).strip()
        if color_str.lower().startswith("0x"):
            return int(color_str, 16)
        if color_str.startswith("#"):
            color_str = color_str[1:]
        if len(color_str) == 6:
            color_str = "FF" + color_str # Add alpha FF
        return int(color_str, 16)
    except Exception as e:
        print(f"Warning: could not parse color value '{color_val}', defaulting. Error: {e}")
        return 0xFFCCCCCC

def main():
    json_path = "import_vocabulary.json"
    db_out_dir = "app/src/main/assets/database"
    db_name = "vocabulary.db"

    # If being run locally on user computer, default to outputting in the current directory
    if not os.path.exists("app/src/main"):
        db_out_dir = "."
        print("Note: App directories not found. Running in standalone local mode. Database will be saved in current folder.")

    db_path = os.path.join(db_out_dir, db_name)

    print("=============================================")
    print("      Matching Game SQLite Database Seeder   ")
    print("      Dutch to Macedonian Language Schema    ")
    print("=============================================")

    # Load JSON data
    if not os.path.exists(json_path):
        print(f"Error: Could not find '{json_path}'. Please place your flat vocabulary JSON beside this script.")
        create_default_json(json_path)
        print(f"I generated a template '{json_path}' for you so you can modify it right away.")

    with open(json_path, 'r', encoding='utf-8') as f:
        try:
            data = json.load(f)
        except Exception as e:
            print(f"Error parsing JSON: {e}")
            sys.exit(1)

    # Ensure output asset directory exists
    if db_out_dir != "." and not os.path.exists(db_out_dir):
        os.makedirs(db_out_dir, exist_ok=True)
        print(f"Created assets database directory: {db_out_dir}")

    # Remove existing db to start fresh
    if os.path.exists(db_path):
        try:
            os.remove(db_path)
        except OSError as e:
            print(f"Could not overwrite existing db at {db_path}: {e}")

    # Connect to SQLite
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()

    try:
        # Enable Foreign Keys
        cursor.execute("PRAGMA foreign_keys = ON;")

        print("Creating Room-compatible database schema...")

        # 1. progress_record
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS `progress_record` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `timestamp` INTEGER NOT NULL, 
                `dutchWord` TEXT NOT NULL, 
                `macedonianWord` TEXT NOT NULL, 
                `isCorrect` INTEGER NOT NULL, 
                `starsEarned` INTEGER NOT NULL
            );
        """)

        # 2. categories
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS `categories` (
                `id` TEXT NOT NULL, 
                `emoji` TEXT NOT NULL, 
                `defaultName` TEXT NOT NULL, 
                `description` TEXT NOT NULL, 
                `themeColorHex` INTEGER NOT NULL, 
                PRIMARY KEY(`id`)
            );
        """)

        # 3. words
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS `words` (
                `id` TEXT NOT NULL, 
                `categoryId` TEXT NOT NULL, 
                `imagePath` TEXT NOT NULL, 
                `emoji` TEXT NOT NULL, 
                `themeColorHex` INTEGER NOT NULL, 
                PRIMARY KEY(`id`), 
                FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
            );
        """)

        # 4. languages
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS `languages` (
                `code` TEXT NOT NULL, 
                `name` TEXT NOT NULL, 
                PRIMARY KEY(`code`)
            );
        """)

        # 5. translations
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS `translations` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `wordId` TEXT NOT NULL, 
                `languageCode` TEXT NOT NULL, 
                `translationValue` TEXT NOT NULL, 
                `pronunciationText` TEXT NOT NULL, 
                `audioPath` TEXT NOT NULL, 
                FOREIGN KEY(`wordId`) REFERENCES `words`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, 
                FOREIGN KEY(`languageCode`) REFERENCES `languages`(`code`) ON UPDATE NO ACTION ON DELETE CASCADE
            );
        """)
        # Index for translations
        cursor.execute("CREATE UNIQUE INDEX IF NOT EXISTS `index_translations_wordId_languageCode` ON `translations` (`wordId`, `languageCode`);")

        # 6. category_translations
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS `category_translations` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `categoryId` TEXT NOT NULL, 
                `languageCode` TEXT NOT NULL, 
                `name` TEXT NOT NULL, 
                FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, 
                FOREIGN KEY(`languageCode`) REFERENCES `languages`(`code`) ON UPDATE NO ACTION ON DELETE CASCADE
            );
        """)
        # Index for category_translations
        cursor.execute("CREATE UNIQUE INDEX IF NOT EXISTS `index_category_translations_categoryId_languageCode` ON `category_translations` (`categoryId`, `languageCode`);")

        # 7. room_master_table
        cursor.execute("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT);")
        cursor.execute("INSERT OR REPLACE INTO room_master_table (id, identity_hash) VALUES(42, ?);", (ROOM_IDENTITY_HASH,))

        print("Inserting supported languages for Dutch-to-Macedonian (with Cyrillic and Latin transcription)...")
        languages = [
            ("NL", "Nederlands"),
            ("MK", "Makedonski (Latin)"),
            ("MK_CYR", "Македонски (Кирилица)")
        ]
        cursor.executemany("INSERT INTO languages (code, name) VALUES(?, ?);", languages)

        # Process categories
        categories_data = data.get("categories", [])
        print(f"Importing {len(categories_data)} categories...")
        for cat in categories_data:
            cat_id = cat.get("id")
            emoji = cat.get("emoji", "📂")
            default_name = cat.get("defaultName", cat_id)
            description = cat.get("description", "")
            theme_color = parse_color(cat.get("themeColorHex"))

            cursor.execute("""
                INSERT INTO categories (id, emoji, defaultName, description, themeColorHex)
                VALUES (?, ?, ?, ?, ?);
            """, (cat_id, emoji, default_name, description, theme_color))

            # Add default translation
            cursor.execute("""
                INSERT INTO category_translations (categoryId, languageCode, name)
                VALUES (?, 'NL', ?);
            """, (cat_id, default_name))

        # Process words flat structure
        words_data = data.get("words", [])
        print(f"Importing {len(words_data)} flat Macedonian words...")
        for word in words_data:
            word_id = word.get("id")
            category_id = word.get("categoryId")
            image_path = word.get("imagePath", word_id)
            emoji = word.get("emoji", "🌟")
            theme_color = parse_color(word.get("themeColorHex"))

            # Simple direct language mappings from JSON
            dutch_value = word.get("dutch", "")
            macedonian_cyrillic = word.get("macedonian", "")
            macedonian_latin = word.get("macedonianLatin", "")
            pron_text = word.get("pronunciation", "")
            audio_path = word.get("audioPath", f"sound/{word_id}.mp3")

            # 1. Insert words row
            cursor.execute("""
                INSERT INTO words (id, categoryId, imagePath, emoji, themeColorHex)
                VALUES (?, ?, ?, ?, ?);
            """, (word_id, category_id, image_path, emoji, theme_color))

            # 2. Insert NL, MK, and MK_CYR translations automatically into the DB
            translations = [
                (word_id, "NL", dutch_value, "", ""),
                (word_id, "MK", macedonian_latin, "", ""),
                (word_id, "MK_CYR", macedonian_cyrillic, pron_text, audio_path)
            ]

            for w_id, lang_code, val, pron, audio in translations:
                cursor.execute("""
                    INSERT INTO translations (wordId, languageCode, translationValue, pronunciationText, audioPath)
                    VALUES (?, ?, ?, ?, ?);
                """, (w_id, lang_code, val, pron, audio))

        conn.commit()
        print("---------------------------------------------")
        print(f"🎉 SUCCESS! Database built successfully.")
        print(f"📁 Database Location: {os.path.abspath(db_path)}")
        print("---------------------------------------------")
        print("Standalone Instructions:")
        if db_out_dir != ".":
            print("1. Done! Your local assets database is fully seeded of your new simplified vocabulary items.")
        else:
            print("1. Copy the generated 'vocabulary.db' to 'app/src/main/assets/database/vocabulary.db' in your Android Studio project.")
            print("2. Compile and launch your app!")

    except sqlite3.Error as e:
        conn.rollback()
        print(f"Database error during migration: {e}")
        sys.exit(1)
    finally:
        conn.close()

def create_default_json(path):
    default_data = {
        "categories": [
            {
                "id": "Gezin",
                "emoji": "👨‍👩‍👦",
                "defaultName": "Gezin",
                "description": "Mama, Papa, Oma, Baby...",
                "themeColorHex": "0xFFFFD180"
            },
            {
                "id": "Keuken",
                "emoji": "🍳",
                "defaultName": "Keuken",
                "description": "Vork, Lepel, Glas, Mes...",
                "themeColorHex": "0xFFFFF59D"
            },
            {
                "id": "Vakantie",
                "emoji": "🏖️",
                "defaultName": "Vakantie",
                "description": "Strand, Zee, IJsje, Bal...",
                "themeColorHex": "0xFF80DEEA"
            }
        ],
        "words": [
            {
                "id": "mama",
                "categoryId": "Gezin",
                "emoji": "👩",
                "themeColorHex": "0xFFFFE5EC",
                "dutch": "Mama",
                "macedonian": "Мајка",
                "macedonianLatin": "Majka",
                "pronunciation": "MAYE-kah",
                "audioPath": "sound/mama.mp3"
            },
            {
                "id": "papa",
                "categoryId": "Gezin",
                "emoji": "👨",
                "themeColorHex": "0xFFE3F2FD",
                "dutch": "Papa",
                "macedonian": "Татко",
                "macedonianLatin": "Tatko",
                "pronunciation": "TAT-ko",
                "audioPath": "sound/papa.mp3"
            },
            {
                "id": "baby",
                "categoryId": "Gezin",
                "emoji": "👶",
                "themeColorHex": "0xFFF3E5F5",
                "dutch": "Baby",
                "macedonian": "Бебе",
                "macedonianLatin": "Bebe",
                "pronunciation": "BEH-beh",
                "audioPath": "sound/baby.mp3"
            },
            {
                "id": "oma",
                "categoryId": "Gezin",
                "emoji": "👵",
                "themeColorHex": "0xFFE8F5E9",
                "dutch": "Oma",
                "macedonian": "Баба",
                "macedonianLatin": "Baba",
                "pronunciation": "BAH-bah",
                "audioPath": "sound/oma.mp3"
            }
        ]
    }
    with open(path, 'w', encoding='utf-8') as f:
        json.dump(default_data, f, indent=4, ensure_ascii=False)

if __name__ == "__main__":
    main()

<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://ai.google.dev/static/site-assets/images/share-ais-513315318.png" />
</div>

# Run and deploy your AI Studio app

This contains everything you need to run your app locally.

View your app in AI Studio: https://ai.studio/apps/4d42e615-3e11-4289-8c5b-748dd9ad2627

## Run Locally

**Prerequisites:**  [Android Studio](https://developer.android.com/studio)


1. Open Android Studio
2. Select **Open** and choose the directory containing this project
3. Allow Android Studio to fix any incompatibilities as it imports the project.
4. Create a file named `.env` in the project directory and set `GEMINI_API_KEY` in that file to your Gemini API key (see `.env.example` for an example)
5. Remove this line from the app's `build.gradle.kts` file: `signingConfig = signingConfigs.getByName("debugConfig")`
6. Run the app on an emulator or physical device

## How to Recreate & Roll Out Offline Database
To load your offline-created database, follow these three steps:
# Step 1: Create Your SQLite Database File
Using any SQLite editor (such as DB Browser for SQLite or the SQLite Command Line Interface), create a database file named vocabulary.db and execute the following SQL DDL schemas:
```-- 1. Languages Table
CREATE TABLE IF NOT EXISTS `languages` (
    `code` TEXT NOT NULL, 
    `name` TEXT NOT NULL, 
    PRIMARY KEY(`code`)
);

-- 2. Categories Table
CREATE TABLE IF NOT EXISTS `categories` (
    `id` TEXT NOT NULL, 
    `emoji` TEXT NOT NULL, 
    `defaultName` TEXT NOT NULL, 
    `description` TEXT NOT NULL, 
    `themeColorHex` INTEGER NOT NULL, 
    PRIMARY KEY(`id`)
);

-- 3. Category Translations Table (Unique Localization)
CREATE TABLE IF NOT EXISTS `category_translations` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
    `categoryId` TEXT NOT NULL, 
    `languageCode` TEXT NOT NULL, 
    `name` TEXT NOT NULL, 
    FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, 
    FOREIGN KEY(`languageCode`) REFERENCES `languages`(`code`) ON UPDATE NO ACTION ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS `index_category_translations_categoryId_languageCode` 
ON `category_translations` (`categoryId`, `languageCode`);

-- 4. Words Table
CREATE TABLE IF NOT EXISTS `words` (
    `id` TEXT NOT NULL, 
    `categoryId` TEXT NOT NULL, 
    `imagePath` TEXT NOT NULL, 
    `emoji` TEXT NOT NULL, 
    `themeColorHex` INTEGER NOT NULL, 
    PRIMARY KEY(`id`), 
    FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

-- 5. Translations Table (Unique Words & Pronunciation Texts)
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
CREATE UNIQUE INDEX IF NOT EXISTS `index_translations_wordId_languageCode` 
ON `translations` (`wordId`, `languageCode`);

-- 6. Child Play Session Progress Logs Table
CREATE TABLE IF NOT EXISTS `progress_record` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
    `timestamp` INTEGER NOT NULL, 
    `dutchWord` TEXT NOT NULL, 
    `macedonianWord` TEXT NOT NULL, 
    `isCorrect` INTEGER NOT NULL, 
    `starsEarned` INTEGER NOT NULL
);```

;
Step 2: Bundle It in Your Core Assets
Under your Android app project directory, create a folder nested structure:
app/src/main/assets/database/
Save your created vocabulary.db file directly inside that folder.
Step 3: Pushing Updates
When you modify or add any words or categories on your computer:
Update your SQLite database file (vocabulary.db).
Replace the database file in assets/database/.
Open com/example/data/AppDatabase.kt and increment the @Database(version = X) parameter by an integer (e.g., from 2 to 3 or higher).
Compile the updated APK on your computer! Room's built-in migration handler will automatically identify the update, clear out the stale configuration copy, and populate the new asset database on the child's device.

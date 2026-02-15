# CrashShop
### High-Performance Economy Solution for Modern Servers

[![CodeRefactor](https://img.shields.io/badge/CodeRefactor-A%2B-success?style=for-the-badge)](https://www.codefactor.io/repository/github/crashwho/crashshop) ![Build Passing](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge) ![Version](https://img.shields.io/badge/Version-1.0.0-blue?style=for-the-badge)

**CrashShop** is not just another shop plugin; it is an economy solution designed for server owners who demand stability. Built with strict adherence to clean code principles, this plugin ensures your players can buy, and sell items without ever compromising your server's TPS.

---

## 📸 Visual Showcase

See **CrashShop** in action. The interface is designed to be intuitive and clean.

### 🎥 Main Menu
![Main Shop](https://github.com/user-attachments/assets/fa24ffb6-b568-4ec3-bc6a-f70184f7773d)
*A smooth, lag-free shopping experience even under heavy load.*

### 🖼️ GUI Screenshots
| **Category View** | **Category View** |
|:---:|:---:|
| ![Category View](https://github.com/user-attachments/assets/075b1d04-31d7-47fc-b30f-bab77ada4d42) | ![Category View](https://github.com/user-attachments/assets/42277027-d830-426c-ae26-f9a18a92f624) |
| *Clean layout with customizable icons* | *Easy navigation for players* |

---

## 🏆 Why Choose Crash Shop?

In an ecosystem flooded with unoptimized code, **Crash Shop** stands apart.

### 🛡️ Audited & Certified: A+ Rating
I take performance seriously. This plugin has been rigorously audited by **CodeRefactor**, achieving a rare **A+ Rating**.

**What does this mean for your server?**
* **Zero Lag Spikes:** All transactions are handled perfectly.
* **Memory Efficient:** Optimized garbage collection and no memory leaks, ensuring stability during long uptimes.
* **Clean Architecture:** "Spaghetti code" makes updates dangerous. This clean architecture ensures seamless updates and high maintainability.
* **Scalability:** Whether you have 10 players or 1,000, the plugin performs with the same efficiency.

---

## ✨ Key Features

**CrashShop** combines robust backend performance with a user-friendly frontend experience.

* **⚡ Lightweight & Fast:** Optimized to have a negligible footprint on your CPU and RAM.
* **🎨 Fully Customizable GUIs:** Create immersive menu layouts. Every icon, name, and lore is editable.
* **🔗 Vault Integration:** Seamless support for all major economy providers (EssentialsX, CMI, etc.).
* **🌍 Translatable:** 100% of messages and menu items can be translated to fit your community's language.

---

## 📥 Installation

Follow these steps to get your economy running in minutes:

1.  **Download:** Get the latest `.jar` file from the releases/Modrinth page.
2.  **Dependencies:** Ensure you have **Vault** and an economy provider (e.g., EssentialsX) installed.
3.  **Install:** Drag and drop `CrashShop.jar` into your server's `/plugins/` folder.
4.  **Launch:** Restart your server to generate the default configuration files.
5.  **Configure:** Edit all the files to your liking and run `/shop reload`.

---

## ⌨️ Commands & Permissions

We utilize a granular permission system to give you full control over who can do what.

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/shop` | Opens the main shop GUI. | `crashshop.use` |
| `/shop open [shop]` | Open a specific shop category. | `crashshop.use.specific` |
| `/shop reload` | Reloads the configuration files. | `crashshop.reload` |
| `/sellall` | Sell every items you can sell in your inventory. | `crashshop.sellall` |

---

## ⚙️ Configuration

The plugin generates a `config.yml` that is heavily commented to guide you through every setting.

**Key Configuration Areas:**
* **GUI Settings:** Define the size, title, and sound effects of your menus.
* Different Files:** Every category has his own file.

*Example of ores.yml (A category):*
```yaml
#Here items have two more parameters:
#Slot: the slot where you want to place the item
#Page: the page where the item should be 
id: ore #Category identifier
title: 'ᴏʀᴇ' 
rows: 6 #Total slots of the inventory: 54 (Double Chest)
items:
  coal: 
    material: COAL
    displayname: "<gradient:black:gray>Coal</gradient>"
    slot: 10
    page: 0
    buy-price: 20.0
    sell-price: 2.0
    custom-model-data: 4
    item-model: "yay"
    enchantments:
      minecraft:sharpness:5
    lore:
      - ""
      - " <gray>│ <white>Type: <#708090>Mineral"
      - " <gray>│ <white>Rarity: <green>Common"
      - ""
      - " <gray>➥ <green>Left-Click <white>to Buy: <gold>$20.0 <gray>(Shift x64)"
      - " <gray>➥ <red>Right-Click <white>to Sell: <gold>$2.0 <gray>(Shift SellAll)"
      - ""
  charcoal:
    material: CHARCOAL
    displayname: "<gradient:gray:dark_gray>Charcoal</gradient>"
    slot: 11
    page: 0
    buy-price: 15.0
    sell-price: 1.0
    lore:
      - ""
      - " <gray>│ <white>Type: <#708090>Mineral"
      - " <gray>│ <white>Rarity: <green>Common"
      - ""
      - " <gray>➥ <green>Left-Click <white>to Buy: <gold>$15.0 <gray>(Shift x64)"
      - " <gray>➥ <red>Right-Click <white>to Sell: <gold>$1.0 <gray>(Shift SellAll)"
      - ""
  # Continue...
```

## 👨‍💻 Developer API

Want to hook into **CrashShop** for your own custom plugins?

[![JitPack](https://jitpack.io/v/CrashWho/CrashShop.svg?style=for-the-badge)](https://jitpack.io/#CrashWho/CrashShop)

### Maven
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.CrashWho</groupId>
    <artifactId>CrashShop</artifactId>
    <version>[version]</version>
</dependency>
```
### Gradle
```gradle
repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
```

```gradle
compileOnly 'com.github.CrashWho:CrashShop:[version]'

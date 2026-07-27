package com.testPlugin_A.main.packs;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class InteractorPack {
    // 主菜单
    static public Book get__book_menuHelp(){
        Book book = Book.book(
                Component.text("帮助菜单"),        // 书名
                Component.text("TestPlugin_A"),  // 作者
                MiniMessage.miniMessage().deserialize("""
                        <red>·~·TP_A帮助菜单·~·</red>
                        <green>◀ 玩家进服提示 ▶</green>
                        <gold>当玩家入服的时候，本插件会给予入服提示喵~</gold>
                        """),
                MiniMessage.miniMessage().deserialize("""
                        <gray>这一页神马都木有qwq... </gray>
                        """)
        );
        return book;
    }
}

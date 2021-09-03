package me.oska

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.plugin.java.JavaPlugin


class SurvivalTool: JavaPlugin(), Listener {

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {

    }

    @EventHandler
    fun inventoryClose(event: InventoryCloseEvent) {
        val inv = event.inventory
        if (inv.type == InventoryType.CHEST) {
            inv.contents.sortBy { it.type }
        }
    }

}
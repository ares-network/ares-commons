package com.playares.commons.services.account.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import com.playares.commons.promise.SimplePromise;
import com.playares.commons.services.account.AccountService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
public final class SettingsCommand extends BaseCommand {
    @Getter public final AccountService service;

    @CommandAlias("settings|bukkitsettings")
    @Description("Access your account settings")
    public void onSettings(Player player) {
        service.openSettingsMenu(player, new SimplePromise() {
            @Override
            public void success() {}

            @Override
            public void fail(String err) {
                player.sendMessage(err);
            }
        });
    }
}
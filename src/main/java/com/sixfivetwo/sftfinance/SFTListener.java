package com.sixfivetwo.sftfinance;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SFTListener implements Listener {
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) throws SQLException {
		String UUID = e.getPlayer().getUniqueId().toString();
		String PlayerID = e.getPlayer().getName();
		PreparedStatement statement = Main.conn.prepareStatement("select * from wallets");
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			if (rs.getString("UUID").contains(UUID)) {
				rs.close();
				statement.close();
				return;
			}
		}
		APILibrary.createWallet(UUID,PlayerID);
		rs.close();
		statement.close();
	}
}
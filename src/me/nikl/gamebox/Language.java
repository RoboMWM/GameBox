package me.nikl.gamebox;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.Arrays;
import java.util.List;


/**
 * Created by niklas on 10/17/16.
 *
 * Language class
 * Get all messages on enable
 * save not saved default lang files
 */
public class Language {
	private GameBox plugin;
	private FileConfiguration langFile;

	private YamlConfiguration defaultLang;


	public final String PREFIX = "["+ChatColor.BLUE+"GameBox"+ChatColor.RESET+"]";
	public final String NAME = ChatColor.BLUE+"GameBox"+ChatColor.RESET;
	public final String PLAIN_PREFIX = "[GameBox]";

	// commands
	public String CMD_NO_PERM, CMD_ONLY_PLAYER, CMD_RELOADED, CMD_DISABLED_WORLD;
	public List<String> CMD_HELP, CMD_WRONG_USAGE;

	// Buttons
	public String BUTTON_EXIT, BUTTON_TO_MAIN_MENU, BUTTON_TO_GAME_MENU;
	public List<String> BUTTON_MAIN_MENU_INFO;

	// Inv titles
	public String TITLE_MAIN_GUI, TITLE_GAME_GUI, TITLE_NO_PERM, TITLE_NOT_ENOUGH_MONEY, TITLE_OTHER_PLAYER_NOT_ENOUGH_MONEY, TITLE_ALREADY_IN_ANOTHER_GAME, TITLE_ERROR = ChatColor.RED + "              Error";

	// player input
	public String INPUT_START_MESSAGE, INPUT_TIME_RAN_OUT, INVITATION_SUCCESSFUL, INVITATION_ALREADY_THERE, INVITATION_NOT_VALID_PLAYER_NAME, INVITATION_NOT_ONLINE, INPUT_CLOSED, INVITATION_NOT_YOURSELF;
	public List<String> INPUT_HELP_MESSAGE;

	// tokens
	public String WON_TOKEN;


	
	Language(GameBox plugin){
		this.plugin = plugin;
		getLangFile();
		
		getCommandMessages();
		getInvTitles();
		getButtons();
		getOthers();
	}

	private void getOthers() {
		this.INPUT_START_MESSAGE = getString("others.playerInput.openingMessage");
		this.INPUT_TIME_RAN_OUT = getString("others.playerInput.timeRanOut");
		this.INPUT_HELP_MESSAGE = getStringList("others.playerInput.helpMessage");
		this.INVITATION_SUCCESSFUL = getString("others.playerInput.inputSuccessful");
		this.INVITATION_ALREADY_THERE = getString("others.playerInput.sameInvitation");
		this.INVITATION_NOT_VALID_PLAYER_NAME = getString("others.playerInput.notValidPlayerName");
		this.INVITATION_NOT_ONLINE = getString("others.playerInput.notOnline");
		this.INPUT_CLOSED = getString("others.playerInput.inputClosed");
		this.INVITATION_NOT_YOURSELF = getString("others.playerInput.notInviteYourself");

		this.WON_TOKEN = getString("others.wonToken");
	}

	private void getButtons() {
		this.BUTTON_EXIT = getString("mainButtons.exitButton");
		this.BUTTON_TO_MAIN_MENU = getString("mainButtons.toMainGUIButton");
		this.BUTTON_TO_GAME_MENU = getString("mainButtons.toGameGUIButton");
		this.BUTTON_MAIN_MENU_INFO = getStringList("mainButtons.infoMainMenu");
	}

	private void getInvTitles() {
		// main GUI
		this.TITLE_MAIN_GUI = getString("inventoryTitles.mainGUI");

		this.TITLE_GAME_GUI = getString("inventoryTitles.gameGUIs");
		this.TITLE_NO_PERM = getString("inventoryTitles.noPermMessage");
		this.TITLE_NOT_ENOUGH_MONEY = getString("inventoryTitles.notEnoughMoney");
		this.TITLE_ALREADY_IN_ANOTHER_GAME = getString("inventoryTitles.alreadyInAnotherGame");
		this.TITLE_OTHER_PLAYER_NOT_ENOUGH_MONEY = getString("inventoryTitles.otherPlayerNotEnoughMoney");

	}
	
	private void getCommandMessages() {

		this.CMD_NO_PERM = getString("commandMessages.noPermission");
		this.CMD_DISABLED_WORLD = getString("commandMessages.inDisabledWorld");
		this.CMD_ONLY_PLAYER = getString("commandMessages.onlyAsPlayer");
		this.CMD_RELOADED = getString("commandMessages.pluginReloaded");
		
		
		this.CMD_HELP = getStringList("commandMessages.help");
		this.CMD_WRONG_USAGE = getStringList("commandMessages.wrongUsage");
	}



	private List<String> getStringList(String path) {
		List<String> toReturn;
		if(!langFile.isList(path)){
			toReturn = defaultLang.getStringList(path);
			for(int i = 0; i<toReturn.size(); i++){
				toReturn.set(i, ChatColor.translateAlternateColorCodes('&',toReturn.get(i)));
			}
			return toReturn;
		}
		toReturn = langFile.getStringList(path);
		for(int i = 0; i<toReturn.size(); i++){
			toReturn.set(i, ChatColor.translateAlternateColorCodes('&',toReturn.get(i)));
		}
		return toReturn;
	}

	private String getString(String path) {
		if(!langFile.isString(path)){
			return ChatColor.translateAlternateColorCodes('&',defaultLang.getString(path));
		}
		return ChatColor.translateAlternateColorCodes('&',langFile.getString(path));
	}
	
	private void getLangFile() {

		/*
		 * The default file will always contain the up to date english messages
		 *
		 * Messages from this file will be used if there are some missing
		 * in the given language file. The missing keys will be listed in the console.
		 */


		try {
			String fileName = "language/lang_en.yml";
			this.defaultLang =  YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(fileName), "UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		File defaultEn = new File(plugin.getDataFolder().toString() + File.separatorChar + "language" + File.separatorChar + "lang_en.yml");
		if(!defaultEn.exists()){
			defaultEn.getParentFile().mkdirs();
			plugin.saveResource("language" + File.separatorChar + "lang_en.yml", false);
		}
		File defaultDe = new File(plugin.getDataFolder().toString() + File.separatorChar + "language" + File.separatorChar + "lang_de.yml");
		if(!defaultDe.exists()){
			plugin.saveResource("language" + File.separatorChar + "lang_de.yml", false);
		}
		
		if(!plugin.getConfig().isSet("langFile")){
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4*******************************************************"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Language file is missing in the config!"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " Add the following to your config:"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " langFile: 'default.yml'"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4*******************************************************"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Using default language file"));
			this.langFile = defaultLang;
		} else {
			if(!plugin.getConfig().isString("langFile")){
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4*******************************************************"));
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Language file is invalid (no String)!"));
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4*******************************************************"));
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Using default language file"));
				this.langFile = defaultLang;
			} else {
				String fileName = plugin.getConfig().getString("langFile");
				if(fileName.equalsIgnoreCase("default") || fileName.equalsIgnoreCase("default.yml")){
					this.langFile = defaultLang;
					return;
				}
				File languageFile = new File(plugin.getDataFolder().toString() + File.separatorChar + "language" + File.separatorChar + plugin.getConfig().getString("langFile"));
				if(!languageFile.exists()){
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4*******************************************************"));
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Language file not found!"));
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4*******************************************************"));
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Using default language file"));
					this.langFile = defaultLang;
				} else {
					try {
						this.langFile = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(languageFile), "UTF-8"));
					} catch (UnsupportedEncodingException | FileNotFoundException e) {
						e.printStackTrace();
						Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4*******************************************************"));
						Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Error while loading language file!"));
						Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4*******************************************************"));
						Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Using default language file"));
						this.langFile = defaultLang;
					}
				}
			}
		}
		/*
		 * get missing keys and print them
		 */
		int count = 0;
		for(String key : defaultLang.getKeys(true)){
			if(defaultLang.isString(key)){
				if(!this.langFile.isString(key)){// there is a message missing
					if(count == 0){
						Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*"));
						Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Missing message(s) in your language file!"));
					}
					Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " " + key));
					count++;
				}
			}
		}
		if(count > 0){
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + ""));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Game will use default messages for these paths"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + ""));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Please get an up to date language file"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4Or add the listed paths to your file"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + " &4*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*"));
		}
		return;
		
	}

}


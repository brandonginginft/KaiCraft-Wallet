package com.sixfivetwo.sftfinance;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;


public class Main extends JavaPlugin {
    final static String SFTInfo = "[Kai Wallet]";
    final static String Thanks = "Kai Wallet 1.4.1\nCredit To: https://github.com/Minecraft652/SFTFinance";
    public static Connection conn;
    public static Properties prop;
    public static BlockchainData chainlibrary;
    public static FileConfiguration fileconfig;
    public static FileConfiguration fileexchange;
    public static FileConfiguration filecontract;
    public static FileConfiguration filehelp;
    public static Map<String, Map<Integer, String>> ERC20ContractMap;
    public static Map<String, Map<Integer, String>> ExchangeMap;
    public static Map<String, Map<Integer, String>> HelpPageMap;
    public static PlayerWalletData ConsoleWallet;

    public void onEnable() {

        try {
            ERC20ContractMap = new HashMap<>();
            ExchangeMap = new HashMap<>();
            HelpPageMap = new HashMap<>();
            Map<String, File> FileMaps = new HashMap<>();
            Map<String, InputStream> InternalFileMaps = new HashMap<>();
            File configfile = new File(getDataFolder(), "config.yml");
            File exchangefile = new File(getDataFolder(), "exchange.yml");
            File contractfile = new File(getDataFolder(), "contract.yml");
            File walletfile = new File(getDataFolder(), "wallets.db");
            File zhhelpfile = new File(getDataFolder(), "help_zh_CN.yml");
            File enhelpfile = new File(getDataFolder(), "help_en_US.yml");
            File zhlanfile = new File(getDataFolder(), "zh_CN.properties");
            File enlanfile = new File(getDataFolder(), "en_US.properties");
            FileMaps.put("config.yml", configfile);
            FileMaps.put("exchange.yml", exchangefile);
            FileMaps.put("contract.yml", contractfile);
            FileMaps.put("wallets.db", walletfile);
            FileMaps.put("help_zh_CN.yml", zhhelpfile);
            FileMaps.put("help_en_US.yml", enhelpfile);
            FileMaps.put("zh_CN.properties", zhlanfile);
            FileMaps.put("en_US.properties", enlanfile);
            InternalFileMaps.put("config.yml", getResource("config.yml"));
            InternalFileMaps.put("exchange.yml", getResource("exchange.yml"));
            InternalFileMaps.put("contract.yml", getResource("contract.yml"));
            InternalFileMaps.put("wallets.db", getResource("wallets.db"));
            InternalFileMaps.put("help_zh_CN.yml", getResource("help_zh_CN.yml"));
            InternalFileMaps.put("help_en_US.yml", getResource("help_en_US.yml"));
            InternalFileMaps.put("zh_CN.properties", getResource("zh_CN.properties"));
            InternalFileMaps.put("en_US.properties", getResource("en_US.properties"));

            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            for (String keySet : FileMaps.keySet()) {
                File externalfile = FileMaps.get(keySet);
                if (!externalfile.exists()) {
                    InputStream internalfiles = InternalFileMaps.get(keySet);
                    APILibrary.inputStream2File(internalfiles, externalfile);
                }
            }

            fileconfig = YamlConfiguration.loadConfiguration(configfile);
            fileexchange = YamlConfiguration.loadConfiguration(exchangefile);
            filecontract = YamlConfiguration.loadConfiguration(contractfile);

            for (String contractroot : filecontract.getKeys(false)) {
                Map<Integer, String> FileMapContract = new HashMap<>();
                FileMapContract.put(1, filecontract.getString(contractroot + ".Address"));
                FileMapContract.put(2, filecontract.getString(contractroot + ".Symbol"));
                FileMapContract.put(3, filecontract.getString(contractroot + ".GasLimit"));
                FileMapContract.put(4, filecontract.getString(contractroot + ".Decimal"));
                ERC20ContractMap.put(contractroot, FileMapContract);
            }

            for (String exchangeroot : fileexchange.getKeys(false)) {
                Map<Integer, String> FileMapExchange = new HashMap<>();
                FileMapExchange.put(1, fileexchange.getString(exchangeroot + ".Tokentype"));
                FileMapExchange.put(2, fileexchange.getString(exchangeroot + ".Price"));
                FileMapExchange.put(3, fileexchange.getString(exchangeroot + ".Executecommand"));
                ExchangeMap.put(exchangeroot, FileMapExchange);
            }

            if (Objects.requireNonNull(fileconfig.getString("Language")).contains("zh")) {
                filehelp = YamlConfiguration.loadConfiguration(zhhelpfile);
                for (String helproot : filehelp.getKeys(false)) {
                    Map<Integer, String> FileMapHelp = new HashMap<>();
                    FileMapHelp.put(1, filehelp.getString(helproot + ".front"));
                    FileMapHelp.put(2, filehelp.getString(helproot + ".comment1"));
                    FileMapHelp.put(3, filehelp.getString(helproot + ".comment2"));
                    FileMapHelp.put(4, filehelp.getString(helproot + ".comment3"));
                    FileMapHelp.put(5, filehelp.getString(helproot + ".comment4"));
                    FileMapHelp.put(6, filehelp.getString(helproot + ".comment5"));
                    FileMapHelp.put(7, filehelp.getString(helproot + ".comment6"));
                    FileMapHelp.put(8, filehelp.getString(helproot + ".comment7"));
                    FileMapHelp.put(9, filehelp.getString(helproot + ".comment8"));
                    FileMapHelp.put(10, filehelp.getString(helproot + ".comment9"));
                    FileMapHelp.put(11, filehelp.getString(helproot + ".comment10"));
                    HelpPageMap.put(helproot, FileMapHelp);
                }
            } else {
                filehelp = YamlConfiguration.loadConfiguration(enhelpfile);
                for (String helproot : filehelp.getKeys(false)) {
                    Map<Integer, String> FileMapHelp = new HashMap<>();
                    FileMapHelp.put(1, filehelp.getString(helproot + ".front"));
                    FileMapHelp.put(2, filehelp.getString(helproot + ".comment1"));
                    FileMapHelp.put(3, filehelp.getString(helproot + ".comment2"));
                    FileMapHelp.put(4, filehelp.getString(helproot + ".comment3"));
                    FileMapHelp.put(5, filehelp.getString(helproot + ".comment4"));
                    FileMapHelp.put(6, filehelp.getString(helproot + ".comment5"));
                    FileMapHelp.put(7, filehelp.getString(helproot + ".comment6"));
                    FileMapHelp.put(8, filehelp.getString(helproot + ".comment7"));
                    FileMapHelp.put(9, filehelp.getString(helproot + ".comment8"));
                    FileMapHelp.put(10, filehelp.getString(helproot + ".comment9"));
                    FileMapHelp.put(11, filehelp.getString(helproot + ".comment10"));
                    HelpPageMap.put(helproot, FileMapHelp);
                }
            }

            prop = new Properties();
            InputStream in = getResource(Objects.requireNonNull(fileconfig.getString("Language")));
            prop.load(in);
            try {
                if (fileconfig.getBoolean("IsMysql")) {
                    String user = fileconfig.getString("MysqlUser");
                    String pass = fileconfig.getString("MysqlPassword");
                    String url = "jdbc:" + fileconfig.getString("MysqlUrl");
                    conn = APILibrary.getConnection("mysql", url, user, pass);
                    APILibrary.createWallet("00000000-0000-0000-0000-000000000000", "CONSOLE");
                } else {
                    String url = "jdbc:sqlite:" + walletfile.toString();
                    conn = APILibrary.getConnection("sqlite", url, "null", "null");
                    APILibrary.createWallet("00000000-0000-0000-0000-000000000000", "CONSOLE");
                }
            } catch (Exception ex) {
            	ex.printStackTrace();
            }

            chainlibrary = new BlockchainData(fileconfig.getString("ChainName"), fileconfig.getString("HttpUrl"), fileconfig.getLong("ChainID"), fileconfig.getString("Symbol"));
            ConsoleWallet = new PlayerWalletData("CONSOLE");

            if (fileconfig.getBoolean("OnPlayerLoginRegisterWallet")) {
                Bukkit.getServer().getPluginManager().registerEvents(new SFTListener(), this);
            }

            Objects.requireNonNull(Bukkit.getPluginCommand("wallet")).setExecutor(new SFTCommand());

            if (Objects.requireNonNull(fileconfig.getString("Language")).contains("zh")) {
                System.out.println(Thanks);
            } else {
                System.out.println(Thanks);
            }

            System.out.println(APILibrary.getVersion());

            if ("".equals(fileconfig.getString("Version")) ||
                    "".equals(fileconfig.getString("Language")) ||
                    "".equals(fileconfig.getString("HttpUrl")) ||
                    "".equals(fileconfig.getString("Symbol")) ||
                    "".equals(fileconfig.getString("ChainName")) ||
                    "".equals(fileconfig.getString("ChainID")) ||
                    "".equals(fileconfig.getString("OnPlayerLoginRegisterWallet")) ||
                    //"".equals(fileconfig.getString("playerCanInsertTheyOwnSeed")) ||
                    "".equals(fileconfig.getString("IsMysql")) ||
                    "".equals(fileconfig.getString("MysqlUrl")) ||
                    "".equals(fileconfig.getString("MysqlUser")) ||
                    "".equals(fileconfig.getString("MysqlPassword")) ||
                    null == fileconfig.getString("Version") ||
                    null == fileconfig.getString("Language") ||
                    null == fileconfig.getString("HttpUrl") ||
                    null == fileconfig.getString("Symbol") ||
                    null == fileconfig.getString("ChainName") ||
                    null == fileconfig.getString("ChainID") ||
                    null == fileconfig.getString("OnPlayerLoginRegisterWallet") ||
                    //null == fileconfig.getString("playerCanInsertTheyOwnSeed") ||
                    null == fileconfig.getString("IsMysql") ||
                    null == fileconfig.getString("MysqlUrl") ||
                    null == fileconfig.getString("MysqlUser") ||
                    null == fileconfig.getString("MysqlPassword")) {
                System.out.println(Main.prop.getProperty("updateconfig"));
                System.out.println(Main.prop.getProperty("updateconfig"));
                System.out.println(Main.prop.getProperty("updateconfig"));
                System.out.println(Main.prop.getProperty("updateconfig"));
                System.out.println(Main.prop.getProperty("updateconfig"));
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onDisable() {
        if (Objects.requireNonNull(fileconfig.getString("Language")).contains("zh")) {
            System.out.println(Thanks);
        } else {
            System.out.println(Thanks);
        }
        System.out.println(APILibrary.getVersion());
    }
}
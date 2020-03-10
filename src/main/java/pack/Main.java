package pack;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Main {

    private static String BOT_NAME = "multiplier_front_bot";
    private static String BOT_TOKEN = "900996069:AAFmfblYhKZDHqoQhiO2ak0ic9iuiWgNNlM" /* your bot's token here */;

//    private static String PROXY_HOST = "109.196.175.17" /* proxy host */;
    private static String PROXY_HOST = "183.88.38.70" /* proxy host */;
//    private static Integer PROXY_PORT = 1198 /* proxy port */;
    private static Integer PROXY_PORT = 8080 /* proxy port */;
//    private static String PROXY_USER = "greamko" /* proxy user */;
    private static String PROXY_USER = "greamko" /* proxy user */;
//    private static String PROXY_PASSWORD = "vole3h" /* proxy password */; */;
    private static String PROXY_PASSWORD = "vole3h" /* proxy password */;

    public static void main(String[] args) {
        try {
            ApiContextInitializer.init();

            // Create the TelegramBotsApi object to register your bots
            TelegramBotsApi botsApi = new TelegramBotsApi();

            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(PROXY_HOST, PROXY_PORT),
                    new UsernamePasswordCredentials(PROXY_USER, PROXY_PASSWORD));

            HttpHost httpHost = new HttpHost(PROXY_HOST, PROXY_PORT);
            // Set up Http proxy
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

            RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).setAuthenticationEnabled(true).build();
//            botOptions.setRequestConfig(requestConfig);
//            botOptions.setCredentialsProvider(credsProvider);
//            botOptions.setHttpProxy(httpHost);

            // Register your newly created AbilityBot
            MyBot bot = new MyBot(BOT_TOKEN, BOT_NAME, botOptions);

            botsApi.registerBot(bot);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}

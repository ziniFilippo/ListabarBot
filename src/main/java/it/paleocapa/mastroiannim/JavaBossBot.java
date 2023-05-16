package it.paleocapa.mastroiannim;

import org.telegram.telegrambots.meta.api.objects.*;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Service
public class JavaBossBot extends TelegramLongPollingBot {

	private static final Logger LOG = LoggerFactory.getLogger(JavaBossBot.class);

	private String botUsername;
	private static String botToken;
	private static JavaBossBot instance;
	private LinkedList<Prodotto>menu;
	private double conto=0;

	public static JavaBossBot getJavaBossBotInstance(String botUsername, String botToken, String menuItems){
		if(instance == null) {
			instance = new JavaBossBot(menuItems);
			instance.botUsername = botUsername;
			JavaBossBot.botToken = botToken;
		}
		return instance;
	}

	private JavaBossBot(@Value("${menu.items}") String menuItems){
		super(botToken);
		menu = new LinkedList<>();
        String[] items = menuItems.split(",");
        for (int i = 0; i < items.length; i += 2) {
            String key = items[i];
            Double value = Double.parseDouble(items[i+1]);
            menu.add(new Prodotto(key,value));
        }
	}

	@Override
	public String getBotToken() {
		return botToken;
	}
	
	@Override
	public String getBotUsername() {
		return botUsername;
	}

	public LinkedList<Prodotto> getMenu() {
		return menu;
	}
	@Override
	public void onUpdateReceived(Update update) {
		Message msg = update.getMessage();
		User user = msg.getFrom();
		long id = user.getId();
		if(msg.getText().equals("/menu")){
			SendMessage message = new SendMessage();
			message.setChatId(id);
			Ordinazione o = new Ordinazione(menu);
			message.setText(o.toString());
			try {
				execute(message);
			} catch (TelegramApiException e) {
				LOG.error(e.getMessage());
			}
			return;
		}
		if(msg.getText().equals("/start")){
			SendMessage message = new SendMessage();
			message.setChatId(id);
			message.setText("Benvenuto!\nPer ordinare usa il comando /order\nPer visualizzare il menù usa il comando /menu");
			
			try {
				execute(message);
			} catch (TelegramApiException e) {
				LOG.error(e.getMessage());
			}
			return;
		} else {
			String[] ordine = msg.getText().split(" ");
			SendMessage message = new SendMessage();
			message.setChatId(id);
			for (Prodotto p: menu) {
				if(p.nome.equals(ordine[0])){
					message.setText("Hai ordinato :" + ordine[0] + " pagando "+ordine[1]+"€\nIl tuo resto: " + String.valueOf(Integer.valueOf(ordine[1])-p.prezzo)+"€");
					try {
						execute(message);
					} catch (TelegramApiException e) {
						LOG.error(e.getMessage());
					}
					return;
				}	
			}
			message.setText("Perfavore scegli un prodotto");
					try {
						execute(message);
					} catch (TelegramApiException e) {
						LOG.error(e.getMessage());
					}
		}
	}
}
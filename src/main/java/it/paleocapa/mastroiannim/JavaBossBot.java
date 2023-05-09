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
	public void order(long id, Message msg){
		SendMessage message = new SendMessage();
			message.setChatId(id);
			Ordinazione o = new Ordinazione(menu);
			if (message.getText().equals("/order")) {
				message.setText("Cosa vuoi ordinare?");
			} else {
				String ordine = msg.getText();
				if(o.searchItem(ordine)!=0.0){

				}
			}
			try {
				execute(message);
			} catch (TelegramApiException e) {
				LOG.error(e.getMessage());
			}
	}
	@Override
	public void onUpdateReceived(Update update) {
		Message msg = update.getMessage();
		User user = msg.getFrom();
		long id = user.getId();
		if(msg.getText().equals("/order")){
			order(id,msg);
		}
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
		}
		if(msg.getText().equals("/start")){
			SendMessage message = new SendMessage();
			message.setChatId(id);
			message.setText("Benvenuto!Inserisci quanti soldi hai\nPer ordinare usa il comando /order\nPer visualizzare il menù usa il comando /menu");
			
			try {
				execute(message);
			} catch (TelegramApiException e) {
				LOG.error(e.getMessage());
			}
		} else {
			SendMessage message = new SendMessage();
			try {
                double orderQuantity = Integer.parseInt(msg.getText());
				message.setChatId(id);
				conto = orderQuantity;
                message.setText("Il tuo conto è di " + conto);
            } catch (NumberFormatException e) {
                message.setText("perfavore inserisci un numero");
            }
		}
	}
}
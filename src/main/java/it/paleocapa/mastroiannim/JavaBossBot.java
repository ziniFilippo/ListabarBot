package it.paleocapa.mastroiannim;

import org.telegram.telegrambots.meta.api.objects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	public static JavaBossBot getJavaBossBotInstance(String botUsername, String botToken){
		if(instance == null) {
			instance = new JavaBossBot();
			instance.botUsername = botUsername;
			JavaBossBot.botToken = botToken;
		}
		return instance;
	}

	private JavaBossBot(){
		super(botToken);
	}

	@Override
	public String getBotToken() {
		return botToken;
	}
	
	@Override
	public String getBotUsername() {
		return botUsername;
	}
	public void order(long id, Message msg){
		SendMessage message = new SendMessage();
			message.setChatId(id);
			Ordinazione o = new Ordinazione();
			message.setText("Cosa vuoi ordinare?\n\n"+o.toString());

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
		} else {
			SendMessage message = new SendMessage();
			message.setChatId(id);
			message.setText("Benvenuto! Come posso aiutarti?");

			try {
				execute(message);
			} catch (TelegramApiException e) {
				LOG.error(e.getMessage());
			}
		}
	}
}
package com.luis.projectlgameengineimp;

public class RscManager {
	
	public static String[] sAllTexts;
	
	public static final byte ENGLISH = 0;
	public static final byte SPANISH = 1;
	public static final byte CATALA = 2;
	//Language text
	public static final int TXT_BLANK = 0;
	public static final int TXT_ENGLISH = TXT_BLANK+1;
	public static final int TXT_SPANISH = TXT_ENGLISH+1;
	public static final int TXT_CATALA = TXT_SPANISH+1;
	public static final int TXT_PLAY = TXT_CATALA +1;
	public static final int TXT_OPTIONS = TXT_PLAY +1;
	public static final int TXT_INFO=  TXT_OPTIONS+1;
	public static final int TXT_EXIT=  TXT_INFO+1;
	public static final int TXT_SOUND_ON= TXT_EXIT+1;
	public static final int TXT_SOUND_OFF= TXT_SOUND_ON+1;
	public static final int TXT_VIBRATION_ON= TXT_SOUND_OFF+1;
	public static final int TXT_VIBRATION_OFF= TXT_VIBRATION_ON+1;
	public static final int TXT_WANT_EXIT_GAME= TXT_VIBRATION_OFF+1;
	public static final int TXT_NO= TXT_WANT_EXIT_GAME+1;
	public static final int TXT_YES= TXT_NO+1;
	public static final int TXT_HELP= TXT_YES+1;
	public static final int TXT_ABOUT= TXT_HELP+1;
	public static final int TXT_HELP_DESCRIP= TXT_ABOUT+1;
	public static final int TXT_ABOUT_DESCRIP= TXT_HELP_DESCRIP+1;
	public static final int TXT_POINTS= TXT_ABOUT_DESCRIP+1;
	public static final int TXT_RECORD= TXT_POINTS+1;
	public static final int TXT_RETRY= TXT_RECORD+1;
	public static final int TXT_LEAVE= TXT_RETRY+1;
	public static final int TXT_NEW_RECORD= TXT_LEAVE+1;
	public static final int TXT_RETURN_MENU= TXT_NEW_RECORD+1;
	
	
	
	
	public static final int TOTAL_LINES_TXT = TXT_RETURN_MENU;
	
	
	public static void loadLanguage(int language) {
		String lan = "";
		switch (language) {
		case ENGLISH:
			lan = "/texts/english.txt";
			break;
		case SPANISH:
			lan = "/texts/spanish.txt";
			break;
		case CATALA:
			lan = "/texts/catala.txt";
			break;
			}
		try {
			StreamReader reader;
			reader = new StreamReader(lan, TOTAL_LINES_TXT + 1);
			sAllTexts = reader.read();
//			for (int i = 0; i < ms_sAllTexts.length; i++) {
//				System.out.println("Cargado " + ms_sAllTexts[i]);
//			 }
		} catch (Exception e) {
			System.out.println("No se han podido cargar los textos");
		}
	}
	
	
	
	
	

}

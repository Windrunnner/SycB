package GreatDouBaba;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

public class GDB_DTB_High_Interface implements Runnable {
	GDB_DTB_Low_Interface connecter;
	
	private static String IMAGE_PATH = "/";
	private String action = "none";
	private WebView getWebview;
	@Override
    public void run() {
        try {
            if(action.equals("viewPage"))
            	viewPage(getWebview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	public void setAction(String action){
		this.action = action;
	}
	
	public void setWebview(WebView webview){
		getWebview = webview;
	}
	public GDB_DTB_High_Interface(Context context) {
		Log.d("GDB_DTB_High_Interface start", "test");
		//context.deleteDatabase("default_info");
		connecter = new GDB_DTB_Low_Interface(context, "default");
		Log.d("GDB_DTB_High_Interface end", "test");
	}

	private String convertBitmapToString(Bitmap src) {
		String str = null;
		if (src != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			src.compress(android.graphics.Bitmap.CompressFormat.PNG, 100,
					(OutputStream) os);
			byte[] byteArray = os.toByteArray();
			str = Base64.encodeToString(byteArray, Base64.DEFAULT);
		}
		return str;
	}

	private Bitmap getBitMapFromString(String src) {
		Bitmap bitmap = null;
		if (src != null) {
			byte[] decodedString = Base64
					.decode(src.getBytes(), Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
					decodedString.length);
		}
		return bitmap;
	}

	private Bitmap pictureDrawable2Bitmap(Picture picture) {
		PictureDrawable pictureDrawable = new PictureDrawable(picture);
		Bitmap bitmap = Bitmap.createBitmap(
				400,
				300, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawPicture(pictureDrawable.getPicture());
		return bitmap;
	}
	


	private Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}



	private String getRoot(String Url) {
		int start = 0;
		if ((start = Url.indexOf("://")) == -1)
			return null;
		if (Url.substring(start + 3).indexOf('/') == -1)
			return Url + "/";
		String root = "";
		root = Url.substring(0,
				start + 3 + Url.substring(start + 3).indexOf('/'))
				+ "/";
		return root;
	}

	private String getIco(String Url) {
		String icoUrl = getRoot(Url) + "favicon.ico";
		Bitmap data = getBitmapFromURL(icoUrl);
		if(data==null)
			return null;
		String dataStr = convertBitmapToString(data);
		return dataStr;
	}
	
	
	public void viewPage(WebView webview) { // Url
		try {
			Log.d("view page Start", webview.getUrl());
			String Url = webview.getUrl();
			String Title = "None";
			String words[] = null;
			URL page = new URL(Url);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					page.openStream()));
			StringBuilder contentBDR = new StringBuilder();
			long len=0;
			for (int ch = 0, door = 1; (ch = in.read()) != -1;)
				if (ch == '<' || ch == '\n') {
					if (door == 2) {
						contentBDR.append(" ");
						len = len + 1;
					}
					door = 0;
				} else if (ch == '>')
					door = 1;
				else if ((door == 1 || door == 2)
						&& ((ch > 64 && ch < 91) || (ch > 96 && ch < 123)
								|| (ch > 47 && ch < 58) || ch == ' ')) {
					contentBDR.append((char) (ch));
					door = 2;
					len++;
				}
			
			String content = contentBDR.toString();
			System.out.print(content);
			String[] contentWord = content.split(" ");
			if (webview.getTitle()!=null) {
				Title = webview.getTitle();
				StringBuilder getwords = new StringBuilder();
				for (int i = 0; i < Title.length(); i++) {
					char ch = Title.charAt(i);
					if ((ch > 64 && ch < 91) || (ch > 96 && ch < 123)
							|| (ch > 47 && ch < 58) || ch == ' ')
						getwords.append((char)ch);
				}
				words = getwords.toString().split(" "); // words
				
			}
			String urlIco;
			try{
				urlIco = getIco(Url);
			}catch(Exception e){
				urlIco = "";
				Log.d("thing happend: ico", "but I got this");
			}
			Calendar c = Calendar.getInstance();
			@SuppressWarnings("deprecation")
			String Date = c.getTime().toGMTString(); // date
			String screenshot;
			try{
				Picture picture = webview.capturePicture();
				Bitmap bitmap = pictureDrawable2Bitmap(picture);
				screenshot = convertBitmapToString(bitmap);
			}catch(Exception e){
				screenshot = "";
				Log.d("thing happend: screenshot", "but I got this");
			}
			int Id = webview.getId();
			
			if(connecter.addHistroy(Date, Url, Title, String.valueOf(Id))){
				connecter.addCurrent(Url, Id);
				connecter.addView(Url, screenshot, urlIco, Title, Title);
				connecter.addWordToTitle(words, Url);
				connecter.addWordToContent(contentWord, Url);
			}else
				connecter.addView(Url, screenshot, urlIco, Title, Title);

		//	in.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("viewPage BUG", "test");
		} finally {
			Runtime.getRuntime().gc();
		}
		Log.d("view page End", "test");
	}

	public void closePage(WebView webview) {
		int Id = webview.getId();
		connecter.deleteCurrent(String.valueOf(Id));
	}
	
	public void deleteBookmark(String Id) {
		connecter.deleteBookmark(Id);
	}
	
	public void deleteHistory(String Id){
		connecter.deleteHistory(Id);
	}
	
	public void deleteBookmark(int Id){
		connecter.deleteBookmark(String.valueOf(Id));
	}
	
	public void deleteHistory(int Id){
		connecter.deleteHistory(String.valueOf(Id));
	}
	
	public void addBookmark(WebView webview) {
		String Url = webview.getUrl();
		String Title = webview.getTitle();
		Log.d("GDHI", Url+"="+Title);
		connecter.addBookmark(Url, Title);
	}
	
	public String[][] getBookmark(){
		return connecter.getBookmark();
	}

	public String[] getCurrent(){
		return connecter.getCurrent();
	}
	
	public String[][] getView(int Count){
		return connecter.getView(Count);
	}
	
	public String[] gerWordByTitleWithCase(String sentance){
		return connecter.getWordByTitleHasCase(sentance);
	}
	
	public String[] gerWordByContentWithCase(String sentance){
		return connecter.getWordByContentHasCase(sentance);
	}
	
	public String[] gerWordByTitleNoCase(String sentance){
		return connecter.getWordByTitleNoCase(sentance);
	}
	
	public String[] gerWordByContentNoCase(String sentance){
		return connecter.getWordByContentNoCase(sentance);
	}
	
	public String[][] getHistory(){
		return connecter.getHistory();
	}
	public String[] getUrlInfo(String Url){
		return connecter.getUrlInfo(Url);
	}
	
	public String[] getHistoryColumns(){
		return connecter.getHistoryColumns();
	}
	
	public String[] getBookmarkColumns(){
		return connecter.getBookmarkColumns();
	}
	
	public String[] getBackGroundColumns(){
		return connecter.getBackGroundColumns();
	}
	
	public String[] getWordColumns(){
		return connecter.getWordColumns();
	}
	
	public String[] getContentColumns(){
		return connecter.getContentColumns();
	}
	
	public String[] getViewColumns(){
		return connecter.getViewColumns();
	}
	
	public String[] getCurrentColumns(){
		return connecter.getCurrentColumns();
	}
	
	public String[] getPasswordColumns(){
		return connecter.getPasswordColumns();
	}
}

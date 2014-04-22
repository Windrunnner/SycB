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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebIconDatabase;
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

	public String[] restore() {
		return null;
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
				pictureDrawable.getIntrinsicWidth(),
				pictureDrawable.getIntrinsicHeight(), Config.ARGB_8888);
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
			String[] words = null;
			//String inputLine;
			URL page = new URL(Url);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					page.openStream()));
			//StringBuilder input = new StringBuilder();
			/*while ((inputLine = in.readLine()) != null)
				input.append(inputLine);
			*/
			if (webview.getTitle()!=null) {
				Title = webview.getTitle();
				StringBuilder getwords = new StringBuilder();
				for (int i = 0; i < Title.length(); i++) {
					char ch = Title.charAt(i);
					if ((ch > 64 && ch < 91) || (ch > 96 && ch < 123)
							|| (ch > 47 && ch < 58) || ch == ' ')
						getwords.append((char)(ch > 96 ? ch - 32 : ch));
				}
				words = getwords.toString().split(" "); // words
				Log.d("test Word", words[0]);
			}
			
			String urlIco = getIco(Url);
			Calendar c = Calendar.getInstance();
			@SuppressWarnings("deprecation")
			String Date = c.getTime().toGMTString(); // date
			Picture picture = webview.capturePicture();
			Bitmap bitmap = pictureDrawable2Bitmap(picture);
			String screenshot = convertBitmapToString(bitmap);
			int Id = webview.getId();
			
			if(connecter.addHistroy(Date, Url, Title, String.valueOf(Id))){
				connecter.addCurrent(Url, Id);
				connecter.addView(Url, screenshot, urlIco);
				connecter.addWord(words, Url);
			}else
				connecter.addView(Url, screenshot, urlIco);
			
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("viewPage BUG", "test");
		}
		Log.d("view page End", "test");
	}

	public void closePage(WebView webview) {
		int Id = webview.getId();
		connecter.deleteCurrent(Id);
	}

	public void addBookmark(WebView webview) {

	}

	public void deleteBookmark(WebView webview) {

	}
	
	public String[][] getHistory(){
		return connecter.getHistory();
	}
	public String[] getUrlInfo(String Url){
		return connecter.getUrlInfo(Url);
	}
}

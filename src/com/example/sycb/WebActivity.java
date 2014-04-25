package com.example.sycb;

import GreatDouBaba.GDB_DTB_High_Interface;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {

	private WebView webView;
	public static GDB_DTB_High_Interface GDHI;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		System.out.println("Start");
		setContentView(R.layout.webcontent);

		webView = (WebView) findViewById(R.id.webView);

		webView.getSettings().setJavaScriptEnabled(true);
		
		webView.loadUrl("https://www.cs.purdue.edu/");
		
		GDHI = new GDB_DTB_High_Interface(this);
		webView.setWebViewClient(new WebViewClient() {
				
			   	public void onPageFinished(WebView view, String url) {
			        System.out.println(url);
			        WebActivity.GDHI.setAction("viewPage");
			        WebActivity.GDHI.setWebview(view);
			        Thread viewP = new Thread(WebActivity.GDHI);
			        viewP.start();
			        
			        /*String[][] test = WebActivity.GDHI.getHistory();
			        if(test!=null)
			        for(int i=0; i<test.length; i++){
			        	System.out.println(test[i][0]+ "-"+ test[i][1]+ "-"+ test[i][2]+"-"+test[i][3]);
			        }
			        */
			        
			        System.out.println("----------start--------------");
			        String test2[] = WebActivity.GDHI.gerWordByTitleWithCase("CCc");
			        if(test2!=null)
			        	for(int i=0; i<test2.length; i++)
			        		System.out.println(test2[i]);
			        	
			        
			        //String[] test2 = WebActivity.GDHI.getUrlInfo(test[0][1]);
			    	//System.out.println(test2[0]+ "\n"+ test2[1]+ "\n"+ test2[2]);
			    }
			});
		
		//GDHI.viewPage(webView);
        System.out.println("End");
        
		
		//String customHtml = "<html><body><h2>Greetings from JavaCodeGeeks</h2></body></html>";
		//webView.loadData(customHtml, "text/html", "UTF-8");

	}

}
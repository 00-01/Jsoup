package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        String html = null;

        try {
            String addr = "https://www.naver.com";
            URL url = new URL(addr);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(30000);
            con.setUseCaches(false);

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line + "\n");
            }
            br.close();
            con.disconnect();

            html = sb.toString();
//            System.out.println(html);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        if (html == null) {
            return;
        }
        try {
            /*Document doc = Jsoup.parse(html);
            Elements span = doc.getElementsByClass("an_text");
            for (int i = 0; i < span.size(); i++) {
                Element ele = span.get(i);
                System.out.println(ele.text());
            }*/
            Document doc = Jsoup.parse(html);
            Elements span = doc.select("a");
            for (int i = 0; i < span.size(); i++) {
                Element ele = span.get(i);
                System.out.println(ele.text());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

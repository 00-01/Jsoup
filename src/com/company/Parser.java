package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Parser {
    public static void main(String[] args) {
        try {
            String key = URLEncoder.encode("딥러닝", "utf-8");
            String add = "http://search.hani.co.kr/Search?command=query&keyword=%EB%94%A5%EB%9F%AC%EB%8B%9D&media=news&submedia=&sort=d&period=all&datefrom=2000.01.01&dateto=2020.01.28&pageseq=0";

            URL url = new URL(add);

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
            String html = sb.toString();
            br.close();
            con.disconnect();

            try {
                Document doc = Jsoup.parse(html);
                Elements span = doc.getElementsByTag("span");
                for (int i = 0; i < span.size(); i++) {
                    Element ele = span.get(i);
                    System.out.println(ele.text());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

/*
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
*/
package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Parser {

    public static void main(String[] args) {
        //한겨례 신문사 웹 페이지에서 코로나 로 검색된 기사 내용을 전부 파일에 저장하기

        //1.코로나로 검색된 기사 개수 파악하기
        //기사 개수를 저장할 변수
        int cnt = 0;
        try {
            //한글로 된 검색으를 인코딩
            String keyword = URLEncoder.encode("코로나", "utf-8");
            //한겨례 신문사 뉴스 검색 URL 만들기
            String addr = "http://search.hani.co.kr/Search?command=query&keyword="
                    + keyword
                    + "&media=news&submedia=&sort=d&period=all"
                    + "&datefrom=2000.01.01&dateto=2020.01.28&pageseq=0";
            URL url = new URL(addr);
            //연결 옵션과 연결
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(20000);
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
            //데이터 확인 - 확인되면 주석처리
            //System.out.println(html);

            //html을 DOM으로 펼치기
            Document document = Jsoup.parse(html);

            //태그 이흠으로 찾기
            //Elements spans = document.getElementsByTag("span");

            //클래스 이름으로 찾기
            //Elements spans = document.getElementsByClass("total");

            //선택자 이름으로 찾기
            Elements spans = document.select(
                    "#contents > div.search-result-section.first-child > div > span");

            String temp = spans.get(0).text();
            //240 건 이라고 temp 에 저장
            //240만 추출해서 정수로 변환해서 cnt에 저장
            //공백으로 분할한 후 첫번째 데이터를 정수로 변환해서 cnt에 저장
            String[] ar = temp.split(" ");
            cnt = Integer.parseInt(ar[0]);
            //System.out.println(cnt);


        } catch (Exception e) {
            System.out.println("기사 개수 파악 예외" + e.getMessage());
            e.printStackTrace();
        }

        if (cnt <= 0) {
            System.out.println("검색된 기사가 없습니다.");
            return;
        }

        //검색된 데이터의 링크를 전부 찾아서 list에 삽입
        ArrayList<String> list = new ArrayList<String>();
        try {
            //페이지 개수 구하기
            int pagesu = cnt / 10;
            if (pagesu % 10 != 0) {
                pagesu = pagesu + 1;
            }


            //페이지 단위로 순회
            for (int i = 0; i < pagesu; i = i + 1) {
                //한글로 된 검색으를 인코딩
                String keyword = URLEncoder.encode("코로나", "utf-8");
                //한겨례 신문사 뉴스 검색 URL 만들기
                String addr = "http://search.hani.co.kr/Search?command=query&keyword="
                        + keyword
                        + "&media=news&submedia=&sort=d&period=all"
                        + "&datefrom=2000.01.01&dateto=2020.01.28&pageseq=" + i;
                URL url = new URL(addr);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(10000);
                con.setUseCaches(false);

                //특정 페이지의 데이터를 읽지 못하더라도 다음 페이지의 데이터를 읽기 위해서 반복문 안에 예외처리문장 삽입
                try {
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

                    Document document = Jsoup.parse(html);
                    Elements links = document.select("dl > dt > a");
                    for (int j = 0; j < links.size(); j = j + 1) {
                        //a 태그 안에 있는 문자열을 가져오기
                        //System.out.println(links.get(j).text());

                        //a 태그의 href 속성의 값 가져오기
                        //System.out.println(links.get(j).attr("href"));
                        list.add(links.get(j).attr("href"));
                    }
                    //System.out.println(list.size());

                } catch (Exception e) {
                    System.out.println("링크 수집 실패" + e.getMessage());
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            System.out.println("링크 수집 예외" + e.getMessage());
            e.printStackTrace();
        }


        //list에 저장된 링크의 기사 데이터를 전부 읽어서 파일에 저장하기
        try {
            for (String addr : list) {
                try {
                    URL url = new URL(addr);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(10000);
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
                    //System.out.println(html);
                    //파일에 기록

                    Document document = Jsoup.parse(html);
                    //class가 title 인 태그만 찾아오기
                    Elements articles = document.getElementsByClass("title");
                    //찾아온 데이터를 파일에 기록
                    for (int i = 0; i < articles.size(); i = i + 1) {
                        PrintWriter pw = new PrintWriter(new FileOutputStream("./코로나.txt", true));
                        pw.println(articles.get(i).text());
                        pw.flush();
                        pw.close();

                    }
                    //실제 여러 개의 페이지에서 스크래핑 할 때 딜레이를 주는 것이 좋습니다.
                    //Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("기사 읽기 예외" + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.out.println("링크 읽기 예외" + e.getMessage());
            e.printStackTrace();
        }

    }
}
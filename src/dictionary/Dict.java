package dictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Dict {
	static HashMap<String, String> hMapEV;
	static ArrayList<String> word;
	static final int MORE_KEY = 5;
        
	public static void HasdMapEVOn() {
            hMapEV = new HashMap();
            word = new ArrayList<String>();
            File f = new File("/home/mhieu/NetBeansProjects/T/src/data/Dict.dic");
            String s = "";
            try
            {
            	Scanner sc = new Scanner(f);
            	try {
		while(sc.hasNextLine())
                    {
                        String string = sc.nextLine();
			String[] str = string.split("=");
			if(str[0] != "" && str[1]!=""){
                            word.add(str[0]);
                            hMapEV.put(str[0].trim(),str[1]);
                            s = hMapEV.get(str[0].trim());
                            }
			else System.out.println("Đã có lỗi !");
			}
                    }
		catch(Exception e){
                    System.out.println(e.getMessage());
		}
		finally{
                    Collections.sort(word);
                    if(sc!= null) sc.close();
		}
            }
            catch(Exception ex){
		System.out.println(ex.getMessage());
            }	
	}

    public static ArrayList<String> getMoreWord(String word) {
        ArrayList<String> res = new ArrayList<>();
        hMapEV.entrySet().forEach((item) -> {
            String str1 = item.getKey();
            String str2 = item.getValue();
            String[] kq = str2.split("#");
                if (kq[0].equals(word) && res.size() < MORE_KEY) {
                    res.add(str1);
                }
            });
        return res;
    }
    
    public static String search(String word){
        String resulf=Dict.hMapEV.get(word);   
        
        String in = "";
        if(resulf!=null){
        String[] kq= resulf.split("#");
        in = in+kq[0]+"\n\t"+kq[1];
        ArrayList<String> moreWord = Dict.getMoreWord(kq[0]);
        in=in + "\nNhững từ gợi ý thêm là:";
    
        if(moreWord != null) {
            for(String s : moreWord) {
                in = in+"\n\t"+s;
                }
            }
        }
        return in;
    }
    
}
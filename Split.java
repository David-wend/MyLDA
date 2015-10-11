package nlp.lda.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

public class Split {

	static ArrayList<String> stopWords = new ArrayList<String>();
	// 静态代码块，不用每次都读取
	static {
		try {
			@SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(
							"./data/dictionary/stopwords.txt")), "utf-8"));
			String temp = null;
			while ((temp = in.readLine()) != null) {
				Split.stopWords.add(temp);

			}
			// 系统自动生成
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO 自动生成
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成
			e.printStackTrace();
		}

	}

	public static void splitWord(String line, ArrayList<String> word) {

		List<Term> termList = HanLP.segment(line);

		String result = "";
		Iterator<Term> it = termList.iterator();
		while (it.hasNext()) {
			Term next = it.next();
			if (Split.stopWords.contains(next.word)) {
				continue;
			} else {
				word.add(next.word);
			}
		}
	}
}

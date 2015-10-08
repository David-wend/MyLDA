import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import liuyang.nlp.lda.com.FileUtil;
import liuyang.nlp.lda.com.Stopwords;



public class Documents {
	
	ArrayList<Document> docs; 
	Map<String, Integer> termToIndexMap; //记录词项-下标数
	ArrayList<String> indexToTermMap; // 记录所有词项
	Map<String,Integer> termCountMap; //计算词项-词项数目
	
	public Documents(){
		docs = new ArrayList<Document>();
		termToIndexMap = new HashMap<String, Integer>();
		indexToTermMap = new ArrayList<String>();
		termCountMap = new HashMap<String, Integer>();
	}
	
	public void readDocs(String docsPath){
		for(File docFile : new File(docsPath).listFiles()){
			Document doc = new Document(docFile.getAbsolutePath(), termToIndexMap, indexToTermMap, termCountMap);
			docs.add(doc);
		}
	}
	
	public static class Document {	
		private String docName;
		int[] docWords; //docWords[i]表示词项i的下标数
		
		public Document(String docName, Map<String, Integer> termToIndexMap, ArrayList<String> indexToTermMap, Map<String, Integer> termCountMap){
			this.docName = docName;
			//Read file and initialize word index array
			ArrayList<String> docLines = new ArrayList<String>();
			ArrayList<String> words = new ArrayList<String>();
			FileUtil.readLines(docName, docLines);
			for(String line : docLines){
				FileUtil.tokenizeAndLowerCase(line, words);
			}
			
			//Remove stop words and noise words
			for(int i = 0; i < words.size(); i++){
				if(Stopwords.isStopword(words.get(i)) || isNoiseWord(words.get(i))){
					words.remove(i);
					i--;
				}
			}
			
			//Transfer word to index
			this.docWords = new int[words.size()];
			for(int i = 0; i < words.size(); i++){
				String word = words.get(i);
				if(!termToIndexMap.containsKey(word)){
					int newIndex = termToIndexMap.size();
					termToIndexMap.put(word, newIndex);
					indexToTermMap.add(word);
					termCountMap.put(word, new Integer(1));
					docWords[i] = newIndex;
				} else {
					docWords[i] = termToIndexMap.get(word);
					termCountMap.put(word, termCountMap.get(word) + 1);
				}
			}
			words.clear();
		}
		
		// 去除噪声，包含网址的文档
		public boolean isNoiseWord(String string) {
			// TODO Auto-generated method stub
			string = string.toLowerCase().trim();
			Pattern MY_PATTERN = Pattern.compile(".*[a-zA-Z]+.*");
			Matcher m = MY_PATTERN.matcher(string);
			// filter @xxx and URL
			if(string.matches(".*www\\..*") || string.matches(".*\\.com.*") || 
					string.matches(".*http:.*") )
				return true;
			if (!m.matches()) {
				return true;
			} else
				return false;
		}
		
	}
}

/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength=DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    ArrayList<String> wordList;
    HashSet<String> wordSet;
    HashMap<Integer,ArrayList<String>> sizeToWord;
    HashMap<String,ArrayList<String>> lettersToWord;
    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList=new ArrayList<>();
        wordSet=new HashSet<>();
        sizeToWord=new HashMap<>();
        lettersToWord=new HashMap<>();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortWord=sortLetters(word);
            //letterToWords started
                     if (lettersToWord.containsKey(sortWord)) {
                         lettersToWord.get(sortWord).add(word);
                     } else {
                         ArrayList<String> lw = new ArrayList<>();
                         lw.add(word);
                         lettersToWord.put(sortWord, lw);
                     }

                         if (sizeToWord.containsKey(word.length())) {
                   sizeToWord.get(word.length()).add(word);
               } else {
                   ArrayList<String> sw = new ArrayList<>();
                   sw.add(word);
                   sizeToWord.put(word.length(), sw);
               }
        }
    }

    public boolean isGoodWord(String word, String base) {
        //Log.d("mytag","isgoodword");
        if(wordSet.contains(word))
        {
            if(!word.contains(base))
            {
                return true;
            }
        }
        return false;
    }

    public String sortLetters(String word)
    {
        if(word!=null) {
            char ch[] = word.toCharArray();
            Arrays.sort(ch);
            String str = new String(ch);
            return str;
        }
        return null;
    }
    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String str=sortLetters(targetWord);
        for(int i=0;i<wordList.size();++i)
        {
            String newWord=wordList.get(i);
            if(str.equals(sortLetters(newWord)))
            {
                result.add(newWord);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char ch='a';ch<='z';++ch)
        {
           // Log.d("mytag","in for");
            ArrayList<String> listAna=lettersToWord.get(sortLetters(word + ch));
                   if(listAna!=null) {
                       for (int i = 0; i < listAna.size(); ++i) {
                           if (isGoodWord(word, listAna.get(i))) {
                               result.add(listAna.get(i));
                           }
                       }
                   }

        }
        return result;
    }

    public String pickGoodStarterWord() {
        int randomNumber;
        int numAnagrams = 0;

        // restrict your search to the words of length wordLength
        ArrayList<String> listWordsMaxLength = sizeToWord.get(wordLength);
        int arraySize = listWordsMaxLength.size();

        // while less than, keep picking new random word
        while (numAnagrams < MIN_NUM_ANAGRAMS) {

            // get random number of size of array, then use it
            // to choose a (random) word from the fixed length array of words
            randomNumber = random.nextInt(arraySize);
            String randomWord = listWordsMaxLength.get(randomNumber);

            // get size of array with all anagrams of randomly selected word
            // call getAnagramsWithOneMoreLetter since word.length() 3 on average contain 1 anagram max
            // creating infinite loop
            numAnagrams = getAnagramsWithOneMoreLetter(randomWord).size();
            if (numAnagrams >= MIN_NUM_ANAGRAMS) {
                if(wordLength < MAX_WORD_LENGTH) {
                    wordLength++;
                }
                return randomWord;
            }
        }
        return "stop";
    }
}

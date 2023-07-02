package prog11;

import prog05.ArrayQueue;

import java.util.*;

public class NotGPT implements SearchEngine{

    Disk pageDisk = new Disk();
    Disk wordDisk = new Disk();
    Map<String, Long> url2index = new TreeMap<>();
    Map<String, Long> word2index = new HashMap<>();
    @Override
    public void collect(Browser browser, List<String> startingURLs) {

        System.out.println("starting pages " + startingURLs);
        Queue<Long> indices = new ArrayDeque<>();
        for (String urls: startingURLs) {
            if (!url2index.containsKey(urls)) {
                Long index = indexPage(urls);
                indices.offer(index);
            }
        }
        while (!indices.isEmpty()) {
            System.out.println("queue " + indices);
            Long indexOfPageFile = indices.poll();
            InfoFile pageFile = pageDisk.get(indexOfPageFile);
            System.out.println("Dequeued " + indexOfPageFile + " " + pageFile);

//indexing pages-----------------------------------
            if (browser.loadPage(pageFile.data)) {
                List<String> urlList = browser.getURLs();
                Set<String> urlsOnPage = new HashSet<>();
                System.out.println("urls " + urlList);

                for (String url: urlList) {
                    if (!urlsOnPage.contains(url)) {
                        urlsOnPage.add(url);
                        Long indexOfUrl = url2index.get(url);
                        if (indexOfUrl == null) {
                            indexOfUrl = indexPage(url);
                            indices.offer(indexOfUrl);
                        }
                        pageFile.indices.add(indexOfUrl);
                    }
                 }
                pageDisk.put(indexOfPageFile, pageFile);
                System.out.println("updated page file " + pageFile);

//Indexing words-----------------------------------
                List<String> wordsList = browser.getWords();
                Set<String> wordsOnPage = new HashSet<>();
                System.out.println("words " + wordsList);

                for (String word: wordsList) {
                    if (!wordsOnPage.contains(word)) {
                        wordsOnPage.add(word);
                        Long indexOfWord = word2index.get(word);
                        if (indexOfWord == null) {
                            indexOfWord = indexWord(word);
                        }
                        InfoFile wordFile = wordDisk.get(indexOfWord);
                        wordFile.indices.add(indexOfPageFile);
                        wordDisk.put(indexOfWord, wordFile);
                        System.out.println("updated word file " + wordFile);
                    }
                }
            }
        }
    }

    @Override
    public void rank(boolean fast) {
        int count = 0; //Pages without any links

        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            if (file.indices.size() == 0) {
                count++;
            }
                file.priority = 1.0;
                file.tempPriority = 0.0;
        }

        double defaultPriority = 1.0 * count / pageDisk.size();

        if (fast) {
            for (int i = 0; i < 20; i++)
                rankFast(defaultPriority);
        }
        else {
            for (int i = 0; i < 20; i++)
                rankSlow(defaultPriority);
        }
    }

    public Long indexPage(String url) {
        Long index = pageDisk.newFile();
        InfoFile pageFile = new InfoFile(url);
        pageDisk.put(index, pageFile);
        url2index.put(url, index);
        System.out.println("Indexing page " + index + " " + pageFile);
        return index;
    }

    public Long indexWord(String word) {
        Long index = wordDisk.newFile();
        InfoFile wordFile = new InfoFile(word);
        wordDisk.put(index, wordFile);
        word2index.put(word, index);
        System.out.println("Indexing word " + index + " " + wordFile);
        return index;
    }

    public void rankSlow(double defaultPriority) {
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            double priorityPerIndex = 0.0;
            priorityPerIndex = file.priority / file.indices.size();
            for (long indicesOnPage: file.indices) {
                pageDisk.get(indicesOnPage).tempPriority += priorityPerIndex;
            }
        }
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.priority = file.tempPriority + defaultPriority;
            file.tempPriority = 0.0;
        }
    }

    public void rankFast(double defaultPriority) {
        List<Vote> voteList = new ArrayList<>();

        for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            double priorityPerIndex = file.priority / file.indices.size();

            for (Long indexOnPage : file.indices) {
                Vote vote = new Vote(indexOnPage, priorityPerIndex);
                voteList.add(vote);
            }
        }

        Collections.sort(voteList);
        Iterator<Vote> voteIterator = voteList.iterator();
        Vote vote = null;
        if (voteIterator.hasNext()) {
            vote = voteIterator.next();
        }
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            double previousPriority = file.priority;
            file.priority = defaultPriority;

            while (vote != null && vote.index == index) {
                file.priority += vote.vote;
                if (voteIterator.hasNext()) {
                    vote = voteIterator.next();
                } else {
                    vote = null;
                }
            }
        }
    }

        /*int count = 0;
        Vote iteratorVote = voteList.get(count);
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.priority = defaultPriority;
            while (iteratorVote.index == index) {
                file.priority += iteratorVote.vote;
                count++;
                if (count < voteList.size()) {
                    iteratorVote = voteList.get(count);
                } else {
                    break;
                }
            }
        }*/

    @Override
    public String[] search(List<String> searchWords, int numResults) {
        // Step 5
        // Matching pages with the least popular page on the top of the queue.
        PriorityQueue<Long> bestPageIndexes = new PriorityQueue<>(new PageComparator());

        Iterator<Long>[] worldFileIterators = (Iterator<Long>[]) new Iterator[searchWords.size()];

        //Initialize currentPageIndexes.  You just have to allocate the array.
        long[] currentPageIndexes = new long[searchWords.size()];

        // Write a loop to initialize the entries of wordFileIterators.
        // wordFileIterators[i] should be set to an iterator over the word
        // file of searchWords[i].
        for (int i = 0; i < worldFileIterators.length; i++) {
            String currentWord = searchWords.get(i);
            long index = word2index.get(currentWord);
            InfoFile wordFile = wordDisk.get(index);
            worldFileIterators[i] = wordFile.indices.iterator();
        }

        // Step 4
        // Implement the loop of search.  While getNextPageIndexes is true
        // check if the entries of currentPageIndexes are all equal.
        // If so, you have a found a match.  Print out its URL.
        while (getNextPageIndexes(currentPageIndexes, worldFileIterators)) {
            if (allEqual(currentPageIndexes)) { // If/When you find a match
                Long indexOfUrlMatch = currentPageIndexes[0];
                InfoFile pageFile = pageDisk.get(indexOfUrlMatch);
                String urlMatch = pageFile.data;
                System.out.println(urlMatch);
                // Step 6
                if (bestPageIndexes.size() != numResults) {
                    bestPageIndexes.offer(indexOfUrlMatch);
                } else if (bestPageIndexes.size() == numResults) {
                    Long root = bestPageIndexes.peek();
                    PageComparator rootCheck = new PageComparator();
                    if (rootCheck.compare(indexOfUrlMatch, root) > 0) {
                        bestPageIndexes.poll();
                        bestPageIndexes.offer(indexOfUrlMatch);
                    }
                }
            }
        }
        // Step 7
        String[] results = new String[bestPageIndexes.size()];
        double[] priorities = new double[bestPageIndexes.size()];
        for (int i = bestPageIndexes.size() - 1; i >= 0; i--) {
            Long currentIndex = bestPageIndexes.poll();
            InfoFile pageFile2 = pageDisk.get(currentIndex);
            String urlMatch2 = pageFile2.data;
            results[i] = urlMatch2;
            priorities[i] = pageFile2.priority;
        }
        // check priorities
        System.out.println("Priorities:");
        for (double p: priorities) {
            System.out.println(p);
        }
        return results;
    }

    /** Check if all elements in an array of long are equal.
    @param array an array of numbers
    @return true if all are equal, false otherwise
  */
    private boolean allEqual (long[] array) {
        int count = 0;
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == array[i + 1]) {
                count++;
            }
        }
        if (count == (array.length - 1)) {
            return true;
        }
        return false;
    }

        /** Get the largest element of an array of long.
         @param array an array of numbers
         @return largest element
         */
        private long getLargest (long[] array){
            //long largest = Arrays.stream(array).max().getAsLong();
            long max = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
            }
            return max;
        }

        /** If all the elements of currentPageIndexes are equal,
    set each one to the next() of its Iterator,
    but if any Iterator hasNext() is false, just return false.

    Otherwise, do that for every element not equal to the largest element.

    Return true.

    @param currentPageIndexes array of current page indexes
    @param wordFileIterators array of iterators with next page indexes
      @return true if all page indexes are updated, false otherwise
  */
    private boolean getNextPageIndexes (long[] currentPageIndexes, Iterator<Long>[] wordFileIterators) {
        if (allEqual(currentPageIndexes)) {
            for (int i = 0; i < currentPageIndexes.length; i++) {
                if (!wordFileIterators[i].hasNext()) {
                    return false;
                }
                currentPageIndexes[i] = wordFileIterators[i].next();
            }
        } else {
            long largest = getLargest(currentPageIndexes);
            for (int i = 0; i < currentPageIndexes.length; i++) {
                if (currentPageIndexes[i] < largest) {
                    if (!wordFileIterators[i].hasNext()) {
                        return false;
                    }
                    currentPageIndexes[i] = wordFileIterators[i].next();
                }
            }
        }
        return true;
    }

//----------------------------  Vote Class  ----------------------------------------------------------------------------
    public class Vote implements Comparable<Vote> {
        Long index;
        double vote;

    public Vote(Long index, double vote) {
        this.index = index;
        this.vote = vote;
    }

    @Override
    public int compareTo(Vote o) {
        return index.compareTo(o.index);
    }
  }
//----------------------------  PageComparator Class  ----------------------------------------------------------------------------
  public class PageComparator implements Comparator<Long> {
        // Step 5
        // returns a -1 of the page with index pageIndex1 has lower priority
        //   than the page with index pageIndex2, +1 if it is higher, and 0 if
        //   they are the same.

    public int compare(Long pageIndex1, Long pageIndex2) {
            InfoFile pageFile1 = pageDisk.get(pageIndex1);
            InfoFile pageFile2 = pageDisk.get(pageIndex2);

            if (pageFile1.priority < pageFile2.priority) {
                return -1;
            } else if (pageFile1.priority > pageFile2.priority) {
                return 1;
            }

            return 0;
    }
  }
}

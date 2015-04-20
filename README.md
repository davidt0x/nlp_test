# nlp_test
An implementation of word and sentence boundary tokenization. This is done as a programming excercise in natural language processing. While sentence boundary detection is considered a solved problem in the NLP literature for the most part, it still presents some tough challenges. It essetially boils down to detecting whether periods mark sentence boundaries (and sometimes whitespace alone) mark boundaries of sentences or not. All periods do not mark sentence boundaries in english because the period has multiple orthographic uses, (e.g. Abbreviations, Intitals, Decimal Points, Ellipsis, etc.) Most advanced algorithms perform a first pass tokenization of words and punctuations and then annotate sentence boundaries by using some king of pre-trained binary classifier. The classifier attempts to learn orthographic evidence for making this decision. I began by doing some reasearch into sentence tokenization approaches. I came accross the following paper:

  *Read, Jonathon, et al. "Sentence Boundary Detection: A Long Solved Problem?." COLING (Posters). 2012.*
  http://www.aclweb.org/anthology/C12-2096

This paper offers a nice objective survey of sentence boundary detection, both supervised and unsupervised. It tries to combat some of the problems in comparing performance between different papers because of the use of different datasets. In addition, authors have a tendecy to tune against their specific datasets so this leads to bias. The authors collect a wide range of standard datasets from different subject areas and compare several algorithms. 

After reviewing the literature I made a decision to try and implement a version of the Punkt algorithm described in:

  *Kiss, Tibor and Strunk, Jan (2006): Unsupervised Multilingual Sentence Boundary Detection.  Computational Linguistics 32: 485-525.*
  
I chose this algorithm for a few reasons. First, its performance on a wide range of datasets was admirable. Second, it is trained in an unsupervised manner. And finally, a great reference implentation is available in Python library nltk. 

However, I did not get a full implemetnation done in time. I have implemented the first preliminary annotation phase of Punkt and it performs decent on the provided data. Simple cases like ellipsis, numbers, and some abbreviations (common ones which I have hard coded) are detected and ignored properly. I will work on implementing training and the second phase annotation but probably will not finnish by tomorrow.

This code also performs very simple named entity recognition using case insensitive regular expressions. This simple matching is very brittle and will miss matches that have different whitespace and punctuation or alternate spellings. A better approach would be to use a fuzzy string matching algorithm like jaccard similiarity or lenkowski distances. I hoped to implement this as well but I was running out of time for this excercise.

Finally, the code implements parallel processing of zip files containing multiple text files. XML output files are in the repository under the test_out directory.

##Example Output
```
1. =================================================
The term "First World War" was first used in September 1914 by the German philosopher Ernst Haeckel, who claimed that "there is no doubt that the course and character of the feared 'European War' ...<E> will become the first world war in the full sense of the word."<S>
Why did the war begin?<S>
The immediate trigger for war was the 28 June 1914 assassination of Archduke Franz Ferdinand of Austria, heir to the throne of Austria-Hungary, by Yugoslav nationalist Gavrilo Princip (19 years old at the time) in Sarajevo.<S>
This set off a diplomatic crisis when Austria-Hungary delivered an ultimatum to the Kingdom of Serbia, and entangled international alliances formed over the previous decades were invoked.<S>
Within weeks, the major powers were at war and the conflict soon spread around the world.<S>

2. =================================================
The term "First World War" was first used in September 1914 by the German philosopher Ernst<Ent> Haeckel<Ent>, who claimed that "there is no doubt that the course and character of the feared 'European War' ...<E> will become the first world war in the full sense of the word."<S>
Why did the war begin?<S>
The immediate trigger for war was the 28 June 1914 assassination of Archduke Franz<Ent> Ferdinand<Ent> of Austria, heir to the throne of Austria-Hungary<Ent>, by Yugoslav nationalist Gavrilo<Ent> Princip<Ent> (19 years old at the time) in Sarajevo.<S><Ent>
This set off a diplomatic crisis when Austria-Hungary<Ent> delivered an ultimatum to the Kingdom of Serbia<Ent>, and entangled international alliances formed over the previous decades were invoked.<S>
Within weeks, the major powers were at war and the conflict soon spread around the world.<S>

3. =================================================
Processing Zip File ... Complete.
Done
```

############################################################################### Natural Language Processing ########################################################################

####################################################################################################################################################################################
###################################################################### This module was developed by Merola Alessandro ##############################################################
###################################################################### Written using Vim text editor                  ##############################################################
###################################################################### OS Peppermint Linux (Ubuntu/Debian based)      ##############################################################
####################################################################################################################################################################################

from nltk.tokenize import RegexpTokenizer
from stop_words import get_stop_words
from nltk.stem.porter import PorterStemmer
from decimal import *
import numpy as np
import logging
import gensim
import collections
import bz2
import os


######################################################################################################
# This class implements the natural language processing techniques summarized below:
# - puntuaction removal
# - stop word removal
# - creating bag of word
# - create the document-term matrix
# - character normalization
# This class provide also tfIdf and LDA tecnique for feature selection
# Usage example:
# NLP = NLP()
# NLP.createDocumentVector("Dataset/", "documentsName.txt")
######################################################################################################
######################################################################################################
# Questa classe implementa le tecniche di natural language processing riassunte di seguito:
# - rimozione della punteggiatura
# - rimozione delle stop word
# - creazione della bag of word
# - creazione della matrice termini documenti
# - normalizzazione dei caratteri
# Questa classe fornisce anche le tecniche Tf-Idf ed LDA per la features selection
# Esempio di utilizzo:
# NLP = NLP()
# NLP.createDocumentVector("Dataset/", "documentsName.txt")
######################################################################################################
class NLP:


	###################################################################################################
	# class destroyer
	# distruttore di classe
	def __del__(self):
		if(self.verbose):
			print "Distruzione dell'oggetto"
			print "Execution completed successfully"
	###################################################################################################


	###################################################################################################
	# class constructor
	# costruttore di classe
	def __init__(self, verbose):
		self.stemmed_tokens = []
		self.nFeatures = 0
		self.verbose = verbose
		self.documentsList = []
		self.documents = []
		self.texts = []
		self.topics = []
		self.points = []
	###################################################################################################


	###################################################################################################
	# create a list of documents name and a list of content's document
	# usage example: NLP.createDocumentVector("Dataset/", "documentsName.txt")
	# crea una lisata con i nomi dei documenti
	# esempio di utilizzo: NLP.createDocumentVector("Dataset/", "documentsName.txt")
	def createDocumentVector(self, inputPath, outputFileName):
		if (self.verbose):
			print "createDocumentVector: creating the file with the documents name --------> OK"
		content = ""
		for document in os.listdir(inputPath):
			self.documents.append(document)
		self.documents.sort()
		with open(outputFileName, "w") as myfile:
			for document in self.documents:
				myfile.write("" + str(document) + "\n")
		myfile.close()
		for document in self.documents:
			with open(str(inputPath) + str(document)) as otherFile:
				for line in otherFile:
					content = content + line.decode('utf-8')
					for i in "&!?^|/\;:-,.][)(}{'\"_":
						content = content.replace(i, '  ')
				self.documentsList.append(content)
				content = ""
		otherFile.close()
	###################################################################################################


	###################################################################################################
	# create automatically label
	# usage example: NLP.createLabelNameForDocument("nomiEtichette.txt")
	# crea le etichette in automatico
	# esempio di utilizzo: NLP.createLabelNameForDocument("nomiEtichette.txt")
	def createLabelNameForDocument(self, outputFileName):
		if (self.verbose):
			print "createLabelNameForDocument: writing the labels name --------> OK"
		i = 0
		with open(outputFileName, "w") as myfile:
			while(i < len(self.documents)):
				myfile.write(""+str(self.documents[i].split("_")[0])+"\n")
				i = i + 1
		myfile.close()
	###################################################################################################


	###################################################################################################
	# clean documents removing useless words
	# usage example: NLP.preprocessingDocument("stopWord.txt")
	# rimuove le stopword
	# esempio di utilizzo: NLP.preprocessingDocument("stopWord.txt")
	def preprocessingDocuments(self, stopWordFile):
		p_stemmer = PorterStemmer()
		if (self.verbose):
			print "preprocessingDocuments: deleting stop word now --------> OK"
		tokenizer = RegexpTokenizer(r'\w+')
		en_stop = get_stop_words('en')

		with open(stopWordFile) as myfile:
			for line in myfile:
				wordList = line.split()
		myfile.close()
		en_stop = en_stop + wordList

		for i in self.documentsList:
			raw = i.lower()
			tokens = tokenizer.tokenize(raw)
			stopped_tokens = [i for i in tokens if not i in en_stop and not i.isdigit() and len(i)>1]
			self.stemmed_tokens = [p_stemmer.stem(i) for i in stopped_tokens]
			self.texts.append(self.stemmed_tokens)
	###################################################################################################


	###################################################################################################
	# convert the created corpus into a document-term matrix
	# usage example: NLP.getAsMatrix("documentTermMatrix.txt")
	# converte una bag-of-word in una document-term matrix
	# esempio di utilizzo: NLP.getAsMatrix("documentTermMatrix.txt")
	def getAsMatrix(self, outputFileName):
		if (self.verbose):
			print "getAsMatrix: create the document-term matrix only with frequence --------> OK"
		self.numpyMatrix = gensim.matutils.corpus2dense(self.corpus, num_terms = len(self.dictionary))
		with open(outputFileName, "w") as myfile:
			for x in self.numpyMatrix:
				for d in x:
					myfile.write("" + str(int(d)) + " ")
				myfile.write("\n")
		myfile.close()
		return self.numpyMatrix
	###################################################################################################


	###################################################################################################
	# create a file usefull to perform the feature selection
	# usage example: NLP.printWord("matrixColumn.txt")
	# crea un file utile per effettuare la features selection
	# esempio di utilizzo: NLP.printWord("matrixColumn.txt")
	def printWord(self, outputFileName):
		if (self.verbose):
			print "printWord: creating the column header file for R data frame --------> OK"
		keys = self.dictionary.token2id.values()
		keys.sort()
		with open(outputFileName, "w") as myfile:
			for k in keys:
				myfile.write("" + str(self.dictionary[k]) + "\n")
			myfile.write("Classe")
		self.nFeatures = len(keys)
		if (self.verbose):
			print "Feature selected: " + str(len(keys)) + " "
	###################################################################################################


	###################################################################################################
	# create a corpus with Tf-Idf transformation
	# usage example: NLP.tfIdfModel()
	# crea un corpus con il Tf-Idf
	# esempio di utilizzo: NLP.tfIdfModel()
	def tfIdfModel(self):
		if (self.verbose):
			print "tfIdfModel: creating the tf-idf model --------> OK"
		self.tfIdfModel = gensim.models.tfidfmodel.TfidfModel(self.corpus, normalize = True)
		return self.tfIdfModel
	###################################################################################################


	###################################################################################################
	# convert the Tf-Idf created corpus into a document-term matrix
	# usage example: NLP.getAsTfIdfMatrix("documentTermMatrix.txt")
	# converte il corpus creato con Tf-Idf in una document-term matrix
	# esempio di utilizzo: NLP.getAsTfIdfMatrix("documentTermMatrix.txt")
	def getAsTfIdfMatrix(self, outputFileName):
		if (self.verbose):
			print "getAsTfIdfMatrix: creating the tf-idf matrix --------> OK"
		tfIdfObj = self.tfIdfModel[self.corpus]
		self.numpyMatrix = gensim.matutils.corpus2dense(tfIdfObj, num_terms = len(self.dictionary))
		with open(outputFileName, "w") as myfile:
			for x in self.numpyMatrix.T:
				for d in x:
					myfile.write("" + str(float(d)) + " ")
				myfile.write("\n")
		myfile.close()
	###################################################################################################


	###################################################################################################
	# perform the LDA method to extract topics from document corpus
	# usage example: NLP.ldaModel(7, 7)
	# esegue il metodo LDA per estrearre i topic dal corpus di documenti
	# esempio di utilizzo: NLP.ldaModel(7, 7)
	def ldaModel(self, nTopics, nWords):
		if (self.verbose):
			print "ldaModel: Creating the lda model --------> OK"
		self.lda_model = gensim.models.ldamodel.LdaModel(corpus = self.corpus, id2word = self.dictionary, num_topics = nTopics, update_every = 3, chunksize = 10, passes = 1)
		self.topics = self.lda_model.print_topics(num_topics = nTopics, num_words = nWords)
	###################################################################################################


	###################################################################################################
	# print the word extracted from topics
	# usage example: NLP.onlyPrintWords("LDAword.txt")
	# note: the producted file is used by "createBagOfWord()" method for LDA-feature-selection
	# stampa le parole estratte dai topics
	# esempio di utilizzo: NLP.onlyPrintWords("LDAword.txt")
	# nota: il file prodotto viene utilizzato dal metodo "createBagOfWord()" per la features selection mediante LDA
	def onlyPrintWords(self, outputFileName):
		appListOne 		= []
		appListTwo 		= []
		appListThree 		= []
		ensemble 		= set()

		if (self.verbose):
			print "onlyPrintWord: creating the LDA selected word --------> OK"

		for t in self.topics:
			appListOne = t[1].split("+")
			appListTwo.append(appListOne)
		for	t in appListTwo:
			for e in t:
				s = e.split("*")
				appListThree.append(s)
			for w in appListThree:
				ensemble.add(w[1].strip())

		with open(outputFileName, "w") as myfile:
			for w in ensemble:
				myfile.write("" + str(w.strip()) + " ")
		myfile.close()
	##################################################################################################


	##################################################################################################
	# create a bow used to select the word
	# usage example: NLP.createBagOfWord("LDAword.txt")
	# crea una bow usata per selezionare le parole
	# esempio di utilizzo: NLP.createBagOfWord("LDAword.txt")
	def createBagOfWord(self, inputFileName):
		self.texts = []
		p_stemmer = PorterStemmer()
		e = set()
		tokenizer = RegexpTokenizer(r'\w+')
		stemmed_tokens = []
		if (self.verbose):
			print "createBagOfWord: creating the bag of word from LDA selected word --------> OK"

		wordList = ""

		with open(inputFileName) as myfile:
			for line in myfile:
				wordList = line.split()
		myfile.close()

		print "Lista delle parole nel file " + str(len(wordList))

		self.texts = [[p_stemmer.stem(word) for word in tokenizer.tokenize(document.lower()) if p_stemmer.stem(word) in wordList] for document in self.documentsList]

		frequency = collections.defaultdict(int)
		for text in self.texts:
			for token in text:
				frequency[token] += 1

		self.texts = [[token for token in text if frequency[token] > 0] for text in self.texts]
	###################################################################################################


	###################################################################################################
	# create the dictionary used into corpus object (needed for Gensim representation)
	# usage example: NLP.createDictionary()
	# crea il dizionario usato nell'oggetto corpus (necessario per la rappresentazione in stile Gensim)
	# esempio di utilizzo: NLP.createDictionary()
	def createDictionary(self):
		if (self.verbose):
			print "createDictionary: creating the dictionary for Gensim represenation --------> OK"
		self.dictionary = gensim.corpora.Dictionary(self.texts)
	###################################################################################################


	###################################################################################################
	# create a corpus object (needed for Gensim representation)
	# usage example: NLP.createCorpus()
	# crea l'oggetto corpus (necessario per la rappresentazione in stile Gensim)
	# esempio di utilizzo: NLP.createCorpus()
	def createCorpus(self):
		if (self.verbose):
			print "createCorpus: creating the corpus for Gensim represenation --------> OK"
		self.corpus = [self.dictionary.doc2bow(text) for text in self.texts]
	###################################################################################################

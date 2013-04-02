#!/usr/bin/python
from __future__ import division

import codecs
from math import log
from collections import defaultdict

from BeautifulSoup import BeautifulStoneSoup

# How big a likelihood to give to unseen terms
SMOOTHING = 0.1

def product(xs):
    return reduce(lambda x,y: x*y, xs)


class Corpus(object):

    @staticmethod
    def parse_docs_from(filename):
        xml = codecs.open(filename, 'r', 'utf8').read()
        parsed = BeautifulStoneSoup(xml)

        for text in parsed.findAll(name='reuters'):
            title = text.title.contents[0]
            body = text.body.contents[0]
            try:
                topics = text.topics.contents
            except:
                topics = []

            tokens = tokenize(body)
            yield Document(title=title, tokens=tokens, categories=[t.contents[0] for t in topics])
    
    def __init__(self, filenames):

        if type(filenames) == str:
            filenames = [filenames]

        self.filenames = filenames
    
        parses = [Corpus.parse_docs_from(filename) for filename in self.filenames]

        self.documents = sum([list(parse) for parse in parses], [])


    def __iter__(self):
        return iter(self.documents)

    def __str__(self):
        return str(self.documents)

    def __repr__(self):
        return "<%s '%s'>" % (self.__class__.__name__, ', '.join(self.filenames)) 

    def __len__(self):
        return len(self.documents)

    def getitem(self, category):
        return 

 
    def categories(self):
        return

class Category(object):
    
    def __init__(self, name, corpus):
        self.name = name
        self.corpus = corpus

    def __iter__(self):
        return (d for d in self.corpus if d in self)

    def __contains__(self, document):
        return self.name in document.categories
    
    def __repr__(self):
        return "<%s '%s'>" % (self.__class__.__name__, self.name)

    def df(self, term):
        '''Document frequency'''
        return sum(1 for doc in self if term in doc)

    @property
    def tokens(self):
        return sum([doc.tokens for doc in self], [])

class Document(object):

    def __init__(self, title=None, tokens=[], categories=[]):
        self.title = title
        self.id = id
        if categories.__class__ != list:
            categories = [categories]
            
        self.categories = categories
        self.tokens = [t for t in tokens if len(t) > 0]

    def __repr__(self):
        return "<%s '%s'>" % (self.__class__.__name__, self.title)

    def __iter__(self):
        return iter(self.tokens)

    def __contains__(self, token):
        return token in self.tokens

    def __len__(self):
        return len(self.tokens)

def get_stopwords(filename):
    return open(filename).read().splitlines()

def tokenize(text):
    # FIXME - poor man's tokenizer
    return sum([line.split(' ') for line in text.splitlines()],[])

def reduced_term_set(corpus, stopwordlist, aggressiveness, method, target_category):
    # Only DR by DF is implemented here
    return method(corpus,stopwordlist, aggressiveness, target_category)

def filter_by_df(corpus, stopwordlist, aggressiveness, target_category):
    tokens = set([t for t in target_category.tokens if t not in stopwordlist])
    ranked = dict((t,target_category.df(t)) for t in tokens)

    reduced_term_set = dict((t,f) for t,f in ranked.items() if f > aggressiveness)
    return reduced_term_set

def nb_learn(Tr, categories, stopwordlist, aggressiveness, method):
    P = defaultdict(lambda: SMOOTHING)
    reduced_termsets = {}

    for c in categories:
        Trj = [t for t in Tr if t in c]
        P[c] = len(Trj)/len(Tr)
        Textj = sum([d.tokens for d in Trj], [])
        n = len(Textj)
        T = reduced_term_set(Tr, stopwordlist, aggressiveness, method, c)
        for word in T:
            nk = sum(1 for x in Textj if x == word)
            P[(word,c)] = (nk + 1)/(n + len(T))
        
        reduced_termsets[c] = T

    return ((lambda word,c: P[(word,c)]), reduced_termsets)

def make_binary_classifier(probability_model, target_category, categories, reduced_termsets):
    P = probability_model
    T = reduced_termsets[target_category]
    
    def P_not(t, c):
        return sum(P(t,ci) for ci in categories if ci != c)/(len(categories) - 1)

    def csv(c,d):
        return product(P(t,c) for t in d)
    
    return (lambda d: csv(target_category, d))

def make_classifier(probability_model, categories, reduced_termsets):
    binary_classifiers = {}

    for c in categories:
        binary_classifiers[c] = make_binary_classifier(probability_model, c, categories, reduced_termsets)

    
    def multi_classifier(d):
        csvs = defaultdict(int)
        for category, classifier in binary_classifiers.items():
            csvs[category] = (classifier)(d)

        return max(csvs, key=lambda c: csvs[c])
        

    return multi_classifier


def test(classifier):
    t = '''test01.sgm test02.sgm test03.sgm test04.sgm test05.sgm
    test06.sgm test07.sgm test08.sgm test09.sgm test10.sgm test11.sgm test12.sgm
    test13.sgm test14.sgm test15.sgm test16.sgm test17.sgm test18.sgm
    test19.sgm'''.split()
    test_filenames = ['test-data/'+f for f in t]

    test_set = Corpus(test_filenames)

    results = [classifier(d) for d in test_set]


def main():
    from sys import argv,exit

    def usage():
        print 'Usage: %s <stopword file> <aggressiveness> <corpus file> [<corpus file> ...]' % argv[0]

    if len(argv) < 5:
        usage()
        exit(1)

    stopword_file, aggressiveness = argv[1:3]
    corpus_filenames = argv[3:]
    
    training_set = Corpus(corpus_filenames)
    stopwordlist = get_stopwords(stopword_file)

    aggressiveness = int(aggressiveness)

    method = filter_by_df
    categories = [Category(c, training_set) for c in ['acq', 'notacq']]

    probability_model, reduced_termsets = nb_learn(training_set, categories, stopwordlist, aggressiveness, method)
    
    classifier = make_classifier(probability_model, categories, reduced_termsets)

    test(classifier)

if __name__ == '__main__':
    main()


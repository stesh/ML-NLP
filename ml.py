#!/usr/bin/python

from __future__ import division
from BeautifulSoup import BeautifulStoneSoup
import codecs

class Corpus(object):

    @staticmethod
    def parse_docs_from(filename):
        xml = codecs.open(filename, 'r', 'utf8').read()
        parsed = BeautifulStoneSoup(xml)

        for text in parsed.findAll(name='reuters'):
            title = text.title.contents[0]
            body = text.body.contents[0]
            topics = text.topics.contents

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

    def df(self, term):
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
    return method(corpus,stopwordlist, aggressiveness, target_category)

def filter_by_df(corpus, stopwordlist, aggressiveness, target_category):
    tokens = set([t for t in target_category.tokens if t not in stopwordlist])
    ranked = dict((t,target_category.df(t)) for t in tokens)

    return dict((t,f) for t,f in ranked.items() if f > aggressiveness)

def nb_learn(Tr, C, stopwordlist, aggressiveness, method):
    C = Category(C, Tr)
    T = reduced_term_set(Tr, stopwordlist, aggressiveness, method, C)
    P = {}

    for c in C:
        Trj = [t for t in Tr if t in c]
        P[c] = len(Trj)/len(Tr)
        Textj = Document(sum([d.tokens for d in Trj], []), C)
        n = len(Textj)
        for word in T:
            nk = sum(1 for x in Textj if x == word)
            P[(word,c)] = (nk + 1)/(n + len(T))

    print P
    return (lambda word,c: P[(word,c)])


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

    probability_model = nb_learn(training_set, 'acq', stopwordlist, aggressiveness, method)

if __name__ == '__main__':
    main()

# Do not import anything other than sys and re!
import sys
import re

# this function removes punctuation from a string.
# you should use this before adding a word to the index,
# so that "asdf." and "asdf" and "asdf," are considered to be
# the same word.

def remove_punctuation(s):
  return re.sub(r'[^\w\s]', '', s)

assert(remove_punctuation('asdf.') == 'asdf')
assert(remove_punctuation('asdf,') == 'asdf')
assert(remove_punctuation("?;'.!") == '')

# index is a global dictionary. The keys should be words, and the
# values should be tuples of (filename, line number, position in line).

index = {}


def build_index():
  for i in range(len(sys.argv)):
    if i != 0:
      f = open(sys.argv[i]);
      lineNumber = 0;
      for l in f:
        charNumber = 0;
        words = l.split(' ')
        for w in words:
          wNew = remove_punctuation(w.lower()).replace("\n", "")
          if len(wNew) > 0:
            if not wNew in index:
              index[wNew] = [(sys.argv[i], lineNumber, charNumber)]
            else:
              index[wNew].append((sys.argv[i], lineNumber, charNumber))
            charNumber += len(w) + 1;
        lineNumber += 1;

build_index()


# commands

def words(args):
  keys = [];
  if len(args) == 0:
    args = [""];
  for a in args:
    argLen = len(a);
    for key in index:
      if key[0:argLen] == a:
        if len(key) != 0:
          keys.append(key);
  keys = sorted(keys);
  for k in keys:
    print(k);

#"File " + fileName + ", Line " + str(lineNumber) + ", Character " + str(charNumber)

def occurrences(args):
  keys = [];
  if len(args) == 0:
    args = [""];
  for a in args:
    argLen = len(a);
    for key in index:
      if key[0:argLen] == a:
        if len(key) != 0:
          keys.append(key);
  keys = sorted(keys);
  i = 0
  for k in keys:
    for t in index[k]:
      fileName = t[0];
      lineNumber = t[1];
      charNumber = t[2];
      print("(" + str(i) + ") File " + fileName + ", Line " + str(lineNumber) + ", Character " + str(charNumber));
      i += 1;
    
def context(args):
  keys = [];
  if len(args) == 0:
    args = [""];
  for a in args:
    argLen = len(a);
    for key in index:
      if key[0:argLen] == a:
        if len(key) != 0:
          keys.append(key);
  keys = sorted(keys);
  i = 0
  occ = {}
  for k in keys:
    for t in index[k]:
      fileName = t[0];
      lineNumber = t[1];
      charNumber = t[2];
      occ[i] = t #"(" + str(i) + ") File " + fileName + ", Line " + str(lineNumber) + ", Character " + str(charNumber);
      i += 1;
  f = open(occ[int(args[1])][0]);
  l = occ[int(args[1])][1];
  c = occ[int(args[1])][2];
  lines = f.readlines()
  print(lines[l].replace("\n", ""));
  for i in range(c + len(args[0])):
    if i < c:
      sys.stdout.write(" ");
    else:
      sys.stdout.write("^");
  print();

def output(args):
  keys = [];
  for k in index:
    keys.append(k);
  keys = sorted(keys);
  for k in keys:
    i = 0;
    print(k);
    for t in index[k]:
      fileName = t[0];
      lineNumber = t[1];
      charNumber = t[2];
      print("  (" + str(i) + ") File " + fileName + ", Line " + str(lineNumber) + ", Character " + str(charNumber));
      i += 1;
    
cmds = {
  'words' : words,
  'occurrences' : occurrences,
  'context' : context,
  'output' : output,
  }

def interact():
  # print the prompt line
  print('> ', end='', flush=True)
  
  for ln in sys.stdin:
    ln = ln.strip().split(' ')
    if ln[0] == 'quit':
      return
    else:
      cmds[ln[0]](ln[1:])

    # print the next prompt line
    print('> ', end='', flush=True)

interact()

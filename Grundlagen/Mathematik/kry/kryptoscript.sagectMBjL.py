# -*- coding: utf-8 -*-
# -*- encoding: utf-8 -*-
######################################################################
# This file was *autogenerated* from the file /Users/shadowshine/Documents/FHNW/FHNW-Informatik-Zusammenfassungen/krypto/kryptoscript.sage.
######################################################################
_sage_const_2 = Integer(2); _sage_const_1 = Integer(1); _sage_const_0 = Integer(0); _sage_const_4 = Integer(4)
def helpfunc():
  print "Available Functions: wiener2, pminus1, disc_log_diffie"
  print "wiener2 takes two arguments: n and e"
  print "pminus1 takes three arguments: n, B, a"
  print "disc_log_diffie takes four arguments: p, w(Omega), a, b"


def wiener2 ( n, e):
    contFrac = continued_fraction_list( e/n, partial_convergents=True)
    parConv = contFrac[_sage_const_1 ]
    print "number of partial convergents = ", len(parConv)
    del parConv[_sage_const_0 ]
    L = []
    for i in parConv:
        h = (e*i[_sage_const_1 ]-_sage_const_1 )
        if h%i[_sage_const_0 ] == _sage_const_0 :
            phi = h//i[_sage_const_0 ]
            L.append(phi)
    print "number of candidates = ", len(L)
    print L
    for i in L:
        a = n - i + _sage_const_1 
        b = sqrt(a*a-_sage_const_4 *n)
        if type(b) == sage.rings.integer.Integer:
            p = (a + b)/_sage_const_2 
            q = n/p
            break
    return [p,q]

def pminus1(n,B,a):
  k = factorial(B)
  b = a.powermod(k,n)
  return gcd(b-_sage_const_1 ,n)

def disc_log_diffie(p,w,a,b):
  for i in range(p):
    if w.powermod(i,p) == a:
      print i
      break


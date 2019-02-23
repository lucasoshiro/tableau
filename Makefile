#*******************************************************************************
#
#        Ep1 - MAC0239 – Introdução á Lógica e Verificação de Programas
#        Professora: Ana C. V. de Melo
#
#    15/10/2016
#
#    por:
#    Lucas Seiki Oshiro
#    Matheus Tavares Bernardino
#
#*******************************************************************************

.PHONY: all
all: Tableau.class Expressao.class

Tableau.class: Tableau.java
	javac Tableau.java

Expressao.class: Expressao.java
	javac $<

.PHONY: clean
clean:
	rm -f *.class

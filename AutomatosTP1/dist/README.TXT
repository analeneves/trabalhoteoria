PROFESSOR: Vin�cius Durelli, Teoria de Linguagem 2018/2

ALUNOS: Ana Let�cia Neves, Valney Faria

INSTRU��ES PARA ENTRADA:

-Basta indicar a localiza��o do seu arquivo txt na interface, e ele deve conter o seguinte formato do exemplo caso voc� queira alterar:

1�linha-> quantidade de s�mbolos do alfabeto, 
2� e 3� linhas-> quais ser�o os s�mbolos usados;
4� linha-> quantidade de estados; 
5� e 6� linhas-> quais s�o os estados;
7� linha->quantidade de transi��es, da forma "estado,simbolo,estado"; 
8� 9� e 10� linhas->transi��es; 
11� linha->estado inicial; 
12� linha-> quantidade de estados finais; 
13� linha-> estado final.

Altera��o no c�digo: A vari�vel String palavra = "babbabababbaba" dever� ser alterado pelo usu�rio para realizar a verifica��o de aceita��o. Abrir o c�digo correspondente ao simulador desejado. "entrada.txt":

"2

a

b

2

q0

q1

3

q0,a,q1

q0,b,q0

q1,b,q0

q0

1

q0"
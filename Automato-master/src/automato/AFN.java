package automato;

import java.util.ArrayList;
import java.util.LinkedList;

public class AFN {

	public void Verifica(String palavra) {
		// Realiza a Leitura do Arquivo de Entrada
		Leitura anaLe = new Leitura("entradaN.txt");

		// Esse valor armazena o numero de Nos
		int nNos = anaLe.getNumDeEstados();

		// Vetor que armazena todos os Nos
		NoN[] vetorNos = new NoN[nNos];

		// No Inicial
		NoN noInicial = new NoN();

		// Seta todos os estados como n�o-finais e n�o-iniciais
		for (int i = 0; i < vetorNos.length; i++) {
			vetorNos[i] = new NoN();
			vetorNos[i].setEstadoInicial(0);
			vetorNos[i].setEstadoFinal(0);
			vetorNos[i].setNome(anaLe.getEstados().get(i));
		}

		// Define o estado inicial
		for (int i = 0; i < vetorNos.length; i++) {
			if (vetorNos[i].getNome().equals(anaLe.getEstadoInicial())) {
				vetorNos[i].setEstadoInicial(1);
				noInicial = vetorNos[i];
			}
		}

		// Define os estados finais
		for (int i = 0; i < vetorNos.length; i++) {
			for (int j = 0; j < anaLe.getNumDeEstadosFinais(); j++) {
				if (vetorNos[i].getNome().equals(anaLe.getEstadosFinais().get(j))) {
					vetorNos[i].setEstadoFinal(1);
				}
			}
		}

		// Definir as transicoes para cada No
		String[][] trans = new String[anaLe.getNumDeTransicoes()][3];

		String s[] = new String[3];

		for (int i = 0; i < anaLe.getNumDeTransicoes(); i++) {
			s = anaLe.getTransicoes().get(i).split(",");
			for (int j = 0; j < 3; j++) {
				trans[i][j] = s[j];
			}

			int NoPosition = BuscaPosByName(vetorNos, trans[i][0]);

			// Cria uma transi��o temporaria
			Transition t = new Transition();
			t.setSimbolo(trans[i][1]);
			t.setEstadoOrigem(trans[i][0]);
			t.setEstadoDestino(trans[i][2]);

			// Adiciona a Transicao ao No
			vetorNos[NoPosition].getLTrans().add(t);
		}

		// Exibir a lista de transi��es de todos os Nos
		for (int i = 0; i < vetorNos.length; i++) {
			ShowTransList(vetorNos[i]);
		}

		boolean aux = false;

		aux = VerificaPalavra(palavra, vetorNos, anaLe.getSimbolos(), noInicial);

		if (aux) {
			System.out.printf("\nA palavra '%s' � aceita pelo automato!\n", palavra);
		} else {
			System.out.printf("\nA palavra '%s' n�o � aceita pelo automato!\n", palavra);
		}
	}

	private boolean VerificaPalavra(String palavra, NoN[] vetorNos, ArrayList<String> alfabeto, NoN noInicial) {
		// Listas que armazenar�o os N�s Temporariamente
		LinkedList<NoN> A = new LinkedList<NoN>();
		LinkedList<NoN> B = new LinkedList<NoN>();

		boolean flag = true;

		char[] vetPalavra = new char[palavra.length()];

		vetPalavra = palavra.toCharArray();

		// Verificacao por Simbolos Invalidos
		System.out.println("VALIDANDO CARACTERES DA PALAVRA INSERIDA");
		for (int i = 0; i < palavra.length(); i++) {
			// Verifica se todos os s�mbolos estao contidos no alfabeto
			if (alfabeto.contains(Character.toString(vetPalavra[i])) || palavra.equals("$")) {
				System.out.println("Caractere " + i + " V�lido: " + vetPalavra[i]);
				continue;
			}
			// Se algum caractere estranho for encontrado, a verificacao �
			// terminada e a palavra n�o � aceita
			else {
				System.out.println("CARACTERE INVALIDO: " + vetPalavra[i]);
				System.out.printf("\nO simbolo %s n�o pertence ao alfabeto!\n", vetPalavra[i]);
				flag = false;
				return flag;
			}
		}
		System.out.println("VALIDACAO DE CARACTERES CONCLUIDA!\n");

		// Verificacao de Palavra Vazia
		System.out.println("VERIFICANDO PALAVRA VAZIA");
		if (palavra.equals("$") && noInicial.getEstadoFinal() == 1) {
			System.out.println("Palavra Vazia!");
			flag = true;
			return flag;
		} else if (palavra.equals("$") && noInicial.getEstadoFinal() == 0) {
			System.out.println("Palavra Vazia!");
			flag = false;
			return flag;
		}
		System.out.println("N�O � PALAVRA VAZIA!\n");

		// Verificacao de Palavra com 1 Simbolo

		// Se o estado inicial � final, o tamanho da palavra � 1 e o No tem um
		// transi��o com o simbolo
		System.out.println("VALIDANDO PALAVRA DE UM S�MBOLO");
		if (palavra.length() == 1 && noInicial.getEstadoFinal() == 1 && ValidaInicialFinalSimbolo(noInicial, palavra)) {
			System.out.println("\nPalavra V�lida de um s�mbolo!");
			flag = true;
			// Aceitar
			return flag;
			// Senao, se o s�mbolo n�o estiver no alfabeto e
		} else if (palavra.length() == 1 && alfabeto.contains(palavra) != true) {
			System.out.println("\nPalavra Inv�lida de um s�mbolo!");
			flag = false;
			return flag;
		}
		System.out.println("A PALAVRA POSSUI MAIS DE UM S�MBOLO!\n");

		// Verifica��o de Palavra

		System.out.println("INICIANDO VERIFICA��O DE PALAVRA\n");

		// Adiciona o noInicial � Lista A
		A.add(noInicial);

		System.out.println("INICIO LISTA A: ");
		ShowL(A);

		// Para cada simbolo da palavra
		for (int i = 0; i < vetPalavra.length; i++) {
			String pal = Character.toString(vetPalavra[i]);
			System.out.println("\n::ANALISANDO SIMBOLO: " + pal);
			// Para cada No na Lista A
			for (NoN n : A) {
				// Para cada Transicao na Lista de Transicoes do No
				for (Transition t : n.getLTrans()) {
					// Se a Transi��o possui o simbolo
					if (t.getSimbolo().equals(pal)) {
						// Verificar se o No existe em B
						if (B.contains(BuscaNoByName(vetorNos, t.getEstadoDestino()))) {
							System.out.println("NO " + n.getNome() + " INSERIDO ANTERIORMENTE!");
						} else {
							// Buscar o No destino e adicion�-lo em B
							B.add(BuscaNoByName(vetorNos, t.getEstadoDestino()));
							System.out.println("NO " + t.getEstadoDestino() + " INSERIDO EM B!");
						}
					}
				}
			}

			// Esvazia a Lista A
			A.clear();

			// Copia a Lista B para A
			CopyListToAnotherList(A, B);

			// Esvazia a Lista B
			B.clear();
		}

		// Para cada No em A
		for (NoN n : A) {
			// Verificar se algum � final
			if (n.getEstadoFinal() == 1) {
				// Se sim, aceite
				System.out.println("\nESTADO FINAL ENCONTRADO EM A!");
				return true;
			}
		}
		// Sen�o, rejeite
		return false;
	}

	private NoN BuscaNoByName(NoN[] vetorNos, String estadoDestino) {
		// Para cada No em VetorNos
		for (NoN n : vetorNos) {
			// Se o nome do No for igual ao no destino buscado
			if (n.getNome().equals(estadoDestino)) {
				return n;
			}
		}
		// Sen�o
		System.out.println("NO N�O ENCONTRADO!");
		return null;
	}

	// Exibe uma Lista
	private void ShowL(LinkedList<NoN> L) {
		// Verifica��o de Lista Vazia
		if (L.size() == 0) {
			System.out.println("-> LISTA VAZIA!\n");
			return;
		}
		// Exibe o nome de cada um dos itens na lista
		for (NoN noN : L) {
			System.out.println(noN.getNome());
		}
	}

	// Copia uma Lista L2 para uma Lista L1
	private void CopyListToAnotherList(LinkedList<NoN> L1, LinkedList<NoN> L2) {
		for (NoN noN : L2) {
			L1.add(noN);
		}
	}

	// Busca a Posicao de um nome num dado vetor
	public int BuscaPosByName(NoN vetor[], String nome) {
		for (int i = 0; i < vetor.length; i++) {
			if (vetor[i].getNome().equals(nome)) {
				return i;
			}
		}
		return 0;
	}

	// Validacao para palavras de um Simbolo
	public boolean ValidaInicialFinalSimbolo(NoN noInicial, String simbolo) {
		for (int i = 0; i < noInicial.getLTrans().size(); i++) {
			if (noInicial.getLTrans().get(i).getSimbolo().equals(simbolo)) {
				// Se o nome do No Inicial for igual ao do No Destino da
				// transicao
				if (noInicial.getNome().equals(noInicial.getLTrans().get(i).getEstadoDestino())) {
					// Aceitar
					return true;
				}
			}
		}
		// Sen�o, Rejeitar
		return false;
	}

	// Exibe a Lista de Transi��es do N�
	public void ShowTransList(NoN no) {
		System.out.println("\nLista de Transi��es do No " + no.getNome() + ":");
		// Verifica��o de Lista Vazia
		if (no.getLTrans().size() == 0) {
			System.out.println("VAZIA!\n");
			return;
		}
		// Exibe as transi��es de forma formatada
		for (int i = 0; i < no.getLTrans().size(); i++) {
			System.out.print(i + ": " + no.getLTrans().get(i).getEstadoOrigem() + ",");
			System.out.print(no.getLTrans().get(i).getSimbolo() + ",");
			System.out.println(no.getLTrans().get(i).getEstadoDestino());
		}
	}

	// Verifica se algum dos estados de uma lista � final
	public boolean HasFinalState(ArrayList<NoN> Lista) {
		for (int i = 0; i < Lista.size(); i++) {
			// Se algum No na Lista � estado Final
			if (Lista.get(i).getEstadoFinal() == 1) {
				// Retornar Verdadeiro
				return true;
			}
		}
		// Senao, retornar Falso
		return false;
	}
}
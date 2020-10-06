package compilador1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	public static int local;
	public static boolean entrou;
	public static int posicao;
	public static int numeroToken;
	public static int n;
	public static String token;
	public static String texto = "";
	public static Character[] digitos = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	public static Character[] letras = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'x', 'w', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'W', 'Y', 'Z' };
	public static Character[] simbolos = { ';', '.', ',', '+', '-', '*', '(', ')', '<', '>', ':', '=', '{', '}', '/',
			'@' };
	public static Character[] simbolosTerminais = { ';', '.', '*', '(', ')', '=', '{', '}', ',', '@', '/' };

	public static void main(String[] args) {

		// ------------- leitura do arquivo de texto -----------//

		String local = "C:\\projetos\\java\\arquivo.txt";
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(local))) {
			line = br.readLine();
			while (line != null) {
				texto += line + '\n';
				line = br.readLine();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// ------------- inicio da chamada das funções -------------//

		posicao = 0;
		numeroToken = 0;
		//lexico();
		sintatico();
		System.out.println("Programa executado com sucesso!");

	}

	// ------------- funções do lexico -----------//

	public static void lexico() {
		int i = 0;
		n = 0;
		while (i < texto.length()) {
			boolean pular = true;
			for (char letra : letras) {
				if (texto.charAt(i) == letra) {
					if (!reconhecerPr(i).isEmpty()) {
						n++;
						System.out.println(n + "º Token: " + reconhecerPr(i));
						i += reconhecerPr(i).length() - 19;
						pular = false;
					} else {
						n++;
						System.out.println(n + "º Token: " + reconhecerIdt(i));
						i += reconhecerIdt(i).length() - 15;
						pular = false;
					}
				}
			}

			if (texto.charAt(i) == '/' || texto.charAt(i) == '@') {
				if (!reconhecerComentario(i).isEmpty()) {
					n++;
					System.out.println(n + "º Token: " + reconhecerComentario(i));
					i += reconhecerComentario(i).length() - 12;
					pular = false;
				}
			}

			if (i >= texto.length()) {
				break;
			}

			for (char digito : digitos) {
				if (texto.charAt(i) == digito) {
					n++;
					System.out.println(n + "º Token: " + reconhecerDigito(i));
					i += reconhecerDigito(i).length() - 8;
					pular = false;
				}
			}

			for (char simbolo : simbolos) {
				if (texto.charAt(i) == simbolo) {
					if (!reconhecerSimbolo(i).isEmpty()) {
						n++;
						System.out.println(n + "º Token: " + reconhecerSimbolo(i));
						i += reconhecerSimbolo(i).length() - 9;
						pular = false;
					}
				}
			}

			if (pular == true) {
				i++;
			}

		}
	}

	private static String reconhecerDigito(int i) {
		String numero = "";
		for (int a = i; a < texto.length(); a++) {
			boolean reconhece = false;
			for (char digito : digitos) {
				if (texto.charAt(a) == digito) {
					numero += digito;
					reconhece = true;
				}
			}

			if (texto.charAt(a) == ',') {
				reconhece = false;
				for (char digito : digitos) {
					if (texto.charAt(a + 1) == digito) {
						reconhece = true;
						numero += ',';
					}
				}
				if (reconhece == false) {
					// System.out.println(("Erro "+n+"º Token: dígito decimal não finalizado"));
					System.out.println(("Erro " + numeroToken + "º Token: dígito decimal não finalizado"));
					System.exit(0);
				}

				for (int b = a + 1; b < texto.length(); b++) {
					reconhece = false;
					for (char digito : digitos) {
						if (texto.charAt(b) == digito) {
							numero += digito;
							reconhece = true;
						}
					}
					if (reconhece == false) {
						return "Digito: " + numero;
					}
				}
			}

			if (reconhece == false) {
				return "Digito: " + numero;
			}
		}

		return "";
	}

	private static String reconhecerComentario(int i) {
		String comentario = "";
		if (texto.charAt(i) == '/') {
			if (texto.charAt(i + 1) == '/') {
				i += 2;
				for (int a = i; a < texto.length(); a++) {
					if (texto.charAt(a) == '/') {
						if (texto.charAt(a + 1) == '/') {
							return "Comentario: //" + comentario + "//";
						}
					}
					comentario += texto.charAt(a);
					if (a == texto.length() - 1) {
						System.out.println("Erro: comentário não finalizado");
						System.exit(0);
					}
				}
				return "Comentario: //" + comentario;
			}
		}

		if (texto.charAt(i) == '/') {
			if (texto.charAt(i + 1) == '*') {
				i += 2;
				for (int a = i; a < texto.length(); a++) {
					if (texto.charAt(a) == '*') {
						if (texto.charAt(a + 1) == '/') {
							return "Comentario: /*" + comentario + "*/";
						}
					}
					comentario += texto.charAt(a);
					if (a == texto.length() - 1) {
						System.out.println("Erro: comentário não finalizado");
						System.exit(0);
					}
				}
				return "Comentario: /*" + comentario;
			}
		}

		if (texto.charAt(i) == '@') {
			if (texto.charAt(i + 1) == '@') {
				i += 2;
				for (int a = i; a < texto.length(); a++) {
					if (texto.charAt(a) == '\n') {
						return "Comentario: @@" + comentario;
					}
					comentario += texto.charAt(a);
				}
			}
		}

		return "";
	}

	private static String reconhecerSimbolo(int i) {

		for (char simbolo : simbolosTerminais) {
			if (texto.charAt(i) == simbolo) {
				return "Simbolo: " + simbolo;
			}
		}

		if (texto.charAt(i) == '+') {
			if (texto.charAt(i + 1) == '+') {
				return "Simbolo: ++";
			} else {
				return "Simbolo: +";
			}
		}

		if (texto.charAt(i) == '-') {
			if (texto.charAt(i + 1) == '-') {
				return "Simbolo: --";
			} else {
				return "Simbolo: -";
			}
		}

		if (texto.charAt(i) == '>') {
			if (texto.charAt(i + 1) == '=') {
				return "Simbolo: >=";
			} else {
				return "Simbolo: >";
			}
		}

		if (texto.charAt(i) == '<') {
			if (texto.charAt(i + 1) == '=') {
				return "Simbolo: <=";
			}
			if (texto.charAt(i + 1) == '>') {
				return "Simbolo: <>";
			} else {
				return "Simbolo: <";
			}
		}

		if (texto.charAt(i) == ':') {
			if (texto.charAt(i + 1) == '=') {
				return "Simbolo: :=";
			} else {
				return "Simbolo: :";
			}
		}

		return "";
	}

	public static String reconhecerPr(int i) {

		if (texto.charAt(i) == 'p') {
			if (texto.charAt(i + 1) == 'r') {
				if (texto.charAt(i + 2) == 'o') {
					if (texto.charAt(i + 3) == 'g') {
						if (texto.charAt(i + 4) == 'r') {
							if (texto.charAt(i + 5) == 'a') {
								if (texto.charAt(i + 6) == 'm') {
									if (texto.charAt(i + 7) == 'a') {
										if (!letraOuDigito(i + 8)) {
											return "Palavra reservada: programa";
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 's') {
			if (texto.charAt(i + 1) == 'e') {
				if (!letraOuDigito(i + 2)) {
					return "Palavra reservada: se";
				}
				if (texto.charAt(i + 2) == 'n') {
					if (texto.charAt(i + 3) == 'a') {
						if (texto.charAt(i + 4) == 'o') {
							if (!letraOuDigito(i + 5)) {
								return "Palavra reservada: senao";
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'e') {
			if (texto.charAt(i + 1) == 'n') {
				if (texto.charAt(i + 2) == 't') {
					if (texto.charAt(i + 3) == 'a') {
						if (texto.charAt(i + 4) == 'o') {
							if (!letraOuDigito(i + 5)) {
								return "Palavra reservada: entao";
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'e') {
			if (texto.charAt(i + 1) == 'n') {
				if (texto.charAt(i + 2) == 'q') {
					if (texto.charAt(i + 3) == 'u') {
						if (texto.charAt(i + 4) == 'a') {
							if (texto.charAt(i + 5) == 'n') {
								if (texto.charAt(i + 6) == 't') {
									if (texto.charAt(i + 7) == 'o') {
										if (!letraOuDigito(i + 8)) {
											return "Palavra reservada: enquanto";
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'f') {
			if (texto.charAt(i + 1) == 'a') {
				if (texto.charAt(i + 2) == 'c') {
					if (texto.charAt(i + 3) == 'a') {
						if (!letraOuDigito(i + 4)) {
							return "Palavra reservada: faca";
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'a') {
			if (texto.charAt(i + 1) == 't') {
				if (texto.charAt(i + 2) == 'e') {
					if (!letraOuDigito(i + 3)) {
						return "Palavra reservada: ate";
					}
				}
			}
		}

		if (texto.charAt(i) == 'r') {
			if (texto.charAt(i + 1) == 'e') {
				if (texto.charAt(i + 2) == 'p') {
					if (texto.charAt(i + 3) == 'i') {
						if (texto.charAt(i + 4) == 't') {
							if (texto.charAt(i + 5) == 'a') {
								if (!letraOuDigito(i + 6)) {
									return "Palavra reservada: repita";
								}
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'i') {
			if (texto.charAt(i + 1) == 'n') {
				if (texto.charAt(i + 2) == 't') {
					if (texto.charAt(i + 3) == 'e') {
						if (texto.charAt(i + 4) == 'i') {
							if (texto.charAt(i + 5) == 'r') {
								if (texto.charAt(i + 6) == 'o') {
									if (!letraOuDigito(i + 7)) {
										return "Palavra reservada: inteiro";
									}
								}
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'r') {
			if (texto.charAt(i + 1) == 'e') {
				if (texto.charAt(i + 2) == 'a') {
					if (texto.charAt(i + 3) == 'l') {
						if (!letraOuDigito(i + 4)) {
							return "Palavra reservada: real";
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'c') {
			if (texto.charAt(i + 1) == 'a') {
				if (texto.charAt(i + 2) == 'r') {
					if (texto.charAt(i + 3) == 'a') {
						if (texto.charAt(i + 4) == 'c') {
							if (texto.charAt(i + 5) == 't') {
								if (texto.charAt(i + 6) == 'e') {
									if (texto.charAt(i + 7) == 'r') {
										if (texto.charAt(i + 8) == 'e') {
											if (!letraOuDigito(i + 9)) {
												return "Palavra reservada: caractere";
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'c') {
			if (texto.charAt(i + 1) == 'a') {
				if (texto.charAt(i + 2) == 's') {
					if (texto.charAt(i + 3) == 'o') {
						if (!letraOuDigito(i + 4)) {
							return "Palavra reservada: caso";
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'e') {
			if (texto.charAt(i + 1) == 's') {
				if (texto.charAt(i + 2) == 'c') {
					if (texto.charAt(i + 3) == 'o') {
						if (texto.charAt(i + 4) == 'l') {
							if (texto.charAt(i + 5) == 'h') {
								if (texto.charAt(i + 6) == 'a') {
									if (!letraOuDigito(i + 7)) {
										return "Palavra reservada: escolha";
									}
								}
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'f') {
			if (texto.charAt(i + 1) == 'i') {
				if (texto.charAt(i + 2) == 'm') {
					if (!letraOuDigito(i + 3)) {
						return "Palavra reservada: fim";
					}
					if (texto.charAt(i + 3) == 's') {
						if (texto.charAt(i + 4) == 'e') {
							if (!letraOuDigito(i + 5)) {
								return "Palavra reservada: fimse";
							}
						}
					}
					if (texto.charAt(i + 3) == 'e') {
						if (texto.charAt(i + 4) == 's') {
							if (texto.charAt(i + 5) == 'c') {
								if (texto.charAt(i + 6) == 'o') {
									if (texto.charAt(i + 7) == 'l') {
										if (texto.charAt(i + 8) == 'h') {
											if (texto.charAt(i + 9) == 'a') {
												if (!letraOuDigito(i + 10)) {
													return "Palavra reservada: fimescolha";
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'p') {
			if (texto.charAt(i + 1) == 'r') {
				if (texto.charAt(i + 2) == 'o') {
					if (texto.charAt(i + 3) == 'c') {
						if (texto.charAt(i + 4) == 'e') {
							if (texto.charAt(i + 5) == 'd') {
								if (texto.charAt(i + 6) == 'i') {
									if (texto.charAt(i + 7) == 'm') {
										if (texto.charAt(i + 8) == 'e') {
											if (texto.charAt(i + 9) == 'n') {
												if (texto.charAt(i + 10) == 't') {
													if (texto.charAt(i + 11) == 'o') {
														if (!letraOuDigito(i + 12)) {
															return "Palavra reservada: procedimento";
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'f') {
			if (texto.charAt(i + 1) == 'u') {
				if (texto.charAt(i + 2) == 'n') {
					if (texto.charAt(i + 3) == 'c') {
						if (texto.charAt(i + 4) == 'a') {
							if (texto.charAt(i + 5) == 'o') {
								if (!letraOuDigito(i + 6)) {
									return "Palavra reservada: funcao";
								}
							}
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'd') {
			if (texto.charAt(i + 1) == 'e') {
				if (!letraOuDigito(i + 2)) {
					return "Palavra reservada: de";
				}
			}
		}

		if (texto.charAt(i) == 'p') {
			if (texto.charAt(i + 1) == 'a') {
				if (texto.charAt(i + 2) == 'r') {
					if (texto.charAt(i + 3) == 'a') {
						if (!letraOuDigito(i + 4)) {
							return "Palavra reservada: para";
						}
					}
				}
			}
		}

		if (texto.charAt(i) == 'i') {
			if (texto.charAt(i + 1) == 'n') {
				if (texto.charAt(i + 2) == 'i') {
					if (texto.charAt(i + 3) == 'c') {
						if (texto.charAt(i + 4) == 'i') {
							if (texto.charAt(i + 5) == 'o') {
								if (!letraOuDigito(i + 6)) {
									return "Palavra reservada: inicio";
								}
							}
						}
					}
				}
			}
		}

		return "";
	}

	public static String reconhecerIdt(int i) {
		String identificador = "";
		while (i < texto.length()) {
			boolean reconhece = false;
			for (char letra : letras) {
				if (texto.charAt(i) == letra) {
					identificador += letra;
					reconhece = true;
				}
			}

			for (char digito : digitos) {
				if (texto.charAt(i) == digito) {
					identificador += digito;
					reconhece = true;
				}
			}

			if (texto.charAt(i) == '&') {
				identificador += '&';
				return "Identificador: " + identificador;
			}

			if (reconhece == false) {
				return "Identificador: " + identificador;
			}

			i++;
		}

		return "";
	}

	public static boolean letraOuDigito(int i) {
		for (char digito : digitos) {
			if (texto.charAt(i) == digito) {
				return true;
			}
		}
		for (char letra : letras) {
			if (texto.charAt(i) == letra) {
				return true;
			}
		}
		return false;
	}

	// ------------- funções do sintatico -----------//

	public static String lexico1() {
		int i = posicao;
		while (i < texto.length()) {
			for (char letra : letras) {
				if (texto.charAt(i) == letra) {
					if (!reconhecerPr(i).isEmpty()) {
						local = posicao;
						posicao += reconhecerPr(i).length() - 19;
						return reconhecerPr(i);
					} else {
						local = posicao;
						posicao += reconhecerIdt(i).length() - 15;
						return reconhecerIdt(i);
					}
				}
			}

			if (texto.charAt(i) == '/' || texto.charAt(i) == '@') {
				if (!reconhecerComentario(i).isEmpty()) {
					local = posicao;
					posicao += reconhecerComentario(i).length() - 12;
					return reconhecerComentario(i);
				}
			}

			if (i >= texto.length()) {
				break;
			}

			for (char digito : digitos) {
				if (texto.charAt(i) == digito) {
					local = posicao;
					posicao += reconhecerDigito(i).length() - 8;
					return reconhecerDigito(i);
				}
			}

			for (char simbolo : simbolos) {
				if (texto.charAt(i) == simbolo) {
					if (!reconhecerSimbolo(i).isEmpty()) {
						local = posicao;
						posicao += reconhecerSimbolo(i).length() - 9;
						return reconhecerSimbolo(i);
					}
				}
			}

			i++;
			posicao++;
		}

		return "";
	}

	public static void obterToken() {
		numeroToken++;
		token = lexico1();
		while (token.contains("Comentario: ")) {
			token = lexico1();
		}
	}

	public static void sintatico() {
		obterToken();
		mensagemErro("Palavra reservada: programa");
		obterToken();
		mensagemErro("Identificador");
		obterToken();
		mensagemErro(";");
		obterToken();
		bloco();

	}

	public static int linhaErro() {
		int linha = 0;
		if (texto.charAt(local) != '\n') {
			linha += 1;
		}
		for (int i = 0; i <= local; i++) {
			if (texto.charAt(i) == '\n') {
				linha += 1;
			}
		}

		if (linha == 0) {
			return linha++;
		}
		return linha;
	}

	public static void mensagemErro(String palavra) {
		if (!token.contains(palavra)) {
			System.out.println("Erro Linha " + linhaErro() + ", Token " + numeroToken + ": " + palavra + " esperado");
			System.exit(0);
		}

	}

	public static String tipo() {

		if (token.contains("Palavra reservada: inteiro")) {
			return "inteiro";
		}
		if (token.contains("Palavra reservada: caractere")) {
			return "caractere";
		}
		if (token.contains("Palavra reservada: real")) {
			return "real";
		}

		return "";

	}

	public static void bloco() {

		if (!tipo().isEmpty()) {
			declaraTipo();
		}
		if (token.contains("Identificador")) {
			declaraVar();
		}
		if (token.contains("Palavra reservada: funcao")) {
			declaraFuncao();
		}
		if (token.contains("Palavra reservada: procedimento")) {
			declaraProcedimento();
		}
		if (token.contains("Palavra reservada: inicio")) {
			comandoComposto();
		}
		if (token.contains("Palavra reservada: ") || token.contains("Simbolo: ")) {
			System.out.println("Erro Linha " + linhaErro() + ", Token " + numeroToken + ": Token não esperado");
			System.exit(0);
		}
	}

	public static void comandoComposto() {
		entrou = false;
		comandoSemRotulo();
		mensagemErro("Palavra reservada: fim");
		obterToken();
		bloco();
	}

	public static void comandoSemRotulo() {
		if (entrou == false) {
			obterToken();
		}
		if (token.equals("Palavra reservada: se")) {
			comandoCondicional();
		}
		if (token.contains("Palavra reservada: enquanto")) {
			comandoRepetitivo();
		}
		if (token.contains("Identificador: leia")) {
			leia();
		}
		if (token.contains("Identificador: imprima")) {
			imprima();
		}
		if (token.contains("Identificador")) {
			obterToken();

			if (token.contains("=")) {
				atribuicao();
			}
			if (token.contains("(")) {
				chamarFuncao();
			}

		}

	}

	public static void comandoRepetitivo() {
		obterToken();
		expressao();
		obterToken();
		mensagemErro("Palavra reservada: faca");
		comandoSemRotulo();
	}

	public static void comandoCondicional() {
		entrou = false;
		obterToken();
		expressao();
		obterToken();
		mensagemErro("Palavra reservada: entao");
		comandoSemRotulo();
		if (token.contains("Palavra reservada: senao")) {
			comandoSemRotulo();
		}
		entrou = true;
		comandoSemRotulo();
	}

	public static void chamarFuncao() {
		expressao();
		comandoSemRotulo();
	}

	public static void leia() {
		obterToken();
		mensagemErro("(");
		obterToken();
		mensagemErro("Identificador");
		listaIdentificadores();
		comandoSemRotulo();
	}

	public static void imprima() {
		obterToken();
		expressao();
		comandoSemRotulo();
	}

	public static void listaIdentificadores() {

		while (!token.contains(")")) {
			obterToken();
			if (token.contains(")")) {
				continue;
			}
			mensagemErro(",");
			obterToken();
			mensagemErro("Identificador");
		}
		obterToken();
		mensagemErro(";");

	}

	private static void atribuicao() {
		obterToken();
		expressao();
		comandoSemRotulo();

	}

	public static void expressao() {
		mensagemErro("(");
		obterToken();
		while (!token.contains(")")) {
			if (fator().isEmpty()) {
				mensagemErro("Fator");
			}

			while (!token.contains(")")) {

				if (!operador().isEmpty()) {
					obterToken();
				} else {
					mensagemErro(")");
				}

				if (fator().isEmpty()) {
					mensagemErro("Fator");
				}
			}
		}
		obterToken();
		mensagemErro(";");
	}

	public static String fator() {
		if (token.contains("Digito: ")) {
			obterToken();
			return "digito";
		}

		if (token.contains("Identificador")) {
			obterToken();
			if (chamadaFuncao()) {
				return "funcao";
			} else {
				return "identificador";
			}
		}

		return "";
	}

	public static boolean relacao() {
		if (token.contains("=") || token.contains("<>") || token.contains("<") || token.contains("<=")
				|| token.contains(">") || token.contains(">=")) {
			return true;
		}
		return false;
	}

	public static boolean chamadaFuncao() {
		if (token.contains("(")) {
			expressao();
			obterToken();
			return true;
		}

		return false;
	}

	public static String operador() {
		if (token.contains("+") || token.contains("-") || token.contains("*") || token.contains("/")
				|| token.contains(",")) {
			return "operador";
		}
		if (relacao()) {
			return "relacao";
		}

		return "";
	}

	public static void declaraProcedimento() {
		obterToken();
		mensagemErro("Identificador");
		obterToken();
		if (token.contains("(")) {
			parametrosFormais();
		}
		mensagemErro(";");
		obterToken();
		bloco();

	}

	public static void parametrosFormais() {
		obterToken();
		while (!token.contains(")")) {
			if (tipo().isEmpty()) {
				mensagemErro("tipo");
			}
			obterToken();
			mensagemErro("Identificador");
			obterToken();
			if (token.contains(",")) {
				obterToken();
				if (token.contains(")")) {
					mensagemErro("tipo");
				}
				continue;
			}

			mensagemErro(")");

		}
		obterToken();
	}

	public static void declaraFuncao() {
		obterToken();
		mensagemErro("Identificador");
		obterToken();
		mensagemErro("(");
		parametrosFormais();
		mensagemErro("=");
		obterToken();
		if (tipo().isEmpty()) {
			mensagemErro("tipo");
		}
		obterToken();
		mensagemErro(";");
		obterToken();
		bloco();

	}

	public static void declaraVar() {
		obterToken();
		mensagemErro("=");
		obterToken();
		mensagemErro("Identificador");
		obterToken();
		mensagemErro(";");
		obterToken();
		bloco();
	}

	public static void declaraTipo() {
		obterToken();
		mensagemErro("Identificador");
		obterToken();
		if (token.contains(",")) {
			while (!token.contains(";")) {
				mensagemErro(",");
				obterToken();
				mensagemErro("Identificador");
				obterToken();
				if (token.contains("=")) {
					obterToken();
					mensagemErro("Identificador");
					obterToken();
				}

			}
		}
		if (token.contains("=")) {
			while (!token.contains(";")) {
				obterToken();
				mensagemErro("Identificador");
				obterToken();
				if (token.contains(";")) {
					continue;
				}
				mensagemErro(",");
				if (token.contains(",")) {
					while (!token.contains(";")) {
						mensagemErro(",");
						obterToken();
						mensagemErro("Identificador");
						obterToken();
						if (token.contains("=")) {
							obterToken();
							mensagemErro("Identificador");
							obterToken();
						}
					}
				}
			}
		}

		mensagemErro(";");
		obterToken();
		bloco();
	}
}

/*******************************************************************************

        Ep1 - MAC0239 – Introdução á Lógica e Verificação de Programas
        Professora: Ana C. V. de Melo

    15/10/2016

    por:
    Lucas Seiki Oshiro
    Matheus Tavares Bernardino

*******************************************************************************/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

/* Representa um tableau. */
public class Tableau {
    ArrayList<Expressao> expressoesIniciais;

    static final int ALFA = 0;
    static final int BETA = 1;

    /* Resultados de uma expansão: as duas expressões e o tipo de expansão, alfa
       ou beta. */
    private class Retorno {
        int tipo;
        Expressao e1;
        Expressao e2;

        /* Constrói um objeto de retorno da expansão. */
        public Retorno (int tipo, Expressao e1, Expressao e2) {
            this.tipo = tipo;
            this.e1 = e1;
            this.e2 = e2;
        }
    }

    /* Constrói um tableau expresões fornecidas por parâmetro. */
    public Tableau (Expressao[] expressoes) {
        expressoesIniciais = new ArrayList<Expressao> ();
        for (Expressao e : expressoes) {
            if (e.tipo == Expressao.LITERAL)
                expressoesIniciais.add (expressoesIniciais.size (), e);
            else expressoesIniciais.add (0, e);
        }
    }

    /* Devolve um HashMap com literais e seus valores, de forma que eles
       invalidem o sequente. Caso não haja possibilidade de invalidar o
       sequente, é devolvido null. */
    public HashMap<Character, Boolean> resolve () {
        return resolve (expressoesIniciais);
    }

    /* Devolve um HashMap com literais e seus valores, de forma que não haja
       contradição nas expressões dadas por parâmetro. Caso haja uma contradição
       é devolvido null. */
    private HashMap<Character, Boolean> resolve
        (ArrayList<Expressao> expressoes) {
        Expressao e = expressoes.remove (0);
        if (e.tipo == Expressao.LITERAL) {
            expressoes.add (0, e);
            HashMap <Character, Boolean> literais;
            literais = new HashMap <Character, Boolean> ();

            for (Expressao exp : expressoes) {
                Boolean val = literais.get (new Character (exp.lit));
                if (val == null)
                    literais.put (new Character (exp.lit),
                                  new Boolean (exp.valor));
                else if ((boolean) val != exp.valor)
                    return null;
            }
            return literais;
        }
        else {
            Retorno ret = expansao (e);
            if (ret.tipo == ALFA) {
                if (ret.e1.tipo == Expressao.LITERAL)
                    expressoes.add (expressoes.size (), ret.e1);
                else
                    expressoes.add (0, ret.e1);
                if (ret.e2 != null) {
                    if (ret.e2.tipo == Expressao.LITERAL)
                        expressoes.add (expressoes.size (), ret.e2);
                    else
                        expressoes.add (0, ret.e2);
                }
                return resolve (expressoes);
            }
            else {
                ArrayList<Expressao> nova;
                nova = new ArrayList<Expressao> (expressoes);
                
                if (ret.e1.tipo == Expressao.LITERAL)
                    expressoes.add (expressoes.size (), ret.e1);
                else
                    expressoes.add (0, ret.e1);
                if (ret.e2.tipo == Expressao.LITERAL)
                    nova.add (nova.size (), ret.e2);
                else
                    nova.add (0, ret.e2);

                HashMap<Character, Boolean> solucao = resolve (expressoes);
                return solucao != null ? solucao : resolve (nova);
            }
        }
    }

    /* Expande a expressão dada por parâmetro, devolvendo o um objeto contendo
       as expressões geradas, e o tipo de expansão feito. */
    private Retorno expansao (Expressao exp) {
        switch (exp.id) {
        case 'N':
            exp.esq.valor = !exp.valor;
            return new Retorno (ALFA, exp.esq, null);

        case 'A':
            exp.esq.valor = exp.dir.valor = exp.valor;
            return new Retorno (exp.valor ? ALFA : BETA, exp.esq, exp.dir);

        case 'O':
            exp.esq.valor = exp.dir.valor = exp.valor;
            return new Retorno (exp.valor ? BETA : ALFA, exp.esq, exp.dir);

        case 'I':
            exp.esq.valor = !exp.valor;
            exp.dir.valor = exp.valor;
            return new Retorno (exp.valor ? BETA : ALFA, exp.esq, exp.dir);

        default:
            return null;
        }
    }

    /* Recebe como parâmetro uma string contendo uma expressão lógica escrita
       por extenso, e devolve um objeto do tipo Expressão contendo a mesma
       expressão numa estrutura de dados que possa ser resolvida pelo programa. 
    */
    public static Expressao reconheceExpressao (String s) {
        if (s.charAt (0) != '(') return new Expressao (true, s.charAt(0));
        if (s.length () == 3) return new Expressao (true, s.charAt (1));
        if (s.substring (1, 4).equals (".N."))
            return new Expressao (true, 'N', reconheceExpressao (s.substring
                                              (4, s.length () - 1)), null);
        int contaParenteses = 0;
        int i;
        for (i = 1; contaParenteses != 0 || s.charAt(i) != '.'; i++)
            if (s.charAt(i) == '(') contaParenteses++;
            else if (s.charAt(i) == ')') contaParenteses--;

        return new Expressao (true, s.charAt (i + 1),
                              reconheceExpressao (s.substring (1, i)),
                              reconheceExpressao (s.substring (i + 3,
                                                               s.length () - 1)));
    }

    /* Lê a entrada. Devolve um vetor de expressões, sendo todas elas premissas
       (com valor verdadeiro), exceto a última, que é a consequência lógica (que
       recebe valor falso). */
    public static Expressao[] leEntrada () {
        Scanner s = new Scanner (System.in);
        String[] strings = s.nextLine ().replace (" ", "").split (",");
        Expressao[] expressoes = new Expressao[strings.length];
        
        for (int i = 0; i < strings.length; i++)
            expressoes[i] = reconheceExpressao (strings[i]);
        expressoes[strings.length - 1].valor = false;
        
        return expressoes;
    }

    /* Main. Recebe na entrada padrão as premissas e as consequencias lógicas em
       uma linha, e escreve na saída padrão se tal sequente é válido ou se é
       inválido, e caso seja inválido escreve uma valoração para os literais que
       invalidam o sequente. */
    public static void main (String[] args) {
        Tableau tableau = new Tableau (leEntrada ());
        HashMap<Character, Boolean> solucao = tableau.resolve ();
        
        if (solucao == null) System.out.println ("Válido.");
        else {
            System.out.print ("Inválido.\nContra-exemplo: ");
            for (Character lit : solucao.keySet ())
                System.out.print ((char) lit +
                                  ((boolean) solucao.get (lit) ? "=T " : "=F "));
            System.out.println ();
        }
    }
}

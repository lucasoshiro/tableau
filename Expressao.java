/*******************************************************************************

        Ep1 - MAC0239 – Introdução á Lógica e Verificação de Programas
        Professora: Ana C. V. de Melo

    15/10/2016

    por:
    Lucas Seiki Oshiro
    Matheus Tavares Bernardino

*******************************************************************************/

/* Armazena uma expressão, e se ela é falsa ou verdadeira. */
public class Expressao {
    int tipo;
    boolean valor;
    char lit;
    char id;
    Expressao esq;
    Expressao dir;

    static final int NAO_LITERAL = 0;
    static final int LITERAL = 1;

    /* Constroi uma expressão que contem apenas um literal lit, e seu valor. */
    public Expressao (boolean valor, char lit) {
        this.tipo = LITERAL;
        this.valor = valor;
        this.lit = lit;
        this.esq = this.dir = null;
        this.id = '\0';
    }

    /* Constroi uma expressao composta por um operador id, suas subexpressões
       esq e dir (podendo ser dir igual a null caso só haja uma subexpressão), e
       o valor da expressão. */
    public Expressao (boolean valor, char id, Expressao esq, Expressao dir) {
        this.tipo = NAO_LITERAL;
        this.valor = valor;
        this.lit = '\0';
        this.esq = esq;
        this.dir = dir;
        this.id = id;
    }

    /* Devolve uma string que representa a expressão por extenso. */
    public String toString () {
        if (tipo == LITERAL) return "" + lit;
        if (id == 'N') return "(.N." + esq + ")";
        String s = "(";
        s += esq;
        s += "." + id + ".";
        s += dir;
        s += ")";
        return s;
    }
}

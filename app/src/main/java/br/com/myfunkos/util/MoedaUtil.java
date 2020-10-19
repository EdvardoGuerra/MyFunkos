package br.com.myfunkos.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MoedaUtil {

    public static final String PORTUGUES = "pt";
    public static final String BRASIL = "br";
    public static final String FORMATO_PADRAO = "R$";
    public static final String FORMATO_COM_ESPACO = "R$ ";

    public static String formataParaReal(Double valor) {
        NumberFormat formato =
                DecimalFormat.getCurrencyInstance(new Locale(PORTUGUES, BRASIL));
        return formato
                .format(valor)
                .replace(FORMATO_PADRAO, FORMATO_COM_ESPACO);
    }
}

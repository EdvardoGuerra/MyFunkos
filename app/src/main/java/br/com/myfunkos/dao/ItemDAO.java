package br.com.myfunkos.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.myfunkos.model.Item;

public class ItemDAO {
    public List<Item> lista() {
        List<Item> itens = new ArrayList<>();
//        List<Item> itens = new ArrayList<>(Arrays.asList(
//                new Item("Betty Boop Enfermeira", "betty_boop",
//                        "Animation #524", "Betty Boop",
//                        "09/06/2020", 99.00),
//                new Item("Gohan", "gohan",
//                        "Animation #104", "Dragon Ball Z",
//                        "05/08/2019", 120.00),
//                new Item("Morte", "morte",
//                        "Heroes #234", "DC Super Heroes",
//                        "07/07/2019", 200.00),
//                new Item("Piccolo", "piccolo",
//                        "Animation #670", "Dragon Ball Z",
//                        "20/02/2020", 145.00),
//                new Item("Yamcha & Pual", "yamcha_pual",
//                        "Animation #531", "Dragon Ball Z",
//                        "15/11/2018", 100.00)
//        ));
        return itens;
    }
}

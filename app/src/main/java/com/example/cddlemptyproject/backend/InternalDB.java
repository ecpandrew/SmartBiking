package com.example.cddlemptyproject.backend;

import com.example.cddlemptyproject.logic.data.model.Group;
import com.example.cddlemptyproject.logic.data.model.RoutesAvailable;
import com.example.cddlemptyproject.logic.data.model.RoutesPerformed;
import com.example.cddlemptyproject.logic.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InternalDB {

    public static User getLoggedInUserAndre(){
        return new User("André Cardoso", "andre.cardoso@lsdi.ufma.br");
    }
    public static User getLoggedInUserThiago(){
        return new User("Thiago Wallass", "thiago.wallass@lsdi.ufma.br");
    }


    public static List<String> getMyGroups(String email){
        List<String> resource_ids = new ArrayList<>();

        switch (email) {
            case "andre.cardoso@lsdi.ufma.br":
                resource_ids.add("5c88e1e3-f5a0-4c1b-9682-97cd3f5f5d9a");
                resource_ids.add("9a26b506-5e8e-4230-a150-cd5e1e29c856");
                resource_ids.add("b5d934a6-3448-417a-8fc9-963df23676a4");

                return resource_ids;
            case "thiago.wallass@lsdi.ufma.br":
                resource_ids.add("4a29e932-2a8d-47d7-882b-cb6753e4bffa");
                return resource_ids;
            case "fssilva@lsdi.ufma.br":
                resource_ids.add("9a26b506-5e8e-4230-a150-cd5e1e29c856");
                return resource_ids;
            case "ariel@lsdi.ufma.br":
                resource_ids.add("011fd32f-702c-447b-ba70-7fd3751a23c2");
                return resource_ids;
            default:
                return resource_ids;
        }
    }

    public static List<Group> getAllGroups(){


        return Arrays.asList(
                new Group("326dbfc1-a4f3-40de-a355-800cb577888f", "Grupo de Ciclismo Iniciante", "lcmuniz@lsdi.ufma.br" ),
                new Group("4a29e932-2a8d-47d7-882b-cb6753e4bffa", "Grupo de Ciclismo Intermediario", "thiago.wallass@lsdi.ufma.br"),
                new Group("5c88e1e3-f5a0-4c1b-9682-97cd3f5f5d9a", "Grupo de Ciclismo Avançado", "andre.cardoso@lsdi.ufma.br"),
                new Group("9a26b506-5e8e-4230-a150-cd5e1e29c856", "Grupo de Ciclismo FSSILVA", "fssilva@lsdi.ufma.br"),
                new Group("011fd32f-702c-447b-ba70-7fd3751a23c2", "Grupo de Ciclismo do Ariel", "ariel@lsdi.ufma.br"),
                new Group("b5d934a6-3448-417a-8fc9-963df23676a4", "Grupo de Ciclismo de TESTES 2", "andre.cardoso@lsdi.ufma.br")

                );

    }


    public static List<RoutesAvailable> getAvailableRoutes(){


        return Arrays.asList(
                new RoutesAvailable("Circuito Litorânea Avançado"),
                new RoutesAvailable("Circuito Litorânea Intermediário"),
                new RoutesAvailable("Circuito Litorânea Iniciante"),
                new RoutesAvailable("Circuito UFMA"),
                new RoutesAvailable("Circuito Beira-mar"),
                new RoutesAvailable("Circuito Cohatrac-Litorânea"),
                new RoutesAvailable("Circuito UFMA-Litorânea")
                );
    }
    public static List<RoutesPerformed> getPerformedRoutes(){


        return Arrays.asList(
                new RoutesPerformed("Passeio Ciclista 1º Edição"),
                new RoutesPerformed("Passeio Ciclista 2º Edição"),
                new RoutesPerformed("Passeio Ciclista 3º Edição"),
                new RoutesPerformed("Passeio de Natal"),
                new RoutesPerformed("Pedal Resistencia"),
                new RoutesPerformed("Passeio aos Domingos 1"),
                new RoutesPerformed("Passeio aos Domingos 2")
        );
    }


    public static List<RoutesAvailable> getMembers(){


        return Arrays.asList(
                new RoutesAvailable("André Luiz Almeida Cardoso"),
                new RoutesAvailable("Thiago Wallass"),
                new RoutesAvailable("Fssilva"),
                new RoutesAvailable("Jean"),
                new RoutesAvailable("Ariel")
        );
    }

}

package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.execao.ErroDeConversaoDeAnoException;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalComBusca {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        var filme = "";
        List<Titulo> filmes = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        while (!filme.equalsIgnoreCase("sair")) {
            System.out.println("Digite o nome do filme:");
            filme = scanner.nextLine();
            String endereco = "http://www.omdbapi.com/?t=" + filme.replace(" ", "+") + "&apikey=9a77f3df";
            System.out.println(endereco);

            try {
                if (!filme.equalsIgnoreCase("sair")) {
                    HttpClient client = HttpClient.newHttpClient();

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(endereco))
                            .build();
                    HttpResponse<String> response = client
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    String json = response.body();
                    System.out.println(json);

                    TituloOmdb meuTituloOmdb = gson.fromJson(json, TituloOmdb.class);
                    System.out.println(meuTituloOmdb);

                    Titulo meuTitulo = new Titulo(meuTituloOmdb);
                    System.out.println("Titulo já convertido");
                    System.out.println(meuTitulo);

                    filmes.add(meuTitulo);
                }

            } catch (NumberFormatException e) {
                System.out.println("Ocorreu um erro na formatação de número:");
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Ocorreu um erro na declaração de um argumento:");
                System.out.println(e.getMessage());
            } catch (ErroDeConversaoDeAnoException e) {
                System.out.println(e.getMessage());
            }
        }

        FileWriter escrita = new FileWriter("filmes.json");
        escrita.write(gson.toJson(filmes));
        escrita.close();

        System.out.println(filmes);
        System.out.println("Execução finalizada!");
    }
}

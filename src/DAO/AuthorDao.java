/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DAO.Interfaces.IAuthorDao;
import model.Author;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorDao implements IAuthorDao {
    private String authorPath = "files/author.csv";

    public AuthorDao() throws Exception {
    }

    @Override
    public ArrayList getAllAuthors(){
        try(InputStream is = new FileInputStream(this.authorPath);
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
        ) {
            String linha;
            ArrayList<Author> authorListGlobal = new ArrayList<>();

            while ((linha = br.readLine()) != null){

                String[] authors = linha.split(",");

                int id = Integer.parseInt(authors[0]);
                String firstName = authors[1];
                String lastName = authors[2];

                var authorModel = new Author(id,firstName,lastName);

                authorListGlobal.add(authorModel);
            }
            return authorListGlobal;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Author getAuthorByNames(String fName, String lName) {
        try(InputStream is = new FileInputStream(this.authorPath);
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
        ) {
            String linha;
            while ((linha = br.readLine()) != null){

                String[] authors = linha.split(",");

                int id = Integer.parseInt(authors[0]);
                String firstName = authors[1];
                String lastName = authors[2];

                if (firstName.equals(fName) && lastName.equals(lName)){
                    return new Author(id, firstName, lastName);
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getByNameReturnId(String fName) {
        try(InputStream is = new FileInputStream(this.authorPath);
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
        ) {
            String linha;
            while ((linha = br.readLine()) != null){

                String[] authors = linha.split(",");

                int id = Integer.parseInt(authors[0]);
                String firstNane = authors[1];
                String lastName = authors[2];

                String nameAuthor = firstNane + " " + lastName;



                if (nameAuthor.equals(fName)){;
                    return id;
                }
            }
            return -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void insertAuthor(String fName, String name) throws SQLException {
        try (FileWriter fw = new FileWriter(this.authorPath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(getNextAuthorId() + "," +fName + "," + name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editAuthor(String name, String fName, int author_id) throws SQLException {
       try(InputStream is = new FileInputStream(this.authorPath);
           InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
           BufferedReader br = new BufferedReader(isr)){
           String linha;
           ArrayList<Author> authorListGlobal = new ArrayList<>();
           String lineToEdit = String.valueOf(author_id);

           while ((linha = br.readLine()) != null) {

               String[] authors = linha.split(",");

               int authorId = Integer.parseInt(authors[0]);
               String firstName = authors[1];
               String lastName = authors[2];

               if (authorId == author_id) {
                   lastName = name;
                   firstName = fName;
               }

               authorListGlobal.add(new Author(authorId,firstName, lastName));
           }

           try (FileWriter fw = new FileWriter(this.authorPath, false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
               for (Author author : authorListGlobal) {
                   out.println(author.getId() + "," + author.getLastName() + "," + author.getFirstName());
               }
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }

    private int getNextAuthorId() throws IOException {
        int highestId = 0;

        try (InputStream is = new FileInputStream(this.authorPath);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] authorData = line.split(",");
                if (authorData.length >= 1) {
                    try {
                        int currentId = Integer.parseInt(authorData[0]);
                        highestId = Math.max(highestId, currentId);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing ID: " + line);
                    }
                }
            }
        }
        return highestId + 1;
    }


    @Override
    public Author getAuthorById(int id) throws SQLException {
        return null;
    }

    @Override
    public void deleteAuthor(int author_id) throws SQLException {
        try(InputStream is = new FileInputStream(this.authorPath);
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr)){
            String linha;
            ArrayList<Author> authorListGlobal = new ArrayList<>();
            String lineToDelete = String.valueOf(author_id);

            while ((linha = br.readLine()) != null) {

                String[] authors = linha.split(",");

                int authorId = Integer.parseInt(authors[0]);
                String lastName = authors[1];
                String firstName = authors[2];

                if (authorId != author_id) {
                    authorListGlobal.add(new Author(authorId, lastName, firstName));
                }
            }

            try (FileWriter fw = new FileWriter(this.authorPath, false);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                for (Author author : authorListGlobal) {
                    out.println(author.getId() + "," + author.getLastName() + "," + author.getFirstName());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void deleteRelationAuthorBooks(int author_id) throws SQLException {

    }

}

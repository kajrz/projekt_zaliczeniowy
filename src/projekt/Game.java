package projekt;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    //przycisk rozpoczynający grę
    private JButton load;
    //okno gry
    private JPanel MainWindow;
    //labele wyświetlające komunikaty dla użytkownika
    private JLabel KomunikatWybierz;
    private JLabel wyswietlHaslo;
    private JLabel szanse;
    private JLabel progres;
    private JLabel podpowiedz;
    //przycisk wyświetlający zasady gry
    private JButton zasady;
    //przycisk zamykający grę
    private JButton zamknij;
    //ścieżka do pliku
    static final String path = "c:" + File.separator + "java";
    //zmienna przechowująca hasło do odgadnięcia
    public String haslo;
    //lista haseł wczytana z pliku
    public List<String> wyrazy = new ArrayList<>();
    public String statusLoadu;
    //znaki do listy rozwijanej
    public Character[] znaki ={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    //lista rozwijana ze znakami
    private JComboBox literki;
    //dodatkowo labele z kategorią hasła i podpowiedzią dla użytkownika
    private JLabel kat;
    private JLabel pod;
    //przycisk wyświetlający zasady obsługi pliku text.txt
    private JButton zasRed;
    //ilość znnaków w haśle i ilość szans
    public int b, lifes;
    //zmienna znakowa przechowująca pierwszy i ostatni znak z wyrazu oraz znak wybrany przez użytkownika
    public Character first, last, guess;
    //tabela przechowująca znaki z hasła i znaki odgadnięte przez użytkownika
    public char[] wynik, litery;


    public Game() {

        //wczytanie pliku text.txt i rozpoczęcie gry
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)  {


                try {
                    File FILE = new File(path);
                    if (FILE.isDirectory()) {
                        //zmienna zliczająca ile linii zostało wczytanych z pliku text.txt
                        int licznik = 0;
                        File plik = new File(path + File.separator + "text.txt");
                        Scanner scanner = new Scanner(plik);
                        while (scanner.hasNext()) {
                            String line = scanner.nextLine();
                            wyrazy.add(line);
                            licznik++;
                        }
                        scanner.close();
                        //wylosowanie jednej linijki z wszystkich wczytanych haseł
                        Random rand = new Random();
                        int a = rand.nextInt(licznik-1);
                        String linijka = wyrazy.get(a+1);
                        //ustalenie pozycji hashtaga w celu rozdzielenia linijki na hasło i opis
                        int przerywnik = linijka.indexOf('#');
                        //wydzielenie hasła
                        haslo = linijka.substring(0,przerywnik);
                        JOptionPane.showMessageDialog(null, "Plik wczytany, nowa gra");
                        statusLoadu="loaded";
                        //odkrycie labeli po wczytaniu pliku
                        podpowiedz.setVisible(true);
                        wyswietlHaslo.setVisible(true);
                        KomunikatWybierz.setVisible(true);
                        szanse.setVisible(true);
                        literki.setVisible(true);
                        kat.setVisible(true);
                        pod.setVisible(true);

                        //ustalenie długości hasła
                        b = haslo.length();
                        //ustawienie liczby szans w zależności od długości hasła (-2 bo dwie litery są podane użytkownikowi)
                        lifes = b-2;
                        //rozbicie hasła na znaki
                        litery = haslo.toCharArray();
                        //wydzielenie pierwszej litery
                        first = litery[0];
                        //wydzielenie ostatniej litery
                        last = litery[b-1];
                        //ustawienie tekstu na labelach
                        kat.setText("Kategoria: " + wyrazy.get(0));
                        podpowiedz.setText("Liczba liter w haśle: " + b + ". Jego pierwsza litera to \"" + first + "\". Ostatnia litera to: \"" + last + "\"." );
                        pod.setText( "Podpowiedź: " + linijka.substring(przerywnik+1));
                        //złożenie w string hasła rozbitego na znaki + separatory
                        String ukryte = first.toString();
                        for (int i=0; i<b-2; i++){
                            ukryte = ukryte + " ,_";
                        }
                        ukryte = ukryte + "," + last.toString();
                        //wyświetlenie "ukrytego" hasła z widoczną pierwszą i ostatnią literą
                        wyswietlHaslo.setText(ukryte);
                        //wyświetlenie szans
                        szanse.setText("Szanse: " + lifes);
                        //ustawienie tabeli ze znakami odgadnietymi przez użytkownika
                        wynik = new char[b];
                        //wszystkie znaki poza pierwszym i ostatnim zostają zastąpione "_"
                        for (int z = 0; z < b; z++){
                            wynik[z]= '_';
                        }
                        wynik[0] = first;
                        wynik[b-1] = last;
                        //jeśli w haśle są takie same litery jak pierwsza i ostatnia to zostają automatycznie odkryte
                        for (int z = 0; z < b; z++){
                            if(litery[z]== first){wynik[z] = first;}
                            if(litery[z]== last){wynik[z] = last;}
                        }

                    }
                    //dodanie znaków do listy rozwijanej
                    for ( Character znakAdd : znaki) {
                        literki.addItem(znakAdd);
                    }

                }
                //wyjątki jeśli plik text.txt nie istnieje, nie zawiera rozdzielonych haseł lub jest pusty
                catch (FileNotFoundException | StringIndexOutOfBoundsException | IllegalArgumentException fnf) {
                    JOptionPane.showMessageDialog(null, "W katalogu C:\\java nie ma pliku wsadowego z hasłami lub plik ma niepoprawny format");
                }

            }
        });

        //ukrycie labeli w głównym oknie po uruchomieniu gry, przed wczytaniem pliku text.txt
        MainWindow.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                podpowiedz.setVisible(false);
                wyswietlHaslo.setVisible(false);
                KomunikatWybierz.setVisible(false);
                progres.setVisible(false);
                szanse.setVisible(false);
                literki.setVisible(false);
                kat.setVisible(false);
                pod.setVisible(false);
            }
        });

            //przycisk do zamknięcia programu
        zamknij.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //"wsad" do przycisku z opisem zasad
        zasady.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String zasadyGry;
                zasadyGry = "1. Gra wczytuje plik tekstowy z hasłami z pliku C:\\java\\text.txt po wciśnięciu przycisku Nowa gra";
                zasadyGry = zasadyGry + "\n2. Program wyświetla podpowiedź na temat długości słowa, skrajnych liter, kategorii i opis dołączony do hasła";
                zasadyGry = zasadyGry + "\n3. Literę wybiera się z listy rozwijanej i zatwierdza dowolnym klawiszem na klawiaturze";
                zasadyGry = zasadyGry + "\n4. Zasady budowania pliku text.txt są dostępne w opcji Zasady redakcji haseł";
                zasadyGry = zasadyGry + "\nPowodzenia :)";
                JOptionPane.showMessageDialog(null,zasadyGry);
            }
        });

        //opis akcji po zatwierdzeniu litery dowolnym klawiszem
        literki.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                //jeśli użytkownikowi skończyły się szanse wszystkie komponenty poza buttonami zostają ukryte i zostaje wyśwyetlony komunikat
                if (lifes == 0) {
                    podpowiedz.setVisible(false);
                    wyswietlHaslo.setVisible(false);
                    KomunikatWybierz.setVisible(false);
                    progres.setVisible(false);
                    szanse.setVisible(false);
                    literki.setVisible(false);
                    kat.setVisible(false);
                    pod.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Koniec gry. Rozpocznij nową grę lub zamknij program");


                } else {
                    //jeśli użytkownikowi pozostały jeszcze jakieś szanse
                    //sprawdzenie czy wybrany znak znajduje się w haśle i przypisanie go do tabeli odgadniętych znaków
                    guess = (Character)literki.getSelectedItem();
                    for (int w = 1; w < b-1; w++){
                        if(litery[w]== guess){
                            wynik[w] = guess;
                        }
                    }
                    //złożenie odgadniętych znaków w string, który jest wyświetlany użytkownikowi
                    String aktWyraz = first.toString();
                    for (int w = 1; w < b-1; w++){
                        aktWyraz = aktWyraz + ", " + Character.valueOf(wynik[w]).toString();
                    }
                    aktWyraz = aktWyraz + ", " + last.toString();
                    wyswietlHaslo.setText(aktWyraz);

                }
                //sprawdzenie czy użytkowik odgadł hasło
                String czyKoniec = "";
                for (int w = 0; w < b; w++){
                    czyKoniec = czyKoniec + Character.valueOf(wynik[w]).toString();
                }
                if (haslo.equals(czyKoniec)){
                    JOptionPane.showMessageDialog(null, "Gratulacje. Odgadłeś hasło. Poprawna odpowiedź to: " + czyKoniec);
                    //ukrycie wszystkich kompnentów poza buttonami
                    podpowiedz.setVisible(false);
                    wyswietlHaslo.setVisible(false);
                    KomunikatWybierz.setVisible(false);
                    progres.setVisible(false);
                    szanse.setVisible(false);
                    literki.setVisible(false);
                    kat.setVisible(false);
                    pod.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Koniec gry. Rozpocznij nową grę lub zamknij program");

                }

                //pomniejszenie licznik szans i wyświetlenie na ekran
                lifes = lifes -1;
                szanse.setText("Szanse: " + lifes);


            }
        });

        //przycisk wyświetlający zasady obsługi pliku text.txt
        zasRed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String zasadyRed;
                zasadyRed = "1. W pierwszym wierszu pliku txt powinna znajdować się nazwa kategorii (bez znaków specjalnych)";
                zasadyRed = zasadyRed + "\n2. W kolejnych wierszch powinny znajdować się hasła do odgadnięcia";
                zasadyRed = zasadyRed + "\n3. Hasło powinno być zbudowane wg schematu: hasło#opis hasła";
                zasadyRed = zasadyRed + "\n4. Hashtag (#) oddziela hasło od jego opisu";
                zasadyRed = zasadyRed + "\n5. Hasło powinno składać się z jednego wyrazu, bez polskich znaków, spacji i znaków specjalnych";
                JOptionPane.showMessageDialog(null,zasadyRed);
            }
        });
    }



//metoda główna do działania aplikacji
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game");
        frame.setContentPane(new Game().MainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}

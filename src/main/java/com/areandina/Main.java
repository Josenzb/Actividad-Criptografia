package com.areandina;

import com.areandina.database.mysql.MySqlOperation;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.Security;
import java.sql.SQLException;
import java.util.Scanner;

import static com.areandina.CifrarAsimetrico.*;
import static com.areandina.CifrarSimetrico.*;

public class Main {
    private static final String SERVER= "localhost";
    private static final String DATA_BASE_NAME="baseeje3";
    private static final String USER="root";
    private static final String PASSWORD="Colombia.2022";
    private static final MySqlOperation mySqlOperation = new MySqlOperation();

    public static void main(String[] args) throws Exception {
        SetSQL setSQL = new SetSQL();
        Security.addProvider(new BouncyCastleProvider());
        openConnection();
        closeConnection();
        Scanner scanner = new Scanner(System.in);
        int opcion;
        String nombre;
        String email;
        String password;
        SecretKey secretKey = generateAESKey();
        KeyPair keyPair = generateRSAKeyPair();
        String correoCifrado;
        String passCifrado;
        do {
            System.out.println("Menu Principal");
            System.out.println("1. Mostrar usuarios");
            System.out.println("2. Registrar usuarios");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    System.out.println("Mostrar usuarios seleccionado.");
                    System.out.println("Inserte su nombre: ");
                    nombre = scanner.next();
                    System.out.println("Inserte su email: ");
                    email = scanner.next();
                    System.out.println("Inserte su clave: ");
                    password = scanner.next();
                    correoCifrado = encryptText(email, secretKey);
                    passCifrado = encryptTextAsimetric(password, keyPair.getPublic());
                    String conSQL = setSQL.preSqlNombre(nombre);
                    System.out.println("El texto cifrado es: ");
                    SelectUsuario(conSQL);
                    System.out.println("El texto sin cifrar es: ");
                    String textoDescifrado = decryptText(correoCifrado, secretKey);
                    System.out.println("Email descifrado: " + textoDescifrado);
                    String passDescifrado = decryptTextAsimetric(passCifrado, keyPair.getPrivate());
                    System.out.println("Clave descifrada: " + passDescifrado);

                    break;
                case 2:
                    System.out.println("Registrar seleccionado.");
                    System.out.println("-------------------------.");
                    System.out.println("Ingrese su nombre: ");
                    nombre = scanner.next();
                    System.out.println("Ingrese su email: ");
                    email = scanner.next();
                    System.out.println("Ingrese su contraseña: ");
                    password = scanner.next();
                    String emailCifrado = encryptText(email, secretKey);
                    String paswordCifrado = encryptTextAsimetric(password, keyPair.getPublic());
                    String consulta = setSQL.preSql(nombre, emailCifrado, paswordCifrado);
                    insertUsuario(consulta);
                    System.out.println("Usuario creado");
                    break;
                case 3:
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
                    break;
            }
        } while (opcion != 3);
        scanner.close();
    }

    public static void openConnection(){
        mySqlOperation.setServer(SERVER);
        mySqlOperation.setDataBaseName(DATA_BASE_NAME);
        mySqlOperation.setUser(USER);
        mySqlOperation.setPassword(PASSWORD);
    }

    public static void closeConnection(){
        mySqlOperation.close();
    }

    public static void insertUsuario(String a)throws SQLException {
        mySqlOperation.setSqlStatement(a);
        mySqlOperation.executeSqlStatementvoid();
    }

    public static void SelectUsuario(String a)throws SQLException{
        mySqlOperation.setSqlStatement(a);
        mySqlOperation.executeSqlStatement();
        mySqlOperation.printResultset();
    }
}
package aula_tcp;

/**
 * TCPServer: Servidor para conexao TCP com Threads Descricao: Recebe uma
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;


public class TCPServer {

    public static void main(String args[]) {
            Scanner reader = new Scanner(System.in);        
        try {
            int serverPort = 6666; // porta do servidor

            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);

           
            
            String buffer = " ";
            while (true) {
                System.out.println("Servidor aguardando conexao ...");
                buffer = reader.nextLine(); 
                /* aguarda conexoes */
                Socket clientSocket = listenSocket.accept();
                DataOutputStream out =new DataOutputStream( clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream( clientSocket.getInputStream());

                /* cria um thread para atender a conexao */
                ClientThread c = new ClientThread(clientSocket);
                ClientThread c2 = new ClientThread(clientSocket);
                /* inicializa a thread */
                c.start();
                c2.start();

                System.out.print("Mensagem: ");
                out.writeUTF(buffer);      	// envia a mensagem para o servidor
		
                if (buffer.equals("PARAR")) break;
                    
                buffer = in.readUTF();      // aguarda resposta do servidor


                
            } //while

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //main
} //class

/**
 * Classe ClientThread: Thread responsavel pela comunicacao
 * Descricao: Rebebe um socket, cria os objetos de leitura e escrita,
 * aguarda msgs clientes e responde com a msg + :OK
 */
class ClientThread extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public ClientThread(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Connection:" + ioe.getMessage());
        } //catch
    } //construtor

    /* metodo executado ao iniciar a thread - start() */
    @Override
    public void run() {
        try {
            String buffer = "";
            while (true) {
                buffer = in.readUTF();   /* aguarda o envio de dados */

                System.out.println("Cliente disse: " + buffer);

                if (buffer.equals("PARAR")) break;

                buffer += ":OK";
                out.writeUTF(buffer);
            }
        } catch (EOFException eofe) {
            System.out.println("EOF: " + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOE: " + ioe.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException ioe) {
                System.err.println("IOE: " + ioe);
            }
        }
        System.out.println("Thread comunicação cliente finalizada.");
    } //run
} //class

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 *
 * @author Isaac Kaufman
 * @version 0.9
 */
public class WHSChat extends javax.swing.JFrame {

    public WHSChat() {
        this.setTitle("WHSChat");
        initComponents();
        //this.outputTextPane.setContentType("text/html");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        messageField = new javax.swing.JTextField();
        submitButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        outputTextPane = new javax.swing.JEditorPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        showInfo = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        messageField.setEditable(false);
        messageField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        messageField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageFieldActionPerformed(evt);
            }
        });

        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        outputTextPane.setEditable(false);
        jScrollPane2.setViewportView(outputTextPane);

        jMenu1.setText("Info");

        showInfo.setText("Client Info");
        showInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInfoActionPerformed(evt);
            }
        });
        jMenu1.add(showInfo);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(301, Short.MAX_VALUE)
                .addComponent(submitButton)
                .addGap(297, 297, 297))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(messageField))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(submitButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        try
        {
            Message message = new Message(Message.Type.USER, messageField.getText());
            if(!message.isValid())
                {
                    JOptionPane.showMessageDialog(this, "Invalid message.");
                }
            else
            {
                out.writeObject(message);
                messageField.setText("");
            }
        }
        catch (IOException e)
        {
            // handle
        }
    }                                            

    private void messageFieldActionPerformed(java.awt.event.ActionEvent evt) {                                             
        try
        {
            Message message = new Message(Message.Type.USER, messageField.getText());
            if(!message.isValid())
                {
                    JOptionPane.showMessageDialog(this, "Invalid message.");
                }
            else
            {
                out.writeObject(message);
                messageField.setText("");
            }
        }
        catch (IOException e)
        {
            // handle
        }
    }                                            

    private void showInfoActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JOptionPane.showMessageDialog(this, "WHSChat Client\nVersion 0.9");
    }                                        

    ObjectInputStream in;
    ObjectOutputStream out;
    

    public String getUsername(UsernameProtocol up) throws ClassNotFoundException
    {
        try 
        {
            String username = "";
            while (true)
            {
                Message message = (Message) in.readObject();
                if (message.getType() == Message.Type.SUBMITNAME)
                {
                    username = JOptionPane.showInputDialog(this, "Choose a Username");
                    if (!up.isValid(username))
                    {
                        JOptionPane.showMessageDialog(this, "Username must be at least " + up.getMinChars() +" characters and no more than " + up.getMaxChars() + ".");
                    }
                    out.writeObject(new Message(Message.Type.USER, username));
                }
                else if (message.getType() == Message.Type.NAMEACCEPTED)
                {
                    messageField.setEditable(true);
                    return username;
                }
			}
		}
        catch (IOException e)
		{
			return "default";
        }
    }

    /*public String getUsername(UsernameProtocol up)
    {
        String username = "";
        while (true)
        {
            username = JOptionPane.showInputDialog(this, "Choose a Username");
            if(!up.isValid(username))
            {
                JOptionPane.showMessageDialog(this, "Username must be at least " + up.getMinChars() +" characters and no more than " + up.getMaxChars() + ".");
            }
            else
            {
                return username;
            }
        }
        catch (IOException | ClassNotFoundException e)
        {

        }
    }*/
    //Connects to the server then enters the processing loop.
    private void run() throws IOException
    {
        try
        {
            // Make connection and initialize streams
            String serverAddress = JOptionPane.showInputDialog(this, "Enter IP address of server.");
            Socket socket = new Socket(serverAddress, 8888);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Process messages from server, according to the protocol.
            getUsername((UsernameProtocol) in.readObject());
            while (true)
            {
                // TODO switch and fix everything here
                Message message = (Message) in.readObject();

                switch (message.getType())
                {
                    case USER:
                    {
                        outputTextPane.setText(outputTextPane.getText() + message.getSender() + ": " + message.getMessage() + "\n");
                        break;
                    }
                    case SYS:
                    {
                        outputTextPane.setText(outputTextPane.getText() + message.getMessage() + "\n");
                        break;
                    }
                    default:
                    {

                    }
                UsernameProtocol unameProtocol = (UsernameProtocol) in.readObject();
                message = (Message) in.readObject();
                if (message.getType() == Message.Type.SUBMITNAME)
                {
                    out.writeObject(new Message(Message.Type.USER, getUsername(unameProtocol)));
                }
            }
        }
		}
        catch (ClassNotFoundException e)
        {
            // handle
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WHSChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WHSChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WHSChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WHSChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        WHSChat client = new WHSChat();
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.setVisible(true);
        client.run();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField messageField;
    private javax.swing.JEditorPane outputTextPane;
    private javax.swing.JMenuItem showInfo;
    private javax.swing.JButton submitButton;
    // End of variables declaration                   
}

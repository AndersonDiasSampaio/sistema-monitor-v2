package com.exemplo.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AdminPanel extends JFrame {
    private JTextField portaTextField;
    private JButton iniciarButton;
    private JButton pararButton;
    private JLabel statusLabel;
    private Process processo;
    private Properties config;

    public AdminPanel() {
        config = new Properties();
        carregarConfiguracoes();

        setTitle("Painel de Administração - Sistema Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campo de porta
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Porta:"), gbc);

        portaTextField = new JTextField(10);
        portaTextField.setText(config.getProperty("server.port", "9000"));
        gbc.gridx = 1;
        mainPanel.add(portaTextField, gbc);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iniciarButton = new JButton("Iniciar Serviço");
        pararButton = new JButton("Parar Serviço");
        pararButton.setEnabled(false);

        buttonPanel.add(iniciarButton);
        buttonPanel.add(pararButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        // Status
        statusLabel = new JLabel("Status: Parado");
        gbc.gridy = 2;
        mainPanel.add(statusLabel, gbc);

        // Eventos
        iniciarButton.addActionListener(e -> iniciarServico());
        pararButton.addActionListener(e -> pararServico());

        add(mainPanel);
    }

    private void carregarConfiguracoes() {
        File configFile = new File("application.properties");
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void salvarConfiguracoes() {
        config.setProperty("server.port", portaTextField.getText());
        try (FileOutputStream fos = new FileOutputStream("application.properties")) {
            config.store(fos, "Configurações do Sistema Monitor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarServico() {
        try {
            salvarConfiguracoes();

            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar", "target/sistema-monitor-0.0.1-SNAPSHOT.jar"
            );
            pb.redirectErrorStream(true);
            processo = pb.start();

            iniciarButton.setEnabled(false);
            pararButton.setEnabled(true);
            statusLabel.setText("Status: Em execução");

            // Monitorar saída do processo
            new Thread(() -> {
                try {
                    int exitCode = processo.waitFor();
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Status: Parado (Código: " + exitCode + ")");
                        iniciarButton.setEnabled(true);
                        pararButton.setEnabled(false);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao iniciar o serviço: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pararServico() {
        if (processo != null && processo.isAlive()) {
            processo.destroy();
            processo = null;
            iniciarButton.setEnabled(true);
            pararButton.setEnabled(false);
            statusLabel.setText("Status: Parado");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AdminPanel().setVisible(true);
        });
    }
} 
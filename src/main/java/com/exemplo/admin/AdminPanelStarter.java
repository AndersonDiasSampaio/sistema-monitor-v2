package com.exemplo.admin;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.exemplo.SistemaMonitorApplication;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class AdminPanelStarter {
    private static JFrame frame;
    private static Properties config;
    private static JLabel statusLabel;
    private static JButton pararButton;
    private static JButton reiniciarButton;
    private static JTextField portaTextField;
    private static boolean servicoRodando = true;
    private static ConfigurableApplicationContext applicationContext;
    
    @Autowired
    public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        AdminPanelStarter.applicationContext = applicationContext;
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void startAdminPanel() {
        // Força o modo não-headless
        System.setProperty("java.awt.headless", "false");
        
        // Verifica se o ambiente suporta GUI
        if (!GraphicsEnvironment.isHeadless()) {
            config = new Properties();
            carregarConfiguracoes();
            
            SwingUtilities.invokeLater(() -> {
                try {
                    // Tenta usar o look and feel do sistema
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    try {
                        // Fallback para o look and feel padrão
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                
                // Se já existe uma janela, não cria outra
                if (frame != null && frame.isVisible()) {
                    return;
                }
                
                frame = new JFrame("Painel de Administração - Sistema Monitor");
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setSize(400, 300);
                frame.setLocationRelativeTo(null);
                
                // Painel principal
                JPanel mainPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Status
                statusLabel = new JLabel("Status: Em execução");
                statusLabel.setForeground(Color.BLACK); // Texto preto
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                mainPanel.add(statusLabel, gbc);
                
                // Painel de configuração da porta
                JPanel portaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                portaPanel.setBorder(BorderFactory.createTitledBorder("Configuração da Porta"));
                
                JLabel portaLabel = new JLabel("Porta:");
                portaLabel.setForeground(Color.BLACK); // Texto preto
                portaTextField = new JTextField(6);
                portaTextField.setText(config.getProperty("server.port", "9000"));
                
                JButton salvarPortaButton = new JButton("Salvar");
                salvarPortaButton.setBackground(new Color(0, 123, 255)); // Azul
                salvarPortaButton.setForeground(Color.BLACK); // Texto preto
                salvarPortaButton.setFocusPainted(false);
                salvarPortaButton.addActionListener(e -> salvarPorta());
                
                portaPanel.add(portaLabel);
                portaPanel.add(portaTextField);
                portaPanel.add(salvarPortaButton);
                
                gbc.gridy = 1;
                mainPanel.add(portaPanel, gbc);
                
                // Painel de botões
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                
                // Botão de parar
                pararButton = new JButton("Parar Serviço");
                pararButton.setBackground(new Color(220, 53, 69)); // Vermelho
                pararButton.setForeground(Color.BLACK); // Texto preto
                pararButton.setFocusPainted(false);
                buttonPanel.add(pararButton);
                
                // Botão de reiniciar
                reiniciarButton = new JButton("Reiniciar Serviço");
                reiniciarButton.setBackground(new Color(40, 167, 69)); // Verde
                reiniciarButton.setForeground(Color.BLACK); // Texto preto
                reiniciarButton.setFocusPainted(false);
                reiniciarButton.setEnabled(false);
                buttonPanel.add(reiniciarButton);
                
                gbc.gridy = 2;
                mainPanel.add(buttonPanel, gbc);
                
                // Eventos
                pararButton.addActionListener(e -> pararServico());
                reiniciarButton.addActionListener(e -> reiniciarServico());
                
                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        encerrarSistema();
                    }
                });
                
                frame.add(mainPanel);
                frame.setVisible(true);
            });
        } else {
            System.out.println("Ambiente sem suporte a interface gráfica. O sistema continuará rodando em modo servidor.");
        }
    }
    
    private static void carregarConfiguracoes() {
        File configFile = new File("application.properties");
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void salvarPorta() {
        String novaPorta = portaTextField.getText().trim();
        
        // Validação da porta
        try {
            int porta = Integer.parseInt(novaPorta);
            if (porta < 1 || porta > 65535) {
                throw new NumberFormatException("Porta inválida");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                "Por favor, insira uma porta válida (1-65535)",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Salva a nova porta no arquivo de configuração
        config.setProperty("server.port", novaPorta);
        File configFile = new File("application.properties");
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            config.store(fos, "Configurações do Sistema Monitor");
            JOptionPane.showMessageDialog(frame,
                "Porta alterada com sucesso! A alteração será aplicada após reiniciar o serviço.",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                "Erro ao salvar a configuração: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void pararServico() {
        if (servicoRodando) {
            servicoRodando = false;
            statusLabel.setText("Status: Parado");
            statusLabel.setForeground(Color.BLACK); // Texto preto
            pararButton.setEnabled(false);
            reiniciarButton.setEnabled(true);
            
            // Para o contexto da aplicação Spring Boot
            if (applicationContext != null) {
                applicationContext.close();
                System.out.println("Serviço Spring Boot parado");
            }
        }
    }
    
    private static void reiniciarServico() {
        if (!servicoRodando) {
            servicoRodando = true;
            statusLabel.setText("Status: Em execução");
            statusLabel.setForeground(Color.BLACK); // Texto preto
            pararButton.setEnabled(true);
            reiniciarButton.setEnabled(false);
            
            // Reinicia a aplicação Spring Boot
            new Thread(() -> {
                try {
                    String[] args = new String[0];
                    SpringApplication.run(SistemaMonitorApplication.class, args);
                    System.out.println("Serviço Spring Boot reiniciado");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, 
                        "Erro ao reiniciar o serviço: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        }
    }
    
    private static void encerrarSistema() {
        int opcao = JOptionPane.showConfirmDialog(
            frame,
            "Deseja realmente encerrar o sistema?",
            "Confirmar Encerramento",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (opcao == JOptionPane.YES_OPTION) {
            if (applicationContext != null) {
                applicationContext.close();
            }
            System.exit(0);
        }
    }
} 
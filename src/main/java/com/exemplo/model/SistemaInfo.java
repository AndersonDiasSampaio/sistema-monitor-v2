package com.exemplo.model;

import java.util.ArrayList;
import java.util.List;

public class SistemaInfo {
    private double usoCPU;
    private double usoMemoria;
    private double usoDisco;
    private long memoriaTotal;
    private long memoriaDisponivel;
    private long discoTotal;
    private long discoUsado;
    private List<DiscoInfo> discos;

    public SistemaInfo(double usoCPU, double usoMemoria, double usoDisco,
                      long memoriaTotal, long memoriaDisponivel,
                      long discoTotal, long discoUsado) {
        this.usoCPU = usoCPU;
        this.usoMemoria = usoMemoria;
        this.usoDisco = usoDisco;
        this.memoriaTotal = memoriaTotal;
        this.memoriaDisponivel = memoriaDisponivel;
        this.discoTotal = discoTotal;
        this.discoUsado = discoUsado;
        this.discos = new ArrayList<>();
    }

    // Getters e Setters
    public double getUsoCPU() { return usoCPU; }
    public void setUsoCPU(double usoCPU) { this.usoCPU = usoCPU; }

    public double getUsoMemoria() { return usoMemoria; }
    public void setUsoMemoria(double usoMemoria) { this.usoMemoria = usoMemoria; }

    public double getUsoDisco() { return usoDisco; }
    public void setUsoDisco(double usoDisco) { this.usoDisco = usoDisco; }

    public long getMemoriaTotal() { return memoriaTotal; }
    public void setMemoriaTotal(long memoriaTotal) { this.memoriaTotal = memoriaTotal; }

    public long getMemoriaDisponivel() { return memoriaDisponivel; }
    public void setMemoriaDisponivel(long memoriaDisponivel) { this.memoriaDisponivel = memoriaDisponivel; }

    public long getDiscoTotal() { return discoTotal; }
    public void setDiscoTotal(long discoTotal) { this.discoTotal = discoTotal; }

    public long getDiscoUsado() { return discoUsado; }
    public void setDiscoUsado(long discoUsado) { this.discoUsado = discoUsado; }

    public List<DiscoInfo> getDiscos() { return discos; }
    public void setDiscos(List<DiscoInfo> discos) { this.discos = discos; }

    public static class DiscoInfo {
        private String nome;
        private String montagem;
        private long total;
        private long usado;
        private long disponivel;
        private double usoPercentual;

        public DiscoInfo(String nome, String montagem, long total, long usado, long disponivel) {
            this.nome = nome;
            this.montagem = montagem;
            this.total = total;
            this.usado = usado;
            this.disponivel = disponivel;
            this.usoPercentual = ((double) usado / total) * 100;
        }

        // Getters
        public String getNome() { return nome; }
        public String getMontagem() { return montagem; }
        public long getTotal() { return total; }
        public long getUsado() { return usado; }
        public long getDisponivel() { return disponivel; }
        public double getUsoPercentual() { return usoPercentual; }
    }
} 
import { Component, Inject, PLATFORM_ID, AfterViewInit, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformServer } from '@angular/common';

import { CaixaRepresentation, VendaRepresentation, ProdutoRepresetation, ItemVendaProdutoRepresentation } from '../../models'; // Importar ProdutoRepresetation
import { CaixaService } from '../../services/caixa.service';
import { VendaService } from '../../services/venda.service';
import { ProdutoService } from '../../services/produto.service'; // Importar ProdutoService

interface VendaStats { // Definir a interface VendaStats aqui ou importar se já estiver em um arquivo separado
  total: number;
  variacao: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements AfterViewInit {
  loading = false;
  error: string | null = null;

  vendasTotaisHoje: VendaStats | null = null;
  numeroVendasHoje: VendaStats | null = null;
  ticketMedioHoje: VendaStats | null = null;

  // Novas propriedades para Semana
  vendasTotaisSemana: VendaStats | null = null;
  numeroVendasSemana: VendaStats | null = null;
  ticketMedioSemana: VendaStats | null = null;

  // Novas propriedades para Mês
  vendasTotaisMes: VendaStats | null = null;
  numeroVendasMes: VendaStats | null = null;
  ticketMedioMes: VendaStats | null = null;

  topProdutosVendidos: ItemVendaProdutoRepresentation[] = [];
  produtosBaixoEstoque: ProdutoRepresetation[] = [];
  produtosEmFalta: ProdutoRepresetation[] = [];

  @ViewChild('salesChart') salesChartRef!: ElementRef<HTMLCanvasElement>;
  private salesChart: any;

  constructor(
    private caixaService: CaixaService,
    private vendaService: VendaService,
    private produtoService: ProdutoService, // Injetar ProdutoService
    @Inject(PLATFORM_ID) private platformId: any,
    private cdr: ChangeDetectorRef
  ) {
    if (!isPlatformServer(this.platformId)) {
      this.loadDashboardCards();
    }
  }

  loadDashboardCards() {
    this.loading = true;
    this.error = null;
    const today = new Date();

    // Vendas Totais Hoje
    this.vendaService.getVendasTotaisPorPeriodo('hoje').subscribe({
      next: (data) => this.vendasTotaisHoje = data,
      error: (e) => console.error('Erro ao carregar vendas totais hoje:', e)
    });

    // Número de Vendas Hoje
    this.vendaService.getNumeroVendasPorPeriodo('hoje').subscribe({
      next: (data) => this.numeroVendasHoje = data,
      error: (e) => console.error('Erro ao carregar número de vendas hoje:', e)
    });

    // Ticket Médio Hoje
    this.vendaService.getTicketMedioPorPeriodo('hoje').subscribe({
      next: (data) => this.ticketMedioHoje = data,
      error: (e) => console.error('Erro ao carregar ticket médio hoje:', e)
    });

    // Vendas Totais Semana
    this.vendaService.getVendasTotaisPorPeriodo('semana').subscribe({
      next: (data) => this.vendasTotaisSemana = data,
      error: (e) => console.error('Erro ao carregar vendas totais semana:', e)
    });

    // Número de Vendas Semana
    this.vendaService.getNumeroVendasPorPeriodo('semana').subscribe({
      next: (data) => this.numeroVendasSemana = data,
      error: (e) => console.error('Erro ao carregar número de vendas semana:', e)
    });

    // Ticket Médio Semana
    this.vendaService.getTicketMedioPorPeriodo('semana').subscribe({
      next: (data) => this.ticketMedioSemana = data,
      error: (e) => console.error('Erro ao carregar ticket médio semana:', e)
    });

    // Vendas Totais Mês
    this.vendaService.getVendasTotaisPorPeriodo('mes').subscribe({
      next: (data) => this.vendasTotaisMes = data,
      error: (e) => console.error('Erro ao carregar vendas totais mês:', e)
    });

    // Número de Vendas Mês
    this.vendaService.getNumeroVendasPorPeriodo('mes').subscribe({
      next: (data) => this.numeroVendasMes = data,
      error: (e) => console.error('Erro ao carregar número de vendas mês:', e)
    });

    // Ticket Médio Mês
    this.vendaService.getTicketMedioPorPeriodo('mes').subscribe({
      next: (data) => this.ticketMedioMes = data,
      error: (e) => console.error('Erro ao carregar ticket médio mês:', e)
    });

    // Top Produtos Vendidos
    this.vendaService.getTopProdutosVendidos(5).subscribe({
      next: (data) => this.topProdutosVendidos = data,
      error: (e) => console.error('Erro ao carregar top produtos vendidos:', e)
    });

    // Produtos com Baixo Estoque
    this.produtoService.getProdutosBaixoEstoque(5).subscribe({
      next: (data) => this.produtosBaixoEstoque = data,
      error: (e) => console.error('Erro ao carregar produtos com baixo estoque:', e)
    });

    // Produtos em falta
    this.produtoService.getProdutosEmFalta(5).subscribe({
      next: (data) => this.produtosEmFalta = data,
      error: (e) => console.error('Erro ao carregar produtos em falta:', e)
    });
  }

  ngAfterViewInit(): void {
    // chart will be created when vendas are loaded
  }
}

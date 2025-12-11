import { Component, Inject, PLATFORM_ID, AfterViewInit, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformServer } from '@angular/common';

import { CaixaRepresentation, VendaRepresentation, ProdutoRepresetation } from '../../models'; // Importar ProdutoRepresetation
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

  caixaValor: number = 0;
  produtosVendidosCount: number = 0;
  valorVendido: number = 0;

  // Novas propriedades para os cards
  vendasTotaisHoje: VendaStats | null = null;
  numeroVendasHoje: VendaStats | null = null;
  topProdutosVendidos: ProdutoRepresetation[] = [];
  produtosBaixoEstoque: ProdutoRepresetation[] = [];

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

  private isSameDay(d1?: string, d2?: Date): boolean {
    if (!d1) return false;
    const d = new Date(d1);
    return d.getFullYear() === d2!.getFullYear() && d.getMonth() === d2!.getMonth() && d.getDate() === d2!.getDate();
  }

  loadDashboardCards() {
    this.loading = true;
    this.error = null;
    const today = new Date();

    // Load caixas and vendas in parallel
    this.caixaService.listarCaixas().subscribe({
      next: (caixas: CaixaRepresentation[]) => {
        const todays = (caixas || []).filter(c => this.isSameDay(c.dataAbertura, today));
        // Sum valorFinal if present, otherwise valorInicial
        this.caixaValor = todays.reduce((acc, c) => acc + (c.valorFinal ?? c.valorInicial ?? 0), 0);
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      },
      error: (e) => {
        this.error = this.error || (e?.message || 'Erro ao carregar caixas');
      }
    });

    this.vendaService.listarVendas().subscribe({
      next: (vendas: VendaRepresentation[]) => {
        const todays = (vendas || []).filter(v => this.isSameDay(v.dataVenda, today));
        this.produtosVendidosCount = todays.reduce((acc, v) => acc + (v.itens?.reduce((s, it) => s + (it.quantidade ?? 0), 0) ?? 0), 0);
        this.valorVendido = todays.reduce((acc, v) => acc + (v.totalLiquido ?? v.totalBruto ?? 0), 0);
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      },
      error: (e) => {
        this.error = this.error || (e?.message || 'Erro ao carregar vendas');
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      }
    });

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

    // Top Produtos Vendidos
    this.produtoService.getTopProdutosVendidos(5).subscribe({
      next: (data) => this.topProdutosVendidos = data,
      error: (e) => console.error('Erro ao carregar top produtos vendidos:', e)
    });

    // Produtos com Baixo Estoque
    this.produtoService.getProdutosBaixoEstoque(5).subscribe({
      next: (data) => this.produtosBaixoEstoque = data,
      error: (e) => console.error('Erro ao carregar produtos com baixo estoque:', e)
    });
  }

  ngAfterViewInit(): void {
    // chart will be created when vendas are loaded
  }
}

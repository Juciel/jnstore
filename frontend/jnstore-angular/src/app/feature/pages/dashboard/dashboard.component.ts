import { Component, Inject, PLATFORM_ID, AfterViewInit, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformServer } from '@angular/common';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

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

    const requests = {
      vendasTotaisHoje: this.vendaService.getVendasTotaisPorPeriodo('hoje').pipe(catchError(e => of(null))),
      numeroVendasHoje: this.vendaService.getNumeroVendasPorPeriodo('hoje').pipe(catchError(e => of(null))),
      ticketMedioHoje: this.vendaService.getTicketMedioPorPeriodo('hoje').pipe(catchError(e => of(null))),
      vendasTotaisSemana: this.vendaService.getVendasTotaisPorPeriodo('semana').pipe(catchError(e => of(null))),
      numeroVendasSemana: this.vendaService.getNumeroVendasPorPeriodo('semana').pipe(catchError(e => of(null))),
      ticketMedioSemana: this.vendaService.getTicketMedioPorPeriodo('semana').pipe(catchError(e => of(null))),
      vendasTotaisMes: this.vendaService.getVendasTotaisPorPeriodo('mes').pipe(catchError(e => of(null))),
      numeroVendasMes: this.vendaService.getNumeroVendasPorPeriodo('mes').pipe(catchError(e => of(null))),
      ticketMedioMes: this.vendaService.getTicketMedioPorPeriodo('mes').pipe(catchError(e => of(null))),
      topProdutosVendidos: this.vendaService.getTopProdutosVendidos(5).pipe(catchError(e => of([]))),
      produtosBaixoEstoque: this.produtoService.getProdutosBaixoEstoque(5).pipe(catchError(e => of([]))),
      produtosEmFalta: this.produtoService.getProdutosEmFalta(5).pipe(catchError(e => of([])))
    };

    forkJoin(requests).subscribe({
      next: (data: any) => {
        this.vendasTotaisHoje = data.vendasTotaisHoje;
        this.numeroVendasHoje = data.numeroVendasHoje;
        this.ticketMedioHoje = data.ticketMedioHoje;
        this.vendasTotaisSemana = data.vendasTotaisSemana;
        this.numeroVendasSemana = data.numeroVendasSemana;
        this.ticketMedioSemana = data.ticketMedioSemana;
        this.vendasTotaisMes = data.vendasTotaisMes;
        this.numeroVendasMes = data.numeroVendasMes;
        this.ticketMedioMes = data.ticketMedioMes;
        this.topProdutosVendidos = data.topProdutosVendidos;
        this.produtosBaixoEstoque = data.produtosBaixoEstoque;
        this.produtosEmFalta = data.produtosEmFalta;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (e) => {
        console.error('Erro ao carregar dados do dashboard:', e);
        this.error = 'Erro ao carregar dados do dashboard.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  ngAfterViewInit(): void {
    // chart will be created when vendas are loaded
  }
}

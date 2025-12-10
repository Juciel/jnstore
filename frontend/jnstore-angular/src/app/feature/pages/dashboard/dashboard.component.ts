import { Component, Inject, PLATFORM_ID, AfterViewInit, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformServer } from '@angular/common';

import { CaixaRepresentation, VendaRepresentation } from '../../models';
import { CaixaService } from '../../services/caixa.service';
import { VendaService } from '../../services/venda.service';

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
  @ViewChild('salesChart') salesChartRef!: ElementRef<HTMLCanvasElement>;
  private salesChart: any;

  constructor(private caixaService: CaixaService, private vendaService: VendaService, @Inject(PLATFORM_ID) private platformId: any, private cdr: ChangeDetectorRef) {
    if (!isPlatformServer(this.platformId)) {
      this.loadStats();
    }
  }

  private isSameDay(d1?: string, d2?: Date): boolean {
    if (!d1) return false;
    const d = new Date(d1);
    return d.getFullYear() === d2!.getFullYear() && d.getMonth() === d2!.getMonth() && d.getDate() === d2!.getDate();
  }

  loadStats() {
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
        // also render historical chart for last 7 days
        try {
          this.renderSalesHistory(vendas || []);
        } catch (e) {
          // ignore chart errors
          console.error('Chart render error', e);
        }
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      },
      error: (e) => {
        this.error = this.error || (e?.message || 'Erro ao carregar vendas');
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      }
    });
  }

  ngAfterViewInit(): void {
    // chart will be created when vendas are loaded
  }

  private getLastNDates(n: number): Date[] {
    const arr: Date[] = [];
    const today = new Date();
    for (let i = n - 1; i >= 0; i--) {
      const d = new Date(today);
      d.setDate(today.getDate() - i);
      d.setHours(0, 0, 0, 0);
      arr.push(d);
    }
    return arr;
  }

  private renderSalesHistory(vendas: VendaRepresentation[]) {
    const days = this.getLastNDates(7);
    const labels = days.map(d => `${d.getDate().toString().padStart(2,'0')}/${(d.getMonth()+1).toString().padStart(2,'0')}`);
    const totals = days.map(() => 0);

    vendas.forEach(v => {
      if (!v.dataVenda) return;
      const d = new Date(v.dataVenda);
      d.setHours(0,0,0,0);
      for (let i = 0; i < days.length; i++) {
        if (d.getTime() === days[i].getTime()) {
          totals[i] += (v.totalLiquido ?? v.totalBruto ?? 0);
          break;
        }
      }
    });

    // ensure Chart.js is available (loaded via CDN)
    const Chart = (window as any).Chart;
    if (!Chart) return;

    // destroy previous chart
    if (this.salesChart) {
      try { this.salesChart.destroy(); } catch { }
    }

    const ctx = this.salesChartRef?.nativeElement.getContext('2d');
    if (!ctx) return;

    this.salesChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: 'Vendas (R$)',
          data: totals,
          borderColor: '#d4af37',
          backgroundColor: 'rgba(212,175,55,0.2)',
          tension: 0.3,
          fill: true
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: { beginAtZero: true }
        }
      }
    });
  }
}

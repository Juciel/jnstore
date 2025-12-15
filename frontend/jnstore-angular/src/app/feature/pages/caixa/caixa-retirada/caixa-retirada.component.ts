import { Component, OnInit, ChangeDetectorRef } from '@angular/core'; // Importar ChangeDetectorRef
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CaixaService } from 'src/app/feature/services/caixa.service';
import { VendaService } from 'src/app/feature/services/venda.service';
import { CurrencyMaskDirective } from 'src/app/feature/directives/currency-mask.directive';
import { CaixaRepresentation,VendaRepresentation } from 'src/app/feature/models';

@Component({
  selector: 'app-caixa-retirada',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyMaskDirective],
  templateUrl: './caixa-retirada.component.html',
  styleUrls: ['./caixa-retirada.component.scss']
})
export class CaixaRetiradaComponent implements OnInit {
  caixaId: number | null = null;
  caixa: CaixaRepresentation | undefined;
  vendas: VendaRepresentation[] = [];
  valorRetirada?: number; // Mantido para o input do formulário
  loading = false;
  closeError: string | null = null;

  totalPix: number = 0;
  totalDinheiro: number = 0;
  totalCredito: number = 0;
  totalDebito: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private caixaService: CaixaService,
    private vendaService: VendaService,
    private cdr: ChangeDetectorRef // Injetar ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.caixaId = +id;
        this.buscarCaixaPorId(this.caixaId);
      }
    });
  }

  buscarCaixaPorId(id: number): void {
    this.loading = true;
    this.caixaService.buscarPorId(id).subscribe({
      next: (caixa: CaixaRepresentation) => {
        this.caixa = caixa;
        this.loading = false;
        this.cdr.detectChanges(); // Forçar detecção de mudanças
      },
      error: e => {
        this.closeError = e?.message || 'Erro ao buscar caixa';
        console.error('buscarCaixaPorId: Erro ao buscar caixa:', e); // Log para depuração
        this.loading = false;
        this.cdr.detectChanges(); // Forçar detecção de mudanças mesmo em caso de erro
      }
    });

    this.vendaService.getVendasByCaixaId(id).subscribe({
      next: (vendas: VendaRepresentation[]) => {
        this.vendas = vendas;
        this.calcularTotaisPagamentos();
        if (this.totalDinheiro !== undefined && this.totalDinheiro !== null) {
          this.valorRetirada = this.totalDinheiro;
        }
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        this.closeError = 'Não foi possível carregar as vendas do caixa.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  retiradaCaixa(): void {
    if (!this.caixaId) {
      this.closeError = 'ID do caixa não encontrado.';
      console.error('retiradaCaixa: ID do caixa não encontrado.'); // Log para depuração
      return;
    }
    if (this.valorRetirada === undefined || this.valorRetirada === null) {
      this.closeError = 'O valor de retirada é obrigatório.';
      console.error('retiradaCaixa: Valor de retirada é obrigatório.'); // Log para depuração
      return;
    }

    this.loading = true;
    this.closeError = null;

    this.caixaService.retiradaCaixa(this.caixaId, { valor: this.valorRetirada }).subscribe({
      next: res => {
        this.router.navigate(['/caixa'], { state: { successMessage: 'Caixa retirado com sucesso!' } });
      },
      error: e => {
        this.closeError = e?.error?.message || e?.message || 'Erro ao retirar caixa';
        this.loading = false;
        console.error('retiradaCaixa: Erro ao retirar caixa:', e); // Log para depuração
        this.cdr.detectChanges();
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/caixa']);
  }

  private calcularTotaisPagamentos(): void {
    this.totalPix = 0;
    this.totalDinheiro = 0;
    this.totalCredito = 0;
    this.totalDebito = 0;

    if (!this.vendas) {
      return;
    }

    for (const venda of this.vendas) {
      if (venda.pagamentos) {
        for (const pagamento of venda.pagamentos) {
          if (pagamento.valorPago) {
            switch (pagamento.forma) {
              case 'PIX':
                this.totalPix += pagamento.valorPago;
                break;
              case 'DINHEIRO':
                this.totalDinheiro += pagamento.valorPago;
                break;
              case 'CREDITO':
                this.totalCredito += pagamento.valorPago;
                break;
              case 'DEBITO':
                this.totalDebito += pagamento.valorPago;
                break;
            }
          }
        }
      }
    }
  }
}

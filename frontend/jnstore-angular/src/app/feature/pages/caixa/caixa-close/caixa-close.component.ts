import { Component, OnInit, ChangeDetectorRef } from '@angular/core'; // Importar ChangeDetectorRef
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CaixaService } from 'src/app/feature/services/caixa.service';
import { CurrencyMaskDirective } from 'src/app/feature/directives/currency-mask.directive';
import { CaixaRepresentation } from 'src/app/feature/models';

@Component({
  selector: 'app-caixa-close',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyMaskDirective],
  templateUrl: './caixa-close.component.html',
  styleUrls: ['./caixa-close.component.scss']
})
export class CaixaCloseComponent implements OnInit {
  caixaId: number | null = null;
  caixa: CaixaRepresentation | undefined;
  valorFechamento?: number; // Mantido para o input do formulário
  loading = false;
  closeError: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private caixaService: CaixaService,
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
        // Se o caixa já tiver um valorFinal, exibi-lo no campo valorFechamento
        if (this.caixa.valorFinal !== undefined && this.caixa.valorFinal !== null) {
          this.valorFechamento = this.caixa.valorFinal;
        }
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
  }

  fecharCaixa(): void {
    if (!this.caixaId) {
      this.closeError = 'ID do caixa não encontrado.';
      console.error('fecharCaixa: ID do caixa não encontrado.'); // Log para depuração
      return;
    }
    if (this.valorFechamento === undefined || this.valorFechamento === null) {
      this.closeError = 'O valor de fechamento é obrigatório.';
      console.error('fecharCaixa: Valor de fechamento é obrigatório.'); // Log para depuração
      return;
    }

    this.loading = true;
    this.closeError = null;

    this.caixaService.fecharCaixa(this.caixaId, { valor: this.valorFechamento }).subscribe({
      next: res => {
        this.router.navigate(['/caixa'], { state: { successMessage: 'Caixa fechado com sucesso!' } });
      },
      error: e => {
        this.closeError = e?.error?.message || e?.message || 'Erro ao fechar caixa';
        this.loading = false;
        console.error('fecharCaixa: Erro ao fechar caixa:', e); // Log para depuração
        this.cdr.detectChanges();
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/caixa']);
  }
}
